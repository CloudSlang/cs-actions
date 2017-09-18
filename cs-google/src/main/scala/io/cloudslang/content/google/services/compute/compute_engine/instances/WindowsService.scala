/*
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
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
import io.cloudslang.content.google.utils.Constants.NEW_LINE
import org.apache.commons.lang3.StringUtils.EMPTY
import org.bouncycastle.jce.provider.BouncyCastleProvider
import spray.json.DefaultJsonProtocol._
import spray.json._

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._


object WindowsService {
  private val WINDOWS_KEYS = "windows-keys"
  private val RSA_KEY = "RSA"
  private val MODULUS = "modulus"
  private val EXPONENT = "exponent"
  private val USER_NAME = "userName"
  private val EXPIRE_ON = "expireOn"
  private val EMAIL = "email"

  def resetWindowsPassword(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String,
                           zone: String, instanceName: String, userName: String, emailOpt: Option[String], syncTime: Long,
                           timeout: Long, pollingInterval: Long): Option[String] = {
    val keyPair = generateKeyPair()
    val metadata = prepareInstanceMetadata(httpTransport, jsonFactory, credential, project, zone, instanceName, userName, emailOpt, syncTime, timeout, keyPair, pollingInterval)
    InstanceService.getSerialPortOutput(httpTransport, jsonFactory, credential, project, zone, instanceName, 4, 0)
      .getContents
      .split(NEW_LINE)
      .reverse
      .toStream
      .map(_.parseJson.convertTo[Map[String, JsValue]])
      .find(mapEntry =>
        List(mapEntry.get(USER_NAME), mapEntry.get(MODULUS), mapEntry.get(EXPONENT)).flatten.map(_.convertTo[String]) ==
          List(Some(userName), metadata.get(MODULUS), metadata.get(EXPONENT)).flatten)
      .map { mapEntry =>
        val encryptedPassword: String = mapEntry.getOrElse("encryptedPassword", EMPTY).toString
        decryptPassword(encryptedPassword, keyPair)
      }
  }

  private def prepareInstanceMetadata(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String,
                                      zone: String, instanceName: String, userName: String, emailOpt: Option[String], syncTime: Long,
                                      timeout: Long, keyPair: KeyPair, pollingInterval: Long): Map[String, String] = {
    val instanceMetadata = InstanceService.get(httpTransport, jsonFactory, credential, project, zone, instanceName)
      .getMetadata
    val rfc339FormattedDate = rfc339DateFormat(timeWithOffset(syncTime))

    val keyMetadata = buildKeyMetadata(keyPair, rfc339FormattedDate, userName, emailOpt)
    replaceMetadata(instanceMetadata, keyMetadata)

    InstanceService.setMetadata(httpTransport, jsonFactory, credential, project, zone, instanceName, instanceMetadata, sync = true, timeout, pollingInterval)
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

  private def encodeBase64:(Array[Byte]) => String = base64.encode(_).replaceAll(NEW_LINE, EMPTY)

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
