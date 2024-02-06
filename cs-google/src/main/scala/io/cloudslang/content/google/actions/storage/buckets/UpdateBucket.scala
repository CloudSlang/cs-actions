/*
 * Copyright 2024 Open Text
 * This program and the accompanying materials
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



package io.cloudslang.content.google.actions.storage.buckets

import java.util

import com.hp.oo.sdk.content.annotations.{Action, Output, Param, Response}
import com.hp.oo.sdk.content.plugin.ActionMetadata.{MatchType, ResponseType}
import io.cloudslang.content.constants.OutputNames.{EXCEPTION, RETURN_CODE, RETURN_RESULT}
import io.cloudslang.content.constants.{ResponseNames, ReturnCodes}
import io.cloudslang.content.google.services.storage.buckets.BucketService
import io.cloudslang.content.google.utils.Constants.StorageBucketConstants._
import io.cloudslang.content.google.utils.Constants.{FALSE, NEW_LINE}
import io.cloudslang.content.google.utils.action.DefaultValues.StorageBucket.{DEFAULT_LABELS, DEFAULT_PROJECTION, DEFAULT_RETENTION_PERIOD_TYPE}
import io.cloudslang.content.google.utils.action.DefaultValues.{DEFAULT_PRETTY_PRINT, DEFAULT_PROXY_PORT}
import io.cloudslang.content.google.utils.action.Descriptions.Common._
import io.cloudslang.content.google.utils.action.Descriptions.StorageBucketDesc.{BUCKET_NAME_DESC, METAGENERATION_MATCH_DESC, METAGENERATION_NOT_MATCH_DESC, PROJECTION_DESC}
import io.cloudslang.content.google.utils.action.Descriptions.UpdateStorageBucketDesc._
import io.cloudslang.content.google.utils.action.InputNames.StorageBucketInputs.{BUCKET_NAME, METAGENERATION_MATCH, METAGENERATION_NOT_MATCH, PROJECTION}
import io.cloudslang.content.google.utils.action.InputNames.UpdateStorageBucketInputs.{LABELS, _}
import io.cloudslang.content.google.utils.action.InputNames._
import io.cloudslang.content.google.utils.action.InputUtils.verifyEmpty
import io.cloudslang.content.google.utils.action.InputValidator.{validateBoolean, validateLong, validateProxyPort}
import io.cloudslang.content.google.utils.action.OutputUtils.toPretty
import io.cloudslang.content.google.utils.action.Outputs.SQLDatabaseInstance.SELF_LINK
import io.cloudslang.content.google.utils.action.Outputs.StorageBucketOutputs._
import io.cloudslang.content.google.utils.service.{GoogleAuth, HttpTransportUtils, JsonFactoryUtils, Utility}
import io.cloudslang.content.utils.BooleanUtilities.toBoolean
import io.cloudslang.content.utils.NumberUtilities.toInteger
import io.cloudslang.content.utils.OutputUtilities.{getFailureResultsMap, getSuccessResultsMap}
import org.apache.commons.lang3.StringUtils.{EMPTY, defaultIfEmpty}

import scala.collection.JavaConversions._

class UpdateBucket {
  @Action(name = UPDATE_BUCKET_OPERATION_NAME,
    description = UPDATE_BUCKET_OPERATION_DESC,
    outputs = Array(
      new Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
      new Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
      new Output(value = EXCEPTION, description = EXCEPTION_DESC),
      new Output(value = STORAGE_CLASS, description = STORAGE_CLASS_DESC),
      new Output(value = LABELS, description = LABELS_DESC),
      new Output(value = RETENTION_PERIOD, description = RETENTION_PERIOD_DESC),
      new Output(value = ACCESS_CONTROL, description = ACCESS_CONTROL_TYPE_DESC),
      new Output(value = DEFAULT_EVENT_BASED_HOLD_ENABLED, description = IS_DEFAULT_EVENT_BASED_HOLD_ENABLED_DESC),
      new Output(value = VERSIONING_ENABLED, description = IS_DEFAULT_EVENT_BASED_HOLD_ENABLED_DESC),
      new Output(value = LOCATION, description = LOCATION_DESC),
      new Output(value = LOCATION_TYPE, description = LOCATION_TYPE_DESC),
      new Output(value = SELF_LINK, description = SELF_LINK_DESC)
    )
    ,
    responses = Array(
      new Response(text = ResponseNames.SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS,
        matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
      new Response(text = ResponseNames.FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE,
        matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
    )
  )
  def execute(@Param(value = ACCESS_TOKEN, required = true, encrypted = true, description = ACCESS_TOKEN_DESC) accessToken: String,
              @Param(value = BUCKET_NAME, required = true, description = BUCKET_NAME_DESC) bucketName: String,
              @Param(value = METAGENERATION_MATCH, description = METAGENERATION_MATCH_DESC) metagenerationMatch: String,
              @Param(value = METAGENERATION_NOT_MATCH, description = METAGENERATION_NOT_MATCH_DESC) metagenerationNotMatch: String,
              @Param(value = PREDEFINED_ACL, description = PREDEFINED_ACL_DESC) predefinedAcl: String,
              @Param(value = PREDEFINED_DEFAULT_OBJECT_ACL, description = PREDEFINED_DEFAULT_OBJECT_ACL_DESC) predefinedDefaultObjectAcl: String,
              @Param(value = PROJECTION, description = PROJECTION_DESC) projection: String,

              @Param(value = STORAGE_CLASS, description = STORAGE_CLASS_DESC) storageClass: String,
              @Param(value = LABELS, description = LABELS_DESC) labels: String,
              @Param(value = IS_DEFAULT_EVENT_BASED_HOLD_ENABLED, description = IS_DEFAULT_EVENT_BASED_HOLD_ENABLED_DESC) isDefaultEventBasedHoldEnabled: String,
              @Param(value = IS_VERSIONING_ENABLED, description = IS_VERSIONING_ENABLED_DESC) isVersioningEnabled: String,
              @Param(value = ACCESS_CONTROL_TYPE, description = ACCESS_CONTROL_TYPE_DESC) accessControlType: String,
              @Param(value = RETENTION_PERIOD_TYPE, description = RETENTION_PERIOD_TYPE_DESC) retentionPeriodType: String,
              @Param(value = RETENTION_PERIOD, description = RETENTION_PERIOD_DESC) retentionPeriod: String,
              @Param(value = REMOVE_RETENTION_POLICY, description = REMOVE_RETENTION_POLICY_DESC) removeRetentionPolicy: String,

              @Param(value = PROXY_HOST, description = PROXY_HOST_DESC) proxyHost: String,
              @Param(value = PROXY_PORT, description = PROXY_PORT_DESC) proxyPort: String,
              @Param(value = PROXY_USERNAME, description = PROXY_USERNAME_DESC) proxyUsername: String,
              @Param(value = PROXY_PASSWORD, encrypted = true, description = PROXY_PASSWORD_DESC) proxyPassword: String,
              @Param(value = PRETTY_PRINT, description = PRETTY_PRINT_DESC) prettyPrintInput: String): util.Map[String, String] = {

    val metagenerationMatchStr = defaultIfEmpty(metagenerationMatch, EMPTY)
    val metagenerationNotMatchStr = defaultIfEmpty(metagenerationNotMatch, EMPTY)
    val predefinedAclStr = defaultIfEmpty(predefinedAcl, EMPTY)
    val predefinedDefaultObjectAclStr = defaultIfEmpty(predefinedDefaultObjectAcl, EMPTY)
    val projectionStr = defaultIfEmpty(projection, DEFAULT_PROJECTION)

    val proxyHostStr = verifyEmpty(proxyHost)
    val proxyUsernameOpt = verifyEmpty(proxyUsername)
    val proxyPortInt = defaultIfEmpty(proxyPort, DEFAULT_PROXY_PORT)
    val proxyPasswordStr = defaultIfEmpty(proxyPassword, EMPTY)
    val prettyPrintStr = defaultIfEmpty(prettyPrintInput, DEFAULT_PRETTY_PRINT)

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

      val bucketDetails = BucketService.getBucket(httpTransport, jsonFactory, credential, bucketName, projectionStr)

      var accessControlTypeStr = defaultIfEmpty(accessControlType, EMPTY)
      var labelsStr = defaultIfEmpty(labels, DEFAULT_LABELS)
      var storageClassStr = defaultIfEmpty(storageClass, EMPTY)
      var retentionPeriodStr = defaultIfEmpty(retentionPeriod, EMPTY)
      val retentionPeriodTypeStr = defaultIfEmpty(retentionPeriodType, DEFAULT_RETENTION_PERIOD_TYPE)
      var isDefaultEventBasedHoldEnabledStr = defaultIfEmpty(isDefaultEventBasedHoldEnabled, EMPTY)
      var isVersioningEnabledStr = defaultIfEmpty(isVersioningEnabled, EMPTY)
      var removeRetentionPolicyStr = EMPTY

      if (storageClass.isEmpty) {
        storageClassStr = bucketDetails.getStorageClass
      } else {
        storageClassStr = storageClass
      }

      if (isDefaultEventBasedHoldEnabled.isEmpty) {
        if (bucketDetails.containsKey(DEFAULT_EVENT_BASED_HOLD_ENABLED)) {
          isDefaultEventBasedHoldEnabledStr = bucketDetails.getDefaultEventBasedHold.toString
        }
      } else {
        val validationStream = validateBoolean(isDefaultEventBasedHoldEnabled, IS_DEFAULT_EVENT_BASED_HOLD_ENABLED)
        if (validationStream.nonEmpty) {
          return getFailureResultsMap(validationStream.mkString(NEW_LINE))
        } else {
          isDefaultEventBasedHoldEnabledStr = isDefaultEventBasedHoldEnabled
        }
      }

      if (accessControlType.isEmpty) {
        if (bucketDetails.getIamConfiguration.getUniformBucketLevelAccess.getEnabled) {
          accessControlTypeStr = UNIFORM_ACCESS_CONTROL
        } else {
          accessControlTypeStr = FINE_GRAINED_ACCESS_CONTROL
        }
      } else {
        accessControlTypeStr = accessControlType
      }

      if (removeRetentionPolicy.isEmpty || removeRetentionPolicy.equalsIgnoreCase(FALSE)) {
        if (retentionPeriod.isEmpty) {
          if (bucketDetails.containsKey(RETENTION_POLICY)) {
            retentionPeriodStr = bucketDetails.getRetentionPolicy.getRetentionPeriod.toString
          }
        } else {
          val validationStream = validateLong(retentionPeriod, RETENTION_PERIOD)
          if (validationStream.nonEmpty) {
            return getFailureResultsMap(validationStream.mkString(NEW_LINE))
          } else {
            retentionPeriodStr = retentionPeriod
          }
        }
      } else {
        if (validateBoolean(removeRetentionPolicy, REMOVE_RETENTION_POLICY).nonEmpty) {
          return getFailureResultsMap(validationStream.mkString(NEW_LINE))
        } else {
          removeRetentionPolicyStr = removeRetentionPolicy
        }
      }

      if (isVersioningEnabled.isEmpty) {
        if (bucketDetails.containsKey(VERSIONING)) {
          isVersioningEnabledStr = bucketDetails.getVersioning.getEnabled.toString
        }
      } else {
        val validationStream = validateBoolean(isVersioningEnabled, IS_VERSIONING_ENABLED)
        if (validationStream.nonEmpty) {
          return getFailureResultsMap(validationStream.mkString(NEW_LINE))
        } else {
          isVersioningEnabledStr = isVersioningEnabled
        }
      }

      if (labels.isEmpty) {
        if (bucketDetails.containsKey(LABELS)) {
          labelsStr = bucketDetails.getLabels.toString
        }
      } else {
        labelsStr = labels
      }

      val bucketUpdate = BucketService.update(httpTransport, jsonFactory, credential, bucketName, metagenerationMatchStr,
        metagenerationNotMatchStr, predefinedAclStr, predefinedDefaultObjectAclStr, projectionStr, storageClassStr,
        isDefaultEventBasedHoldEnabledStr, isVersioningEnabledStr, accessControlTypeStr, retentionPeriodTypeStr,
        retentionPeriodStr, removeRetentionPolicy, Utility.jsonToMap(labelsStr))

      getSuccessResultsMap(toPretty(prettyPrint, bucketUpdate)) +
        (STORAGE_CLASS -> bucketUpdate.getStorageClass) +
        (RETENTION_PERIOD -> (if (bucketUpdate.containsKey(RETENTION_POLICY)) {
          bucketUpdate.getRetentionPolicy.getRetentionPeriod.toString
        } else {
          EMPTY
        })) +
        (LABELS -> (if (bucketUpdate.containsKey(LABELS)) {
          bucketUpdate.getLabels.toString
        } else {
          DEFAULT_LABELS
        })) +
        (ACCESS_CONTROL -> (if (bucketUpdate.getIamConfiguration.getUniformBucketLevelAccess.getEnabled) {
          UNIFORM_ACCESS_CONTROL
        } else {
          FINE_GRAINED_ACCESS_CONTROL
        })) +
        (DEFAULT_EVENT_BASED_HOLD_ENABLED -> (if (bucketUpdate.containsKey(DEFAULT_EVENT_BASED_HOLD_KEY)) {
          bucketUpdate.getDefaultEventBasedHold.toString
        } else {
          EMPTY
        })) +
        (VERSIONING_ENABLED -> (if (bucketUpdate.containsKey(VERSIONING)) {
          bucketUpdate.getVersioning.getEnabled.toString
        } else {
          EMPTY
        })) +
        (LOCATION -> bucketUpdate.getLocation) +
        (LOCATION_TYPE -> bucketUpdate.getLocationType) +
        (SELF_LINK -> bucketUpdate.getSelfLink)

    } catch {
      case e: Throwable => getFailureResultsMap(e)
    }
  }
}
