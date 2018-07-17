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

package io.cloudslang.content.alibaba.factory.helpers;

import static io.cloudslang.content.alibaba.entities.constants.Constants.AlibabaParams.*;
import static io.cloudslang.content.alibaba.utils.InputsUtil.setCommonQueryParamsMap;

import io.cloudslang.content.alibaba.entities.inputs.InputsWrapper;

import static io.cloudslang.content.alibaba.entities.constants.Constants.AlibabaSignatureParams.SIGNATURE;
import static io.cloudslang.content.alibaba.entities.constants.Constants.AlibabaSignatureParams.SIGNATURE_METHOD;
import static io.cloudslang.content.alibaba.entities.constants.Constants.AlibabaSignatureParams.SIGNATURE_NONCE;
import static io.cloudslang.content.alibaba.entities.constants.Constants.AlibabaSignatureParams.SIGNATURE_VERSION;
import static io.cloudslang.content.alibaba.entities.constants.Constants.AlibabaSignatureParams.TIMESTAMP;

import java.util.Map;
import java.util.HashMap;

import static java.lang.String.valueOf;


public class InstanceUtils {

    private static final String KEY_NAME = "KeyName";
    private static final String MAX_COUNT = "MaxCount";
    private static final String MAX_RESULTS = "MaxResults";
    private static final String MIN_COUNT = "MinCount";

    private static final String ATTRIBUTE = "Attribute";


    public Map<String, String> getRunInstancesQueryParamsMap(InputsWrapper wrapper) throws Exception {
        Map<String, String> queryParamsMap = new HashMap<>();
        setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion(), wrapper.getCommonInputs().getAccessKeyId());
        queryParamsMap.put(INSTANCE_NAME, valueOf(wrapper.getInstanceInputs().getInstanceName()));
        queryParamsMap.put(INSTANCE_TYPE, valueOf(wrapper.getInstanceInputs().getInstanceType()));
        queryParamsMap.put(IMAGE_ID, wrapper.getInstanceInputs().getImageId());
        queryParamsMap.put(SECURITY_GROUP_ID, valueOf(wrapper.getInstanceInputs().getSecurityGroupId()));
        queryParamsMap.put(REGION_ID, valueOf(wrapper.getInstanceInputs().getRegionId()));
        queryParamsMap.put(INTERNET_MAX_BANDWITH_OUT, valueOf(wrapper.getInstanceInputs().getInternetMaxBandwidthOut()));
        queryParamsMap.put(SIGNATURE, valueOf(wrapper.getSignatureInputs().getSignature()));
        queryParamsMap.put(SIGNATURE_METHOD, valueOf(wrapper.getSignatureInputs().getSignatureMethod()));
        queryParamsMap.put(SIGNATURE_NONCE, valueOf(wrapper.getSignatureInputs().getSignatureNonce()));
        queryParamsMap.put(SIGNATURE_VERSION, valueOf(wrapper.getSignatureInputs().getSignatureVersion()));
        queryParamsMap.put(TIMESTAMP, valueOf(wrapper.getSignatureInputs().getTimestamp()));
        queryParamsMap.put(FORMAT, FORMAT_TYPE);


        return queryParamsMap;
    }

}
