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
import io.cloudslang.content.google.utils.Constants._
import io.cloudslang.content.google.utils.action.InputNames.EMAIL
import io.cloudslang.content.google.utils.exceptions.OperationException
import org.apache.commons.lang3.StringUtils.EMPTY
import org.bouncycastle.jce.provider.BouncyCastleProvider
import spray.json.DefaultJsonProtocol._
import spray.json._

import scala.annotation.tailrec
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration.Inf
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.language.postfixOps
import scala.util.Try

object WindowsService {

  def resetWindowsPassword(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String,
                           zone: String, instanceName: String, userName: String, emailOpt: Option[String], syncTime: Long,
                           timeout: Long, pollingInterval: Long): String = {
    val keyPair = generateKeyPair()

    Await.result(Future {
      val metadata = prepareInstanceMetadata(httpTransport, jsonFactory, credential, project, zone, instanceName, userName, emailOpt, syncTime, 0, keyPair, pollingInterval)

      def passwordFrom: (Long) => (Long, Option[String]) = {
        Thread.sleep(pollingInterval)
        getPasswordFrom(keyPair, httpTransport, jsonFactory, credential, project, zone, instanceName, userName,
          metadata.get(MODULUS), metadata.get(EXPONENT))
      }

      waitForPassword(passwordFrom)
    }, if (timeout == 0) Inf else timeout seconds)
  }

  @tailrec
  private def waitForPassword(call: (Long) => (Long, Option[String]), from: Long = 0): String = call(from) match {
    case (next, None) => waitForPassword(call, next)
    case (_, Some(password)) => password
  }

  private def getPasswordFrom(keyPair: KeyPair, httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String,
                              zone: String, instanceName: String, username: String, modulus: Option[String], exponent: Option[String])
                             (from: Long): (Long, Option[String]) = {
    val serialOutput = InstanceService.getSerialPortOutput(httpTransport, jsonFactory, credential, project, zone, instanceName, 4, from)
    val next = serialOutput.getNext
    val password = serialOutput.getContents
      .split(NEW_LINE)
      .reverse
      .toStream
      .flatMap { line => Try(line.parseJson.convertTo[Map[String, JsValue]]).toOption }
      .find(mapEntry =>
        List(mapEntry.get(USER_NAME), mapEntry.get(MODULUS), mapEntry.get(EXPONENT)).flatten.map(_.convertTo[String]) ==
          List(Some(username), modulus, exponent).flatten)
      .map { mapEntry =>
        if (mapEntry.containsKey(ERROR_MESSAGE)) {
          throw OperationException(mapEntry.getOrElse(ERROR_MESSAGE, EMPTY).toString)
        }
        val encryptedPassword: String = mapEntry.getOrElse(ENCRYPTED_PASSWORD, EMPTY).toString

        decryptPassword(encryptedPassword, keyPair)
      }
    (next, password)
  }

  private def prepareInstanceMetadata(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String,
                                      zone: String, instanceName: String, userName: String, emailOpt: Option[String], syncTime: Long,
                                      timeout: Long, keyPair: KeyPair, pollingInterval: Long): Map[String, String] = {
    val instanceMetadata = InstanceService.get(httpTransport, jsonFactory, credential, project, zone, instanceName)
      .getMetadata
    val rfc339FormattedDate = rfc339DateFormat(timeWithOffset(syncTime))

    val keyMetadata = buildKeyMetadata(keyPair, rfc339FormattedDate, userName, emailOpt)
    replaceMetadata(instanceMetadata, keyMetadata)

    val metadataOp = InstanceService.setMetadata(httpTransport, jsonFactory, credential, project, zone, instanceName,
      instanceMetadata, async = false, timeout, pollingInterval)
    if (metadataOp.getError != null) {
      throw OperationException(Try(metadataOp.getError.toPrettyString).getOrElse(EMPTY))
    }

    keyMetadata
  }

  private def replaceMetadata(input: Metadata, newMetadataItem: Map[String, String]): Unit = {
    val newItemString = newMetadataItem.toJson.toString

    Option(input.getItems) match {
      case Some(items) => items.find(_.getKey.compareTo(WINDOWS_KEYS) == 0) match {
        case Some(item) => item.setValue(newItemString)
        case None => items.add(new Metadata.Items().setKey(WINDOWS_KEYS).setValue(newItemString))
      }
      case None => input.setItems(List(new Metadata.Items().setKey(WINDOWS_KEYS).setValue(newItemString)).asJava)
    }
  }

  private def buildKeyMetadata(pair: KeyPair, dateString: String, userName: String, emailOpt: Option[String]): Map[String, String] = {
    val newMetadata = jsonEncode(pair) +
      (USER_NAME -> userName) +
      (EXPIRE_ON -> dateString)
    emailOpt match {
      case Some(email) => newMetadata + (EMAIL -> email)
      case _ => newMetadata
    }
  }

  private def encodeBase64: (Array[Byte]) => String = base64.encode(_).replaceAll(NEW_LINE, EMPTY)

  private def jsonEncode(keys: KeyPair): Map[String, String] = {
    val factory = KeyFactory.getInstance(RSA_KEY)
    val pubSpec = factory.getKeySpec(keys.getPublic, classOf[RSAPublicKeySpec])

    val exponent = encodeBase64(pubSpec.getPublicExponent.toByteArray)
    val modulus = encodeBase64(pubSpec.getModulus.toByteArray.tail)

    Map(MODULUS -> modulus,
      EXPONENT -> exponent)
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
