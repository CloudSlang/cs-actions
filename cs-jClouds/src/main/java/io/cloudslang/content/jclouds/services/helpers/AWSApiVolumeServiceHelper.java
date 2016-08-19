package io.cloudslang.content.jclouds.services.helpers;

import io.cloudslang.content.jclouds.entities.aws.AWSApiAction;
import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.AWSInputsWrapper;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 8/16/2016.
 */
public class AWSApiVolumeServiceHelper {
    private static final String AVAILABILITY_ZONE = "AvailabilityZone";
    private static final String ENCRYPTED = "Encrypted";
    private static final String IOPS = "Iops";
    private static final String KMS_KEY_ID = "KmsKeyId";
    private static final String SIZE = "Size";
    private static final String SNAPSHOT_ID = "SnapshotId";
    private static final String VOLUME_TYPE = "VolumeType";

    public Map<String, String> getCreateVolumeQueryParamsMap(AWSInputsWrapper inputs) {
        Map<String, String> queryParamsMap = new HashMap<>();

        queryParamsMap.put(Constants.AWSParams.ACTION, AWSApiAction.CREATE_VOLUME.getValue());
        queryParamsMap.put(Constants.AWSParams.VERSION, inputs.getVersion());
        queryParamsMap.put(AVAILABILITY_ZONE, inputs.getCustomInputs().getAvailabilityZone());

        setMapEntry(queryParamsMap, KMS_KEY_ID, inputs.getCustomInputs().getKmsKeyId());
        setMapEntry(queryParamsMap, SIZE, inputs.getVolumeInputs().getSize());
        setMapEntry(queryParamsMap, SNAPSHOT_ID, inputs.getVolumeInputs().getSnapshotId());

        if (inputs.getVolumeInputs().isEncrypted()) {
            queryParamsMap.put(ENCRYPTED, Constants.Miscellaneous.SET_FLAG);
        }

        if (!Constants.Miscellaneous.NOT_RELEVANT.equals(inputs.getVolumeInputs().getIops())) {
            queryParamsMap.put(IOPS, inputs.getVolumeInputs().getIops());
        }

        if (Constants.Miscellaneous.NOT_RELEVANT.equals(inputs.getCustomInputs().getVolumeType())) {
            queryParamsMap.put(VOLUME_TYPE, Constants.Miscellaneous.STANDARD);
        } else {
            queryParamsMap.put(VOLUME_TYPE, inputs.getCustomInputs().getVolumeType());
        }

        return queryParamsMap;
    }

    private void setMapEntry(Map<String, String> inputMap, String key, String value) {
        if (StringUtils.isNotBlank(value)) {
            inputMap.put(key, value);
        }
    }
}