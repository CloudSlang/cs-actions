package io.cloudslang.content.google.actions.compute.compute_engine.disks

import java.util

import com.google.api.services.compute.model.Disk
import com.hp.oo.sdk.content.annotations.{Action, Output, Param, Response}
import com.hp.oo.sdk.content.plugin.ActionMetadata.{MatchType, ResponseType}
import io.cloudslang.content.constants.BooleanValues.FALSE
import io.cloudslang.content.constants.OutputNames.{EXCEPTION, RETURN_CODE, RETURN_RESULT}
import io.cloudslang.content.constants.{OutputNames, ResponseNames, ReturnCodes}
import io.cloudslang.content.google.services.compute.compute_engine.disks.{DiskController, DiskService}
import io.cloudslang.content.google.utils.Constants.{NEW_LINE, TIMEOUT_EXCEPTION}
import io.cloudslang.content.google.utils.action.DefaultValues._
import io.cloudslang.content.google.utils.action.GoogleOutputNames.{ZONE_OPERATION_NAME => _, _}
import io.cloudslang.content.google.utils.action.InputNames._
import io.cloudslang.content.google.utils.action.InputUtils.{convertSecondsToMilli, verifyEmpty}
import io.cloudslang.content.google.utils.action.InputValidator._
import io.cloudslang.content.google.utils.action.OutputUtils.toPretty
import io.cloudslang.content.google.utils.service.{GoogleAuth, HttpTransportUtils, JsonFactoryUtils}
import io.cloudslang.content.utils.BooleanUtilities.toBoolean
import io.cloudslang.content.utils.NumberUtilities.{toDouble, toInteger, toLong}
import io.cloudslang.content.utils.OutputUtilities.{getFailureResultsMap, getSuccessResultsMap}
import org.apache.commons.lang3.StringUtils.{EMPTY, defaultIfEmpty}

import scala.collection.JavaConversions._
import scala.concurrent.TimeoutException

/**
  * Created by victor on 3/3/17.
  */
class DisksInsert {

