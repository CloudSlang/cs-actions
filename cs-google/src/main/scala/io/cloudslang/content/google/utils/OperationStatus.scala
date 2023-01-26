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



package io.cloudslang.content.google.utils

import com.google.api.services.compute.model.Operation

object OperationStatus {
  def apply(operation: Operation): OperationStatus =
    if (operation.getError == null) SuccessOperation(operation) else ErrorOperation(operation.getError.toPrettyString)
}

object SQLOperationStatus {
  def apply(sqloperation: com.google.api.services.sqladmin.model.Operation): SQLOperationStatus =
    if (sqloperation.getError == null) SQLSuccessOperation(sqloperation) else SQLErrorOperation(sqloperation.getError.toPrettyString)
}

sealed abstract class OperationStatus extends Serializable

case class SuccessOperation(operation: Operation) extends OperationStatus

sealed abstract class SQLOperationStatus extends Serializable

case class SQLSuccessOperation(sqloperation: com.google.api.services.sqladmin.model.Operation) extends SQLOperationStatus

case class ErrorOperation(error: String) extends OperationStatus

case class SQLErrorOperation(error: String) extends SQLOperationStatus
