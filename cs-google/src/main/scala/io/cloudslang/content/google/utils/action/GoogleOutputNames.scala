/*
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package io.cloudslang.content.google.utils.action

import io.cloudslang.content.constants.OutputNames

/**
  * Created by sandorr 
  * 3/2/2017.
  */
object GoogleOutputNames extends OutputNames {
  final val STATUS = "status"
  final val ZONE_OPERATION_NAME = "zoneOperationName"
  final val NEXT_INDEX = "nextIndex"
  final val INSTANCE_ID = "instanceId"
  final val INSTANCE_DETAILS = "instanceDetails"
  final val NETWORK_NAME = "networkName"
  final val IPS = "ips"
  final val DISKS = "disks"
  final val DISK_ID = "diskId"
  final val PASSWORD = "password"
  final val NETWORK_ID = "networkId"
  final val METADATA = "metadata"
  final val TAGS = "tags"
}
