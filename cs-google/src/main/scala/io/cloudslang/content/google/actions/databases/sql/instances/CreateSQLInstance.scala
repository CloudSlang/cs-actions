/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
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
import io.cloudslang.content.constants.OutputNames.{EXCEPTION, RETURN_CODE, RETURN_RESULT}
import io.cloudslang.content.constants.{ResponseNames, ReturnCodes}
import io.cloudslang.content.google.services.databases.sql.instances.SQLDatabaseInstanceService
import io.cloudslang.content.google.utils.Constants.NEW_LINE
import io.cloudslang.content.google.utils.{SQLErrorOperation, SQLOperationStatus, SQLSuccessOperation}
import io.cloudslang.content.google.utils.action.DefaultValues.{DEFAULT_PRETTY_PRINT, DEFAULT_PROXY_PORT}
import io.cloudslang.content.google.utils.action.InputNames.{ACCESS_TOKEN, PRETTY_PRINT, PROJECT_ID, PROXY_HOST, PROXY_PASSWORD, PROXY_PORT, PROXY_USERNAME}
import io.cloudslang.content.google.utils.action.InputNames.CreateSQLDatabaseInstanceInputs.{ACTIVATION_POLICY, AVAILABILITY_TYPE, DATABASE_VERSION, INSTANCE_ID, INSTANCE_PASSWORD, LABELS, MACHINE_TYPE, PREFERRED_MAINTENANCE_WINDOW_DAY, PREFERRED_MAINTENANCE_WINDOW_HOUR, REGION, STORAGE_CAPACITY, STORAGE_TYPE, ZONE}
import io.cloudslang.content.google.utils.action.InputUtils.verifyEmpty
import io.cloudslang.content.google.utils.action.InputValidator.{validateBoolean, validateProxyPort}
import io.cloudslang.content.google.utils.action.OutputUtils.toPretty
import io.cloudslang.content.google.utils.action.Outputs.CreateSQLDatabaseInstance.STATE
import io.cloudslang.content.google.utils.service.Utility
import io.cloudslang.content.google.utils.action.Descriptions.CreateSQLDataBaseInstance.{ACTIVATION_POLICY_DESC, AVAILABILITY_TYPE_DESC, DATABASE_VERSION_DESC, INSTANCE_ID_DESC, INSTANCE_PASSWORD_DESC, LABELS_DESC, MACHINE_TYPE_DESC, PREFERRED_MAINTENANCE_WINDOW_DAY_DESC, PREFERRED_MAINTENANCE_WINDOW_HOUR_DESC, REGION_DESC, STATE_DESC, STORAGE_AUTO_RESIZE_DESC, STORAGE_CAPACITY_DESC, STORAGE_TYPE_DESC, ZONE_DESC}
import io.cloudslang.content.google.utils.action.Descriptions.Common.{ACCESS_TOKEN_DESC, PROJECT_ID_DESC, RETURN_CODE_DESC, RETURN_RESULT_DESC}
import io.cloudslang.content.google.utils.action.DefaultValues.CreateSQLDatabaseInstance.{DEFAULT_ACTIVATION_POLICY, DEFAULT_AVAILABILITY_TYPE, DEFAULT_LABELS, DEFAULT_PREFERRED_MAINTENANCE_WINDOW_DAY, DEFAULT_PREFERRED_MAINTENANCE_WINDOW_HOUR, DEFAULT_STORAGE_AUTO_RESIZE, DEFAULT_STORAGE_CAPACITY, DEFAULT_STORAGE_TYPE, DEFAULT_ZONE}
import io.cloudslang.content.google.utils.service.{GoogleAuth, HttpTransportUtils, JsonFactoryUtils}
import io.cloudslang.content.utils.BooleanUtilities.toBoolean
import io.cloudslang.content.utils.NumberUtilities.toInteger
import io.cloudslang.content.utils.OutputUtilities.{getFailureResultsMap, getSuccessResultsMap}
import org.apache.commons.lang3.StringUtils.{EMPTY, defaultIfEmpty}

class CreateSQLInstance {

