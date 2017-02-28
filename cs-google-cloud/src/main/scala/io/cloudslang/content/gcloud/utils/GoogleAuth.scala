package io.cloudslang.content.gcloud.utils

import java.io.InputStream

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.services.compute.ComputeScopes

import scala.collection.JavaConversions._

/**
  * Created by victor on 2/26/17.
  */
object GoogleAuth {

  def fromJsonWithScopes(jsonInputStream: InputStream, transport: HttpTransport, jsonFactory: JsonFactory, scopes: List[String], timeout: Long): Credential =
    GoogleCredential.fromStream(jsonInputStream, transport, jsonFactory)
      .setExpiresInSeconds(timeout)
      .createScoped(List(ComputeScopes.COMPUTE))

  def fromAccessToken(accessToken: String): Credential =
    new GoogleCredential().setAccessToken(accessToken)
}
