package io.cloudslang.content.gcloud.services.compute


import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.services.compute.Compute

/**
  * Created by victor on 2/26/17.
  */
object ComputeService {

  def instancesService: (HttpTransport, JsonFactory, Credential) => Compute#Instances = computeService(_, _, _).instances()

  def zoneOperationsService: (HttpTransport, JsonFactory, Credential) => Compute#ZoneOperations = computeService(_, _, _).zoneOperations()

  def disksService: (HttpTransport, JsonFactory, Credential) => Compute#Disks = computeService(_, _, _).disks()

  def networksService: (HttpTransport, JsonFactory, Credential) => Compute#Networks = computeService(_, _, _).networks()

  private def computeService(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential): Compute = new Compute(httpTransport, jsonFactory, credential)
}
