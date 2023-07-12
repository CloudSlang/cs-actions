/*
 * Copyright 2019-2023 Open Text
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




package io.cloudslang.content.couchbase.factory.nodes;

import io.cloudslang.content.couchbase.entities.inputs.InputsWrapper;

import static io.cloudslang.content.couchbase.entities.constants.Constants.HttpClientInputsValues.ALL_TYPE_HEADER;
import static io.cloudslang.content.couchbase.entities.constants.Constants.HttpClientInputsValues.FORM_URL_ENCODED;
import static io.cloudslang.content.couchbase.entities.constants.Constants.NodeActions.FAIL_OVER_NODE;

/**
 * Created by TusaM
 * 5/9/2017.
 */
public class NodeHeadersBuilder {
    private NodeHeadersBuilder() {
        // prevent instantiation
    }

    public static void setNodeHeaders(InputsWrapper wrapper) {
        switch (wrapper.getCommonInputs().getAction()) {
            default:
                wrapper.getHttpClientInputs().setHeaders(ALL_TYPE_HEADER);
                wrapper.getHttpClientInputs().setContentType(FORM_URL_ENCODED);
        }
    }
}