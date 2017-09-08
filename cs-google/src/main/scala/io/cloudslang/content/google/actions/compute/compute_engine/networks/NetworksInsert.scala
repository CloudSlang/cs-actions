package io.cloudslang.content.google.actions.compute.compute_engine.networks

import java.util

import com.google.api.services.compute.model.Network
import com.hp.oo.sdk.content.annotations.{Action, Output, Param, Response}
import com.hp.oo.sdk.content.plugin.ActionMetadata.{MatchType, ResponseType}
import io.cloudslang.content.constants.BooleanValues.FALSE
import io.cloudslang.content.constants.OutputNames.{EXCEPTION, RETURN_CODE, RETURN_RESULT}
import io.cloudslang.content.constants.{ResponseNames, ReturnCodes}
import io.cloudslang.content.google.services.compute.compute_engine.networks.{NetworkController, NetworkService}
import io.cloudslang.content.google.utils.Constants.{NEW_LINE, TIMEOUT_EXCEPTION}
import io.cloudslang.content.google.utils.action.DefaultValues._
import io.cloudslang.content.google.utils.action.GoogleOutputNames.{NETWORK_NAME, NETWORK_ID, STATUS}
import io.cloudslang.content.google.utils.action.InputNames._
import io.cloudslang.content.google.utils.action.InputUtils.{convertSecondsToMilli, verifyEmpty}
import io.cloudslang.content.google.utils.action.InputValidator.{validateBoolean, validateNonNegativeDouble, validateNonNegativeLong, validateProxyPort}
import io.cloudslang.content.google.utils.action.OutputUtils.toPretty
import io.cloudslang.content.google.utils.service.{GoogleAuth, HttpTransportUtils, JsonFactoryUtils}
import io.cloudslang.content.utils.BooleanUtilities.toBoolean
import io.cloudslang.content.utils.NumberUtilities.{toDouble, toInteger, toLong}
import io.cloudslang.content.utils.OutputUtilities.{getFailureResultsMap, getSuccessResultsMap}
import org.apache.commons.lang3.StringUtils.{EMPTY, defaultIfEmpty}

import scala.collection.JavaConversions._
import scala.concurrent.TimeoutException

/**
  * Created by victor on 26.04.2017.
  */
class NetworksInsert {


  /**
    * Creates a disk resource in the specified project using the data included as inputs.
    *
    * @param projectId                Name of the Google Cloud project.
    * @param accessToken              The access token from GetAccessToken.
    * @param networkName              Name of the Network. Provided by the client when the Network is created. The name must be
    *                                 1-63 characters long, and comply with RFC1035. Specifically, the name must be 1-63 characters
    *                                 long and match the regular expression [a-z]([-a-z0-9]*[a-z0-9])? which means the first
    *                                 character must be a lowercase letter, and all following characters must be a dash, lowercase
    *                                 letter, or digit, except the last character, which cannot be a dash.
    * @param networkDescriptionInp    Optional - The description of the new Network
    * @param ipV4RangeInp             Optional - The range of internal addresses that are legal on this network. This range is a CIDR
    *                                 specification, for example: 192.168.0.0/16. Provided by the client when the network is created.
    * @param autoCreateSubnetworksInp Optional - When set to true, the network is created in "auto subnet mode". When set to false, the network
    *                                 is in "custom subnet mode".
    *                                 In "auto subnet mode", a newly created network is assigned the default CIDR of 10.128.0.0/9 and
    *                                 it automatically creates one subnetwork per region.
    *                                 Note: If <ipV4RangeInp> is set, then this input is ignored
    * @param syncInp                  Optional - Boolean specifying whether the operation to run sync or async.
    *                                 Valid values: "true", "false"
    *                                 Default: "false"
    * @param timeoutInp               Optional - The time, in seconds, to wait for a response if the sync input is set to "true".
    *                                 If the value is 0, the operation will wait until zone operation progress is 100.
    *                                 Valid values: Any positive number including 0.
    *                                 Default: "30"
    * @param pollingIntervalInp       Optional - The time, in seconds, to wait before a new request that verifies if the operation finished
    *                                 is executed, if the sync input is set to "true".
    *                                 Valid values: Any positive number including 0.
    *                                 Default: "1"
    * @param proxyHost                Optional - Proxy server used to connect to Google Cloud API. If empty no proxy will
    *                                 be used.
    * @param proxyPortInp             Optional - Proxy server port.
    *                                 Default: "8080"
    * @param proxyUsername            Optional - Proxy server user name.
    * @param proxyPasswordInp         Optional - Proxy server password associated with the proxyUsername input value.
    * @param prettyPrintInp           Optional - Whether to format (pretty print) the resulting json.
    *                                 Valid values: "true", "false"
    *                                 Default: "true"
    * @return A map containing a GlobalOperation resource as returnResult, it's name as globalOperationName and the
    *         status of the operation. If <syncInp> is set to true the map will also contain the name of the network
    *         and the network id.
    *         In case an exception occurs the failure message is provided.
    */

