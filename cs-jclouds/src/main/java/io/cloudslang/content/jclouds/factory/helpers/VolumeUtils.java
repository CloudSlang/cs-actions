package io.cloudslang.content.jclouds.factory.helpers;

import io.cloudslang.content.jclouds.entities.inputs.InputsWrapper;
import io.cloudslang.content.jclouds.utils.InputsUtil;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.ENCRYPTED;
import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.FORCE;
import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.INSTANCE_ID;
import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.IOPS;
import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.SNAPSHOT_ID;
import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.STANDARD;
import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.VOLUME_TYPE;
import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.VOLUME_ID;

import static io.cloudslang.content.jclouds.entities.constants.Constants.Miscellaneous.NOT_RELEVANT;

import static io.cloudslang.content.jclouds.entities.constants.Constants.Values.ONE;

/**
 * Created by Mihai Tusa.
 * 9/9/2016.
 */
public class VolumeUtils {
    private static final String AVAILABILITY_ZONE = "AvailabilityZone";
    private static final String DEVICE = "Device";
    private static final String KMS_KEY_ID = "KmsKeyId";
    private static final String SIZE = "Size";

    public Map<String, String> getAttachVolumeQueryParamsMap(InputsWrapper wrapper) {
        return getAttachDetachVolumeCommonQueryParamsMap(wrapper);
    }

    public Map<String, String> getCreateVolumeQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(),
                wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(AVAILABILITY_ZONE, wrapper.getCustomInputs().getAvailabilityZone());

        String volumeType = NOT_RELEVANT.equals(wrapper.getCustomInputs().getVolumeType()) ? STANDARD :
                wrapper.getCustomInputs().getVolumeType();
        queryParamsMap.put(VOLUME_TYPE, volumeType);

        InputsUtil.setOptionalMapEntry(queryParamsMap, KMS_KEY_ID, wrapper.getCustomInputs().getKmsKeyId(),
                isNotBlank(wrapper.getCustomInputs().getKmsKeyId()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, SIZE, wrapper.getVolumeInputs().getSize(),
                isNotBlank(wrapper.getVolumeInputs().getSize()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, SNAPSHOT_ID, wrapper.getVolumeInputs().getSnapshotId(),
                isNotBlank(wrapper.getVolumeInputs().getSnapshotId()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, ENCRYPTED, String.valueOf(ONE), wrapper.getVolumeInputs().isEncrypted());
        InputsUtil.setOptionalMapEntry(queryParamsMap, IOPS, wrapper.getVolumeInputs().getIops(),
                !NOT_RELEVANT.equals(wrapper.getVolumeInputs().getIops()));

        return queryParamsMap;
    }

    public Map<String, String> getDetachVolumeQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = getAttachDetachVolumeCommonQueryParamsMap(wrapper);
        InputsUtil.setOptionalMapEntry(queryParamsMap, FORCE, String.valueOf(wrapper.getVolumeInputs().isForce()),
                wrapper.getVolumeInputs().isForce());

        return queryParamsMap;
    }

    public Map<String, String> getDeleteVolumeQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(VOLUME_ID, wrapper.getCustomInputs().getVolumeId());

        return queryParamsMap;
    }

    private Map<String, String> getAttachDetachVolumeCommonQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(DEVICE, wrapper.getVolumeInputs().getDeviceName());
        queryParamsMap.put(INSTANCE_ID, wrapper.getCustomInputs().getInstanceId());
        queryParamsMap.put(VOLUME_ID, wrapper.getCustomInputs().getVolumeId());

        return queryParamsMap;
    }
}