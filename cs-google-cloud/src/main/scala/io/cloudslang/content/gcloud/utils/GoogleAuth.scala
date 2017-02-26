package io.cloudslang.content.gcloud.utils

import java.io.InputStream

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory

import scala.collection.JavaConversions._

/**
  * Created by victor on 2/26/17.
  */
object GoogleAuth {

  def fromJsonWithScopes(fileinputStream: InputStream, transport: HttpTransport, jsonFactory: JsonFactory, scopes: List[String]): Credential =
    GoogleCredential.fromStream(fileinputStream, transport, jsonFactory)
      .createScoped(scopes)
}
