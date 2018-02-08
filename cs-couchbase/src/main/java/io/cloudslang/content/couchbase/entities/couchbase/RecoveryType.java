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

/**
 * Created by TusaM
 * 5/17/2017.
 */
public enum RecoveryType {
    DELTA("delta"),
    FULL("full");

    private static final Map<String, String> RECOVERY_TYPE_MAP = new HashMap<>();

    static {
        stream(values())
                .forEach(recoveryType -> RECOVERY_TYPE_MAP.put(recoveryType.name().toLowerCase(), recoveryType.getValue()));
    }

    private final String value;

    RecoveryType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static String getRecoveryTypeValue(String input) {
        return Optional
                .ofNullable(RECOVERY_TYPE_MAP.get(input))
                .orElseThrow(() -> new RuntimeException(format("Invalid Couchbase node recovery type value: '%s'. Valid values: '%s'.",
                        input, getEnumValues(RecoveryType.class))));
    }
}