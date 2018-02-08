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

package io.cloudslang.content.couchbase.factory.cluster;

import io.cloudslang.content.couchbase.entities.inputs.InputsWrapper;

import static io.cloudslang.content.couchbase.entities.constants.Constants.ClusterActions.GET_CLUSTER_DETAILS;
import static io.cloudslang.content.couchbase.entities.constants.Constants.ClusterActions.GET_CLUSTER_INFO;
import static io.cloudslang.content.couchbase.entities.constants.Constants.ClusterActions.GET_DESTINATION_CLUSTER_REFERENCE;
import static io.cloudslang.content.couchbase.entities.constants.Constants.ClusterActions.REBALANCING_NODES;
import static io.cloudslang.content.couchbase.entities.constants.Constants.HttpClientInputsValues.ALL_TYPE_HEADER;
import static io.cloudslang.content.couchbase.entities.constants.Constants.HttpClientInputsValues.FORM_URL_ENCODED;
import static io.cloudslang.content.couchbase.entities.constants.Constants.HttpClientInputsValues.X_MEMCACHEKV_STORE_CLIENT_SPECIFICATION_VERSION_0_1;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

/**
 * Created by TusaM
 * 4/20/2017.
 */
public class ClusterHeadersBuilder {
    private ClusterHeadersBuilder() {
        // prevent instantiation
    }

    public static void setClusterHeaders(InputsWrapper wrapper) {
        switch (wrapper.getCommonInputs().getAction()) {
            case REBALANCING_NODES:
                wrapper.getHttpClientInputs().setHeaders(ALL_TYPE_HEADER);
                wrapper.getHttpClientInputs().setContentType(FORM_URL_ENCODED);
                break;
            case GET_CLUSTER_DETAILS:
                wrapper.getHttpClientInputs().setHeaders(X_MEMCACHEKV_STORE_CLIENT_SPECIFICATION_VERSION_0_1);
                wrapper.getHttpClientInputs().setContentType(APPLICATION_JSON.getMimeType());
                break;
            case GET_CLUSTER_INFO:
                wrapper.getHttpClientInputs().setHeaders(X_MEMCACHEKV_STORE_CLIENT_SPECIFICATION_VERSION_0_1);
                wrapper.getHttpClientInputs().setContentType(APPLICATION_JSON.getMimeType());
                break;
            case GET_DESTINATION_CLUSTER_REFERENCE:
                wrapper.getHttpClientInputs().setHeaders(X_MEMCACHEKV_STORE_CLIENT_SPECIFICATION_VERSION_0_1);
                wrapper.getHttpClientInputs().setContentType(APPLICATION_JSON.getMimeType());
                break;
            default:
                wrapper.getHttpClientInputs().setContentType(APPLICATION_JSON.getMimeType());
        }
    }
}