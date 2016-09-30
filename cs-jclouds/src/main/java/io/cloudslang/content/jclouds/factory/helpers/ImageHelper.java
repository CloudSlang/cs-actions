package io.cloudslang.content.jclouds.factory.helpers;

import io.cloudslang.content.jclouds.entities.inputs.InputsWrapper;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.ATTRIBUTE;
import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.DESCRIPTION;
import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.IMAGE_ID;
import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.INSTANCE_ID;
import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.NAME;
import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.NO_REBOOT;
import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.OPERATION_TYPE;
import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.USER_GROUP;
import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.USER_ID;


/**
 * Created by Tirla Alin.
 * Date: 30/9/2016.
 */
public class ImageHelper {
    public Map<String, String> getDeregisterImageQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(),
                wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(IMAGE_ID, wrapper.getCustomInputs().getImageId());

        return queryParamsMap;
    }

    public Map<String, String> getCreateImageQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(),
                wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(INSTANCE_ID, wrapper.getImageInputs().getCustomInputs().getInstanceId());
        queryParamsMap.put(NAME, wrapper.getImageInputs().getImageName());
        InputsUtil.setOptionalMapEntry(queryParamsMap, DESCRIPTION, wrapper.getImageInputs().getDescription(),
                StringUtils.isNotBlank(wrapper.getImageInputs().getDescription()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, NO_REBOOT, String.valueOf(wrapper.getImageInputs().isImageNoReboot()),
                StringUtils.isNotBlank(String.valueOf(wrapper.getImageInputs().isImageNoReboot())));

        return queryParamsMap;
    }

    public Map<String, String> getDescribeImageAttributeQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(),
                wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(IMAGE_ID, wrapper.getCustomInputs().getImageId());
        queryParamsMap.put(ATTRIBUTE, wrapper.getCustomInputs().getAttribute());

        return queryParamsMap;
    }

    public Map<String, String> getModifyImageAttributeQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        Set<String> userIds = InputsUtil.getStringsSet(wrapper.getImageInputs().getUserIdsString(), wrapper.getCommonInputs().getDelimiter());
        Set<String> userGroupIds = InputsUtil.getStringsSet(wrapper.getImageInputs().getUserGroupsString(), wrapper.getCommonInputs().getDelimiter());
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(),
                wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(ATTRIBUTE, wrapper.getImageInputs().getCustomInputs().getAttribute());
        queryParamsMap.put(OPERATION_TYPE, wrapper.getImageInputs().getCustomInputs().getOperationType());
        queryParamsMap.put(IMAGE_ID, wrapper.getImageInputs().getCustomInputs().getImageId());
        putSetInQueryMap(queryParamsMap, USER_ID, userIds);
        putSetInQueryMap(queryParamsMap, USER_GROUP, userGroupIds);

        return queryParamsMap;
    }

    private void putSetInQueryMap(Map<String, String> queryParamsMap, String paramName, Set<String> set) {
        int step;
        Iterator<String> iterator = set.iterator();
        for (step = 1; iterator.hasNext(); step++) {
            String curValue = iterator.next();
            queryParamsMap.put(String.format("%s.%d", paramName, step), curValue);
        }
    }
}
