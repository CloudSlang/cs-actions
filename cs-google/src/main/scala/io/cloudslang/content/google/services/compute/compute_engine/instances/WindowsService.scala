package io.cloudslang.content.google.services.compute.compute_engine.instances

import java.nio.charset.StandardCharsets.UTF_8
import java.security._
import java.security.spec.RSAPublicKeySpec
import java.text.SimpleDateFormat
import java.util.{Date, TimeZone}
import javax.crypto.Cipher
import javax.crypto.Cipher.DECRYPT_MODE

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64.decodeBase64
import com.google.api.services.compute.model.Metadata
import com.google.common.io.BaseEncoding.base64
import org.apache.commons.lang3.StringUtils.EMPTY
import org.bouncycastle.jce.provider.BouncyCastleProvider
import spray.json.DefaultJsonProtocol._
import spray.json._

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._


object WindowsService {

  // Constants for configuring user name, email, and SSH key expiration.

  // Keys are one-time use, so the metadata doesn't need to stay around for long.
  // 5 minutes chosen to allow for differences between time on the client
  // and time on the server.
  private val WINDOWS_KEYS = "windows-keys"
  private val RSA_KEY = "RSA"
  private val NEW_LINE = "\n"
  private val MODULUS = "modulus"
  private val EXPONENT = "exponent"

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
                                      zone: String, instanceName: String, userName: String, email: String, expireTime: Long,
                                      timeout: Long, keyPair: KeyPair): Unit = {
    val instanceMetadata = InstanceService.get(httpTransport, jsonFactory, credential, project, zone, instanceName)
      .getMetadata
    val rfc339FormattedDate = rfc339DateFormat(timeWithOffset(expireTime))

    val keyMetadata = buildKeyMetadata(keyPair, rfc339FormattedDate, userName, email)
    replaceMetadata(instanceMetadata, keyMetadata)
    // Tell Compute Engine to update the instance metadata with our changes.

    InstanceService.setMetadata(httpTransport, jsonFactory, credential, project, zone, instanceName, instanceMetadata, sync = true, timeout)

  }

  private def replaceMetadata(input: Metadata, newMetadataItem: Map[String, String]): Unit = {
    // Transform the JSON object into a string that the API can use.
    val newItemString = newMetadataItem.toJson.toString
    // Get the list containing all of the Metadata entries for this instance.
    Option(input.getItems) match {
      case Some(items) => items.find(_.getKey.compareTo(WINDOWS_KEYS) == 0) match {
        case Some(item) => item.setValue(newItemString)
        case None => items.add(new Metadata.Items().setKey(WINDOWS_KEYS).setValue(newItemString))
      }
      case None => input.setItems(List(new Metadata.Items().setKey(WINDOWS_KEYS).setValue(newItemString)).asJava)
    }
  }

  private def buildKeyMetadata(pair: KeyPair, dateString: String, userName: String, email: String): Map[String, String] = {
    jsonEncode(pair) +
      ("userName" -> userName) +
      ("email" -> email) +
      ("expireOn" -> dateString)
  }

  private def jsonEncode(keys: KeyPair) = {
    val factory = KeyFactory.getInstance(RSA_KEY)
    // Get the RSA spec for key manipulation.
    val pubSpec = factory.getKeySpec(keys.getPublic, classOf[RSAPublicKeySpec])
    // Extract required parts of the key.
    val modulus = pubSpec.getModulus
    val exponent = pubSpec.getPublicExponent

    // Strip out the leading 0 byte in the modulus.
    val newModulus = modulus.toByteArray.tail

    val modulusString = base64.encode(newModulus).replaceAll(NEW_LINE, EMPTY)
    val exponentString = base64.encode(exponent.toByteArray).replaceAll(NEW_LINE, EMPTY)

    Map(MODULUS -> modulusString,
      EXPONENT -> exponentString)
  }

  private def timeWithOffset(offset: Long): Date = new Date(new Date().getTime + offset)

  private def rfc339DateFormat(offset: Date): String = {
    val rfc3339Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    rfc3339Format.setTimeZone(TimeZone.getTimeZone("UTC"))
    rfc3339Format.format(offset)
  }

  private def generateKeyPair(): KeyPair = {
    val keyGen = KeyPairGenerator.getInstance(RSA_KEY)
    keyGen.initialize(2048)
    keyGen.genKeyPair
  }

  private def decryptPassword(message: String, keys: KeyPair): String = {
    val rawMessage = decodeBase64(message)
    val decryptedText = rsaOaepPaddingCipher(keys, rawMessage)
    new String(decryptedText, UTF_8)
  }

  private def rsaOaepPaddingCipher(keys: KeyPair, rawMessage: Array[Byte]): Array[Byte] = {
    Security.addProvider(new BouncyCastleProvider)
    val rsa = Cipher.getInstance("RSA/NONE/OAEPPadding", "BC")
    rsa.init(DECRYPT_MODE, keys.getPrivate)
    rsa.doFinal(rawMessage)
  }

}
