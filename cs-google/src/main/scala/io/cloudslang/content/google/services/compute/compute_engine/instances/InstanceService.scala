package io.cloudslang.content.google.services.compute.compute_engine.instances

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.services.compute.model.Metadata.Items
import com.google.api.services.compute.model._
import io.cloudslang.content.google.services.compute.compute_engine.{ComputeController, ComputeService}

import scala.collection.JavaConversions._

/**
  * Created by victor on 2/23/17.
  */
object InstanceService {

  def list(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, zone: String,
           filterOpt: Option[String], orderByOpt: Option[String]): List[Instance] = {
    val computeInstances = ComputeService.instancesService(httpTransport, jsonFactory, credential)
    val request = computeInstances.list(project, zone)

    filterOpt.foreach { filter => request.setFilter(filter) }
    orderByOpt.foreach { orderBy => request.setOrderBy(orderBy) }

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

  def start(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, zone: String, instanceName: String): Operation =
    ComputeService.instancesService(httpTransport, jsonFactory, credential)
      .start(project, zone, instanceName)
      .execute()

  def stop(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, zone: String, instanceName: String): Operation =
    ComputeService.instancesService(httpTransport, jsonFactory, credential)
      .stop(project, zone, instanceName)
      .execute()

  def restart(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, zone: String, instanceName: String): Operation =
    ComputeService.instancesService(httpTransport, jsonFactory, credential)
      .reset(project, zone, instanceName)
      .execute()

  def insert(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, zone: String, instance: Instance, sync: Boolean, timeout: Long, pollingInterval: Long): Operation = {
    val operation = ComputeService.instancesService(httpTransport, jsonFactory, credential)
      .insert(project, zone, instance)
      .execute()
    if (sync) {
      ComputeController.awaitSuccessOperation(httpTransport, jsonFactory, credential, project, zone, operation, timeout, pollingInterval)
    } else {
      operation
    }
  }

  def delete(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, zone: String, instanceName: String): Operation =
    ComputeService.instancesService(httpTransport, jsonFactory, credential)
      .delete(project, zone, instanceName)
      .execute()

  def setTags(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, zone: String, instanceName: String, tags: Tags): Operation = {
    val instanceTagFingerprint = get(httpTransport, jsonFactory, credential, project, zone, instanceName)
      .getTags.getFingerprint

    tags.setFingerprint(instanceTagFingerprint)

    ComputeService.instancesService(httpTransport, jsonFactory, credential)
      .setTags(project, zone, instanceName, tags)
      .execute()
  }

  def get(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, zone: String, instanceName: String): Instance =
    ComputeService.instancesService(httpTransport, jsonFactory, credential)
      .get(project, zone, instanceName)
      .execute()

  def setMetadata(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, zone: String, instanceName: String, items: List[Items]): Operation = {
    val computeInstances = ComputeService.instancesService(httpTransport, jsonFactory, credential)
    val metadata = get(httpTransport, jsonFactory, credential, project, zone, instanceName).getMetadata.setItems(items)

    val request = computeInstances.setMetadata(project, zone, instanceName, metadata)

    request.execute()
  }

  def attachDisk(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, zone: String, instanceName: String, attachedDisk: AttachedDisk): Operation =
    ComputeService.instancesService(httpTransport, jsonFactory, credential)
      .attachDisk(project, zone, instanceName, attachedDisk)
      .execute()

  def detachDisk(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, zone: String, instanceName: String, deviceName: String): Operation =
    ComputeService.instancesService(httpTransport, jsonFactory, credential)
      .detachDisk(project, zone, instanceName, deviceName)
      .execute()

  def getSerialPortOutput(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, zone: String, instanceName: String, port: Int, start: Long): SerialPortOutput =
    ComputeService.instancesService(httpTransport, jsonFactory, credential)
      .getSerialPortOutput(project, zone, instanceName)
      .setPort(port)
      .setStart(start)
      .execute()

}
