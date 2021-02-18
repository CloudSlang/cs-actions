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
import io.cloudslang.content.constants.BooleanValues.TRUE
import io.cloudslang.content.constants.OutputNames.{EXCEPTION, RETURN_CODE, RETURN_RESULT}
import io.cloudslang.content.constants.{ResponseNames, ReturnCodes}
import io.cloudslang.content.google.services.databases.sql.instances.SQLDatabaseInstanceService
import io.cloudslang.content.google.utils.Constants.NEW_LINE
import io.cloudslang.content.google.utils.Constants.SQLInstancesConstant.{CREATE_SQL_INSTANCE_OPERATION_NAME, IP_ADDRESS_TYPE_PRIMARY, IP_ADDRESS_TYPE_PRIVATE}
import io.cloudslang.content.google.utils.action.DefaultValues.CreateSQLDatabaseInstance._
import io.cloudslang.content.google.utils.action.DefaultValues._
import io.cloudslang.content.google.utils.action.Descriptions.Common._
import io.cloudslang.content.google.utils.action.Descriptions.CreateSQLDataBaseInstance._
import io.cloudslang.content.google.utils.action.Descriptions.SQLDataBaseInstances.CREATE_SQL_INSTANCE_OPERATION_DESCRIPTION
import io.cloudslang.content.google.utils.action.Descriptions.UpdateSQLDataBaseInstance.{IS_IPV4_ENABLED_DESC, PRIVATE_IP_ADDRESS_DESC, PRIVATE_NETWORK_DESC}
import io.cloudslang.content.google.utils.action.GoogleOutputNames.STATUS
import io.cloudslang.content.google.utils.action.InputNames.CreateSQLDatabaseInstanceInputs.{MACHINE_TYPE, ZONE, _}
import io.cloudslang.content.google.utils.action.InputNames.UpdateSQLDatabaseInstanceInputs.{IS_IPV4_ENABLED, LABELS, PRIVATE_NETWORK}
import io.cloudslang.content.google.utils.action.InputNames._
import io.cloudslang.content.google.utils.action.InputUtils.{convertSecondsToMilli, verifyEmpty}
import io.cloudslang.content.google.utils.action.InputValidator._
import io.cloudslang.content.google.utils.action.OutputUtils.toPretty
import io.cloudslang.content.google.utils.action.Outputs.SQLDatabaseInstance.{CONNECTION_NAME, PRIVATE_IP_ADDRESS, PUBLIC_IP_ADDRESS, SELF_LINK}
import io.cloudslang.content.google.utils.service.{GoogleAuth, HttpTransportUtils, JsonFactoryUtils, Utility}
import io.cloudslang.content.google.utils.{SQLErrorOperation, SQLOperationStatus, SQLSuccessOperation}
import io.cloudslang.content.utils.BooleanUtilities.toBoolean
import io.cloudslang.content.utils.NumberUtilities.{toDouble, toInteger, toLong}
import io.cloudslang.content.utils.OutputUtilities.{getFailureResultsMap, getSuccessResultsMap}
import org.apache.commons.lang3.StringUtils.{EMPTY, defaultIfEmpty}

import scala.collection.JavaConversions._

class CreateSQLInstance {

