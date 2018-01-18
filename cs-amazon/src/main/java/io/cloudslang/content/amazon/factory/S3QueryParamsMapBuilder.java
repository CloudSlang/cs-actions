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

package io.cloudslang.content.amazon.factory;

import io.cloudslang.content.amazon.entities.inputs.InputsWrapper;
import io.cloudslang.content.amazon.factory.helpers.StorageUtils;

import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Constants.ErrorMessages.UNSUPPORTED_QUERY_API;
import static io.cloudslang.content.amazon.entities.constants.Constants.S3QueryApiActions.GET_BUCKET;

/**
 * Created by TusaM
 * 12/23/2016.
 */
class S3QueryParamsMapBuilder {
    private S3QueryParamsMapBuilder() {
        // prevent instantiation
    }

    static Map<String, String> getS3QueryParamsMap(InputsWrapper wrapper) {
        switch (wrapper.getCommonInputs().getAction()) {
            case GET_BUCKET:
                return new StorageUtils().retrieveGetBucketQueryParamsMap(wrapper);
            default:
                throw new RuntimeException(UNSUPPORTED_QUERY_API);
        }
    }
}