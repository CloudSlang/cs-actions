package io.cloudslang.content.google.actions.compute.compute_engine.instances

import java.util

import com.hp.oo.sdk.content.annotations.{Action, Output, Param, Response}
import com.hp.oo.sdk.content.plugin.ActionMetadata.{MatchType, ResponseType}
import io.cloudslang.content.constants.OutputNames.{EXCEPTION, RETURN_CODE, RETURN_RESULT}
import io.cloudslang.content.constants.{ResponseNames, ReturnCodes}
import io.cloudslang.content.google.services.compute.compute_engine.instances.InstanceService
import io.cloudslang.content.google.utils.Constants.{COMMA, NEW_LINE}
import io.cloudslang.content.google.utils.action.DefaultValues.{DEFAULT_PRETTY_PRINT, DEFAULT_PROXY_PORT}
import io.cloudslang.content.google.utils.action.GoogleOutputNames._
import io.cloudslang.content.google.utils.action.InputNames._
import io.cloudslang.content.google.utils.action.InputUtils.verifyEmpty
import io.cloudslang.content.google.utils.action.InputValidator.{validateBoolean, validateProxyPort}
import io.cloudslang.content.google.utils.action.OutputUtils.toPretty
import io.cloudslang.content.google.utils.service.{GoogleAuth, HttpTransportUtils, JsonFactoryUtils}
import io.cloudslang.content.utils.BooleanUtilities.toBoolean
import io.cloudslang.content.utils.NumberUtilities.toInteger
import io.cloudslang.content.utils.OutputUtilities.{getFailureResultsMap, getSuccessResultsMap}
import org.apache.commons.lang3.StringUtils.{EMPTY, defaultIfEmpty}

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

/**
  * Created by sandorr
  * 2/27/2017.
  */
class InstancesGet {

  /**
    * This operation can be used to retrieve an instance resource, as JSON object.
    *
    * @param projectId        Google Cloud project id.
    *                         Example: "example-project-a"
    * @param zone             The name of the zone where the Instance resource is located.
    *                         Examples: "us-central1-a", "us-central1-b", "us-central1-c"
    * @param instanceName     Name of the instance resource to return.
    *                         Example: "instance-1234"
    * @param accessToken      The access token returned by the GetAccessToken operation, with at least the
    *                         following scope: "https://www.googleapis.com/auth/compute.readonly".
    * @param proxyHost        Optional - Proxy server used to connect to Google Cloud API. If empty no proxy will
    *                         be used.
    * @param proxyPortInp     Optional - Proxy server port used to access the provider services.
    *                         Default: "8080"
    * @param proxyUsername    Optional - Proxy server user name.
    * @param proxyPasswordInp Optional - Proxy server password associated with the <proxyUsername> input value.
    * @param prettyPrintInp   Optional - Whether to format (pretty print) the resulting json.
    *                         Valid values: "true", "false"
    *                         Default: "true"
    * @return a map containing a Instance resource as <returnResult>, the id as <instanceId>, the name as <instanceName>,
    *         a list of IPs as <ips>, the status of the instance as <status>, the metadata of the instance as pairs in a list
    *         as <metadata>, a list of tags as <tags>, a list of device names representing the disks as <disks> and the
    *         <exception> in case something wrong happens.
    */
  @Action(name = "Get Instance",
    outputs = Array(
      new Output(RETURN_CODE),
      new Output(RETURN_RESULT),
      new Output(INSTANCE_ID),
      new Output(INSTANCE_NAME),
      new Output(IPS),
      new Output(STATUS),
      new Output(METADATA),
      new Output(TAGS),
      new Output(DISKS),
      new Output(EXCEPTION)
    ),
    responses = Array(
      new Response(text = ResponseNames.SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
      new Response(text = ResponseNames.FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
    )
  )
  def execute(@Param(value = PROJECT_ID, required = true) projectId: String,
              @Param(value = ZONE, required = true) zone: String,
              @Param(value = INSTANCE_NAME, required = true) instanceName: String,
              @Param(value = ACCESS_TOKEN, required = true, encrypted = true) accessToken: String,
              @Param(value = PROXY_HOST) proxyHost: String,
              @Param(value = PROXY_PORT) proxyPortInp: String,
              @Param(value = PROXY_USERNAME) proxyUsername: String,
              @Param(value = PROXY_PASSWORD, encrypted = true) proxyPasswordInp: String,
              @Param(value = PRETTY_PRINT) prettyPrintInp: String): util.Map[String, String] = {

    val proxyHostOpt = verifyEmpty(proxyHost)
    val proxyUsernameOpt = verifyEmpty(proxyUsername)
    val proxyPortStr = defaultIfEmpty(proxyPortInp, DEFAULT_PROXY_PORT)
    val proxyPassword = defaultIfEmpty(proxyPasswordInp, EMPTY)
    val prettyPrintStr = defaultIfEmpty(prettyPrintInp, DEFAULT_PRETTY_PRINT)

    val validationStream = validateProxyPort(proxyPortStr) ++
      validateBoolean(prettyPrintStr, PRETTY_PRINT)

    if (validationStream.nonEmpty) {
      return getFailureResultsMap(validationStream.mkString(NEW_LINE))
    }

    val proxyPort = toInteger(proxyPortStr)
    val prettyPrint = toBoolean(prettyPrintStr)

    try {
      val httpTransport = HttpTransportUtils.getNetHttpTransport(proxyHostOpt, proxyPort, proxyUsernameOpt, proxyPassword)
      val jsonFactory = JsonFactoryUtils.getDefaultJacksonFactory
      val credential = GoogleAuth.fromAccessToken(accessToken)

      val instance = InstanceService.get(httpTransport, jsonFactory, credential, projectId, zone, instanceName)
      val metadata = Option(instance.getMetadata.getItems).getOrElse(List().asJava)
      val tags = Option(instance.getTags.getItems).getOrElse(List().asJava)
      val networkInterfaces = Option(instance.getNetworkInterfaces).getOrElse(List().asJava)
      val instanceId = Option(instance.getId).getOrElse(BigInt(0)).toString
      val status = defaultIfEmpty(instance.getStatus, EMPTY)
      val name = defaultIfEmpty(instance.getName, EMPTY)
      val disksNames = Option(instance.getDisks).getOrElse(List().asJava).map(_.getDeviceName)

      getSuccessResultsMap(toPretty(prettyPrint, instance)) +
        (INSTANCE_ID -> instanceId) +
        (INSTANCE_NAME -> name) +
        (IPS -> networkInterfaces.map(_.getNetworkIP).mkString(COMMA)) +
        (STATUS -> status) +
        (METADATA -> metadata.map(toPretty(prettyPrint, _)).mkString(COMMA)) +
        (TAGS -> tags.mkString(COMMA)) +
        (DISKS -> disksNames.mkString(COMMA))
    } catch {
      case e: Throwable => getFailureResultsMap(e)
    }
  }
}
