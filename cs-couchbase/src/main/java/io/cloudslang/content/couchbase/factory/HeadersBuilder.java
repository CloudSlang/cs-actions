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

package io.cloudslang.content.couchbase.factory;

import io.cloudslang.content.couchbase.entities.inputs.InputsWrapper;

import static io.cloudslang.content.couchbase.entities.constants.Constants.Api.BUCKETS;
import static io.cloudslang.content.couchbase.entities.constants.Constants.Api.CLUSTER;
import static io.cloudslang.content.couchbase.entities.constants.Constants.Api.NODES;
import static io.cloudslang.content.couchbase.entities.constants.Constants.Api.VIEWS;
import static io.cloudslang.content.couchbase.entities.constants.Constants.ErrorMessages.UNKNOWN_COUCHBASE_HEADER;
import static io.cloudslang.content.couchbase.factory.buckets.BucketHeadersBuilder.setBucketHeaders;
import static io.cloudslang.content.couchbase.factory.cluster.ClusterHeadersBuilder.setClusterHeaders;
import static io.cloudslang.content.couchbase.factory.nodes.NodeHeadersBuilder.setNodeHeaders;
import static io.cloudslang.content.couchbase.factory.views.ViewHeadersBuilder.setViewHeaders;

/**
 * Created by TusaM
 * 4/14/2017.
 */
public class HeadersBuilder {
    private HeadersBuilder() {
        // prevent instantiation
    }

    public static void buildHeaders(InputsWrapper wrapper) {
        switch (wrapper.getCommonInputs().getApi()) {
            case BUCKETS:
                setBucketHeaders(wrapper);
                break;
            case CLUSTER:
                setClusterHeaders(wrapper);
                break;
            case NODES:
                setNodeHeaders(wrapper);
                break;
            case VIEWS:
                setViewHeaders(wrapper);
                break;
            default:
                throw new RuntimeException(UNKNOWN_COUCHBASE_HEADER);
        }
    }
}