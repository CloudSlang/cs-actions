/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
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



package io.cloudslang.content.google.services.compute.compute_engine.networks

import com.google.api.services.compute.model.{AccessConfig, Network, NetworkInterface}

import scala.collection.JavaConversions._

/**
  * Created by victor on 26.04.2017.
  */
object NetworkController {
  def createNetwork(networkName: String, networkDescription: String, autoCreateSubnetworks: Boolean, ipV4Range: Option[String]): Network = {
    val network = new Network()
      .setName(networkName)
      .setDescription(networkDescription)
    if (ipV4Range.isDefined) {
      network.setIPv4Range(ipV4Range.get)
    } else {
      network.setAutoCreateSubnetworks(autoCreateSubnetworks)
    }
  }

  def createNetworkInterface(networkOpt: Option[String],
                             subNetworkOpt: Option[String],
                             accessConfigNameOpt: Option[String],
                             accessConfigType: String): NetworkInterface = {
    val networkInterface = (networkOpt, subNetworkOpt) match {
      case (Some(network), Some(subNetwork)) => new NetworkInterface().setNetwork(network).setSubnetwork(subNetwork)
      case (Some(network), None) => new NetworkInterface().setNetwork(network)
      case (None, Some(subNetwork)) => new NetworkInterface().setSubnetwork(subNetwork)
      case _ => new NetworkInterface()
    }
    accessConfigNameOpt match {
      case Some(accessConfigName) => networkInterface.setAccessConfigs(List(new AccessConfig()
        .setName(accessConfigName)
        .setType(accessConfigType)))
      case _ => networkInterface
    }
  }
}
