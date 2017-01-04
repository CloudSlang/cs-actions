/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.amazon.factory.helpers;

import io.cloudslang.content.amazon.entities.inputs.InputsWrapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by TusaM
 * 12/23/2016.
 */
public class StorageUtils {
    private static final String LIST_TYPE_KEY = "list-type";
    private static final String LIST_TYPE_VALUE = "2";

    public Map<String, String> retrieveGetBucketQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        queryParamsMap.put(LIST_TYPE_KEY, LIST_TYPE_VALUE);

        return queryParamsMap;
    }
}