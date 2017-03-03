package io.cloudslang.content.gcloud.services.compute.disks

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.services.compute.model._
import io.cloudslang.content.gcloud.services.compute.ComputeService

import scala.collection.JavaConversions._

/**
  * Created by victor on 03.03.2017.
  */
object DiskService {


  def list(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, zone: String): List[Disk] = {
    val computeInstances = ComputeService.disksService(httpTransport, jsonFactory, credential)
    val request = computeInstances.list(project, zone)

    var disks: List[Disk] = List()
    var response: DiskList = null
    do {
      response = request.execute()
      if (response.getItems != null) {
        disks ++= response.getItems
        request.setPageToken(response.getNextPageToken)
      }
    } while (response.getNextPageToken != null)

    disks
  }

  def get(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, zone: String, diskName: String): Disk =
    ComputeService.disksService(httpTransport, jsonFactory, credential)
      .get(project, zone, diskName)
      .execute()


  def insert(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, zone: String, disk: Disk): Operation =
    ComputeService.disksService(httpTransport, jsonFactory, credential)
      .insert(project, zone, disk)
      .execute()

}
