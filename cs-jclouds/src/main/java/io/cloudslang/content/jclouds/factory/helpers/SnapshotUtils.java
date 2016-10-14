package io.cloudslang.content.jclouds.factory.helpers;

import io.cloudslang.content.jclouds.entities.inputs.InputsWrapper;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.DESCRIPTION;
import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.SNAPSHOT_ID;
import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.VOLUME_ID;

/**
 * Created by Mihai Tusa
 * 10/3/2016.
 */
public class SnapshotUtils {
    public Map<String, String> getCreateSnapshotQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(),
                wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(VOLUME_ID, wrapper.getCustomInputs().getVolumeId());

        InputsUtil.setOptionalMapEntry(queryParamsMap, DESCRIPTION, wrapper.getVolumeInputs().getDescription(),
                StringUtils.isNotBlank(wrapper.getVolumeInputs().getDescription()));

        return queryParamsMap;
    }

    public Map<String, String> getDeleteSnapshotQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(),
                wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(SNAPSHOT_ID, wrapper.getVolumeInputs().getSnapshotId());

        return queryParamsMap;
    }
}