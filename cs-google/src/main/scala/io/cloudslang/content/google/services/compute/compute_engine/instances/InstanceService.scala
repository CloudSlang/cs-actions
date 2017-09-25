/*
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
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

  def start(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, zone: String,
            instanceName: String, async: Boolean, timeout: Long, pollingInterval: Long): Operation = {
    val operation = ComputeService.instancesService(httpTransport, jsonFactory, credential)
      .start(project, zone, instanceName)
      .execute()
    ComputeController.awaitSuccessOperation(httpTransport, jsonFactory, credential, project, Some(zone), operation, async,
      timeout, pollingInterval)

  }

  def stop(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, zone: String,
           instanceName: String, async: Boolean, timeout: Long, pollingInterval: Long): Operation = {
    val operation = ComputeService.instancesService(httpTransport, jsonFactory, credential)
      .stop(project, zone, instanceName)
      .execute()
    ComputeController.awaitSuccessOperation(httpTransport, jsonFactory, credential, project, Some(zone), operation, async,
      timeout, pollingInterval)

  }

  def restart(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, zone: String,
              instanceName: String, async: Boolean, timeout: Long, pollingInterval: Long): Operation = {
    val operation = ComputeService.instancesService(httpTransport, jsonFactory, credential)
      .reset(project, zone, instanceName)
      .execute()
    ComputeController.awaitSuccessOperation(httpTransport, jsonFactory, credential, project, Some(zone), operation, async,
      timeout, pollingInterval)

  }

  def insert(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, zone: String,
             instance: Instance, async: Boolean, timeout: Long, pollingInterval: Long): Operation = {
    val operation = ComputeService.instancesService(httpTransport, jsonFactory, credential)
      .insert(project, zone, instance)
      .execute()
    ComputeController.awaitSuccessOperation(httpTransport, jsonFactory, credential, project, Some(zone), operation, async,
      timeout, pollingInterval)
  }

  def delete(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, zone: String,
             instanceName: String, async: Boolean, timeout: Long, pollingInterval: Long): Operation = {
    val operation: Operation = ComputeService.instancesService(httpTransport, jsonFactory, credential)
      .delete(project, zone, instanceName)
      .execute()
    ComputeController.awaitSuccessOperation(httpTransport, jsonFactory, credential, project, Some(zone), operation, async,
      timeout, pollingInterval)
  }

  def setTags(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, zone: String,
              instanceName: String, tags: Tags, async: Boolean, timeout: Long, pollingInterval: Long): Operation = {
    val instanceTagFingerprint = get(httpTransport, jsonFactory, credential, project, zone, instanceName)
      .getTags.getFingerprint

    tags.setFingerprint(instanceTagFingerprint)

    val operation = ComputeService.instancesService(httpTransport, jsonFactory, credential)
      .setTags(project, zone, instanceName, tags)
      .execute()
    ComputeController.awaitSuccessOperation(httpTransport, jsonFactory, credential, project, Some(zone), operation, async,
      timeout, pollingInterval)
  }

  def setMetadata(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, zone: String,
                  instanceName: String, items: List[Items], async: Boolean, timeout: Long, pollingInterval: Long): Operation = {
    val metadata = get(httpTransport, jsonFactory, credential, project, zone, instanceName)
      .getMetadata
      .setItems(items)
    setMetadata(httpTransport, jsonFactory, credential, project, zone, instanceName, metadata, async, timeout, pollingInterval)
  }

  def get(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, zone: String,
          instanceName: String): Instance =
    ComputeService.instancesService(httpTransport, jsonFactory, credential)
      .get(project, zone, instanceName)
      .execute()

  def setMetadata(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, zone: String,
                  instanceName: String, metadata: Metadata, async: Boolean, timeout: Long, pollingInterval: Long): Operation = {
    val operation = ComputeService.instancesService(httpTransport, jsonFactory, credential)
      .setMetadata(project, zone, instanceName, metadata)
      .execute()
    ComputeController.awaitSuccessOperation(httpTransport, jsonFactory, credential, project, Some(zone), operation, async,
      timeout, pollingInterval)
  }

  def attachDisk(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, zone: String,
                 instanceName: String, attachedDisk: AttachedDisk, async: Boolean, timeout: Long, pollingInterval: Long): Operation = {
    val operation = ComputeService.instancesService(httpTransport, jsonFactory, credential)
      .attachDisk(project, zone, instanceName, attachedDisk)
      .execute()
    ComputeController.awaitSuccessOperation(httpTransport, jsonFactory, credential, project, Some(zone), operation, async,
      timeout, pollingInterval)
  }

  def detachDisk(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, zone: String,
                 instanceName: String, deviceName: String, async: Boolean, timeout: Long, pollingInterval: Long): Operation = {
    val operation = ComputeService.instancesService(httpTransport, jsonFactory, credential)
      .detachDisk(project, zone, instanceName, deviceName)
      .execute()
    ComputeController.awaitSuccessOperation(httpTransport, jsonFactory, credential, project, Some(zone), operation, async,
      timeout, pollingInterval)
  }

  def getSerialPortOutput(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String,
                          zone: String, instanceName: String, port: Int, start: Long): SerialPortOutput =
    ComputeService.instancesService(httpTransport, jsonFactory, credential)
      .getSerialPortOutput(project, zone, instanceName)
      .setPort(port)
      .setStart(start)
      .execute()

  def setMachineType(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String,
                     zone: String, instanceName: String, machineTypeRequest: InstancesSetMachineTypeRequest, async: Boolean,
                     timeout: Long, pollingInterval: Long): Operation = {
    val operation = ComputeService.instancesService(httpTransport, jsonFactory, credential)
      .setMachineType(project, zone, instanceName, machineTypeRequest)
      .execute()
    ComputeController.awaitSuccessOperation(httpTransport, jsonFactory, credential, project, Some(zone), operation, async, timeout, pollingInterval)
  }


}
