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

package io.cloudslang.content.couchbase.entities.couchbase;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static io.cloudslang.content.couchbase.utils.InputsUtil.getEnumValues;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by TusaM
 * 4/14/2017.
 */
public enum BucketType {
    COUCHBASE("couchbase"),
    MEMCACHED("membase");

    private static final Map<String, String> BUCKET_TYPE_MAP = new HashMap<>();

    static {
        stream(values())
                .forEach(bucketType -> BUCKET_TYPE_MAP.put(bucketType.name().toLowerCase(), bucketType.getValue()));
    }

    private final String value;

    BucketType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static String getBucketTypeValue(String input) {
        return isBlank(input) ? MEMCACHED.getValue() :
                Optional
                        .ofNullable(BUCKET_TYPE_MAP.get(input))
                        .orElseThrow(() -> new RuntimeException(format("Invalid Couchbase bucket type value: '%s'. Valid values: '%s'.",
                                input, getEnumValues(BucketType.class))));
    }
}