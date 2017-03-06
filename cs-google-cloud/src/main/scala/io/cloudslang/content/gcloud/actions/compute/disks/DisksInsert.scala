package io.cloudslang.content.gcloud.actions.compute.disks

import java.util

import com.google.api.services.compute.model.Disk
import com.hp.oo.sdk.content.annotations.{Action, Output, Param, Response}
import com.hp.oo.sdk.content.plugin.ActionMetadata.{MatchType, ResponseType}
import io.cloudslang.content.constants.{OutputNames, ResponseNames, ReturnCodes}
import io.cloudslang.content.gcloud.services.compute.disks.{DiskController, DiskService}
import io.cloudslang.content.gcloud.utils.Constants.NEW_LINE
import io.cloudslang.content.gcloud.utils.action.DefaultValues._
import io.cloudslang.content.gcloud.utils.action.GoogleOutputNames.ZONE_OPERATION_NAME
import io.cloudslang.content.gcloud.utils.action.InputNames._
import io.cloudslang.content.gcloud.utils.action.InputUtils.verifyEmpty
import io.cloudslang.content.gcloud.utils.action.InputValidator._
import io.cloudslang.content.gcloud.utils.service.{GoogleAuth, HttpTransportUtils, JsonFactoryUtils}
import io.cloudslang.content.utils.BooleanUtilities.toBoolean
import io.cloudslang.content.utils.NumberUtilities.toInteger
import io.cloudslang.content.utils.OutputUtilities.{getFailureResultsMap, getSuccessResultsMap}
import org.apache.commons.lang3.StringUtils.{EMPTY, defaultIfEmpty}

import scala.collection.JavaConversions._
/**
  * Created by victor on 3/3/17.
  */
class DisksInsert {
  @Action(name = "Insert Disk",
    outputs = Array(
      new Output(OutputNames.RETURN_CODE),
      new Output(OutputNames.RETURN_RESULT),
      new Output(OutputNames.EXCEPTION),
      new Output(ZONE_OPERATION_NAME)
    ),
    responses = Array(
      new Response(text = ResponseNames.SUCCESS, field = OutputNames.RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
      new Response(text = ResponseNames.FAILURE, field = OutputNames.RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
    )
  )
  def execute(@Param(value = PROJECT_ID, required = true) projectId: String,
              @Param(value = ZONE, required = true) zone: String,
              @Param(value = ACCESS_TOKEN, required = true, encrypted = true) accessToken: String,
              @Param(value = DISK_NAME, required = true) diskName: String,
              @Param(value = DISK_SIZE) diskSizeInp: String,
              @Param(value = DISK_DESCRIPTION) diskDescriptionInp: String,
              @Param(value = LICENSES) licensesListInp: String,
              @Param(value = LICENSES_DELIMITER) licensesDelimiterInp: String,
              @Param(value = SOURCE_IMAGE) sourceImage: String,
              @Param(value = SNAPSHOT_IMAGE) snapshotImage: String,
              @Param(value = IMAGE_ENCRYPTION_KEY) imageEncryptionKey: String,
              @Param(value = DISK_TYPE, required = true) diskType: String,
              @Param(value = DISK_ENCRYPTION_KEY) diskEncryptionKey: String,
              @Param(value = PROXY_HOST) proxyHost: String,
              @Param(value = PROXY_PORT) proxyPortInp: String,
              @Param(value = PROXY_USERNAME) proxyUsername: String,
              @Param(value = PROXY_PASSWORD, encrypted = true) proxyPasswordInp: String,
              @Param(value = PRETTY_PRINT) prettyPrintInp: String): util.Map[String, String] = {

    val proxyHostOpt = verifyEmpty(proxyHost)
    val proxyUsernameOpt = verifyEmpty(proxyUsername)
    val sourceImageOpt = verifyEmpty(sourceImage)
    val snapshotImageOpt = verifyEmpty(snapshotImage)
    val imageEncryptionKeyOpt = verifyEmpty(imageEncryptionKey)
    val diskEncryptionKeyOpt = verifyEmpty(diskEncryptionKey)
    val diskDescription = defaultIfEmpty(diskDescriptionInp, EMPTY)
    val diskSizeStr = defaultIfEmpty(diskSizeInp, DEFAULT_DISK_SIZE)
    val proxyPortStr = defaultIfEmpty(proxyPortInp, DEFAULT_PROXY_PORT)
    val proxyPassword = defaultIfEmpty(proxyPasswordInp, EMPTY)
    val prettyPrintStr = defaultIfEmpty(prettyPrintInp, DEFAULT_PRETTY_PRINT)
    val licensesList = defaultIfEmpty(licensesListInp, EMPTY)
    val licensesDel = defaultIfEmpty(licensesDelimiterInp, DEFAULT_LICENSES_DELIMITER)

    val validationStream = validateProxyPort(proxyPortStr) ++
      validateBoolean(prettyPrintStr, PRETTY_PRINT) ++
      validateDiskSize(diskSizeStr, DISK_SIZE)

    if (validationStream.nonEmpty) {
      return getFailureResultsMap(validationStream.mkString(NEW_LINE))
    }

    val proxyPort = toInteger(proxyPortStr)
    val prettyPrint = toBoolean(prettyPrintStr)
    val diskSize = toInteger(diskSizeStr).toLong

    try {
      val httpTransport = HttpTransportUtils.getNetHttpTransport(proxyHostOpt, proxyPort, proxyUsernameOpt, proxyPassword)
      val jsonFactory = JsonFactoryUtils.getDefaultJacksonFactory
      val credential = GoogleAuth.fromAccessToken(accessToken)

      val computeDisk: Disk = DiskController.createDisk(
        zone = zone,
        diskName = diskName,
        sourceImageOpt = sourceImageOpt,
        snapshotImageOpt = snapshotImageOpt,
        imageEncryptionKeyOpt = imageEncryptionKeyOpt,
        diskEncryptionKeyOpt = diskEncryptionKeyOpt,
        diskType = diskType,
        diskDescription = diskDescription,
        licensesList = licensesList,
        licensesDel = licensesDel,
        diskSize = diskSize)

      val operation = DiskService.insert(httpTransport, jsonFactory, credential, projectId, zone, computeDisk)
      val resultString = if (prettyPrint) operation.toPrettyString else operation.toString

      getSuccessResultsMap(resultString) + (ZONE_OPERATION_NAME -> operation.getName)
    } catch {
      case e: Throwable => getFailureResultsMap(e)
    }
  }
}
