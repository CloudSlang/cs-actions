/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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

package io.cloudslang.content.google.services.compute.compute_engine.disks

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.services.compute.model._
import io.cloudslang.content.google.services.compute.compute_engine.{ComputeController, ComputeService}

import scala.collection.JavaConversions._

/**
  * Created by victor on 03.03.2017.
  */
object DiskService {

  def list(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, zone: String,
           filterOpt: Option[String], orderByOpt: Option[String]): List[Disk] = {
    val computeDisks = ComputeService.disksService(httpTransport, jsonFactory, credential)
    val request = computeDisks.list(project, zone)

    filterOpt.foreach { filter => request.setFilter(filter) }
    orderByOpt.foreach { orderBy => request.setOrderBy(orderBy) }

    var disks: List[Disk] = List()
    var response: DiskList = null
    do {
      response = request.execute()
      if (response.getItems != null) {
        disks ++= response.getItems
        request.setPageToken(response.getNextPageToken)
      }
    } while (response.getNextPageToken != null)

    disks
  }

  def get(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, zone: String, diskName: String): Disk =
    ComputeService.disksService(httpTransport, jsonFactory, credential)
      .get(project, zone, diskName)
      .execute()

  def insert(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, zone: String, disk: Disk, async: Boolean, timeout: Long, pollingInterval: Long): Operation = {
    val operation = ComputeService.disksService(httpTransport, jsonFactory, credential)
      .insert(project, zone, disk)
      .execute()
    ComputeController.awaitSuccessOperation(httpTransport, jsonFactory, credential, project, Some(zone), operation, async,
      timeout, pollingInterval)

  }

  def delete(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, zone: String, diskName: String, async: Boolean, timeout: Long, pollingInterval: Long): Operation = {
    val operation = ComputeService.disksService(httpTransport, jsonFactory, credential)
      .delete(project, zone, diskName)
      .execute()
    ComputeController.awaitSuccessOperation(httpTransport, jsonFactory, credential, project, Some(zone), operation, async,
      timeout, pollingInterval)
  }
}
