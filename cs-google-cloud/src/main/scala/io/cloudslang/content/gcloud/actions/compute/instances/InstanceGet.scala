package io.cloudslang.content.gcloud.actions.compute.instances

import java.io.{File, FileInputStream}
import java.nio.charset.StandardCharsets
import java.util

import com.google.api.services.compute.ComputeScopes
import com.hp.oo.sdk.content.annotations.{Action, Output, Param, Response}
import com.hp.oo.sdk.content.plugin.ActionMetadata.{MatchType, ResponseType}
import io.cloudslang.content.constants.{OutputNames, ResponseNames, ReturnCodes}
import io.cloudslang.content.gcloud.actions.compute.utils.GetAuthorizationToken
import io.cloudslang.content.gcloud.services.compute.instances.InstanceService
import io.cloudslang.content.gcloud.utils.InputNames._
import io.cloudslang.content.gcloud.utils.InputValidator.validate
import io.cloudslang.content.gcloud.utils._
import io.cloudslang.content.utils.{BooleanUtilities, NumberUtilities, OutputUtilities}
import org.apache.commons.io.IOUtils

import scala.collection.JavaConversions._

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
  def execute(@Param(value = PROJECT_ID, required = true) projectId: String,
              @Param(value = ZONE, required = true) zone: String,
              @Param(value = INSTANCE_NAME, required = true) instanceName: String,
              @Param(value = ACCESS_TOKEN) accessToken: String,
              @Param(value = PROXY_HOST) proxyHost: String,
              @Param(value = PROXY_PORT) proxyPort: String,
              @Param(value = PROXY_USERNAME) proxyUsername: String,
              @Param(value = PROXY_PASSWORD, encrypted = true) proxyPassword: String,
              @Param(value = PRETTY_PRINT) prettyPrintString: String): util.Map[String, String] = {
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
