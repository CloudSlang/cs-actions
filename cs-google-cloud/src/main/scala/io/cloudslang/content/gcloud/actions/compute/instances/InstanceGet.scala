package io.cloudslang.content.gcloud.actions.compute.instances

import java.io.FileInputStream
import java.util

import com.google.api.services.compute.ComputeScopes
import com.hp.oo.sdk.content.annotations.{Action, Output, Param, Response}
import com.hp.oo.sdk.content.plugin.ActionMetadata.{MatchType, ResponseType}
import io.cloudslang.content.constants.{OutputNames, ResponseNames, ReturnCodes}
import io.cloudslang.content.gcloud.actions.compute.utils.GetAuthorizationToken
import io.cloudslang.content.gcloud.services.compute.instances.InstanceService
import io.cloudslang.content.gcloud.utils.InputValidator.validate
import io.cloudslang.content.gcloud.utils.{GoogleAuth, HttpTransportUtils, InputValidator, JsonFactoryUtils}
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
              @Param(value = "accessToken") accessToken: String,
              @Param(value = "proxyHost") proxyHost: String,
              @Param(value = "proxyPort") proxyPort: String,
              @Param(value = "proxyUsername") proxyUsername: String,
              @Param(value = "proxyPassword", encrypted = true) proxyPassword: String,
              @Param(value = "prettyPrint") prettyPrintString: String): util.Map[String, String] = {
    try {
      val validationStream = validate(proxyPort)(Option.apply) ++
        validate(proxyHost)(Option.apply)

      val validationResult = validationStream.mkString("\n")
      println(validationResult)

      val httpTransport = HttpTransportUtils.getNetHttpTransport(Option(proxyHost), NumberUtilities.toInteger(proxyPort), Option(proxyUsername), proxyPassword)
      val jsonFactory = JsonFactoryUtils.getDefaultJacksonFactory

      val credential = GoogleAuth.fromAccessToken(accessToken)

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
  def main(args: Array[String]): Unit = {
    val jsonToken = ""

    val getAuthorizationToken = new GetAuthorizationToken
    val authTokenMap = getAuthorizationToken.execute(
      jsonToken,
      timeoutStr = "99999",
      ComputeScopes.COMPUTE_READONLY,
      scopesDel = ",",
      proxyHost = "web-proxy.corp.hpecorp.net",
      proxyPort = "8080",
      proxyUsername = "",
      proxyPassword = ""
    )

    val instanceGet = new InstanceGet()
    val resultMap = instanceGet.execute(projectId = "cogent-range-159508",
      zone = "europe-west1-d",
      instanceName = "instance-1",
      accessToken = authTokenMap.get(OutputNames.RETURN_RESULT),
      proxyHost = "web-proxy.corp.hpecorp.net",
      proxyPort = "8080",
      proxyUsername = "",
      proxyPassword = "",
      prettyPrintString = "true")
    println(resultMap.get(OutputNames.RETURN_RESULT))
  }
}
