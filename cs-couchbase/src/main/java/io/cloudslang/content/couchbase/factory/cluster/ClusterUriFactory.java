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

/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.couchbase.factory.cluster;

import io.cloudslang.content.couchbase.entities.inputs.InputsWrapper;

import static io.cloudslang.content.couchbase.entities.constants.Constants.ClusterActions.GET_AUTO_FAILOVER_SETTINGS;
import static io.cloudslang.content.couchbase.entities.constants.Constants.ClusterActions.GET_CLUSTER_DETAILS;
import static io.cloudslang.content.couchbase.entities.constants.Constants.ClusterActions.GET_CLUSTER_INFO;
import static io.cloudslang.content.couchbase.entities.constants.Constants.ClusterActions.GET_DESTINATION_CLUSTER_REFERENCE;
import static io.cloudslang.content.couchbase.entities.constants.Constants.ClusterActions.REBALANCING_NODES;
import static io.cloudslang.content.couchbase.entities.couchbase.ApiUriSuffix.AUTO_FAILOVER;
import static io.cloudslang.content.couchbase.entities.couchbase.ApiUriSuffix.DEFAULT;
import static io.cloudslang.content.couchbase.entities.couchbase.CouchbaseApi.CONTROLLER;
import static io.cloudslang.content.couchbase.entities.couchbase.CouchbaseApi.POOLS;
import static io.cloudslang.content.couchbase.entities.couchbase.CouchbaseApi.SETTINGS;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class ClusterUriFactory {
    private ClusterUriFactory() {
        // prevent instantiation
    }

    public static String getClusterUriValue(InputsWrapper wrapper) {
        String action = wrapper.getCommonInputs().getAction();
        switch (action) {
            case GET_AUTO_FAILOVER_SETTINGS:
                return SETTINGS.getValue() + AUTO_FAILOVER.getValue();
            case GET_CLUSTER_DETAILS:
                return POOLS.getValue() + DEFAULT.getValue();
            case GET_CLUSTER_INFO:
                return POOLS.getValue();
            case GET_DESTINATION_CLUSTER_REFERENCE:
                return POOLS.getValue() + DEFAULT.getValue();
            case REBALANCING_NODES:
                return CONTROLLER.getValue();
            default:
                return EMPTY;
        }
    }
}
