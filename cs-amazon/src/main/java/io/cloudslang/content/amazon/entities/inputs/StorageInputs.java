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



package io.cloudslang.content.amazon.entities.inputs;

import static io.cloudslang.content.amazon.utils.InputsUtil.getEnforcedBooleanCondition;
import static io.cloudslang.content.amazon.utils.InputsUtil.getRelevantMaxKeys;

/**
 * Created by TusaM
 * 12/21/2016.
 */
public class StorageInputs {
    private final String bucketName;
    private final String continuationToken;
    private final String encodingType;
    private final String prefix;
    private final String startAfter;

    private final Integer maxKeys;

    private final Boolean fetchOwner;

    public String getBucketName() {
        return bucketName;
    }

    public String getContinuationToken() {
        return continuationToken;
    }

    public String getEncodingType() {
        return encodingType;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getStartAfter() {
        return startAfter;
    }

    public Integer getMaxKeys() {
        return maxKeys;
    }

    public Boolean isFetchOwner() {
        return fetchOwner;
    }

    private StorageInputs(StorageInputs.Builder builder) {
        this.bucketName = builder.bucketName;
        this.continuationToken = builder.continuationToken;
        this.encodingType = builder.encodingType;
        this.maxKeys = builder.maxKeys;
        this.prefix = builder.prefix;
        this.startAfter = builder.startAfter;
        this.fetchOwner = builder.fetchOwner;
    }

    public static class Builder {
        private String bucketName;
        private String continuationToken;
        private String encodingType;
        private String prefix;
        private String startAfter;

        private Integer maxKeys;

        private Boolean fetchOwner;

        public StorageInputs build() {
            return new StorageInputs(this);
        }

        public StorageInputs.Builder withBucketName(String inputValue) {
            bucketName = inputValue;
            return this;
        }

        public StorageInputs.Builder withContinuationToken(String inputValue) {
            continuationToken = inputValue;
            return this;
        }

        public StorageInputs.Builder withEncodingType(String inputValue) {
            encodingType = inputValue;
            return this;
        }

        public StorageInputs.Builder withPrefix(String inputValue) {
            prefix = inputValue;
            return this;
        }

        public StorageInputs.Builder withStartAfter(String inputValue) {
            startAfter = inputValue;
            return this;
        }

        public StorageInputs.Builder withMaxKeys(String inputValue) {
            maxKeys = getRelevantMaxKeys(inputValue);
            return this;
        }

        public StorageInputs.Builder withFetchOwner(String inputValue) {
            fetchOwner = getEnforcedBooleanCondition(inputValue, false);
            return this;
        }
    }
}