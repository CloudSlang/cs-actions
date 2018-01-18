/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
