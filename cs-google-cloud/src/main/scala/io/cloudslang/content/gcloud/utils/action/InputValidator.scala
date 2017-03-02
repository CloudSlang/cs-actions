package io.cloudslang.content.gcloud.utils.action

import io.cloudslang.content.constants.OtherValues
import io.cloudslang.content.utils.OtherUtilities.isValidIpPort
import io.cloudslang.content.utils.{BooleanUtilities, NumberUtilities}
import org.apache.commons.lang3.StringUtils

/**
  * Created by sandorr 
  * 2/28/2017.
  */
object InputValidator {

  val INVALID_PORT = "Invalid port value!"
  val INVALID_BOOLEAN = "Invalid value for prettyPrint input!"
  val INVALID_NON_NEGATIVE_INTEGER = "Invalid non-negative integer!"
  val INVALID_PAIRED_LISTS_LENGTH = "Paired lists must have the same length!"

  def validateProxyPort: (String) => Stream[String] = validate(_, InputNames.PROXY_PORT) { value =>
    if (!isValidIpPort(value)) Some(INVALID_PORT) else None
  }

  def validate(inputValue: String, inputName: String)(validator: (String) => (Option[String])): Stream[String] =
    validator(inputValue) match {
      case None => Stream.empty
      case Some(errorValue) => Stream(s"$inputName : $errorValue")
    }

  def validateBoolean: (String, String) => Stream[String] = validate(_, _) { value =>
    if (!BooleanUtilities.isValid(value)) Some(INVALID_BOOLEAN) else None
  }

  def validateNonNegativeInteger: (String, String) => Stream[String] = validate(_, _) { value =>
    if (!NumberUtilities.isValidInt(value, 0, Int.MaxValue)) Some(INVALID_NON_NEGATIVE_INTEGER) else None
  }

  def validatePairedLists(list1: String, list2: String, delimiter: String): String = {
    val firstLength = StringUtils.split(list1, delimiter).length
    val secondLength = StringUtils.split(list2, delimiter).length
    if (firstLength != secondLength) {
      INVALID_PAIRED_LISTS_LENGTH
    } else {
      OtherValues.EMPTY_STRING
    }
  }
}
