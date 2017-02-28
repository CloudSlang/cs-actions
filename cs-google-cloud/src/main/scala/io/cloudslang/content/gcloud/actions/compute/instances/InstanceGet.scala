package io.cloudslang.content.gcloud.actions.compute.instances

import java.io.FileInputStream

import com.google.api.services.compute.ComputeScopes
import com.hp.oo.sdk.content.annotations.{Action, Output, Param, Response}
import com.hp.oo.sdk.content.plugin.ActionMetadata.{MatchType, ResponseType}
import io.cloudslang.content.constants.{OutputNames, ResponseNames, ReturnCodes}
import io.cloudslang.content.gcloud.services.compute.instances.InstanceService
import io.cloudslang.content.gcloud.utils.{GoogleAuth, HttpTransportUtils, JsonFactoryUtils}
import io.cloudslang.content.utils.{BooleanUtilities, NumberUtilities, OutputUtilities}

/**
  * Created by sandorr 
  * 2/27/2017.
  */
class InstanceGet {
  @Action(name = "Get Instance",
    outputs = Array(
      new Output(OutputNames.RETURN_CODE),
      new Output(OutputNames.RETURN_RESULT),
      new Output(OutputNames.EXCEPTION)
    ),
    responses = Array(
      new Response(text = ResponseNames.SUCCESS, field = OutputNames.RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
      new Response(text = ResponseNames.FAILURE, field = OutputNames.RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
    )
  )
  def execute(@Param(value = "projectId", required = true) projectId: String,
              @Param(value = "zone", required = true) zone: String,
              @Param(value = "instanceName", required = true) instanceName: String,
              @Param(value = "proxyHost") proxyHost: String,
              @Param(value = "proxyPort") proxyPort: String,
              @Param(value = "proxyUsername") proxyUsername: String,
              @Param(value = "proxyPassword", encrypted = true) proxyPassword: String,
              @Param(value = "prettyPrint") prettyPrintString: String
             ): java.util.Map[String, String] = {
    try {
      val httpTransport = HttpTransportUtils.getNetHttpTransport(Option(proxyHost), NumberUtilities.toInteger(proxyPort), Option(proxyUsername), proxyPassword)
      val jsonFactory = JsonFactoryUtils.getDefaultJacksonFactory

      val jsonPath = ""
      val credential = GoogleAuth.fromJsonWithScopes(new FileInputStream(jsonPath), httpTransport, jsonFactory, List(ComputeScopes.COMPUTE_READONLY))

      val prettyPrint = BooleanUtilities.toBoolean(prettyPrintString, false)

      val instance = InstanceService.get(httpTransport, jsonFactory, credential, projectId, zone, instanceName)
      val resultString = if (prettyPrint) instance.toPrettyString else instance.toString

      OutputUtilities.getSuccessResultsMap(resultString)
    } catch {
      case e: Throwable => OutputUtilities.getFailureResultsMap(e)
    }
  }
}

object Main {
  def main(): Unit = {
    val instanceGet = new InstanceGet()
    val resultMap = instanceGet.execute(projectId = "cogent-range-159508",
      zone = "europe-west1-d",
      instanceName = "instance-1",
      proxyHost = "web-proxy.corp.hpecorp.net",
      proxyPort = "8080",
      proxyUsername = "",
      proxyPassword = "",
      prettyPrintString = "true")
    println(resultMap)
  }
}
