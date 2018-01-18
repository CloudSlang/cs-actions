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

package io.cloudslang.content.google.services.compute.compute_engine

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.services.compute.model.Operation
import io.cloudslang.content.google.services.compute.compute_engine.operations.{GlobalOperationService, ZoneOperationService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration.Inf
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.language.postfixOps

/**
  * Created by marisca on 4/9/2017.
  */
object ComputeController {
  def awaitSuccessOperation(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, projectId: String,
                            zone: Option[String], operation: Operation, async: Boolean, timeout: Long, pollingInterval: Long): Operation =
    if (async) {
      operation
    } else {
      Await.result(updateOperationProgress(httpTransport, jsonFactory, credential, projectId, zone, operation, pollingInterval), if (timeout == 0) Inf else timeout seconds)
    }

  def updateOperationProgress(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, projectId: String,
                              zone: Option[String], operation: Operation, pollingInterval: Long): Future[Operation] =
    Future {
      Stream.continually {
        Thread.sleep(pollingInterval)

        zone match {
          case Some(zoneStr) => ZoneOperationService.get(httpTransport, jsonFactory, credential, projectId, zoneStr, operation.getName)
          case None => GlobalOperationService.get(httpTransport, jsonFactory, credential, projectId, operation.getName)
        }
      }
        .filter(_.getProgress == 100)
        .head
    }
}
