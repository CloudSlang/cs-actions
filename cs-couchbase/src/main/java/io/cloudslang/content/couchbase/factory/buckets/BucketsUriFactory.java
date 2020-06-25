/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
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


package io.cloudslang.content.couchbase.factory.buckets;

import io.cloudslang.content.couchbase.entities.inputs.InputsWrapper;

import static io.cloudslang.content.couchbase.entities.constants.Constants.BucketActions.CREATE_OR_EDIT_BUCKET;
import static io.cloudslang.content.couchbase.entities.constants.Constants.BucketActions.FLUSH_BUCKET;
import static io.cloudslang.content.couchbase.entities.constants.Constants.BucketActions.GET_ALL_BUCKETS;
import static io.cloudslang.content.couchbase.entities.constants.Constants.BucketActions.GET_BUCKET;
import static io.cloudslang.content.couchbase.entities.constants.Constants.BucketActions.GET_BUCKET_STATISTICS;
import static io.cloudslang.content.couchbase.entities.constants.Constants.BucketActions.DELETE_BUCKET;
import static io.cloudslang.content.couchbase.entities.constants.Constants.Miscellaneous.SLASH;
import static io.cloudslang.content.couchbase.entities.constants.Inputs.BucketInputs.BUCKETS;
import static io.cloudslang.content.couchbase.entities.couchbase.ApiUriSuffix.DO_FLUSH;
import static io.cloudslang.content.couchbase.entities.couchbase.CouchbaseApi.CONTROLLER;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Created by Mihai Tusa
 * 4/8/2017.
 */
public class BucketsUriFactory {
    private BucketsUriFactory() {
        // prevent instantiation
    }

    public static String getBucketsUriValue(InputsWrapper wrapper) {
        String action = wrapper.getCommonInputs().getAction();
        switch (action) {
            case GET_BUCKET:
            case GET_BUCKET_STATISTICS:
            case DELETE_BUCKET:
                return BUCKETS + SLASH + wrapper.getBucketInputs().getBucketName();
            case FLUSH_BUCKET:
                return BUCKETS + SLASH + wrapper.getBucketInputs().getBucketName() + CONTROLLER.getValue() + DO_FLUSH.getValue();
            default:
                return BUCKETS;
        }
    }
}