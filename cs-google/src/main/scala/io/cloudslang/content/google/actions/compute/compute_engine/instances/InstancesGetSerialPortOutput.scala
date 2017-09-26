/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package io.cloudslang.content.google.actions.compute.compute_engine.instances

import java.util

import com.hp.oo.sdk.content.annotations.{Action, Output, Param, Response}
import com.hp.oo.sdk.content.plugin.ActionMetadata.{MatchType, ResponseType}
import io.cloudslang.content.constants.OutputNames._
import io.cloudslang.content.constants.{ResponseNames, ReturnCodes}
import io.cloudslang.content.google.services.compute.compute_engine.instances.InstanceService
import io.cloudslang.content.google.utils.Constants._
import io.cloudslang.content.google.utils.action.DefaultValues._
import io.cloudslang.content.google.utils.action.GoogleOutputNames.NEXT_INDEX
import io.cloudslang.content.google.utils.action.InputNames._
import io.cloudslang.content.google.utils.action.InputUtils.verifyEmpty
import io.cloudslang.content.google.utils.action.InputValidator._
import io.cloudslang.content.google.utils.service.{GoogleAuth, HttpTransportUtils, JsonFactoryUtils}
import io.cloudslang.content.utils.NumberUtilities.toInteger
import io.cloudslang.content.utils.OutputUtilities.{getFailureResultsMap, getSuccessResultsMap}
import org.apache.commons.lang3.StringUtils.{EMPTY, defaultIfEmpty}

import scala.collection.JavaConversions._

/**
  * Created by victor on 25.08.2017.
  */
class InstancesGetSerialPortOutput {

  /**
    * This operation can be used to retrieve the Serial Port Output for an instance.
    *
    * @param projectId        Google Cloud project id.
    *                         Example: "example-project-a"
    * @param zone             The name of the zone where the instance is located.
    *                         Examples: "us-central1-a", "us-central1-b", "us-central1-c"
    * @param accessToken      The access token returned by the GetAccessToken operation, with at least the
    *                         following scope: "https://www.googleapis.com/auth/compute.readonly".
    * @param instanceName     Name of the instance resource to return the serial port output.
    *                         Example: "instance-1234"
    * @param consolePortInp   Optional - Specifies which COM or serial port to retrieve data from.
    *                         Valid values: an integer between 1 and 4 (inclusive)
    *                         Default: 1
    * @param startIndexInp    Optional - The byte position from which to return the output. Use this to page through output
    *                         when the output is too large to return in a single request. For the initial request, leave
    *                         this field unspecified. For subsequent calls, this field should be set to the next value
    *                         returned in the previous call.
    *                         Default: 0
    * @param proxyHost        Optional - Proxy server used to connect to Google Cloud API. If empty no proxy will
    *                         be used.
    * @param proxyPortInp     Optional - Proxy server port used to access the provider services.
    *                         Default: 8080
    * @param proxyUsername    Optional - Proxy server user name.
    * @param proxyPasswordInp Optional - Proxy server password associated with the <proxyUsername> input value.
    * @return the Serial Port Output as returnResult and the nextIndex with the last byte position read
    */
  @Action(name = "Get Serial Port Output",
    outputs = Array(
      new Output(RETURN_CODE),
      new Output(RETURN_RESULT),
      new Output(NEXT_INDEX),
      new Output(EXCEPTION)
    ),
    responses = Array(
      new Response(text = ResponseNames.SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
      new Response(text = ResponseNames.FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
    )
  )
  def execute(@Param(value = PROJECT_ID, required = true) projectId: String,
              @Param(value = ZONE, required = true) zone: String,
              @Param(value = ACCESS_TOKEN, required = true, encrypted = true) accessToken: String,

              @Param(value = INSTANCE_NAME, required = true) instanceName: String,
              @Param(value = CONSOLE_PORT) consolePortInp: String,
              @Param(value = START_INDEX) startIndexInp: String,

              @Param(value = PROXY_HOST) proxyHost: String,
              @Param(value = PROXY_PORT) proxyPortInp: String,
              @Param(value = PROXY_USERNAME) proxyUsername: String,
              @Param(value = PROXY_PASSWORD, encrypted = true) proxyPasswordInp: String): util.Map[String, String] = {
    val proxyHostOpt = verifyEmpty(proxyHost)
    val proxyUsernameOpt = verifyEmpty(proxyUsername)
    val proxyPortStr = defaultIfEmpty(proxyPortInp, DEFAULT_PROXY_PORT)
    val proxyPassword = defaultIfEmpty(proxyPasswordInp, EMPTY)

    val consolePortStr = defaultIfEmpty(consolePortInp, DEFAULT_CONSOLE_PORT)
    val startIndexStr = defaultIfEmpty(startIndexInp, DEFAULT_START_INDEX)

    val validationStream = validateProxyPort(proxyPortStr) ++
      validateNonNegativeInteger(consolePortStr, CONSOLE_PORT) ++
      validateInteger(startIndexStr, START_INDEX)

    if (validationStream.nonEmpty) {
      return getFailureResultsMap(validationStream.mkString(NEW_LINE))
    }

    val proxyPort = toInteger(proxyPortStr)
    val consolePort = toInteger(consolePortStr)
    val startIndex = toInteger(startIndexStr)

    try {
      val httpTransport = HttpTransportUtils.getNetHttpTransport(proxyHostOpt, proxyPort, proxyUsernameOpt, proxyPassword)
      val jsonFactory = JsonFactoryUtils.getDefaultJacksonFactory
      val credential = GoogleAuth.fromAccessToken(accessToken)

      val resultSerialPort = InstanceService.getSerialPortOutput(httpTransport, jsonFactory, credential, projectId, zone, instanceName, consolePort, startIndex)

      getSuccessResultsMap(resultSerialPort.getContents) +
        (NEXT_INDEX -> resultSerialPort.getNext.toString)
    } catch {
      case e: Throwable => getFailureResultsMap(e)
    }
  }
}
