package io.cloudslang.content.google.services.compute.disks

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.services.compute.model._
import io.cloudslang.content.google.services.compute.ComputeService

import scala.collection.JavaConversions._

/**
  * Created by victor on 03.03.2017.
  */
object DiskService {

  def list(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, zone: String,
           filterOpt: Option[String], orderByOpt: Option[String]): List[Disk] = {
    val computeDisks = ComputeService.disksService(httpTransport, jsonFactory, credential)
    val request = computeDisks.list(project, zone)

    filterOpt.foreach { filter => request.setFilter(filter) }
    orderByOpt.foreach { orderBy => request.setOrderBy(orderBy) }

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

  def delete(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, zone: String, diskName: String): Operation =
    ComputeService.disksService(httpTransport, jsonFactory, credential)
      .delete(project, zone, diskName)
      .execute()
}
