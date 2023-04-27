/*
 * (c) Copyright 2023 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * (c) Copyright 2020 Micro Focus, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.cloudslang.content.google.actions.databases.sql.instances

import java.util

import com.hp.oo.sdk.content.annotations.{Action, Output, Param, Response}
import com.hp.oo.sdk.content.plugin.ActionMetadata.{MatchType, ResponseType}
import io.cloudslang.content.constants.OutputNames._
import io.cloudslang.content.constants.{ResponseNames, ReturnCodes}
import io.cloudslang.content.google.services.databases.sql.instances.SQLDatabaseInstanceService
import io.cloudslang.content.google.utils.Constants.NEW_LINE
import io.cloudslang.content.google.utils.Constants.SQLInstancesConstant.{GET_SQL_INSTANCE_OPERATION_NAME, IP_ADDRESS_TYPE_PRIMARY, IP_ADDRESS_TYPE_PRIVATE}
import io.cloudslang.content.google.utils.action.DefaultValues._
import io.cloudslang.content.google.utils.action.Descriptions.Common._
import io.cloudslang.content.google.utils.action.Descriptions.CreateSQLDataBaseInstance._
import io.cloudslang.content.google.utils.action.Descriptions.SQLDataBaseInstances.GET_SQL_INSTANCE_OPERATION_DESCRIPTION
import io.cloudslang.content.google.utils.action.Descriptions.UpdateSQLDataBaseInstance.PRIVATE_IP_ADDRESS_DESC
import io.cloudslang.content.google.utils.action.GoogleOutputNames.STATUS
import io.cloudslang.content.google.utils.action.InputNames.CreateSQLDatabaseInstanceInputs.{MACHINE_TYPE, ZONE, _}
import io.cloudslang.content.google.utils.action.InputNames._
import io.cloudslang.content.google.utils.action.InputUtils.verifyEmpty
import io.cloudslang.content.google.utils.action.InputValidator.{validateBoolean, validateProxyPort}
import io.cloudslang.content.google.utils.action.OutputUtils.toPretty
import io.cloudslang.content.google.utils.action.Outputs.SQLDatabaseInstance._
import io.cloudslang.content.google.utils.service.{GoogleAuth, HttpTransportUtils, JsonFactoryUtils, Utility}
import io.cloudslang.content.utils.BooleanUtilities.toBoolean
import io.cloudslang.content.utils.NumberUtilities.toInteger
import io.cloudslang.content.utils.OutputUtilities.{getFailureResultsMap, getSuccessResultsMap}
import org.apache.commons.lang3.StringUtils.{EMPTY, defaultIfEmpty}

import scala.collection.JavaConversions._

class GetSQLInstance {
  @Action(name = GET_SQL_INSTANCE_OPERATION_NAME,
    description = GET_SQL_INSTANCE_OPERATION_DESCRIPTION,
    outputs = Array(
      new Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
      new Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
      new Output(value = EXCEPTION, description = EXCEPTION_DESC),
      new Output(value = INSTANCE_ID, description = INSTANCE_ID_DESC),
      new Output(value = CONNECTION_NAME, description = CONNECTION_NAME_DESC),
      new Output(value = DATABASE_VERSION, description = DATABASE_VERSION_DESC),
      new Output(value = ZONE, description = ZONE_DESC),
      new Output(value = PUBLIC_IP_ADDRESS, description = PUBLIC_IP_ADDRESS_DESC),
      new Output(value = PRIVATE_IP_ADDRESS, description = PRIVATE_IP_ADDRESS_DESC),
      new Output(value = INSTANCE_ID, description = INSTANCE_ID_DESC),
      new Output(value = REGION, description = REGION_DESC),
      new Output(value = SELF_LINK, description = SELF_LINK_DESC),
      new Output(value = AVAILABILITY_TYPE, description = AVAILABILITY_TYPE_DESC),
      new Output(value = STORAGE_TYPE, description = STORAGE_TYPE_DESC),
      new Output(value = STORAGE_CAPACITY, description = STORAGE_CAPACITY_DESC),
      new Output(value = STATUS, description = STATUS_DESC),
      new Output(value = MACHINE_TYPE, description = MACHINE_TYPE_DESC),
      new Output(value = ACTIVATION_POLICY, description = ACTIVATION_POLICY_DESC)
    )
    ,
    responses = Array(
      new Response(text = ResponseNames.SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS,
        matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
      new Response(text = ResponseNames.FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE,
        matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
    )
  )
  def execute(@Param(value = PROJECT_ID, required = true, encrypted = true, description = PROJECT_ID_DESC) projectId: String,
              @Param(value = ACCESS_TOKEN, required = true, encrypted = true, description = ACCESS_TOKEN_DESC) accessToken: String,
              @Param(value = INSTANCE_ID, required = true, description = INSTANCE_ID_DESC) instanceId: String,
              @Param(value = PROXY_HOST) proxyHost: String,
              @Param(value = PROXY_PORT) proxyPort: String,
              @Param(value = PROXY_USERNAME) proxyUsername: String,
              @Param(value = PROXY_PASSWORD, encrypted = true) proxyPassword: String,
              @Param(value = PRETTY_PRINT) prettyPrintInp: String): util.Map[String, String] = {

    val proxyHostStr = verifyEmpty(proxyHost)
    val proxyUsernameOpt = verifyEmpty(proxyUsername)
    val proxyPortInt = defaultIfEmpty(proxyPort, DEFAULT_PROXY_PORT)
    val proxyPasswordStr = defaultIfEmpty(proxyPassword, EMPTY)
    val prettyPrintStr = defaultIfEmpty(prettyPrintInp, DEFAULT_PRETTY_PRINT)

    val validationStream = validateProxyPort(proxyPortInt) ++
      validateBoolean(prettyPrintStr, PRETTY_PRINT)

    if (validationStream.nonEmpty) {
      return getFailureResultsMap(validationStream.mkString(NEW_LINE))
    }

    val proxyPortVal = toInteger(proxyPortInt)
    val prettyPrint = toBoolean(prettyPrintStr)

    try {
      val httpTransport = HttpTransportUtils.getNetHttpTransport(proxyHostStr, proxyPortVal, proxyUsernameOpt,
        proxyPasswordStr)
      val jsonFactory = JsonFactoryUtils.getDefaultJacksonFactory
      val credential = GoogleAuth.fromAccessToken(accessToken)

      val sqlInstance = SQLDatabaseInstanceService.get(httpTransport, jsonFactory, credential, projectId,
        instanceId)
      val sqlInstanceSettings = sqlInstance.getSettings
      val ipAddresses = sqlInstance.getIpAddresses
      var publicIPAddress = EMPTY
      var privateIPAddress = EMPTY

      if (ipAddresses.size() > 0) {
        if (ipAddresses.get(0).getType.equals(IP_ADDRESS_TYPE_PRIMARY)) {
          publicIPAddress = sqlInstance.getIpAddresses.get(0).getIpAddress
        } else if (ipAddresses.get(0).getType.equals(IP_ADDRESS_TYPE_PRIVATE)) {
          privateIPAddress = ipAddresses.get(0).getIpAddress
        }
      }
      if (ipAddresses.size() > 1) {
        if (ipAddresses.get(1).getType.equals(IP_ADDRESS_TYPE_PRIMARY)) {
          publicIPAddress = sqlInstance.getIpAddresses.get(1).getIpAddress
        } else if (ipAddresses.get(1).getType.equals(IP_ADDRESS_TYPE_PRIVATE)) {
          privateIPAddress = ipAddresses.get(1).getIpAddress
        }
      }

      getSuccessResultsMap(toPretty(prettyPrint, sqlInstance)) +
        (CONNECTION_NAME -> sqlInstance.getConnectionName) +
        (DATABASE_VERSION -> sqlInstance.getDatabaseVersion) +
        (ZONE -> sqlInstance.getGceZone) +
        (PUBLIC_IP_ADDRESS -> (if (sqlInstance.getIpAddresses.size() > 0) {
          publicIPAddress
        } else {
          EMPTY
        })) +
        (PRIVATE_IP_ADDRESS -> (if (sqlInstance.getIpAddresses.size() > 0) {
          privateIPAddress
        } else {
          EMPTY
        })) +
        (INSTANCE_ID -> instanceId) +
        (REGION -> sqlInstance.getRegion) +
        (SELF_LINK -> sqlInstance.getSelfLink) +
        (AVAILABILITY_TYPE -> sqlInstanceSettings.getAvailabilityType) +
        (STORAGE_TYPE -> sqlInstanceSettings.getDataDiskType) +
        (STORAGE_CAPACITY -> sqlInstanceSettings.getDataDiskSizeGb.toString) +
        (STATUS -> Utility.getInstanceStatus(sqlInstanceSettings.getActivationPolicy)) +
        (MACHINE_TYPE -> sqlInstanceSettings.getTier) +
        (ACTIVATION_POLICY -> sqlInstanceSettings.getActivationPolicy)
    } catch {
      case e: Throwable => getFailureResultsMap(e)
    }
  }
}

