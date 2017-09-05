package io.cloudslang.content.google.services.compute.compute_engine.networks

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.services.compute.model._
import io.cloudslang.content.google.services.compute.compute_engine.{ComputeController, ComputeService}

import scala.collection.JavaConversions._

/**
  * Created by victor on 3/3/17.
  */
object NetworkService {

  def list(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, filterOpt: Option[String],
           orderByOpt: Option[String]): List[Network] = {
    val computeNetworks = ComputeService.networksService(httpTransport, jsonFactory, credential)
    val request = computeNetworks.list(project)

    filterOpt.foreach { filter => request.setFilter(filter) }
    orderByOpt.foreach { orderBy => request.setOrderBy(orderBy) }

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

  def insert(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, network: Network, sync: Boolean, timeout: Long, pollingInterval: Long): Operation = {
    val operation = ComputeService.networksService(httpTransport, jsonFactory, credential)
      .insert(project, network)
      .execute()
    if (sync) {
      ComputeController.awaitSuccessOperation(httpTransport, jsonFactory, credential, project, zone = None, operation, timeout, pollingInterval)
    } else {
      operation
    }
  }

  def delete(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, networkName: String, sync: Boolean, timeout: Long, pollingInterval: Long): Operation = {
    val operation = ComputeService.networksService(httpTransport, jsonFactory, credential)
      .delete(project, networkName)
      .execute()
    if (sync) {
      ComputeController.awaitSuccessOperation(httpTransport, jsonFactory, credential, project, zone = None, operation, timeout, pollingInterval)
    } else {
      operation
    }
  }
}
