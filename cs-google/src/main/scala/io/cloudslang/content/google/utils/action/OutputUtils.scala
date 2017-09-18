/*
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package io.cloudslang.content.google.utils.action

import com.google.api.client.json.GenericJson

/**
  * Created by marisca on 4/9/2017.
  */
object OutputUtils {
  def toPretty(prettyPrint: Boolean, genericJson: GenericJson): String =
    if (prettyPrint) genericJson.toPrettyString else genericJson.toString
}
