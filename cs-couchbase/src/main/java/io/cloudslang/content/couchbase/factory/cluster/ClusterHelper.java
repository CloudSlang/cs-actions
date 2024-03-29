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




package io.cloudslang.content.couchbase.factory.cluster;

import io.cloudslang.content.couchbase.entities.inputs.InputsWrapper;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.couchbase.entities.constants.Constants.Miscellaneous.AMPERSAND;
import static io.cloudslang.content.couchbase.entities.constants.Constants.Miscellaneous.EQUAL;
import static io.cloudslang.content.couchbase.entities.constants.Inputs.ClusterInputs.EJECTED_NODES;
import static io.cloudslang.content.couchbase.entities.constants.Inputs.ClusterInputs.KNOWN_NODES;
import static io.cloudslang.content.couchbase.utils.InputsUtil.getPayloadString;
import static io.cloudslang.content.couchbase.utils.InputsUtil.setOptionalMapEntry;
import static io.cloudslang.content.couchbase.utils.InputsUtil.validateNotBothBlankInputs;
import static io.cloudslang.content.couchbase.utils.InputsUtil.validateRebalancingNodesPayloadInputs;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Created by TusaM
 * 5/11/2017.
 */
public class ClusterHelper {
    public String getRebalancingNodesPayload(InputsWrapper wrapper) {
        return getPayloadString(getPayloadMap(wrapper), EQUAL, AMPERSAND, true);
    }

    private Map<String, String> getPayloadMap(InputsWrapper wrapper) {
        String ejectedNodesString = wrapper.getClusterInputs().getEjectedNodes();
        String knownNodesString = wrapper.getClusterInputs().getKnownNodes();
        validateNotBothBlankInputs(ejectedNodesString, knownNodesString, EJECTED_NODES, KNOWN_NODES);

        String delimiter = wrapper.getCommonInputs().getDelimiter();
        validateRebalancingNodesPayloadInputs(ejectedNodesString, delimiter);
        validateRebalancingNodesPayloadInputs(knownNodesString, delimiter);

        Map<String, String> payloadMap = new HashMap<>();
        setOptionalMapEntry(payloadMap, EJECTED_NODES, wrapper.getClusterInputs().getEjectedNodes(), isNotBlank(wrapper.getClusterInputs().getEjectedNodes()));
        setOptionalMapEntry(payloadMap, KNOWN_NODES, wrapper.getClusterInputs().getKnownNodes(), isNotBlank(wrapper.getClusterInputs().getKnownNodes()));

        return payloadMap;
    }
}