  @Action(name = CREATE_SQL_INSTANCE_OPERATION_NAME,
    description = CREATE_SQL_INSTANCE_OPERATION_DESCRIPTION,
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
      new Output(value = REGION, description = REGION_DESC),
      new Output(value = SELF_LINK, description = SELF_LINK_DESC),
      new Output(value = AVAILABILITY_TYPE, description = AVAILABILITY_TYPE_DESC),
      new Output(value = STORAGE_TYPE, description = STORAGE_TYPE_DESC),
      new Output(value = STORAGE_CAPACITY, description = STORAGE_CAPACITY_DESC),
      new Output(value = MACHINE_TYPE, description = MACHINE_TYPE_DESC),
      new Output(value = ACTIVATION_POLICY, description = ACTIVATION_POLICY_DESC),
      new Output(value = STATUS, description = STATUS_DESC)

    )
    ,
    responses = Array(
      new Response(text = ResponseNames.SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
      new Response(text = ResponseNames.FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
    )
  )
  def execute(@Param(value = PROJECT_ID, required = true, encrypted = true, description = PROJECT_ID_DESC) projectId: String,
              @Param(value = ACCESS_TOKEN, required = true, encrypted = true, description = ACCESS_TOKEN_DESC) accessToken: String,
              @Param(value = INSTANCE_ID, required = true, description = INSTANCE_ID_DESC) instanceId: String,
              @Param(value = INSTANCE_PASSWORD, required = true, encrypted = true, description = INSTANCE_PASSWORD_DESC) instancePassword: String,
              @Param(value = REGION, required = true, description = REGION_DESC) region: String,
              @Param(value = ZONE, description = ZONE_DESC) zone: String,
              @Param(value = DATABASE_VERSION, required = true, description = DATABASE_VERSION_DESC) databaseVersion: String,
              @Param(value = MACHINE_TYPE, required = true, description = MACHINE_TYPE_DESC) machineType: String,
              @Param(value = STORAGE_TYPE, description = STORAGE_TYPE_DESC) storageType: String,
              @Param(value = STORAGE_CAPACITY, description = STORAGE_CAPACITY_DESC) storageCapacity: String,
              @Param(value = STORAGE_AUTO_RESIZE, description = STORAGE_AUTO_RESIZE_DESC) storageAutoResize: String,
              @Param(value = PRIVATE_NETWORK, description = PRIVATE_NETWORK_DESC) privateNetwork: String,
              @Param(value = IS_IPV4_ENABLED, description = IS_IPV4_ENABLED_DESC) isIPV4Enabled: String,
              @Param(value = AVAILABILITY_TYPE, description = AVAILABILITY_TYPE_DESC) availabilityType: String,
              @Param(value = PREFERRED_MAINTENANCE_WINDOW_DAY, description = PREFERRED_MAINTENANCE_WINDOW_DAY_DESC) preferredMaintenanceWindowDay: String,
              @Param(value = PREFERRED_MAINTENANCE_WINDOW_HOUR, description = PREFERRED_MAINTENANCE_WINDOW_HOUR_DESC) preferredMaintenanceWindowHour: String,
              @Param(value = ACTIVATION_POLICY, description = ACTIVATION_POLICY_DESC) activationPolicy: String,
              @Param(value = LABELS, description = LABELS_DESC) labels: String,

              @Param(value = ASYNC, description = ASYNC_DESC) asyncInp: String,
              @Param(value = TIMEOUT, description = TIMEOUT_DESC) timeoutInp: String,
              @Param(value = POLLING_INTERVAL, description = POLLING_INTERVAL_DESC) pollingIntervalInp: String,

              @Param(value = PROXY_HOST, description = PROXY_HOST_DESC) proxyHost: String,
              @Param(value = PROXY_PORT, description = PROXY_PORT_DESC) proxyPort: String,
              @Param(value = PROXY_USERNAME, description = PROXY_USERNAME_DESC) proxyUsername: String,
              @Param(value = PROXY_PASSWORD, encrypted = true, description = PROXY_PASSWORD_DESC) proxyPassword: String,
              @Param(value = PRETTY_PRINT, description = PRETTY_PRINT_DESC) prettyPrintInp: String): util.Map[String, String] = {


    val zoneStr = defaultIfEmpty(zone, DEFAULT_ZONE)
    val storageTypeStr = defaultIfEmpty(storageType, DEFAULT_STORAGE_TYPE)
    val storageCapacityInt = defaultIfEmpty(storageCapacity, DEFAULT_STORAGE_CAPACITY)
    val storageAutoResizeStr = defaultIfEmpty(storageAutoResize, DEFAULT_STORAGE_AUTO_RESIZE)
    val isIPV4EnabledStr = defaultIfEmpty(isIPV4Enabled, TRUE)
    val availabilityTypeStr = defaultIfEmpty(availabilityType, DEFAULT_AVAILABILITY_TYPE)
    val preferredMaintenanceWindowDayInt = defaultIfEmpty(preferredMaintenanceWindowDay, DEFAULT_PREFERRED_MAINTENANCE_WINDOW_DAY)
    val preferredMaintenanceWindowHourInt = defaultIfEmpty(preferredMaintenanceWindowHour, DEFAULT_PREFERRED_MAINTENANCE_WINDOW_HOUR)
    val activationPolicyStr = defaultIfEmpty(activationPolicy, DEFAULT_ACTIVATION_POLICY)
    val labelsStr = defaultIfEmpty(labels, DEFAULT_LABELS)

    val asyncStr = defaultIfEmpty(asyncInp, TRUE)
    val timeoutStr = defaultIfEmpty(timeoutInp, DEFAULT_SYNC_TIMEOUT)
    val pollingIntervalStr = defaultIfEmpty(pollingIntervalInp, DEFAULT_POLLING_INTERVAL)

    val proxyHostStr = verifyEmpty(proxyHost)
    val proxyUsernameOpt = verifyEmpty(proxyUsername)
    val proxyPortInt = defaultIfEmpty(proxyPort, DEFAULT_PROXY_PORT)
    val proxyPasswordStr = defaultIfEmpty(proxyPassword, EMPTY)
    val prettyPrintStr = defaultIfEmpty(prettyPrintInp, DEFAULT_PRETTY_PRINT)

    val validationStream = validateDiskSize(storageCapacityInt, STORAGE_CAPACITY) ++
      validateNonNegativeInteger(preferredMaintenanceWindowDayInt, PREFERRED_MAINTENANCE_WINDOW_DAY) ++
      validateinstanceId(instanceId) ++
      validateNonNegativeInteger(preferredMaintenanceWindowHourInt, PREFERRED_MAINTENANCE_WINDOW_HOUR) ++
      validateBoolean(storageAutoResizeStr, STORAGE_AUTO_RESIZE) ++
      validateBoolean(isIPV4EnabledStr, IS_IPV4_ENABLED) ++
      validateProxyPort(proxyPortInt) ++
      validateBoolean(prettyPrintStr, PRETTY_PRINT) ++
      validateBoolean(asyncStr, ASYNC) ++
      validateNonNegativeLong(timeoutStr, TIMEOUT) ++
      validateNonNegativeDouble(pollingIntervalStr, POLLING_INTERVAL)

    if (validationStream.nonEmpty) {
      return getFailureResultsMap(validationStream.mkString(NEW_LINE))
    }
    val storageCapacityVal = toInteger(storageCapacityInt)
    val storageAutoResizeVal = toBoolean(storageAutoResizeStr)
    val isIPV4EnabledVal = toBoolean(isIPV4EnabledStr)
    val preferredMaintenanceWindowHourVal = toInteger(preferredMaintenanceWindowHourInt)
    val preferredMaintenanceWindowDayVal = toInteger(preferredMaintenanceWindowDayInt)
    val proxyPortVal = toInteger(proxyPortInt)
    val prettyPrint = toBoolean(prettyPrintStr)

    val async = toBoolean(asyncStr)
    val timeout = toLong(timeoutStr)
    val pollingIntervalMilli = convertSecondsToMilli(toDouble(pollingIntervalStr))

    try {
      val httpTransport = HttpTransportUtils.getNetHttpTransport(proxyHostStr, proxyPortVal, proxyUsernameOpt, proxyPasswordStr)
      val jsonFactory = JsonFactoryUtils.getDefaultJacksonFactory
      val credential = GoogleAuth.fromAccessToken(accessToken)

      SQLOperationStatus(SQLDatabaseInstanceService.create(httpTransport, jsonFactory, credential, projectId,
        instanceId, instancePassword, region, zoneStr, databaseVersion, machineType, storageTypeStr, storageCapacityVal,
        storageAutoResizeVal, privateNetwork, isIPV4EnabledVal, availabilityTypeStr, preferredMaintenanceWindowDayVal, preferredMaintenanceWindowHourVal,
        activationPolicyStr, Utility.jsonToMap(labelsStr), async,
        timeout, pollingIntervalMilli)) match {
        case SQLSuccessOperation(sqlOperation) =>
          val status = defaultIfEmpty(sqlOperation.getStatus, EMPTY)
          val resultMap = getSuccessResultsMap(toPretty(prettyPrint, sqlOperation)) + (INSTANCE_ID -> sqlOperation.getTargetId) +
            (SELF_LINK -> sqlOperation.getSelfLink) + (STATUS -> status)

          if (async) {
            resultMap +
              (STATUS -> status)
          } else {
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

            resultMap +
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
              (STATUS -> Utility.getInstanceStatus(activationPolicyStr)) +
              (MACHINE_TYPE -> sqlInstanceSettings.getTier) +
              (ACTIVATION_POLICY -> sqlInstanceSettings.getActivationPolicy)
          }
        case SQLErrorOperation(error) => getFailureResultsMap(error)
      }

    } catch {
      case e: Throwable => getFailureResultsMap(e)
    }
  }
}
