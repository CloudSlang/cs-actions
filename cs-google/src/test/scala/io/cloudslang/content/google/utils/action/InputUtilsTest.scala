/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package io.cloudslang.content.google.utils.action

import io.cloudslang.content.google.utils.action.InputUtils.verifyEmpty
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
    verifyEmpty(null) must beNone
    verifyEmpty(EMPTY) must beNone
    verifyEmpty(NON_EMPTY_STRING) mustEqual Option(NON_EMPTY_STRING)
  }

}
