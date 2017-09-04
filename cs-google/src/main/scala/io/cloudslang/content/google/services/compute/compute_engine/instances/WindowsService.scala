package io.cloudslang.content.google.services.compute.compute_engine.instances

import java.nio.charset.StandardCharsets
import java.nio.charset.StandardCharsets.UTF_8
import java.security._
import java.security.spec.RSAPublicKeySpec
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
import com.google.common.io.BaseEncoding.base64
import org.apache.commons.lang3.StringUtils.EMPTY
import org.bouncycastle.jce.provider.BouncyCastleProvider
import spray.json.DefaultJsonProtocol._
import spray.json._

import scala.collection.JavaConversions._


object WindowsService {

  // Constants for configuring user name, email, and SSH key expiration.

  // Keys are one-time use, so the metadata doesn't need to stay around for long.
  // 5 minutes chosen to allow for differences between time on the client
  // and time on the server.
  private val WINDOWS_KEYS = "windows-keys"


  private val NEW_LINE = "\n"

  def resetWindowsPassword(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String,
                           zone: String, instanceName: String, userName: String, email: String, expireTime: Long, timeout: Long): String = {
    val keyPair = generateKeyPair()
    prepareInstanceMetadata(httpTransport, jsonFactory, credential, project, zone, instanceName, userName, email, expireTime, timeout, keyPair)
    val output = InstanceService.getSerialPortOutput(httpTransport, jsonFactory, credential, project, zone, instanceName, 4, 0)
    // Get the last line - this will be a JSON string corresponding to the
    // most recent password reset attempt.
    val entries = output.getContents.split(NEW_LINE)
    val outputEntry: String = entries.last
    // Parse output using the json-simple library.
    val passwordDict = outputEntry.parseJson.convertTo[Map[String, JsValue]]

    val encryptedPassword = passwordDict.getOrElse("encryptedPassword", EMPTY).toString //todo check if the same user, otherwise go up the list <entries>
    // return decrypted password.
    decryptPassword(encryptedPassword, keyPair)
  }

  private def prepareInstanceMetadata(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String,
                                      zone: String, instanceName: String, userName: String, email: String, expireTime: Long, timeout: Long, keyPair: KeyPair): Unit = {
    // Get the instance object to gain access to the instance's metadata.
    val instanceMetadata = InstanceService.get(httpTransport, jsonFactory, credential, project, zone, instanceName)
      .getMetadata
    // Generate the public/private key pair for encryption and decryption.
    // Update metadata from instance with new windows-keys entry.
    val offsetDate = timeWithOffset(expireTime)
    val rfc339FormatedDate = rfc339DateFormat(offsetDate)

    val keyMetadata = buildKeyMetadata(keyPair, rfc339FormatedDate, userName, email)
    replaceMetadata(instanceMetadata, keyMetadata)
    // Tell Compute Engine to update the instance metadata with our changes.

    InstanceService.setMetadata(httpTransport, jsonFactory, credential, project, zone, instanceName, instanceMetadata, sync = true, timeout)
    // Request the output from serial port 4.
    // In production code, this operation should be polled.

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
      if (item.getKey.compareTo(WINDOWS_KEYS) == 0) { // Replace item's value with the new entry.
        // To prevent race conditions, production code may want to maintain a
        // list where the oldest entries are removed once the 32KB limit is
        // reached for the metadata entry.
        item.setValue(newItemString)
        return
      }
    }
    // "windows.keys" entry doesn't exist in the metadata - append it.
    // This occurs when running password-reset for the first time on an instance.
    items.add(new Metadata.Items().setKey(WINDOWS_KEYS).setValue(newItemString))
  }

  private def timeWithOffset(offset: Long): Date = new Date(new Date().getTime + offset)

  private def rfc339DateFormat(offset: Date): String = {
    val rfc3339Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    rfc3339Format.setTimeZone(TimeZone.getTimeZone("UTC"))
    rfc3339Format.format(offset)
  }

  private def buildKeyMetadata(pair: KeyPair, dateString: String, userName: String, email: String): Map[String, String] = {
    jsonEncode(pair) +
      ("userName" -> userName) +
      ("email" -> email) +
      ("expireOn" -> dateString)
  }

  private def jsonEncode(keys: KeyPair) = {
    val factory = KeyFactory.getInstance("RSA")
    // Get the RSA spec for key manipulation.
    val pubSpec = factory.getKeySpec(keys.getPublic, classOf[RSAPublicKeySpec])
    // Extract required parts of the key.
    val modulus = pubSpec.getModulus
    val exponent = pubSpec.getPublicExponent

    // Strip out the leading 0 byte in the modulus.
    val arr = util.Arrays.copyOfRange(modulus.toByteArray, 1, modulus.toByteArray.length)

    val modulusString = base64.encode(arr).replaceAll(NEW_LINE, EMPTY)
    val exponentString = base64.encode(exponent.toByteArray).replaceAll(NEW_LINE, EMPTY)

    Map("modulus" -> modulusString, "exponent" -> exponentString)
  }

  private def generateKeyPair(): KeyPair = {
    val keyGen = KeyPairGenerator.getInstance("RSA")

    // Key moduli for encryption/decryption are 2048 bits long.
    keyGen.initialize(2048)

    keyGen.genKeyPair
  }

  private def rsaOaePaddingCipher(keys: KeyPair, rawMessage: Array[Byte]): Array[Byte] = {
    // Add the bouncycastle provider - the built-in providers don't support RSA
    // with OAEPPadding.
    Security.addProvider(new BouncyCastleProvider)
    // Get the appropriate cipher instance.
    val rsa = Cipher.getInstance("RSA/NONE/OAEPPadding", "BC")
    // Add the private key for decryption.
    rsa.init(Cipher.DECRYPT_MODE, keys.getPrivate)
    // Decrypt the text.
    rsa.doFinal(rawMessage)
    }

  private def decryptPassword(message: String, keys: KeyPair): String = {
    val rawMessage = Base64.decodeBase64(message)
    val decryptedText = rsaOaePaddingCipher(keys, rawMessage)
    new String(decryptedText, UTF_8)
  }


}
