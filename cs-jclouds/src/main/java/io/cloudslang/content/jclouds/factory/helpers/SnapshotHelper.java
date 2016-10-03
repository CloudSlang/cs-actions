package io.cloudslang.content.jclouds.factory.helpers;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.InputsWrapper;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mihai Tusa
 * 10/3/2016.
 */
public class SnapshotHelper {
    public Map<String, String> getCreateSnapshotQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(),
                wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(Constants.AwsParams.VOLUME_ID, wrapper.getCustomInputs().getVolumeId());

        InputsUtil.setOptionalMapEntry(queryParamsMap, Constants.AwsParams.DESCRIPTION, wrapper.getVolumeInputs().getDescription(),
                StringUtils.isNotBlank(wrapper.getVolumeInputs().getDescription()));

        return queryParamsMap;
    }

    public Map<String, String> getDeleteSnapshotQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(),
                wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(Constants.AwsParams.SNAPSHOT_ID, wrapper.getVolumeInputs().getSnapshotId());

        return queryParamsMap;
    }
}