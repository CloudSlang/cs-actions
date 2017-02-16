/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.amazon.factory.helpers;

import io.cloudslang.content.amazon.entities.inputs.InputsWrapper;
import io.cloudslang.content.amazon.entities.validators.VolumesFilterValidator;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.*;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.*;
import static io.cloudslang.content.amazon.entities.constants.Constants.Values.ONE;
import static io.cloudslang.content.amazon.factory.helpers.FilterUtils.getFiltersQueryMap;
import static io.cloudslang.content.amazon.utils.InputsUtil.*;
import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.replace;

/**
 * Created by Mihai Tusa.
 * 9/9/2016.
 */
public class VolumeUtils {
    private static final String ATTACHMENT_STATUS_FILTER = "attachment.status";
    private static final String STATUS_FILTER = "status";
    private static final String VOLUME_TYPE_FILTER = "volume-type";
    private static final String NOT_RELEVANT_KEY_STRING = "-1";
    private static final String AVAILABILITY_ZONE = "AvailabilityZone";
    private static final String DEVICE = "Device";
    private static final String KMS_KEY_ID = "KmsKeyId";
    private static final String SIZE = "Size";
    private static final String NEXT_TOKEN = "NextToken";
    private static final String MAX_RESULTS = "MaxResults";

    public Map<String, String> getAttachVolumeQueryParamsMap(InputsWrapper wrapper) {
        return getAttachOrDetachVolumeCommonQueryParamsMap(wrapper);
    }

    public Map<String, String> getCreateVolumeQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(AVAILABILITY_ZONE, wrapper.getCustomInputs().getAvailabilityZone());

        String volumeType = NOT_RELEVANT.equals(wrapper.getCustomInputs().getVolumeType()) ? STANDARD : wrapper.getCustomInputs().getVolumeType();
        queryParamsMap.put(VOLUME_TYPE, volumeType);

        setOptionalMapEntry(queryParamsMap, KMS_KEY_ID, wrapper.getCustomInputs().getKmsKeyId(), isNotBlank(wrapper.getCustomInputs().getKmsKeyId()));
        setOptionalMapEntry(queryParamsMap, SIZE, wrapper.getVolumeInputs().getSize(), isNotBlank(wrapper.getVolumeInputs().getSize()));
        setOptionalMapEntry(queryParamsMap, SNAPSHOT_ID, wrapper.getVolumeInputs().getSnapshotId(), isNotBlank(wrapper.getVolumeInputs().getSnapshotId()));
        setOptionalMapEntry(queryParamsMap, ENCRYPTED, String.valueOf(ONE), wrapper.getVolumeInputs().isEncrypted());
        setOptionalMapEntry(queryParamsMap, IOPS, wrapper.getVolumeInputs().getIops(), !NOT_RELEVANT.equals(wrapper.getVolumeInputs().getIops()));

        return queryParamsMap;
    }

    public Map<String, String> getDetachVolumeQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = getAttachOrDetachVolumeCommonQueryParamsMap(wrapper);
        setOptionalMapEntry(queryParamsMap, FORCE, String.valueOf(wrapper.getVolumeInputs().isForce()), wrapper.getVolumeInputs().isForce());

        return queryParamsMap;
    }

    public Map<String, String> getDeleteVolumeQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(VOLUME_ID, wrapper.getCustomInputs().getVolumeId());

        return queryParamsMap;
    }

    public Map<String, String> getDescribeVolumesQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());

        setOptionalMapEntry(queryParamsMap, MAX_RESULTS, wrapper.getVolumeInputs().getMaxResults(),
                !NOT_RELEVANT.equalsIgnoreCase(wrapper.getVolumeInputs().getMaxResults()));
        setOptionalMapEntry(queryParamsMap, NEXT_TOKEN, wrapper.getVolumeInputs().getNextToken(),
                isNotBlank(wrapper.getVolumeInputs().getNextToken()));

        VolumesFilterValidator volumesFilterValidator = new VolumesFilterValidator();

        final Map<String, String> filterQueryMap = getFiltersQueryMap(wrapper.getFilterInputs(), volumesFilterValidator);
        queryParamsMap.putAll(filterQueryMap);

        return queryParamsMap;
    }


    private Map<String, String> getAttachOrDetachVolumeCommonQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(DEVICE, wrapper.getVolumeInputs().getDeviceName());
        queryParamsMap.put(INSTANCE_ID, wrapper.getCustomInputs().getInstanceId());
        queryParamsMap.put(VOLUME_ID, wrapper.getCustomInputs().getVolumeId());

        return queryParamsMap;
    }

}
