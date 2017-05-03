package io.cloudslang.content.google.services.compute.operations

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.services.compute.model.Operation
import io.cloudslang.content.google.services.compute.ComputeService

/**
  * Created by sandorr 
  * 3/1/2017.
  */
object ZoneOperationService {
  def get(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, zone: String, operationName: String): Operation =
    ComputeService.zoneOperationsService(httpTransport, jsonFactory, credential)
      .get(project, zone, operationName)
      .execute()
}
