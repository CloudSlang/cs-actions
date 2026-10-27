/*
 * Copyright 2026-2027 Open Text
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



package io.cloudslang.content.couchbase.factory;

import io.cloudslang.content.couchbase.entities.inputs.InputsWrapper;
import io.cloudslang.content.couchbase.factory.buckets.BucketsHelper;
import io.cloudslang.content.couchbase.factory.cluster.ClusterHelper;
import io.cloudslang.content.couchbase.factory.nodes.NodesHelper;
import static io.cloudslang.content.couchbase.entities.constants.HttpClientInputNames.POST_METHOD;

import static io.cloudslang.content.couchbase.entities.constants.Constants.BucketActions.CREATE_OR_EDIT_BUCKET;
import static io.cloudslang.content.couchbase.entities.constants.Constants.ClusterActions.REBALANCING_NODES;
import static io.cloudslang.content.couchbase.entities.constants.Constants.NodeActions.FAIL_OVER_NODE;
import static io.cloudslang.content.couchbase.entities.constants.Constants.NodeActions.GRACEFUL_FAIL_OVER_NODE;
import static io.cloudslang.content.couchbase.entities.constants.Constants.NodeActions.SET_RECOVERY_TYPE;

/**
 * Created by TusaM
 * 4/14/2017.
 */
public class PayloadBuilder {
    private PayloadBuilder() {
        // prevent instantiation
    }

    public static void buildPayload(InputsWrapper wrapper) {
        if (POST_METHOD.equalsIgnoreCase(wrapper.getHttpClientInputs().getMethod())) {
            switch (wrapper.getCommonInputs().getAction()) {
                case CREATE_OR_EDIT_BUCKET:
                    wrapper.setBody(new BucketsHelper().getCreateBucketPayload(wrapper));
                    break;
                case FAIL_OVER_NODE:
                case GRACEFUL_FAIL_OVER_NODE:
                    wrapper.setBody(new NodesHelper().getFailOverNodePayloadString(wrapper));
                    break;
                case REBALANCING_NODES:
                    wrapper.setBody(new ClusterHelper().getRebalancingNodesPayload(wrapper));
                    break;
                case SET_RECOVERY_TYPE:
                    wrapper.setBody(new NodesHelper().getRecoveryTypePayloadString(wrapper));
                    break;
                default:
                    break;
            }
        }
    }
}