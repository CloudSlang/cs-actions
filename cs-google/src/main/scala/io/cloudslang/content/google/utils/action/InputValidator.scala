package io.cloudslang.content.google.utils.action

import io.cloudslang.content.constants.OtherValues
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
  val INVALID_INTEGER = "Invalid integer!"
  val INVALID_PAIRED_LISTS_LENGTH = "Paired lists must have the same length!"
  val INVALID_DISK_SIZE = "Invalid diskSize, the size has to be an integer >= 10!"

  def validateProxyPort: (String) => Stream[String] = validate(_, InputNames.PROXY_PORT) { value =>
    if (!isValidIpPort(value)) Some(INVALID_PORT) else None
  }

  def validateBoolean: (String, String) => Stream[String] = validate(_, _) { value =>
    if (!BooleanUtilities.isValid(value)) Some(INVALID_BOOLEAN) else None
  }

  def validateInteger: (String, String) => Stream[String] = validate(_, _) { value =>
    if (!NumberUtilities.isValidInt(value)) Some(INVALID_INTEGER) else None
  }

  def validateNonNegativeInteger: (String, String) => Stream[String] = validate(_, _) { value =>
    if (!NumberUtilities.isValidInt(value, 0, Int.MaxValue)) Some(INVALID_NON_NEGATIVE_INTEGER) else None
  }

  def validateDiskSize: (String, String) => Stream[String] = validate(_, _) { value =>
    if (NumberUtilities.isValidInt(value, 10, Int.MaxValue)) None else Some(INVALID_DISK_SIZE)
  }

  def validate(inputValue: String, inputName: String)(validator: (String) => Option[String]): Stream[String] =
    validator(inputValue) match {
      case None => Stream.empty
      case Some(errorValue) => Stream(s"$inputName : $errorValue")
    }

  def validatePairedLists(list1: String, list2: String, delimiter: String): String = {
    val firstLength = toList(list1, delimiter).size()
    val secondLength = toList(list2, delimiter).size()
    if (firstLength != secondLength) {
      INVALID_PAIRED_LISTS_LENGTH
    } else {
      OtherValues.EMPTY_STRING
    }
  }

  def validateRequiredExclusion(firstInputValue: Option[String], firstInputName: String, secondInputValue: Option[String], secondInputName: String): Stream[String] =
    (firstInputValue, secondInputValue) match {
      case (Some(_), Some(_)) => Stream(s"$firstInputName and $secondInputName cannot have value at the same time.")
      case (None, None) => Stream(s"One of the $firstInputName and $secondInputName is required.")
      case _ => Stream.empty

    }
}
