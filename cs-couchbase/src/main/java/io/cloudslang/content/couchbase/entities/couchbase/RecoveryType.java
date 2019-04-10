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



package io.cloudslang.content.couchbase.entities.couchbase;

import static io.cloudslang.content.couchbase.utils.InputsUtil.getEnumValidValuesString;
import static java.lang.String.format;

/**
 * Created by TusaM
 * 5/17/2017.
 */
public enum RecoveryType {
    DELTA("delta"),
    FULL("full");

    private final String value;

    RecoveryType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static String getValue(String input) {
        for (RecoveryType type : values()) {
            if (type.getValue().equalsIgnoreCase(input)) {
                return type.getValue();
            }
        }

        throw new RuntimeException(format("Invalid Couchbase node recovery type value: '%s'. Valid values: '%s'.",
                input, getEnumValidValuesString(RecoveryType.class)));
    }
}