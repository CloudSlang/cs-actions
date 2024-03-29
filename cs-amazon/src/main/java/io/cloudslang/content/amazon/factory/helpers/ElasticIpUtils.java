/*
 * Copyright 2019-2024 Open Text
 * This program and the accompanying materials
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
/*
 * Copyright 2019-2024 Open Text
 * This program and the accompanying materials
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
 



package io.cloudslang.content.amazon.factory.helpers;

import io.cloudslang.content.amazon.entities.inputs.InputsWrapper;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import static io.cloudslang.content.amazon.utils.InputsUtil.setCommonQueryParamsMap;
import static io.cloudslang.content.amazon.utils.InputsUtil.setOptionalMapEntry;

import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.ALLOCATION_ID;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.PUBLIC_IP;

/**
 * Created by Mihai Tusa.
 * 9/14/2016.
 */
public class ElasticIpUtils {
    private static final String DOMAIN = "Domain";

    public Map<String, String> getAllocateAddressQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(DOMAIN, wrapper.getCustomInputs().getDomain());

        return queryParamsMap;
    }

    public Map<String, String> getReleaseAddressQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());

        setOptionalMapEntry(queryParamsMap, ALLOCATION_ID, wrapper.getCustomInputs().getAllocationId(),
                isNotBlank(wrapper.getCustomInputs().getAllocationId()));
        setOptionalMapEntry(queryParamsMap, PUBLIC_IP, wrapper.getElasticIpInputs().getPublicIp(),
                isNotBlank(wrapper.getElasticIpInputs().getPublicIp()));

        return queryParamsMap;
    }
}
