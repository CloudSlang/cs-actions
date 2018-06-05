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

import io.cloudslang.content.couchbase.entities.inputs.BucketInputs;
import io.cloudslang.content.couchbase.entities.inputs.ClusterInputs;
import io.cloudslang.content.couchbase.entities.inputs.CommonInputs;
import io.cloudslang.content.couchbase.entities.inputs.InputsWrapper;
import io.cloudslang.content.couchbase.entities.inputs.NodeInputs;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;

import static io.cloudslang.content.couchbase.entities.constants.Constants.ErrorMessages.UNKNOWN_BUILDER_TYPE;
import static io.cloudslang.content.couchbase.entities.constants.Constants.Values.INIT_INDEX;

/**
 * Created by Mihai Tusa
 * 4/9/2017.
 */
public class InputsWrapperBuilder {
    private InputsWrapperBuilder() {
        // prevent instantiation
    }

    @SafeVarargs
    public static <T> InputsWrapper buildWrapper(HttpClientInputs httpClientInputs, CommonInputs commonInputs, T... builders) {
        InputsWrapper wrapper = new InputsWrapper.Builder()
                .withHttpClientInputs(httpClientInputs)
                .withCommonInputs(commonInputs)
                .build();

        if (builders.length > INIT_INDEX) {
            for (T builder : builders) {
                if (builder instanceof BucketInputs) {
                    wrapper.setBucketInputs((BucketInputs) builder);
                } else if (builder instanceof ClusterInputs) {
                    wrapper.setClusterInputs((ClusterInputs) builder);
                } else if (builder instanceof NodeInputs) {
                    wrapper.setNodeInputs((NodeInputs) builder);
                } else {
                    throw new RuntimeException(UNKNOWN_BUILDER_TYPE);
                }
            }
        }

        return wrapper;
    }
}