package io.cloudslang.content.google.services.compute.compute_engine.operations

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.services.compute.model.Operation
import io.cloudslang.content.google.services.compute.compute_engine.ComputeService

/**
  * Created by marisca on 5/9/2017.
  */
object GlobalOperationService {
  def get(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, operationName: String): Operation =
    ComputeService.globalOperationsService(httpTransport, jsonFactory, credential)
      .get(project, operationName)
      .execute()
}
