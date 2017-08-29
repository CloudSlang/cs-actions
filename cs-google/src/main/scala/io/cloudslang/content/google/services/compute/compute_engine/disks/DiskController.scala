package io.cloudslang.content.google.services.compute.compute_engine.disks

import com.google.api.services.compute.model._
import io.cloudslang.content.utils.CollectionUtilities.toList

/**
  * Created by victor on 3/5/17.
  */
object DiskController {
  def createDisk(zone: String, diskName: String, sourceImageOpt: Option[String], snapshotImageOpt: Option[String], imageEncryptionKeyOpt: Option[String],
                 diskEncryptionKeyOpt: Option[String], diskType: String, diskDescription: String, licensesList: String,
                 licensesDel: String, diskSize: Long): Disk = {
    val computeDisk = new Disk()
      .setName(diskName)
      .setDescription(diskDescription)
      .setZone(zone)
      .setType(diskType)
      .setLicenses(toList(licensesList, licensesDel))
      .setSizeGb(diskSize)

    if (sourceImageOpt.isDefined) {
      computeDisk.setSourceImage(sourceImageOpt.get)
      imageEncryptionKeyOpt.foreach { encrypt => computeDisk.setSourceImageEncryptionKey(new CustomerEncryptionKey().setRawKey(encrypt)) }
    } else if (snapshotImageOpt.isDefined) {
      computeDisk.setSourceSnapshot(snapshotImageOpt.get)
      imageEncryptionKeyOpt.foreach { encrypt => computeDisk.setSourceSnapshotEncryptionKey(new CustomerEncryptionKey().setRawKey(encrypt)) }
    }
    diskEncryptionKeyOpt.foreach { encrypt => computeDisk.setDiskEncryptionKey(new CustomerEncryptionKey().setRawKey(encrypt)) }
    computeDisk
  }

  def createAttachedDisk(boot: Boolean,
                         autoDelete: Boolean,
                         mountMode: String,
                         sourceOpt: Option[String] = None,
                         deviceNameOpt: Option[String] = None,
                         mountTypeOpt: Option[String] = None,
                         interfaceOpt: Option[String] = None,
                         initializeParamsOpt: Option[AttachedDiskInitializeParams] = None): AttachedDisk = {
    val attachedDisk = new AttachedDisk()
      .setBoot(boot)
      .setAutoDelete(autoDelete)
      .setMode(mountMode)

    sourceOpt.foreach(source => attachedDisk.setSource(source))

    mountTypeOpt.foreach(mountType => attachedDisk.setType(mountType))

    initializeParamsOpt.foreach(initializeParams => attachedDisk.setInitializeParams(initializeParams))

    interfaceOpt.foreach(interface => attachedDisk.setInterface(interface))

    deviceNameOpt match {
      case Some(deviceName) => attachedDisk.setDeviceName(deviceName)
      case _ => attachedDisk
    }
  }

  def createAttachedDiskInitializeParams(diskName: String, diskSourceImage: String, diskTypeOpt: Option[String], diskSize: Long): AttachedDiskInitializeParams = {
    val initParam = new AttachedDiskInitializeParams()
      .setDiskName(diskName)
      .setSourceImage(diskSourceImage)
      .setDiskSizeGb(diskSize)

    diskTypeOpt match {
      case Some(diskType) => initParam.setDiskType(diskType)
      case _ => initParam
    }
  }
}
