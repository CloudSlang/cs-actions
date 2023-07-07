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



package io.cloudslang.content.couchbase.factory.buckets;

import io.cloudslang.content.couchbase.entities.inputs.InputsWrapper;

import static io.cloudslang.content.couchbase.entities.constants.Constants.BucketActions.CREATE_OR_EDIT_BUCKET;
import static io.cloudslang.content.couchbase.entities.constants.Constants.BucketActions.GET_ALL_BUCKETS;
import static io.cloudslang.content.couchbase.entities.constants.Constants.BucketActions.GET_BUCKET;
import static io.cloudslang.content.couchbase.entities.constants.Constants.BucketActions.GET_BUCKET_STATISTICS;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;
import static io.cloudslang.content.couchbase.entities.constants.Constants.HttpClientInputsValues.ALL_TYPE_HEADER;
import static io.cloudslang.content.couchbase.entities.constants.Constants.HttpClientInputsValues.FORM_URL_ENCODED;
import static io.cloudslang.content.couchbase.entities.constants.Constants.HttpClientInputsValues.X_MEMCACHEKV_STORE_CLIENT_SPECIFICATION_VERSION_0_1;

/**
 * Created by TusaM
 * 4/12/2017.
 */
public class BucketHeadersBuilder {
    private BucketHeadersBuilder() {
        // prevent instantiation
    }

    public static void setBucketHeaders(InputsWrapper wrapper) {
        switch (wrapper.getCommonInputs().getAction()) {
            case CREATE_OR_EDIT_BUCKET:
                wrapper.getHttpClientInputs().setHeaders(ALL_TYPE_HEADER);
                wrapper.getHttpClientInputs().setContentType(FORM_URL_ENCODED);
                break;
            case GET_ALL_BUCKETS:
            case GET_BUCKET:
            case GET_BUCKET_STATISTICS:
                wrapper.getHttpClientInputs().setHeaders(X_MEMCACHEKV_STORE_CLIENT_SPECIFICATION_VERSION_0_1);
                wrapper.getHttpClientInputs().setContentType(APPLICATION_JSON.getMimeType());
                break;
            default:
                wrapper.getHttpClientInputs().setContentType(APPLICATION_JSON.getMimeType());
        }
    }
}