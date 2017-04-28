package io.cloudslang.content.gcloud.services.compute.disks

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
                         mountType: String,
                         mountMode: String,
                         autoDelete: Boolean,
                         diskDeviceName: String,
                         diskName: String,
                         diskSourceImage: String,
                         diskType: String,
                         diskSize: Long): AttachedDisk =
    new AttachedDisk()
      .setBoot(boot)
      .setType(mountType)
      .setMode(mountMode)
      .setAutoDelete(autoDelete)
      .setDeviceName(diskDeviceName)
      .setInitializeParams(new AttachedDiskInitializeParams()
        .setDiskName(diskName)
        .setSourceImage(diskSourceImage)
        .setDiskType(diskType)
        .setDiskSizeGb(diskSize))
}
