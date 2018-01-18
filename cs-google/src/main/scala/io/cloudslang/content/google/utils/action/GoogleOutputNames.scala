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
  final val INTERNAL_IPS = "internalIps"
  final val EXTERNAL_IPS = "externalIps"
  final val DISKS = "disks"
  final val DISK_ID = "diskId"
  final val PASSWORD = "password"
  final val NETWORK_ID = "networkId"
  final val METADATA = "metadata"
  final val TAGS = "tags"
}
