package io.cloudslang.content.amazon.factory;

import io.cloudslang.content.amazon.entities.inputs.InputsWrapper;
import io.cloudslang.content.amazon.factory.helpers.LoadBalancingUtils;

import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Constants.LoadBalancingQueryApiActions.CREATE_LOAD_BALANCER;
import static io.cloudslang.content.amazon.entities.constants.Constants.ErrorMessages.UNSUPPORTED_QUERY_API;

/**
 * Created by TusaM
 * 11/10/2016.
 */
class LoadBalancingQueryParamsMapBuilder {
    private LoadBalancingQueryParamsMapBuilder() {
        // prevent instantiation
    }

    static Map<String, String> getLoadBalancingQueryParamsMap(InputsWrapper wrapper) {
        switch (wrapper.getCommonInputs().getAction()) {
            case CREATE_LOAD_BALANCER:
                return new LoadBalancingUtils().getCreateLoadBalancerQueryParamsMap(wrapper);
            default:
                throw new RuntimeException(UNSUPPORTED_QUERY_API);
        }
    }
}