  @Action(name = "Create SQL Instance",

    outputs = Array(
      new Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
      new Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
      new Output(EXCEPTION),
      new Output(value = INSTANCE_ID, description = INSTANCE_ID_DESC),
      new Output(value = STATE, description = STATE_DESC)


  )
      ,
    responses = Array(
      new Response(text = ResponseNames.SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
      new Response(text = ResponseNames.FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
    )
  )
  def execute(@Param(value = PROJECT_ID, required = true, description = PROJECT_ID_DESC) projectId: String,
              @Param(value = ACCESS_TOKEN, required = true, encrypted = true, description = ACCESS_TOKEN_DESC) accessToken: String,
              @Param(value = INSTANCE_ID, required = true, description = INSTANCE_ID_DESC) instanceId: String,
              @Param(value = INSTANCE_PASSWORD, required = true, encrypted=true, description = INSTANCE_PASSWORD_DESC) instancePassword: String,
              @Param(value = REGION, required = true, description = REGION_DESC) region: String,
              @Param(value = ZONE, description = ZONE_DESC) zone: String,
              @Param(value = DATABASE_VERSION, required = true, description = DATABASE_VERSION_DESC) databaseVersion: String,
              @Param(value = MACHINE_TYPE, required = true, description = MACHINE_TYPE_DESC) machineType: String,
              @Param(value = STORAGE_TYPE, description = STORAGE_TYPE_DESC) storageType: String,
              @Param(value = STORAGE_CAPACITY, description = STORAGE_CAPACITY_DESC) storageCapacity: String,
              @Param(value = STORAGE_AUTO_RESIZE_DESC, description = STORAGE_AUTO_RESIZE_DESC) storageAutoResize: String,
              @Param(value = AVAILABILITY_TYPE, description = AVAILABILITY_TYPE_DESC) availabilityType: String,
              @Param(value = PREFERRED_MAINTENANCE_WINDOW_DAY, description = PREFERRED_MAINTENANCE_WINDOW_DAY_DESC) preferredMaintenanceWindowDay: String,
              @Param(value = PREFERRED_MAINTENANCE_WINDOW_HOUR, description = PREFERRED_MAINTENANCE_WINDOW_HOUR_DESC) preferredMaintenanceWindowHour: String,
              @Param(value = ACTIVATION_POLICY, description = ACTIVATION_POLICY_DESC) activationPolicy: String,
              @Param(value = LABELS, description = LABELS_DESC) labels: String,
              @Param(value = PROXY_HOST) proxyHost: String,
              @Param(value = PROXY_PORT) proxyPortInp: String,
              @Param(value = PROXY_USERNAME) proxyUsername: String,
              @Param(value = PROXY_PASSWORD, encrypted = true) proxyPasswordInp: String,
              @Param(value = PRETTY_PRINT) prettyPrintInp: String): util.Map[String, String] = {


     val zoneStr = defaultIfEmpty(zone,DEFAULT_ZONE)
     val storageTypeStr = defaultIfEmpty(storageType,DEFAULT_STORAGE_TYPE)
     val storageCapacityInt = defaultIfEmpty(storageCapacity,DEFAULT_STORAGE_CAPACITY)
     val storageAutoResizeStr = defaultIfEmpty(storageAutoResize,DEFAULT_STORAGE_AUTO_RESIZE)
     val availabilityTypeStr = defaultIfEmpty(availabilityType,DEFAULT_AVAILABILITY_TYPE)
     val preferredMaintenanceWindowDayInt = defaultIfEmpty(preferredMaintenanceWindowDay,DEFAULT_PREFERRED_MAINTENANCE_WINDOW_DAY)
     val preferredMaintenanceWindowHourInt = defaultIfEmpty(preferredMaintenanceWindowHour,DEFAULT_PREFERRED_MAINTENANCE_WINDOW_HOUR)
     val activationPolicyStr = defaultIfEmpty(activationPolicy,DEFAULT_ACTIVATION_POLICY)
     val labelsStr = defaultIfEmpty(labels,DEFAULT_LABELS)

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
    val storageCapacityVal = toInteger(storageCapacityInt)
    val storageAutoResizeVal = toBoolean(storageAutoResizeStr)
    val preferredMaintenanceWindowHourVal = toInteger(preferredMaintenanceWindowHourInt)
    val preferredMaintenanceWindowDayVal = toInteger(preferredMaintenanceWindowDayInt)
    val proxyPort = toInteger(proxyPortStr)
    val prettyPrint = toBoolean(prettyPrintStr)

    try {
      val httpTransport = HttpTransportUtils.getNetHttpTransport(proxyHostOpt, proxyPort, proxyUsernameOpt, proxyPassword)
      val jsonFactory = JsonFactoryUtils.getDefaultJacksonFactory
      val credential = GoogleAuth.fromAccessToken(accessToken)

      SQLOperationStatus(SQLDatabaseInstanceService.create(httpTransport, jsonFactory, credential, projectId,
        instanceId,instancePassword,region,zoneStr,databaseVersion,machineType,storageTypeStr,storageCapacityVal,
        storageAutoResizeVal,availabilityTypeStr,preferredMaintenanceWindowDayVal,preferredMaintenanceWindowHourVal,
        activationPolicyStr,Utility.jsonToMap(labelsStr))) match {
        case SQLSuccessOperation(sqlOoperation) =>

          getSuccessResultsMap(toPretty(prettyPrint, sqlOoperation))

        case SQLErrorOperation(error) => getFailureResultsMap(error)
      }


    } catch {
      case e: Throwable => getFailureResultsMap(e)
    }
  }
}
