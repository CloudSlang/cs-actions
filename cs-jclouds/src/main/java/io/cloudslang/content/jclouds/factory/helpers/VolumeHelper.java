package io.cloudslang.content.jclouds.factory.helpers;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.InputsWrapper;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 9/9/2016.
 */
public class VolumeHelper {
    private static final String AVAILABILITY_ZONE = "AvailabilityZone";
    private static final String ENCRYPTED = "Encrypted";
    private static final String IOPS = "Iops";
    private static final String KMS_KEY_ID = "KmsKeyId";
    private static final String SIZE = "Size";
    private static final String SNAPSHOT_ID = "SnapshotId";
    private static final String STANDARD = "standard";
    private static final String VOLUME_TYPE = "VolumeType";

    public Map<String, String> getCreateVolumeQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        queryParamsMap.put(Constants.AwsParams.ACTION, wrapper.getAction());
        queryParamsMap.put(Constants.AwsParams.VERSION, wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(AVAILABILITY_ZONE, wrapper.getCustomInputs().getAvailabilityZone());

        String volumeType = Constants.Miscellaneous.NOT_RELEVANT.equals(wrapper.getCustomInputs().getVolumeType()) ?
                STANDARD : wrapper.getCustomInputs().getVolumeType();
        queryParamsMap.put(VOLUME_TYPE, volumeType);

        InputsUtil.setOptionalMapEntry(queryParamsMap, KMS_KEY_ID, wrapper.getCustomInputs().getKmsKeyId(),
                StringUtils.isNotBlank(wrapper.getCustomInputs().getKmsKeyId()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, SIZE, wrapper.getVolumeInputs().getSize(),
                StringUtils.isNotBlank(wrapper.getVolumeInputs().getSize()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, SNAPSHOT_ID, wrapper.getVolumeInputs().getSnapshotId(),
                StringUtils.isNotBlank(wrapper.getVolumeInputs().getSnapshotId()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, ENCRYPTED, Constants.Miscellaneous.SET_FLAG,
                wrapper.getVolumeInputs().isEncrypted());
        InputsUtil.setOptionalMapEntry(queryParamsMap, IOPS, wrapper.getVolumeInputs().getIops(),
                !Constants.Miscellaneous.NOT_RELEVANT.equals(wrapper.getVolumeInputs().getIops()));

        return queryParamsMap;
    }
}