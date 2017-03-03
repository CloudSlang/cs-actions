package io.cloudslang.content.gcloud.services.compute.networks

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.services.compute.model._
import io.cloudslang.content.gcloud.services.compute.ComputeService

import scala.collection.JavaConversions._

/**
  * Created by victor on 3/3/17.
  */
object NetworkService {

  def list(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String): List[Network] = {
    val computeNetworks = ComputeService.networksService(httpTransport, jsonFactory, credential)
    val request = computeNetworks.list(project)

    var networks: List[Network] = List()
    var response: NetworkList = null
    do {
      response = request.execute()
      if (response.getItems != null) {
        networks ++= response.getItems
        request.setPageToken(response.getNextPageToken)
      }
    } while (response.getNextPageToken != null)

    networks
  }

  def get(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, networkName: String): Network =
    ComputeService.networksService(httpTransport, jsonFactory, credential)
      .get(project, networkName)
      .execute()

  def insert(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, network: Network): Operation =
    ComputeService.networksService(httpTransport, jsonFactory, credential)
      .insert(project, network)
      .execute()

  def delete(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, networkName: String): Operation =
    ComputeService.networksService(httpTransport, jsonFactory, credential)
      .delete(project, networkName)
      .execute()
}
