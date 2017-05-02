package io.cloudslang.content.google.utils.action

import io.cloudslang.content.google.utils.Constants.NEW_LINE
import io.cloudslang.content.google.utils.action.InputNames.PROXY_PORT
import io.cloudslang.content.google.utils.action.InputValidator._
import org.apache.commons.lang3.StringUtils.EMPTY
import org.junit.Test
import org.specs2.matcher.JUnitMustMatchers

/**
  * Created by victor on 2/25/17.
  */
class InputValidatorTest extends JUnitMustMatchers {

  val TEST_NAME = "inputName"
  val TEST_VALUE = "a"

  @Test
  def validateTest(): Unit = {
    InputValidator.validate("a", TEST_NAME)(value => Some(value)).mkString(NEW_LINE) mustEqual s"$TEST_NAME : $TEST_VALUE"
    InputValidator.validate("a", TEST_NAME)(_ => None).mkString(NEW_LINE) mustEqual EMPTY
  }

  @Test
  def validateProxyPortTest(): Unit = {
    validateProxyPort("").mkString(NEW_LINE) mustEqual s"$PROXY_PORT : $INVALID_PORT"
    validateProxyPort("10").mkString(NEW_LINE) mustEqual EMPTY
    validateProxyPort("-10").mkString(NEW_LINE) mustEqual s"$PROXY_PORT : $INVALID_PORT"
  }

  @Test
  def validateBooleanTest(): Unit = {
    validateBoolean("", TEST_NAME).mkString(NEW_LINE) mustEqual s"$TEST_NAME : $INVALID_BOOLEAN"
    validateBoolean("not_boolean", TEST_NAME).mkString(NEW_LINE) mustEqual s"$TEST_NAME : $INVALID_BOOLEAN"
    validateBoolean("true",TEST_NAME).mkString(NEW_LINE) mustEqual EMPTY
    validateBoolean("false",TEST_NAME).mkString(NEW_LINE) mustEqual EMPTY
  }

  @Test
  def validateNonNegativeIntegerTest(): Unit = {
    validateNonNegativeInteger("", TEST_NAME).mkString(NEW_LINE) mustEqual s"$TEST_NAME : $INVALID_NON_NEGATIVE_INTEGER"
    validateNonNegativeInteger("not_number", TEST_NAME).mkString(NEW_LINE) mustEqual s"$TEST_NAME : $INVALID_NON_NEGATIVE_INTEGER"
    validateNonNegativeInteger("-1", TEST_NAME).mkString(NEW_LINE) mustEqual s"$TEST_NAME : $INVALID_NON_NEGATIVE_INTEGER"
    validateNonNegativeInteger("0",TEST_NAME).mkString(NEW_LINE) mustEqual EMPTY
    validateNonNegativeInteger("10",TEST_NAME).mkString(NEW_LINE) mustEqual EMPTY
  }

}
