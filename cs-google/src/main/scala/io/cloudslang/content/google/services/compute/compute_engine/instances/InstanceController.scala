package io.cloudslang.content.google.services.compute.compute_engine.instances


import com.google.api.services.compute.model.Metadata.Items
import com.google.api.services.compute.model._
import io.cloudslang.content.utils.CollectionUtilities
import io.cloudslang.content.utils.CollectionUtilities.toList

import scala.collection.JavaConversions._

/**
  * Created by victor on 26.04.2017.
  */
object InstanceController {
  def createInstance(instanceName: String, description: String, zone: String, schedulingOpt: Option[Scheduling], machineType: String,
                     metadata: Metadata, tags: Tags, bootDisk: AttachedDisk, networkInterface: NetworkInterface, canIpForward: Boolean,
                     serviceAccountOpt: Option[ServiceAccount]): Instance = {
    val instance = new Instance()
      .setName(instanceName)
      .setDescription(description)
      .setZone(zone)
      .setMachineType(machineType)
      .setMetadata(metadata)
      .setTags(tags)
      .setDisks(List(bootDisk))
      .setNetworkInterfaces(List(networkInterface))
      .setCanIpForward(canIpForward)

    (serviceAccountOpt, schedulingOpt) match {
      case (Some(serviceAccount), Some(scheduling)) => instance.setServiceAccounts(List(serviceAccount))
        .setScheduling(scheduling)
      case (Some(serviceAccount), None) => instance.setServiceAccounts(List(serviceAccount))
      case (None, Some(scheduling)) => instance.setScheduling(scheduling)
      case _ => instance
    }

  }

  def createMetadataItems(itemsKeysList: String, itemsValuesList: String, itemsDelimiter: String): List[Items] =
    toList(itemsKeysList, itemsDelimiter)
      .zip(toList(itemsValuesList, itemsDelimiter))
      .map { case (key, value) =>
        new Items()
          .setKey(key)
          .setValue(value)
      }
      .toList

  def createTags(tagsList: String, tagsDelimiter: String): Tags =
    new Tags().setItems(toList(tagsList, tagsDelimiter))

  def getServiceAccount(serviceAccountEmail: Option[String], scopesListStr: Option[String], scopesDel: String): Option[ServiceAccount] = (serviceAccountEmail, scopesListStr) match {
    case (Some(servAccEmail), Some(scopeListSt)) => Some(new ServiceAccount()
      .setEmail(servAccEmail)
      .setScopes(CollectionUtilities.toList(scopeListSt, scopesDel)))
    case _ => None
  }

  def createScheduling(onHostMaintenanceOpt: Option[String],
                       automaticRestart: Boolean,
                       preemptible: Boolean): Option[Scheduling] = onHostMaintenanceOpt match {
    case Some(onHostMaintenance) => Some(new Scheduling()
      .setAutomaticRestart(automaticRestart)
      .setOnHostMaintenance(onHostMaintenance)
      .setPreemptible(preemptible))
    case _ => None
  }
}
