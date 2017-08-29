package io.cloudslang.content.google.actions.compute.compute_engine.networks

import java.util

import com.google.api.services.compute.model.Network
import com.hp.oo.sdk.content.annotations.{Action, Output, Param, Response}
import com.hp.oo.sdk.content.plugin.ActionMetadata.{MatchType, ResponseType}
import io.cloudslang.content.constants.OutputNames.{EXCEPTION, RETURN_CODE, RETURN_RESULT}
import io.cloudslang.content.constants.{OutputNames, ResponseNames, ReturnCodes}
import io.cloudslang.content.google.services.compute.compute_engine.networks.{NetworkController, NetworkService}
import io.cloudslang.content.google.utils.Constants.NEW_LINE
import io.cloudslang.content.google.utils.action.DefaultValues.{DEFAULT_AUTO_CREATE_SUBNETWORKS, DEFAULT_PRETTY_PRINT, DEFAULT_PROXY_PORT}
import io.cloudslang.content.google.utils.action.GoogleOutputNames.ZONE_OPERATION_NAME
import io.cloudslang.content.google.utils.action.InputNames._
import io.cloudslang.content.google.utils.action.InputUtils.verifyEmpty
import io.cloudslang.content.google.utils.action.InputValidator.{validateBoolean, validateProxyPort}
import io.cloudslang.content.google.utils.service.{GoogleAuth, HttpTransportUtils, JsonFactoryUtils}
import io.cloudslang.content.utils.BooleanUtilities.toBoolean
import io.cloudslang.content.utils.NumberUtilities.toInteger
import io.cloudslang.content.utils.OutputUtilities.{getFailureResultsMap, getSuccessResultsMap}
import org.apache.commons.lang3.StringUtils.{EMPTY, defaultIfEmpty}

import scala.collection.JavaConversions._

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
    * @param proxyHost                Optional - Proxy server used to connect to Google Cloud API. If empty no proxy will
    *                                 be used.
    * @param proxyPortInp             Optional - Proxy server port.
    *                                 Default: "8080"
    * @param proxyUsername            Optional - Proxy server user name.
    * @param proxyPasswordInp         Optional - Proxy server password associated with the proxyUsername input value.
    * @param prettyPrintInp           Optional - Whether to format (pretty print) the resulting json.
    *                                 Valid values: "true", "false"
    *                                 Default: "true"
    * @return A map with strings as keys and strings as values that contains: outcome of the action, returnCode of the
    *         operation, status of the ZoneOperation, or failure message and the exception if there is one
    */

  @Action(name = "Insert Network",
    outputs = Array(
      new Output(RETURN_CODE),
      new Output(RETURN_RESULT),
      new Output(EXCEPTION),
      new Output(ZONE_OPERATION_NAME)
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

    val validationStream = validateProxyPort(proxyPortStr) ++
      validateBoolean(prettyPrintStr, PRETTY_PRINT) ++
      validateBoolean(autoCreateSubnetworksStr, AUTO_CREATE_SUBNETWORKS)

    if (validationStream.nonEmpty) {
      return getFailureResultsMap(validationStream.mkString(NEW_LINE))
    }

    val proxyPort = toInteger(proxyPortStr)
    val prettyPrint = toBoolean(prettyPrintStr)
    val autoCreateSubnetworks = toBoolean(autoCreateSubnetworksStr)

    try {
      val httpTransport = HttpTransportUtils.getNetHttpTransport(proxyHostOpt, proxyPort, proxyUsernameOpt, proxyPassword)
      val jsonFactory = JsonFactoryUtils.getDefaultJacksonFactory
      val credential = GoogleAuth.fromAccessToken(accessToken)

      val computeNetwork: Network = NetworkController.createNetwork(
        networkName = networkName,
        networkDescription = networkDescription,
        autoCreateSubnetworks = autoCreateSubnetworks,
        ipV4Range = ipV4Range)

      val operation = NetworkService.insert(httpTransport, jsonFactory, credential, projectId, computeNetwork)
      val resultString = if (prettyPrint) operation.toPrettyString else operation.toString

      getSuccessResultsMap(resultString) + (ZONE_OPERATION_NAME -> operation.getName)
    } catch {
      case e: Throwable => getFailureResultsMap(e)
    }
  }
}
