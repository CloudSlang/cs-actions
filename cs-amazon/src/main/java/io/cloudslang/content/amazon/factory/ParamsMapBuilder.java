/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



package io.cloudslang.content.amazon.factory;

import io.cloudslang.content.amazon.entities.inputs.InputsWrapper;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;

import static io.cloudslang.content.amazon.utils.InputsUtil.getHeadersOrQueryParamsMap;

import static io.cloudslang.content.amazon.entities.constants.Constants.Apis.EC2_API;
import static io.cloudslang.content.amazon.entities.constants.Constants.Apis.LOAD_BALANCING_API;
import static io.cloudslang.content.amazon.entities.constants.Constants.Apis.S3_API;

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

    public static Map<String, String> getParamsMap(InputsWrapper wrapper) throws Exception {
        Map<String, String> queryParamsMap;
        if (isBlank(wrapper.getCommonInputs().getQueryParams())) {
            switch (wrapper.getCommonInputs().getApiService()) {
                case EC2_API:
                    return Ec2QueryParamsMapBuilder.getEc2QueryParamsMap(wrapper);
                case LOAD_BALANCING_API:
                    return LoadBalancingQueryParamsMapBuilder.getLoadBalancingQueryParamsMap(wrapper);
                case S3_API:
                    return S3QueryParamsMapBuilder.getS3QueryParamsMap(wrapper);
                default:
                    throw new RuntimeException(UNSUPPORTED_AWS_API);
            }
        } else {
            queryParamsMap = getHeadersOrQueryParamsMap(new HashMap<String, String>(), wrapper.getCommonInputs().getQueryParams(), AMPERSAND, EQUAL, false);
        }

        return queryParamsMap;
    }
}
