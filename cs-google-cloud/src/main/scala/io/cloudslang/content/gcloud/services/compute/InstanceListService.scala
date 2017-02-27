package io.cloudslang.content.gcloud.services.compute

import java.io.FileInputStream

import com.google.api.client.googleapis.auth.clientlogin.ClientLogin
import com.google.api.services.compute.model._
import com.google.api.services.compute.{Compute, ComputeScopes}
import io.cloudslang.content.gcloud.utils.{GoogleAuth, HttpTransportUtils, JsonFactoryUtils}

import scala.collection.JavaConversions._

/**
  * Created by victor on 2/23/17.
  */
object InstanceListService {

  def main(args: Array[String]): Unit = {

    val project = "cogent-range-159508"
    val zone = "europe-west1-d"
    val jsonToken = "/home/victor/Desktop/My First Project-af6900eca363.json"

    val httpTransport = HttpTransportUtils.getNetHttpTransport(Some("web-proxy.corp.hpecorp.net"), 8080)
    val jsonFactory = JsonFactoryUtils.getDefaultJacksonFactory

    val credential = GoogleAuth.fromJsonWithScopes(new FileInputStream(jsonToken),
      httpTransport, jsonFactory, List(ComputeScopes.COMPUTE_READONLY))

    val computeInstances = ComputeService.instancesService(httpTransport, jsonFactory, credential)
    val request = computeInstances.list(project, zone)

    var response: InstanceList = null
    do {
      response = request.execute()
      if (response.getItems != null) {

        response.getItems.foreach { (i: Instance) => println(i.getName) }

        request.setPageToken(response.getNextPageToken)
      }
    } while (response.getNextPageToken != null)
  }
}
