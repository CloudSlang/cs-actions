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

import static io.cloudslang.content.couchbase.utils.InputsUtil.getEnumValidValuesString;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by TusaM
 * 4/14/2017.
 */
public enum ConflictResolutionType {
    LWW("lww"),
    SEQNO("seqno");

    private final String value;

    ConflictResolutionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static String getValue(String input) {
        if (isBlank(input)) {
            return SEQNO.getValue();
        }

        for (ConflictResolutionType resolution : values()) {
            if (resolution.getValue().equalsIgnoreCase(input)) {
                return resolution.getValue();
            }
        }

        throw new RuntimeException(format("Invalid Couchbase conflict resolution type value: '%s'. Valid values: '%s'.",
                input, getEnumValidValuesString(ConflictResolutionType.class)));
    }
}