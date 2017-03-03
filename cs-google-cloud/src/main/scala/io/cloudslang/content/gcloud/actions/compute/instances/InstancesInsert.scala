package io.cloudslang.content.gcloud.actions.compute.instances

import java.util

import com.google.api.services.compute.ComputeScopes
import com.google.api.services.compute.model.{AttachedDisk, Disk, Instance, NetworkInterface}
import com.hp.oo.sdk.content.annotations.{Action, Output, Param, Response}
import com.hp.oo.sdk.content.plugin.ActionMetadata.{MatchType, ResponseType}
import io.cloudslang.content.constants.{OutputNames, ResponseNames, ReturnCodes}
import io.cloudslang.content.gcloud.actions.compute.utils.GetAccessToken
import io.cloudslang.content.gcloud.services.compute.disks.DiskService
import io.cloudslang.content.gcloud.services.compute.instances.InstanceService
import io.cloudslang.content.gcloud.utils.Constants.NEW_LINE
import io.cloudslang.content.gcloud.utils.action.DefaultValues.{DEFAULT_PRETTY_PRINT, DEFAULT_PROXY_PASSWORD, DEFAULT_PROXY_PORT}
import io.cloudslang.content.gcloud.utils.action.InputNames._
import io.cloudslang.content.gcloud.utils.action.InputUtils.verifyEmpty
import io.cloudslang.content.gcloud.utils.action.InputValidator.{validateBoolean, validateProxyPort}
import io.cloudslang.content.gcloud.utils.service.{GoogleAuth, HttpTransportUtils, JsonFactoryUtils}
import io.cloudslang.content.utils.BooleanUtilities.toBoolean
import io.cloudslang.content.utils.NumberUtilities.toInteger
import io.cloudslang.content.utils.OutputUtilities.{getFailureResultsMap, getSuccessResultsMap}
import org.apache.commons.lang3.StringUtils.defaultIfEmpty

import scala.collection.JavaConversions._
/**
  * Created by victor on 01.03.2017.
  */
class InstancesInsert {

  @Action(name = "Insert Instance",
    outputs = Array(
      new Output(OutputNames.RETURN_CODE),
      new Output(OutputNames.RETURN_RESULT),
      new Output(OutputNames.EXCEPTION)
    ),
    responses = Array(
      new Response(text = ResponseNames.SUCCESS, field = OutputNames.RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
      new Response(text = ResponseNames.FAILURE, field = OutputNames.RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
    )
  )
  def execute(@Param(value = PROJECT_ID, required = true) projectId: String,
              @Param(value = ZONE, required = true) zone: String,
              @Param(value = INSTANCE_NAME, required = true) instanceName: String,
              @Param(value = ACCESS_TOKEN, required = true, encrypted = true) accessToken: String,
              @Param(value = PROXY_HOST) proxyHost: String,
              @Param(value = PROXY_PORT) proxyPortInp: String,
              @Param(value = PROXY_USERNAME) proxyUsername: String,
              @Param(value = PROXY_PASSWORD, encrypted = true) proxyPasswordInp: String,
              @Param(value = PRETTY_PRINT) prettyPrintInp: String): util.Map[String, String] = {

    val proxyHostOpt = verifyEmpty(proxyHost)
    val proxyUsernameOpt = verifyEmpty(proxyUsername)
    val proxyPortStr = defaultIfEmpty(proxyPortInp, DEFAULT_PROXY_PORT)
    val proxyPassword = defaultIfEmpty(proxyPasswordInp, DEFAULT_PROXY_PASSWORD)
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

      val bootDisk = DiskService.insert(httpTransport, jsonFactory, credential, projectId, zone, new Disk()
        .setName("dummy")
        .setSizeGb(10L)
        .setType("projects/cogent-range-159508/zones/europe-west1-d/diskTypes/pd-standard")
        .setZone("projects/cogent-range-159508/zones/europe-west1-d"))

      print(bootDisk)

//      val instance = new Instance()
//        .setName("my-instance")
//          .setMachineType("projects/cogent-range-159508/zones/europe-west1-d/machineTypes/f1-micro")
//        .setDisks(List(new AttachedDisk()
//          .setBoot(true)
//          .setAutoDelete(true)
//          .setDeviceName("instance-21")
//          .setType("PERSISTENT")
//        ))
//        .setNetworkInterfaces(List(new NetworkInterface()
//          .setName("nic0")
//          .setNetwork("projects/cogent-range-159508/global/networks/default")))


//      val operation = InstanceService.insert(httpTransport, jsonFactory, credential, projectId, zone, instance)
      val resultString = if (prettyPrint) bootDisk.toPrettyString else bootDisk.toString

      getSuccessResultsMap(resultString)
    } catch {
      case e: Throwable => getFailureResultsMap(e)
    }
  }
}
