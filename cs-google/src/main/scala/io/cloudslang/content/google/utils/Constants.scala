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




package io.cloudslang.content.google.utils

/**
 * Created by victor on 28.02.2017.
 */
object Constants {
  val COMMA = ","
  val NEW_LINE = "\n"
  val COMMA_NEW_LINE = ",\n"
  val SQR_LEFT_BRACKET = "["
  val SQR_RIGHT_BRACKET = "]"
  val TIMEOUT_EXCEPTION = "Operation failed because the specified timeout was exceeded."
  val WINDOWS_KEYS = "windows-keys"
  val RSA_KEY = "RSA"
  val MODULUS = "modulus"
  val EXPONENT = "exponent"
  val USER_NAME = "userName"
  val EXPIRE_ON = "expireOn"
  val ERROR_MESSAGE = "errorMessage"
  val ENCRYPTED_PASSWORD = "encryptedPassword"
  val FALSE = "false"
  val TRUE = "true"

  object SQLInstancesConstant {
    final val CREATE_SQL_INSTANCE_OPERATION_NAME = "Create SQL Instance"
    final val DELETE_SQL_INSTANCE_OPERATION_NAME = "Delete SQL Instance"
    final val GET_SQL_INSTANCE_OPERATION_NAME = "Get SQL Instance"
    final val LIST_SQL_INSTANCES_OPERATION_NAME = "List SQL Instances"
    final val START_SQL_INSTANCE_OPERATION_NAME = "Start SQL Instance"
    final val UPDATE_SQL_INSTANCE_OPERATION_NAME = "Update SQL Instance"
    final val STOP_SQL_INSTANCE_OPERATION_NAME = "Stop SQL Instance"
    final val RESTART_SQL_INSTANCE_OPERATION_NAME = "Restart SQL Instance"

    final val IP_ADDRESS_TYPE_PRIVATE = "PRIVATE"
    final val IP_ADDRESS_TYPE_PRIMARY = "PRIMARY"
    final val USER_LABELS = "userLabels"
  }

  object StorageBucketConstants {
    final val UPDATE_BUCKET_OPERATION_NAME = "Update Bucket"
    final val GET_BUCKET_OPERATION_NAME = "Get Bucket"
    final val CREATE_BUCKET_OPERATION_NAME = "Create Bucket"
    final val LIST_BUCKET_OPERATION_NAME = "List Bucket"
    final val DELETE_BUCKET_OPERATION_NAME = "Delete Bucket"

    final val UNIFORM_ACCESS_CONTROL = "Uniform"
    final val FINE_GRAINED_ACCESS_CONTROL = "Fine-grained"
    final val RETENTION_POLICY = "retentionPolicy"
    final val VERSIONING = "versioning"
    final val DEFAULT_EVENT_BASED_HOLD_KEY = "defaultEventBasedHold"
    final val LABELS = "labels"
  }
}
