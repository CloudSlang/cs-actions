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

import io.cloudslang.content.constants.BooleanValues.TRUE
import io.cloudslang.content.google.utils.Constants.COMMA

/**
  * Created by victor on 28.02.2017.
  */
object DefaultValues {
  final val DEFAULT_CONSOLE_PORT = "1"
  final val DEFAULT_START_INDEX = "0"
  final val DEFAULT_PROXY_PORT = "8080"
  final val DEFAULT_SYNC_TIME = "300"
  final val DEFAULT_PRETTY_PRINT = TRUE
  final val DEFAULT_AUTO_CREATE_SUBNETWORKS = TRUE
  final val DEFAULT_LICENSES_DELIMITER = COMMA
  final val DEFAULT_SCOPES_DELIMITER = COMMA
  final val DEFAULT_ITEMS_DELIMITER = COMMA
  final val DEFAULT_TAGS_DELIMITER = COMMA
  final val DEFAULT_TIMEOUT = "600"
  final val DEFAULT_DISK_SIZE = "10"
  final val DEFAULT_SYNC_TIMEOUT = "30"
  final val DEFAULT_POLLING_INTERVAL = "1"
  final val DEFAULT_VOLUME_MOUNT_TYPE = "PERSISTENT"
  final val DEFAULT_VOLUME_MOUNT_MODE = "READ_WRITE"
  final val DEFAULT_ACCESS_CONFIG_TYPE = "ONE_TO_ONE_NAT"
  final val DEFAULT_INTERFACE = "SCSI"
}
