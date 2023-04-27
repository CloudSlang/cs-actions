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
package io.cloudslang.content.google.services.databases

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.services.sqladmin.model.Operation
import io.cloudslang.content.google.services.databases.operations.OperationService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration.Inf
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.language.postfixOps


object DatabaseController {
  def awaitSuccessOperation(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential,
                            projectId: String, operation: Operation, instanceId: Option[String], async: Boolean,
                            timeout: Long, pollingInterval: Long): Operation =
    if (async) {
      operation
    } else {
      Await.result(updateOperationProgress(httpTransport, jsonFactory, credential, projectId, operation, instanceId,
        pollingInterval),
        if (timeout == 0) Inf else timeout seconds)
    }

  def updateOperationProgress(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential,
                              projectId: String, operation: Operation, instanceId: Option[String],
                              pollingInterval: Long): Future[Operation] =
    Future {
      Stream.continually {
        Thread.sleep(pollingInterval)

        operation match {
          case operation: Operation => OperationService.get(httpTransport, jsonFactory, credential, projectId,
            operation.getName)
        }
      }
        .filter(_.getStatus == "DONE")
        .head
    }
}
