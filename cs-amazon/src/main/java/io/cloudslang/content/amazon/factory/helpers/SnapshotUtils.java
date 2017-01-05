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

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import static io.cloudslang.content.amazon.utils.InputsUtil.setCommonQueryParamsMap;
import static io.cloudslang.content.amazon.utils.InputsUtil.setOptionalMapEntry;

import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.DESCRIPTION;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.SNAPSHOT_ID;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.VOLUME_ID;

/**
 * Created by Mihai Tusa
 * 10/3/2016.
 */
public class SnapshotUtils {
    public Map<String, String> getCreateSnapshotQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(VOLUME_ID, wrapper.getCustomInputs().getVolumeId());
        setOptionalMapEntry(queryParamsMap, DESCRIPTION, wrapper.getVolumeInputs().getDescription(),
                isNotBlank(wrapper.getVolumeInputs().getDescription()));

        return queryParamsMap;
    }

    public Map<String, String> getDeleteSnapshotQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(SNAPSHOT_ID, wrapper.getVolumeInputs().getSnapshotId());

        return queryParamsMap;
    }
}