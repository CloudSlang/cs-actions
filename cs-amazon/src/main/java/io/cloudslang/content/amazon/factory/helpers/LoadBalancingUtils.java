package io.cloudslang.content.amazon.factory.helpers;

import io.cloudslang.content.amazon.entities.inputs.InputsWrapper;
import io.cloudslang.content.amazon.utils.InputsUtil;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by TusaM
 * 11/10/2016.
 */
public class LoadBalancingUtils {
    public Map<String, String> getCreateLoadBalancerQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new LinkedHashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(),
                wrapper.getCommonInputs().getVersion());

        return queryParamsMap;
    }
}