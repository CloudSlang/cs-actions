/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package io.cloudslang.content.google.utils.service

import com.google.api.client.json.jackson2.JacksonFactory

/**
  * Created by victor on 2/25/17.
  */
object JsonFactoryUtils {
  def getDefaultJacksonFactory: JacksonFactory = JacksonFactory.getDefaultInstance
}
