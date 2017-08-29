package io.cloudslang.content.google.actions.compute.compute_engine.disks

import java.util

import com.google.api.services.compute.model.Disk
import com.hp.oo.sdk.content.annotations.{Action, Output, Param, Response}
import com.hp.oo.sdk.content.plugin.ActionMetadata.{MatchType, ResponseType}
import io.cloudslang.content.constants.OutputNames.{EXCEPTION, RETURN_CODE, RETURN_RESULT}
import io.cloudslang.content.constants.{ResponseNames, ReturnCodes}
import io.cloudslang.content.google.services.compute.compute_engine.disks.DiskService
import io.cloudslang.content.google.utils.Constants._
import io.cloudslang.content.google.utils.action.DefaultValues.{DEFAULT_PRETTY_PRINT, DEFAULT_PROXY_PORT}
import io.cloudslang.content.google.utils.action.InputNames._
import io.cloudslang.content.google.utils.action.InputUtils.verifyEmpty
import io.cloudslang.content.google.utils.action.InputValidator.{validateBoolean, validateProxyPort}
import io.cloudslang.content.google.utils.service.{GoogleAuth, HttpTransportUtils, JsonFactoryUtils}
import io.cloudslang.content.utils.BooleanUtilities.toBoolean
import io.cloudslang.content.utils.NumberUtilities.toInteger
import io.cloudslang.content.utils.OutputUtilities.{getFailureResultsMap, getSuccessResultsMap}
import org.apache.commons.lang3.StringUtils.{EMPTY, defaultIfEmpty}

/**
  * Created by victor on 3/3/17.
  */
class DisksList {


  /**
    * This operation can be used to retrieve the list of Disk resources, as JSON array.
    *
    * @param projectId        Google Cloud project id.
    *                         Example: "example-project-a"
    * @param zone             The name of the zone where the Disks resource is located.
    *                         Examples: "us-central1-a", "us-central1-b", "us-central1-c"
    * @param accessToken      The access token returned by the GetAccessToken operation, with at least the
    *                         following scope: "https://www.googleapis.com/auth/compute.readonly".
    * @param filter           Optional - Sets a filter expression for filtering listed resources, in the form filter={expression}.
    *                         Your {expression} must be in the format: field_name comparison_string literal_string.
    *                         The field_name is the name of the field you want to compare. Only atomic field types are
    *                         supported (string, number, boolean). The comparison_string must be either eq (equals) or ne
    *                         (not equals). The literal_string is the string value to filter to. The literal value must
    *                         be valid for the type of field you are filtering by (string, number, boolean). For string
    *                         fields, the literal value is interpreted as a regular expression using RE2 syntax. The
    *                         literal value must match the entire field.
    *                         For example, to filter for instances that do not have a name of example-instance, you would
    *                         use filter=name ne example-instance.
    *                         You can filter on nested fields. For example, you could filter on instances that have set
    *                         the scheduling.automaticRestart field to true. Use filtering on nested fields to take
    *                         advantage of labels to organize and search for results based on label values.
    *                         To filter on multiple expressions, provide each separate expression within parentheses. For
    *                         example, (scheduling.automaticRestart eq true) (zone eq us-central1-f). Multiple
    *                         expressions are treated as AND expressions, meaning that resources must match all
    *                         expressions to pass the filters.
    * @param orderBy          Optional - Sorts list results by a certain order. By default, results are returned in alphanumerical
    *                         order based on the resource name.
    *                         You can also sort results in descending order based on the creation timestamp using
    *                         orderBy="creationTimestamp desc". This sorts results based on the creationTimestamp field
    *                         in reverse chronological order (newest result first). Use this to sort resources like
    *                         operations so that the newest operation is returned first.
    *                         Currently, only sorting by name or creationTimestamp desc is supported.
    * @param proxyHost        Optional - Proxy server used to connect to Google Cloud API. If empty no proxy will
    *                         be used.
    * @param proxyPortInp     Optional - Proxy server port used to access the provider services.
    *                         Default: "8080"
    * @param proxyUsername    Optional - Proxy server user name.
    * @param proxyPasswordInp Optional - Proxy server password associated with the <proxyUsername> input value.
    * @param prettyPrintInp   Optional - Whether to format (pretty print) the resulting json.
    *                         Valid values: "true", "false"
    *                         Default: "true"
    * @return a map containing the list of Disks resources as returnResult
    */
  @Action(name = "List Disks",
    outputs = Array(
      new Output(RETURN_CODE),
      new Output(RETURN_RESULT),
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
              @Param(value = FILTER) filter: String,
              @Param(value = ORDER_BY) orderBy: String,
              @Param(value = PROXY_HOST) proxyHost: String,
              @Param(value = PROXY_PORT) proxyPortInp: String,
              @Param(value = PROXY_USERNAME) proxyUsername: String,
              @Param(value = PROXY_PASSWORD, encrypted = true) proxyPasswordInp: String,
              @Param(value = PRETTY_PRINT) prettyPrintInp: String): util.Map[String, String] = {

    val proxyHostOpt = verifyEmpty(proxyHost)
    val proxyUsernameOpt = verifyEmpty(proxyUsername)
    val filterOpt = verifyEmpty(filter)
    val orderByOpt = verifyEmpty(orderBy)
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

      val disksDelimiter = if (prettyPrint) COMMA_NEW_LINE else COMMA

      val resultList = DiskService.list(httpTransport, jsonFactory, credential, projectId, zone, filterOpt, orderByOpt)
        .map { disk: Disk => if (prettyPrint) disk.toPrettyString else disk.toString }
        .mkString(SQR_LEFT_BRACKET, disksDelimiter, SQR_RIGHT_BRACKET)
      getSuccessResultsMap(resultList)
    } catch {
      case e: Throwable => getFailureResultsMap(e)
    }
  }


}
