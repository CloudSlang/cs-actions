/*
 * (c) Copyright 2018 Micro Focus, L.P.
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

package io.cloudslang.content.alibaba.factory;

import java.util.Map;

import io.cloudslang.content.alibaba.entities.inputs.InputsWrapper;
import io.cloudslang.content.alibaba.factory.helpers.*;

import static io.cloudslang.content.alibaba.entities.constants.Constants.EcsQueryApiActions.*;
import static io.cloudslang.content.alibaba.entities.constants.Constants.ErrorMessages.UNSUPPORTED_QUERY_API;

public class ECSQueryParamsMapBuilder {

    private ECSQueryParamsMapBuilder() {
        // prevent instantiation
    }

    static Map<String, String> getEcsQueryParamsMap(InputsWrapper wrapper) throws Exception {
        switch (wrapper.getCommonInputs().getAction()) {
            case CREATE_INSTANCES:
                return new InstanceUtils().getRunInstancesQueryParamsMap(wrapper);
            default:
                throw new RuntimeException(UNSUPPORTED_QUERY_API);
        }
    }
}
