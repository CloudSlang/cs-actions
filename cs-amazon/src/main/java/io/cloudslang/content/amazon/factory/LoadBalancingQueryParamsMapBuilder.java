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
import io.cloudslang.content.amazon.factory.helpers.LoadBalancingUtils;

import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Constants.LoadBalancingQueryApiActions.CREATE_LOAD_BALANCER;
import static io.cloudslang.content.amazon.entities.constants.Constants.LoadBalancingQueryApiActions.DELETE_LOAD_BALANCER;
import static io.cloudslang.content.amazon.entities.constants.Constants.LoadBalancingQueryApiActions.DESCRIBE_LOAD_BALANCERS;
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
            case DELETE_LOAD_BALANCER:
                return new LoadBalancingUtils().getDeleteLoadBalancerQueryParamsMap(wrapper);
            case DESCRIBE_LOAD_BALANCERS:
                return new LoadBalancingUtils().getDescribeLoadBalancersQueryParamsMap(wrapper);
            default:
                throw new RuntimeException(UNSUPPORTED_QUERY_API);
        }
    }
}