  /**
    * Creates a disk resource in the specified project using the data included as inputs.
    *
    * @param projectId            Name of the Google Cloud project.
    * @param zone                 Name of the zone for this request.
    * @param accessToken          The access token from GetAccessToken.
    * @param diskName             Name of the Disk. Provided by the client when the Disk is created. The name must be
    *                             1-63 characters long, and comply with RFC1035. Specifically, the name must be 1-63 characters
    *                             long and match the regular expression [a-z]([-a-z0-9]*[a-z0-9])? which means the first
    *                             character must be a lowercase letter, and all following characters must be a dash, lowercase
    *                             letter, or digit, except the last character, which cannot be a dash.
    * @param diskSizeInp          Optional - Size of the persistent disk, specified in GB. You can specify this field when creating a
    *                             persistent disk using the sourceImage or sourceSnapshot parameter, or specify it alone to
    *                             create an empty persistent disk.
    *                             If you specify this field along with sourceImage or sourceSnapshot, the value of sizeGb must
    *                             not be less than the size of the sourceImage or the size of the snapshot.
    *                             Constraint: Number greater or eaqual with 10
    *                             Default: "10"
    * @param diskDescriptionInp   Optional - The description of the new Disk
    * @param licensesListInp      Optional - A list containing any applicable publicly visible licenses separated by <licensesDelimiterInp>.
    * @param licensesDelimiterInp Optional - The delimiter used to split the <licensesListInp>
    * @param sourceImage          Optional - The source image used to create this disk. If the source image is deleted, this field will not
    *                             be set.
    *                             To create a disk with one of the public operating system images, specify the image by its
    *                             family name. For example, specify family/debian-8 to use the latest Debian 8 image:
    *                             "projects/debian-cloud/global/images/family/debian-8"
    *                             Alternatively, use a specific version of a public operating system image:
    *                             "projects/debian-cloud/global/images/debian-8-jessie-vYYYYMMDD"
    *                             To create a disk with a private image that you created, specify the image name in the following
    *                             format:
    *                             "global/images/my-private-image"
    *                             You can also specify a private image by its image family, which returns the latest version of
    *                             the image in that family. Replace the image name with family/family-name:
    *                             "global/images/family/my-private-family"
    *                             Note: mutual exclusive with <snapshotImage>
    * @param snapshotImage        Optional - The source snapshot used to create this disk. You can provide this as a partial or full URL to
    *                             the resource. For example, the following are valid values:
    *                             "https://www.googleapis.com/compute/v1/projects/project/global/snapshots/snapshot"
    *                             "projects/project/global/snapshots/snapshot"
    *                             "global/snapshots/snapshot"
    *                             Note: mutual exclusive with <sourceImage>
    * @param imageEncryptionKey   Optional - The customer-supplied encryption key of the source image. Required if the source image is
    *                             protected by a customer-supplied encryption key.
    * @param diskType             URL of the disk type resource describing which disk type to use to create the disk. Provide
    *                             this when creating the disk.
    * @param diskEncryptionKey    Optional -  Encrypts the disk using a customer-supplied encryption key.
    *                             After you encrypt a disk with a customer-supplied key, you must provide the same key if you use
    *                             the disk later (e.g. to create a disk snapshot or an image, or to attach the disk to a virtual
    *                             machine).
    *                             Customer-supplied encryption keys do not protect access to metadata of the disk.
    *                             If you do not provide an encryption key when creating the disk, then the disk will be encrypted
    *                             using an automatically generated key and you do not need to provide a key to use the disk
    *                             later.
    * @param syncInp              Optional - Boolean specifying whether the operation to run sync or async.
    *                             Valid values: "true", "false"
    *                             Default: "false"
    * @param timeoutInp           Optional - The time, in seconds, to wait for a response if the syncInp is set to "true".
    *                             If the value is 0, the operation will wait until zone operation progress is 100.
    *                             Valid values: Any positive number including 0.
    *                             Default: "30"
    * @param pollingIntervalInp   Optional - The time, in seconds, to wait before a new request that verifies if the operation finished
    *                             is executed, if the sync input is set to "true".
    *                             Valid values: Any positive number including 0.
    *                             Default: "1"
    * @param proxyHost            Optional - Proxy server used to connect to Google Cloud API. If empty no proxy will
    *                             be used.
    * @param proxyPortInp         Optional - Proxy server port.
    *                             Default: "8080"
    * @param proxyUsername        Optional - Proxy server user name.
    * @param proxyPasswordInp     Optional - Proxy server password associated with the proxyUsername input value.
    * @param prettyPrintInp       Optional - Whether to format (pretty print) the resulting json.
    *                             Valid values: "true", "false"
    *                             Default: "true"
    * @return A map with strings as keys and strings as values that contains: outcome of the action, returnCode of the
    *         operation, zoneOperationName, zone, diskName and the status of the operation if the <syncInp> is false.
    *         If <syncInp> is true the map will also contain the disk size, the disk id and the status of the operation
    *         will be replaced by the status of the disk.
    *         In case an exception occurs the failure message is provided.
    */
  @Action(name = "Insert Disk",
    outputs = Array(
      new Output(RETURN_CODE),
      new Output(RETURN_RESULT),
      new Output(EXCEPTION),
      new Output(ZONE_OPERATION_NAME),
      new Output(DISK_ID),
      new Output(DISK_NAME),
      new Output(DISK_SIZE),
      new Output(STATUS),
      new Output(ZONE)
    ),
    responses = Array(
      new Response(text = ResponseNames.SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
      new Response(text = ResponseNames.FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
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
    val syncStr = defaultIfEmpty(syncInp, FALSE)
    val timeoutStr = defaultIfEmpty(timeoutInp, DEFAULT_SYNC_TIMEOUT)
    val pollingIntervalStr = defaultIfEmpty(pollingIntervalInp, DEFAULT_POLLING_INTERVAL)

    val validationStream = validateProxyPort(proxyPortStr) ++
      validateBoolean(prettyPrintStr, PRETTY_PRINT) ++
      validateDiskSize(diskSizeStr, DISK_SIZE) ++
      validateBoolean(syncStr, SYNC) ++
      validateNonNegativeLong(timeoutStr, TIMEOUT) ++
      validateNonNegativeDouble(pollingIntervalStr, POLLING_INTERVAL)

    if (validationStream.nonEmpty) {
      return getFailureResultsMap(validationStream.mkString(NEW_LINE))
    }

    val proxyPort = toInteger(proxyPortStr)
    val prettyPrint = toBoolean(prettyPrintStr)
    val diskSize = toInteger(diskSizeStr).toLong
    val sync = toBoolean(syncStr)
    val timeout = toLong(timeoutStr)
    val pollingIntervalMilli = convertSecondsToMilli(toDouble(pollingIntervalStr))

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

      val operation = DiskService.insert(httpTransport, jsonFactory, credential, projectId, zone, computeDisk, sync, timeout, pollingIntervalMilli)
      val resultMap = getSuccessResultsMap(toPretty(prettyPrint, operation)) +
        (ZONE_OPERATION_NAME -> operation.getName) +
        (ZONE -> zone) +
        (DISK_NAME -> diskName)

      if (sync) {
        val disk = DiskService.get(httpTransport, jsonFactory, credential, projectId, zone, diskName)
        val diskId = Option(disk.getId).getOrElse(BigInt(0)).toString
        val status = defaultIfEmpty(disk.getStatus, EMPTY)
        val diskSize = Option(disk.getSizeGb).getOrElse(0.toLong).toString

        resultMap +
          (DISK_ID -> diskId) +
          (DISK_SIZE -> diskSize) +
          (STATUS -> status)
      } else {
        val status = defaultIfEmpty(operation.getStatus, EMPTY)

        resultMap +
          (STATUS -> status)
      }
    } catch {
      case t: TimeoutException => getFailureResultsMap(TIMEOUT_EXCEPTION, t)
      case e: Throwable => getFailureResultsMap(e)
    }
  }
}