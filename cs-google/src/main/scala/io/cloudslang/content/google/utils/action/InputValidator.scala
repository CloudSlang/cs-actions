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





package io.cloudslang.content.google.utils.action

import io.cloudslang.content.google.utils.action.InputValidator.INVALID_BUCKET_NAME
import io.cloudslang.content.utils.CollectionUtilities.toList
import io.cloudslang.content.utils.OtherUtilities.isValidIpPort
import io.cloudslang.content.utils.{BooleanUtilities, NumberUtilities}

/**
 * Created by sandorr
 * 2/28/2017.
 */
object InputValidator {

  val INVALID_PORT = "Invalid port value!"
  val INVALID_BOOLEAN = "Invalid boolean value!"
  val INVALID_NON_NEGATIVE_INTEGER = "Invalid non-negative integer!"
  val INVALID_NON_NEGATIVE_LONG = "Invalid non-negative long!"
  val INVALID_NON_NEGATIVE_DOUBLE = "Invalid non-negative double!"
  val INVALID_INTEGER = "Invalid integer!"
  val INVALID_LONG = "Invalid long integer!"
  val INVALID_DOUBLE = "Invalid double!"
  val INVALID_PAIRED_LISTS_LENGTH = "Paired lists must have the same length!"
  val INVALID_DISK_SIZE = "Invalid diskSize, the size has to be an integer >= 10!"
  val INVALID_BOTH_ASSIGNED = "Both of them cannot be assigned"
  val INVALID_NONE_ASSIGNED = "One of them is required."
  val INVALID_INSATNCE_ID = "Invalid instanceId use lowercase letters, numbers,or hyphens. Start with a letter"
  val INVALID_BUCKET_NAME = "Invalid bucket name. Use a valid bucket name"

  def validateProxyPort: (String) => Stream[String] = validate(_, InputNames.PROXY_PORT) { value =>
    if (!isValidIpPort(value)) Some(INVALID_PORT) else None
  }

  val instanceidPattern = "(^[a-z]$|^[a-z][a-z0-9-]*[a-z0-9]$)".r
  val bucketNamePattern = "(^[a-z0-9]$|^[a-z0-9._-]*[a-z0-9]$)".r
  val ipAddressPattern = "(^\\d{1,3}[.]\\d{1,3}[.]\\d{1,3}[.]\\d{1,3}$)".r

  def validateinstanceId: (String) => Stream[String] = validate(_, InputNames.CreateSQLDatabaseInstanceInputs.INSTANCE_ID) { value =>
    if (instanceidPattern.pattern.matcher(value).matches()) None else Some(INVALID_INSATNCE_ID)
  }

  def validateBucketName: (String) => Stream[String] = validate(_, InputNames.StorageBucketInputs.BUCKET_NAME) { value =>
    if (bucketNamePattern.pattern.matcher(value).matches())
      None
    else
      Some(INVALID_BUCKET_NAME)
    if (value.contains(".")==false && (value.length >= 3) && (value.length <= 63)) None
    else if (value.contains(".")==true && (value.length >= 3) && (value.length <= 222)) None else Some(INVALID_BUCKET_NAME)
    if (value.startsWith("goog")==true) Some(INVALID_BUCKET_NAME) else None
    if (ipAddressPattern.pattern.matcher(value).matches()) Some (INVALID_BUCKET_NAME) else None
    if(value.contains(".")==true && value.indexOf(".")>=63) Some (INVALID_BUCKET_NAME) else None
  }

  def validateBoolean: (String, String) => Stream[String] = validate(_, _) { value =>
    if (!BooleanUtilities.isValid(value)) Some(INVALID_BOOLEAN) else None
  }

  def validateInteger: (String, String) => Stream[String] = validate(_, _) { value =>
    if (!NumberUtilities.isValidInt(value)) Some(INVALID_INTEGER) else None
  }

  def validateLong: (String, String) => Stream[String] = validate(_, _) { value =>
    if (!NumberUtilities.isValidLong(value)) Some(INVALID_LONG) else None
  }

  def validateDouble: (String, String) => Stream[String] = validate(_, _) { value =>
    if (!NumberUtilities.isValidDouble(value)) Some(INVALID_DOUBLE) else None
  }

  def validateNonNegativeInteger: (String, String) => Stream[String] = validate(_, _) { value =>
    if (!NumberUtilities.isValidInt(value, 0, Int.MaxValue)) Some(INVALID_NON_NEGATIVE_INTEGER) else None
  }

  def validateNonNegativeLong: (String, String) => Stream[String] = validate(_, _) { value =>
    if (!NumberUtilities.isValidLong(value, 0, Long.MaxValue)) Some(INVALID_NON_NEGATIVE_LONG) else None
  }

  def validateNonNegativeDouble: (String, String) => Stream[String] = validate(_, _) { value =>
    if (!NumberUtilities.isValidDouble(value, 0, Double.MaxValue)) Some(INVALID_NON_NEGATIVE_DOUBLE) else None
  }

  def validate[A](inputValue: A, inputName: String)(validator: (A) => Option[String]): Stream[String] =
    validator(inputValue) match {
      case None => Stream.empty
      case Some(errorValue) => Stream(s"$inputName : $errorValue")
    }

  def validateDiskSize: (String, String) => Stream[String] = validate(_, _) { value =>
    if (NumberUtilities.isValidInt(value, 10, Int.MaxValue)) None else Some(INVALID_DISK_SIZE)
  }

  def validatePairedLists(list1: String, list2: String, delimiter: String, inputName1: String, inputName2: String): Stream[String] = {
    val firstLength = toList(list1, delimiter).size()
    val secondLength = toList(list2, delimiter).size()
    validate((firstLength, secondLength), s"$inputName1, $inputName2") { sizePair =>
      if (sizePair._1 != sizePair._2) Some(INVALID_PAIRED_LISTS_LENGTH) else None
    }
  }

  def validateRequiredExclusion(firstInputValue: Option[String], secondInputValue: Option[String], firstInputName: String, secondInputName: String): Stream[String] =
    validate((firstInputValue, secondInputValue), s"$firstInputName, $secondInputName") {
      case (Some(_), Some(_)) => Option(INVALID_BOTH_ASSIGNED)
      case (None, None) => Option(INVALID_NONE_ASSIGNED)
      case _ => None
    }
}
