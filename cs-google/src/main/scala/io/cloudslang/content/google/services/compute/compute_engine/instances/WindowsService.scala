package io.cloudslang.content.google.services.compute.compute_engine.instances

import java.nio.charset.StandardCharsets
import java.security.spec.{InvalidKeySpecException, RSAPublicKeySpec}
import java.security._
import java.text.SimpleDateFormat
import java.util
import java.util.{Date, TimeZone}
import javax.crypto.Cipher

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64
import com.google.api.services.compute.model.Metadata
import com.google.api.services.compute.model.Metadata.Items
import com.google.common.io.BaseEncoding
import io.cloudslang.content.google.services.compute.compute_engine.ComputeService
import org.bouncycastle.jce.provider.BouncyCastleProvider
import spray.json._
import DefaultJsonProtocol._
import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.services.compute.Compute
import com.google.api.services.compute.model.SerialPortOutput
import jdk.nashorn.internal.parser.JSONParser
import java.security.KeyPair

import io.cloudslang.content.constants.OutputNames
import io.cloudslang.content.google.actions.authentication.GetAccessToken
import io.cloudslang.content.google.utils.action.InputNames

import scala.collection.JavaConversions._


object WindowsService {

  // Constants for configuring user name, email, and SSH key expiration.

  // Keys are one-time use, so the metadata doesn't need to stay around for long.
  // 5 minutes chosen to allow for differences between time on the client
  // and time on the server.
  private val EXPIRE_TIME = 300000


  def resetWindowsPassword(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String,
                           zone: String, instanceName: String, userName: String, email: String, expireTime: Long, timeout: Long): String = {
    // Get the instance object to gain access to the instance's metadata.
    val inst = InstanceService.get(httpTransport, jsonFactory, credential, project, zone, instanceName)
    val metadata = inst.getMetadata
    // Generate the public/private key pair for encryption and decryption.
    val keys = generateKeys()
    // Update metadata from instance with new windows-keys entry.
    replaceMetadata(metadata, buildKeyMetadata(keys, expireTime, userName, email))
    // Tell Compute Engine to update the instance metadata with our changes.

    InstanceService.setMetadata(httpTransport, jsonFactory, credential, project, zone, instanceName, metadata, sync = true, timeout)
    System.out.println("Getting serial output...")
    // Request the output from serial port 4.
    // In production code, this operation should be polled.
    val output = InstanceService.getSerialPortOutput(httpTransport, jsonFactory, credential,project, zone, instanceName, 4, 0)
    // Get the last line - this will be a JSON string corresponding to the
    // most recent password reset attempt.
    val entries = output.getContents.split("\n")
    val outputEntry: String = entries(entries.length - 1)
    // Parse output using the json-simple library.
    val passwordDict = outputEntry.parseJson.convertTo[Map[String, JsValue]]

    val encryptedPassword = passwordDict.getOrElse("encryptedPassword", "").toString
    // Output user name and decrypted password.
    System.out.println("\nUser name: " + passwordDict.get("userName").toString)
    System.out.println("Password: " + decryptPassword(encryptedPassword, keys))
    decryptPassword(encryptedPassword, keys)
  }

  private def generateKeys(): KeyPair = {
    val keyGen = KeyPairGenerator.getInstance("RSA")

    // Key moduli for encryption/decryption are 2048 bits long.
    keyGen.initialize(2048)

    keyGen.genKeyPair
  }


  private def decryptPassword(message: String, keys: KeyPair): String = {
    try { // Add the bouncycastle provider - the built-in providers don't support RSA
      // with OAEPPadding.
      Security.addProvider(new BouncyCastleProvider)
      // Get the appropriate cipher instance.
      val rsa = Cipher.getInstance("RSA/NONE/OAEPPadding", "BC")
      // Add the private key for decryption.
      rsa.init(Cipher.DECRYPT_MODE, keys.getPrivate)
      // Decrypt the text.
      val rawMessage = Base64.decodeBase64(message)
      val decryptedText = rsa.doFinal(rawMessage)
      // The password was encoded using UTF8. Transform into string.
      return new String(decryptedText, StandardCharsets.UTF_8)
    } catch {
      case e: Exception =>
        e.printStackTrace()
    }
    ""
  }

  private def replaceMetadata(input: Metadata, newMetadataItem: Map[String, String]): Unit = {
    // Transform the JSON object into a string that the API can use.
    val newItemString = newMetadataItem.toJson.toString()
    // Get the list containing all of the Metadata entries for this instance.
    var items = input.getItems
    // If the instance has no metadata, items can be returned as null.
    if (items == null) {
      items = new util.ArrayList[Items]()
      input.setItems(items)
    }
    // Find the "windows-keys" entry and update it.
    for ((item: Items) <- items) {
      if (item.getKey.compareTo("windows-keys") == 0) { // Replace item's value with the new entry.
        // To prevent race conditions, production code may want to maintain a
        // list where the oldest entries are removed once the 32KB limit is
        // reached for the metadata entry.
        item.setValue(newItemString)
        return
      }
    }
    // "windows.keys" entry doesn't exist in the metadata - append it.
    // This occurs when running password-reset for the first time on an instance.
    items.add(new Metadata.Items().setKey("windows-keys").setValue(newItemString))
  }


  private def buildKeyMetadata(pair: KeyPair, expireTime: Long, userName: String, email: String): Map[String, String] = {
    // Object used for storing the metadata values.
    // Create the date on which the new keys expire.
    val now = new Date()
    val expireDate = new Date(now.getTime + expireTime)
    // Format the date to match rfc3339.
    val rfc3339Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    rfc3339Format.setTimeZone(TimeZone.getTimeZone("UTC"))
    val dateString = rfc3339Format.format(expireDate)
    // Encode the expiration date for the returned JSON dictionary.
    jsonEncode(pair) +
      ("userName" -> userName) +
      ("email" -> email) +
      ("expireOn" -> dateString)

  }


  @throws[NoSuchAlgorithmException]
  @throws[InvalidKeySpecException]
  private def jsonEncode(keys: KeyPair) = {

    val factory = KeyFactory.getInstance("RSA")
    // Get the RSA spec for key manipulation.
    val pubSpec = factory.getKeySpec(keys.getPublic, classOf[RSAPublicKeySpec])
    // Extract required parts of the key.
    val modulus = pubSpec.getModulus
    val exponent = pubSpec.getPublicExponent
    // Grab an encoder for the modulus and exponent to encode using RFC 3548;
    // Java SE 7 requires an external library (Google's Guava used here)
    // Java SE 8 has a built-in Base64 class that can be used instead. Apache also has an RFC 3548
    // encoder.
    val stringEncoder = BaseEncoding.base64
    // Strip out the leading 0 byte in the modulus.
    val arr = util.Arrays.copyOfRange(modulus.toByteArray, 1, modulus.toByteArray.length)

    // Encode the modulus, add to returned JSON object.
    val modulusString = stringEncoder.encode(arr).replaceAll("\n", "")
    // Encode exponent, add to returned JSON object.
    val exponentString = stringEncoder.encode(exponent.toByteArray).replaceAll("\n", "")

    Map("modulus" -> modulusString, "exponent" -> exponentString)
  }


}
