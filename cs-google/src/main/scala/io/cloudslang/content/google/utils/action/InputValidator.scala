package io.cloudslang.content.google.utils.action

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
  val INVALID_BOTH_ASSIGNED = "Both of them cannot be assigned"
  val INVALID_NONE_ASSIGNED = "One of them is required."

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

  def validate[A <: Any](inputValue: A, inputName: String)(validator: (A) => Option[String]): Stream[String] =
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
