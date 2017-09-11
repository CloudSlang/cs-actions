package io.cloudslang.content.google.utils.action

import org.apache.commons.lang3.StringUtils.isEmpty

/**
  * Created by victor on 28.02.2017.
  */
object InputUtils {
  def verifyEmpty(value: String): Option[String] = if (isEmpty(value)) None else Some(value)

  def convertSecondsToMilli(value: Double): Long = (value * 1000 ceil) toLong
}
