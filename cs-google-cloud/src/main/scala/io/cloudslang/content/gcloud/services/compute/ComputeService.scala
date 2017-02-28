package io.cloudslang.content.gcloud.services.compute


import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.services.compute.Compute

/**
  * Created by victor on 2/26/17.
  */
object ComputeService {

  def instancesService(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential): Compute#Instances =
    new Compute(httpTransport, jsonFactory, credential).instances()
}
