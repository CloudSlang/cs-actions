package io.cloudslang.content.gcloud.services.compute.instances

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.services.compute.model._
import io.cloudslang.content.gcloud.services.compute.ComputeService

import scala.collection.JavaConversions._

/**
  * Created by victor on 2/23/17.
  */
object InstanceService {

  def list(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, zone: String): List[Instance] = {
    val computeInstances = ComputeService.instancesService(httpTransport, jsonFactory, credential)
    val request = computeInstances.list(project, zone)

    var instances: List[Instance] = List()
    var response: InstanceList = null

    do {
      response = request.execute()
      if (response.getItems != null) {
        instances ++= response.getItems
        request.setPageToken(response.getNextPageToken)
      }
    } while (response.getNextPageToken != null)

    instances
  }

  def get(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, zone: String, instanceName: String): Instance = {
    val computeInstances = ComputeService.instancesService(httpTransport, jsonFactory, credential)
    val request = computeInstances.get(project, zone, instanceName)
    request.execute()
  }
}
