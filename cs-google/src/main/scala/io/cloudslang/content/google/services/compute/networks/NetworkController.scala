package io.cloudslang.content.google.services.compute.networks

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
