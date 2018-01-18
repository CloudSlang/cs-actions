/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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

package io.cloudslang.content.couchbase.factory.nodes;

import io.cloudslang.content.couchbase.entities.inputs.InputsWrapper;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.couchbase.entities.constants.Constants.Miscellaneous.AMPERSAND;
import static io.cloudslang.content.couchbase.entities.constants.Constants.Miscellaneous.EQUAL;
import static io.cloudslang.content.couchbase.utils.InputsUtil.getPayloadString;
import static io.cloudslang.content.couchbase.utils.InputsUtil.setOptionalMapEntry;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Created by TusaM
 * 5/9/2017.
 */
public class NodesHelper {
    private static final String OTP_NODE = "otpNode";
    private static final String RECOVERY_TYPE = "recoveryType";

    public String getFailOverNodePayloadString(InputsWrapper wrapper) {
        return getPayloadString(getPayloadMap(wrapper), EQUAL, EMPTY, false);
    }

    public String getRecoveryTypePayloadString(InputsWrapper wrapper) {
        return getPayloadString(getPayloadMap(wrapper), EQUAL, AMPERSAND, true);
    }

    private Map<String, String> getPayloadMap(InputsWrapper wrapper) {
        Map<String, String> payloadMap = new HashMap<>();
        payloadMap.put(OTP_NODE, wrapper.getNodeInputs().getInternalNodeIpAddress());

        setOptionalMapEntry(payloadMap, RECOVERY_TYPE, wrapper.getNodeInputs().getRecoveryType(),
                isNotBlank(wrapper.getNodeInputs().getRecoveryType()));

        return payloadMap;
    }
}