  @Action(name = "Insert Network",
    outputs = Array(
      new Output(RETURN_CODE),
      new Output(RETURN_RESULT),
      new Output(EXCEPTION),
      new Output(GLOBAL_OPERATION_NAME),
      new Output(NETWORK_NAME),
      new Output(NETWORK_ID),
      new Output(STATUS)
    ),
    responses = Array(
      new Response(text = ResponseNames.SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
      new Response(text = ResponseNames.FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
    )
  )
  def execute(@Param(value = PROJECT_ID, required = true) projectId: String,
              @Param(value = ACCESS_TOKEN, required = true, encrypted = true) accessToken: String,
              @Param(value = NETWORK_NAME, required = true) networkName: String,
              @Param(value = NETWORK_DESCRIPTION) networkDescriptionInp: String,
              @Param(value = AUTO_CREATE_SUBNETWORKS) autoCreateSubnetworksInp: String,
              @Param(value = IPV4_RANGE) ipV4RangeInp: String,
              @Param(value = SYNC) syncInp: String,
              @Param(value = TIMEOUT) timeoutInp: String,
              @Param(value = POLLING_INTERVAL) pollingIntervalInp: String,
              @Param(value = PROXY_HOST) proxyHost: String,
              @Param(value = PROXY_PORT) proxyPortInp: String,
              @Param(value = PROXY_USERNAME) proxyUsername: String,
              @Param(value = PROXY_PASSWORD, encrypted = true) proxyPasswordInp: String,
              @Param(value = PRETTY_PRINT) prettyPrintInp: String): util.Map[String, String] = {

    val proxyHostOpt = verifyEmpty(proxyHost)
    val proxyUsernameOpt = verifyEmpty(proxyUsername)
    val networkDescription = defaultIfEmpty(networkDescriptionInp, EMPTY)
    val autoCreateSubnetworksStr = defaultIfEmpty(autoCreateSubnetworksInp, DEFAULT_AUTO_CREATE_SUBNETWORKS)
    val ipV4Range = verifyEmpty(ipV4RangeInp)
    val proxyPortStr = defaultIfEmpty(proxyPortInp, DEFAULT_PROXY_PORT)
    val proxyPassword = defaultIfEmpty(proxyPasswordInp, EMPTY)
    val prettyPrintStr = defaultIfEmpty(prettyPrintInp, DEFAULT_PRETTY_PRINT)
    val syncStr = defaultIfEmpty(syncInp, FALSE)
    val timeoutStr = defaultIfEmpty(timeoutInp, DEFAULT_SYNC_TIMEOUT)
    val pollingIntervalStr = defaultIfEmpty(pollingIntervalInp, DEFAULT_POLLING_INTERVAL)

    val validationStream = validateProxyPort(proxyPortStr) ++
      validateBoolean(prettyPrintStr, PRETTY_PRINT) ++
      validateBoolean(autoCreateSubnetworksStr, AUTO_CREATE_SUBNETWORKS) ++
      validateBoolean(syncStr, SYNC) ++
      validateNonNegativeLong(timeoutStr, TIMEOUT) ++
      validateNonNegativeDouble(pollingIntervalStr, POLLING_INTERVAL)

    if (validationStream.nonEmpty) {
      return getFailureResultsMap(validationStream.mkString(NEW_LINE))
    }

    val proxyPort = toInteger(proxyPortStr)
    val prettyPrint = toBoolean(prettyPrintStr)
    val autoCreateSubnetworks = toBoolean(autoCreateSubnetworksStr)
    val sync = toBoolean(syncStr)
    val timeout = toLong(timeoutStr)
    val pollingIntervalMilli = convertSecondsToMilli(toDouble(pollingIntervalStr))

    try {
      val httpTransport = HttpTransportUtils.getNetHttpTransport(proxyHostOpt, proxyPort, proxyUsernameOpt, proxyPassword)
      val jsonFactory = JsonFactoryUtils.getDefaultJacksonFactory
      val credential = GoogleAuth.fromAccessToken(accessToken)

      val computeNetwork: Network = NetworkController.createNetwork(
        networkName = networkName,
        networkDescription = networkDescription,
        autoCreateSubnetworks = autoCreateSubnetworks,
        ipV4Range = ipV4Range)

      val operation = NetworkService.insert(httpTransport, jsonFactory, credential, projectId, computeNetwork, sync, timeout, pollingIntervalMilli)
      val status = defaultIfEmpty(operation.getStatus, EMPTY)
      val resultMap = getSuccessResultsMap(toPretty(prettyPrint, operation)) +
        (GLOBAL_OPERATION_NAME -> operation.getName) +
        (STATUS -> status)

      if (sync) {
        val network = NetworkService.get(httpTransport, jsonFactory, credential, projectId, networkName)
        val name = defaultIfEmpty(network.getName, EMPTY)
        val networkId = Option(network.getId).getOrElse(BigInt(0)).toString

        resultMap +
          (NETWORK_NAME -> name) +
          (NETWORK_ID -> networkId)
      } else {
        resultMap
      }
    } catch {
      case t: TimeoutException => getFailureResultsMap(TIMEOUT_EXCEPTION, t)
      case e: Throwable => getFailureResultsMap(e)
    }
  }
}
