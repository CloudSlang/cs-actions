package io.cloudslang.content.gcloud.services.compute.instances

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.services.compute.model.Metadata.Items
import com.google.api.services.compute.model._
import io.cloudslang.content.gcloud.services.compute.ComputeService
import org.apache.commons.lang3.StringUtils

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

  def get(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, zone: String, instanceName: String): Instance =
    ComputeService.instancesService(httpTransport, jsonFactory, credential)
      .get(project, zone, instanceName)
      .execute()

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

  def insert(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, zone: String, instance: Instance): Operation =
    ComputeService.instancesService(httpTransport, jsonFactory, credential)
      .insert(project, zone, instance)
      .execute()

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

  def setMetadata(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, zone: String, instanceName: String, items: List[Items]): Operation = {
    val computeInstances = ComputeService.instancesService(httpTransport, jsonFactory, credential)
    val metadata = get(httpTransport, jsonFactory, credential, project, zone, instanceName).getMetadata.setItems(items)

    val request = computeInstances.setMetadata(project, zone, instanceName, metadata)

    request.execute()
  }

}
