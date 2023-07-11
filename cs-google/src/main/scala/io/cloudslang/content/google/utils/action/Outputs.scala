/*
 * Copyright 2023 Open Text
 * This program and the accompanying materials
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

object Outputs {

  object SQLDatabaseInstance {
    final val CONNECTION_NAME = "connectionName"
    final val PUBLIC_IP_ADDRESS = "publicIPAddress"
    final val PRIVATE_IP_ADDRESS = "privateIPAddress"
    final val SELF_LINK = "selfLink"
  }

  object StorageBucketOutputs {
    final val ACCESS_CONTROL = "accessControl"
    final val DEFAULT_EVENT_BASED_HOLD_ENABLED = "defaultEventBasedHoldEnabled"
    final val VERSIONING_ENABLED = "versioningEnabled"
    final val LOCATION = "location"
    final val LOCATION_TYPE = "locationType"
  }

}
