/*
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package io.cloudslang.content.google.utils

import io.cloudslang.content.google.utils.action.InputNames.{SYNC_TIME, USERNAME}

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
  val SYNC_TIME_EXCEPTION = "The password could not be reset. Please check the 'username' and 'syncTime' inputs."
}
