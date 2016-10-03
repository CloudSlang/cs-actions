package io.cloudslang.content.jclouds.factory.helpers;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.InputsWrapper;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 9/14/2016.
 */
public class ElasticIpHelper {
    private static final String DOMAIN = "Domain";

    public Map<String, String> getAllocateAddressQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(),
                wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(DOMAIN, wrapper.getCustomInputs().getDomain());

        return queryParamsMap;
    }

    public Map<String, String> getReleaseAddressQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(),
                wrapper.getCommonInputs().getVersion());

        InputsUtil.setOptionalMapEntry(queryParamsMap, Constants.AwsParams.ALLOCATION_ID, wrapper.getCustomInputs().getAllocationId(),
                StringUtils.isNotBlank(wrapper.getCustomInputs().getAllocationId()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, Constants.AwsParams.PUBLIC_IP, wrapper.getElasticIpInputs().getPublicIp(),
                StringUtils.isNotBlank(wrapper.getElasticIpInputs().getPublicIp()));

        return queryParamsMap;
    }
}