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
  val TEST_NAME2 = "inputName2"

  @Test
  def validateTest(): Unit = {
    InputValidator.validate("a", TEST_NAME)(value => Some(value)).mkString(NEW_LINE) mustEqual s"$TEST_NAME : a"
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
    validateBoolean("true", TEST_NAME).mkString(NEW_LINE) mustEqual EMPTY
    validateBoolean("false", TEST_NAME).mkString(NEW_LINE) mustEqual EMPTY
  }

  @Test
  def validateNonNegativeIntegerTest(): Unit = {
    validateNonNegativeInteger("", TEST_NAME).mkString(NEW_LINE) mustEqual s"$TEST_NAME : $INVALID_NON_NEGATIVE_INTEGER"
    validateNonNegativeInteger("not_number", TEST_NAME).mkString(NEW_LINE) mustEqual s"$TEST_NAME : $INVALID_NON_NEGATIVE_INTEGER"
    validateNonNegativeInteger("-1", TEST_NAME).mkString(NEW_LINE) mustEqual s"$TEST_NAME : $INVALID_NON_NEGATIVE_INTEGER"
    validateNonNegativeInteger("0", TEST_NAME).mkString(NEW_LINE) mustEqual EMPTY
    validateNonNegativeInteger("10", TEST_NAME).mkString(NEW_LINE) mustEqual EMPTY
  }

  @Test
  def validateIntegerTest(): Unit = {
    validateInteger("", TEST_NAME).mkString(NEW_LINE) mustEqual s"$TEST_NAME : $INVALID_INTEGER"
    validateInteger("123", TEST_NAME).mkString(NEW_LINE) mustEqual EMPTY
    validateInteger("-123", TEST_NAME).mkString(NEW_LINE) mustEqual EMPTY
    validateInteger("ana", TEST_NAME).mkString(NEW_LINE) mustEqual s"$TEST_NAME : $INVALID_INTEGER"
  }

  @Test
  def validateDiskSizeTest(): Unit = {
    validateDiskSize("1", TEST_NAME).mkString(NEW_LINE) mustEqual s"$TEST_NAME : $INVALID_DISK_SIZE"
    validateDiskSize("123", TEST_NAME).mkString(NEW_LINE) mustEqual EMPTY
    validateDiskSize("10", TEST_NAME).mkString(NEW_LINE) mustEqual EMPTY
    validateDiskSize("-1", TEST_NAME).mkString(NEW_LINE) mustEqual s"$TEST_NAME : $INVALID_DISK_SIZE"
    validateDiskSize("ana", TEST_NAME).mkString(NEW_LINE) mustEqual s"$TEST_NAME : $INVALID_DISK_SIZE"
  }

  @Test
  def validatePairedListsTest(): Unit = {
    validatePairedLists("1", "1", ",", TEST_NAME, TEST_NAME2).mkString(NEW_LINE) mustEqual EMPTY
    validatePairedLists("1", "1,2", ",", TEST_NAME, TEST_NAME2).mkString(NEW_LINE) mustEqual s"$TEST_NAME, $TEST_NAME2 : $INVALID_PAIRED_LISTS_LENGTH"
    validatePairedLists("1,2", "1", ",", TEST_NAME, TEST_NAME2).mkString(NEW_LINE) mustEqual s"$TEST_NAME, $TEST_NAME2 : $INVALID_PAIRED_LISTS_LENGTH"
    validatePairedLists("", "1,2,3", ",", TEST_NAME, TEST_NAME2).mkString(NEW_LINE) mustEqual s"$TEST_NAME, $TEST_NAME2 : $INVALID_PAIRED_LISTS_LENGTH"
    validatePairedLists("1,2,3", "1,2,3", ",", TEST_NAME, TEST_NAME2).mkString(NEW_LINE) mustEqual EMPTY
  }

  @Test
  def validateRequiredExclusionTest(): Unit = {
    validateRequiredExclusion(Some("1"), Some("1"), TEST_NAME, TEST_NAME2).mkString(NEW_LINE) mustEqual s"$TEST_NAME, $TEST_NAME2 : $INVALID_BOTH_ASSIGNED"
    validateRequiredExclusion(Some("1"), None,  TEST_NAME, TEST_NAME2).mkString(NEW_LINE) mustEqual EMPTY
    validateRequiredExclusion(None, Some("1"), TEST_NAME, TEST_NAME2).mkString(NEW_LINE) mustEqual EMPTY
    validateRequiredExclusion(None, None,  TEST_NAME, TEST_NAME2).mkString(NEW_LINE) mustEqual s"$TEST_NAME, $TEST_NAME2 : $INVALID_NONE_ASSIGNED"
  }


}
