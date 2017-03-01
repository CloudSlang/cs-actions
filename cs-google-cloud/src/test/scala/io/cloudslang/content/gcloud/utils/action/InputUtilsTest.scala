package io.cloudslang.content.gcloud.utils.action

import io.cloudslang.content.gcloud.utils.action.InputUtils.verifyEmpty
import org.apache.commons.lang3.StringUtils.EMPTY
import org.junit.Test
import org.specs2.matcher.JUnitMustMatchers

/**
  * Created by victor on 2/25/17.
  */
class InputUtilsTest extends JUnitMustMatchers {

  val NON_EMPTY_STRING = "a"

  @Test
  def verifyEmptyTest(): Unit = {
    verifyEmpty(null) mustEqual None
    verifyEmpty(EMPTY) mustEqual None
    verifyEmpty(NON_EMPTY_STRING) mustEqual Option(NON_EMPTY_STRING)
  }

}
