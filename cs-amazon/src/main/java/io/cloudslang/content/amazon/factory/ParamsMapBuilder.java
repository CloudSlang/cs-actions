package io.cloudslang.content.amazon.factory;

import io.cloudslang.content.amazon.entities.inputs.InputsWrapper;
import io.cloudslang.content.amazon.utils.InputsUtil;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;

import static io.cloudslang.content.amazon.entities.constants.Constants.Apis.EC2_API;
import static io.cloudslang.content.amazon.entities.constants.Constants.Apis.LOAD_BALANCING_API;

import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.AMPERSAND;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EQUAL;

/**
 * Created by Mihai Tusa.
 * 9/5/2016.
 */
public class ParamsMapBuilder {
    private static final String UNSUPPORTED_AWS_API = "Unsupported Amazon AWS API.";

    private ParamsMapBuilder() {
        // prevent instantiation
    }

    @Nullable
    public static Map<String, String> getParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap;
        if (isBlank(wrapper.getCommonInputs().getQueryParams())) {
            switch (wrapper.getCommonInputs().getApiService()) {
                case EC2_API:
                    return Ec2QueryParamsMapBuilder.getEc2QueryParamsMap(wrapper);
                case LOAD_BALANCING_API:
                    return LoadBalancingQueryParamsMapBuilder.getLoadBalancingQueryParamsMap(wrapper);
                default:
                    throw new RuntimeException(UNSUPPORTED_AWS_API);
            }
        } else {
            queryParamsMap = InputsUtil.getHeadersOrQueryParamsMap(new HashMap<String, String>(),
                    wrapper.getCommonInputs().getQueryParams(), AMPERSAND, EQUAL, false);
        }

        return queryParamsMap;
    }
}