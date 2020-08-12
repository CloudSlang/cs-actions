/*
 * (c) Copyright 2020 EntIT Software LLC, a Micro Focus company, L.P.
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
package io.cloudslang.content.maps.constants;

public final class ExceptionsMsgs {

    public static final String KEY_CANNOT_CONTAIN_MAP_START = "Key cannot contain value of " + InputNames.MAP_START + " input";
    public static final String KEY_CANNOT_CONTAIN_MAP_END = "Key cannot contain value of " + InputNames.MAP_END + " input";
    public static final String KEY_CANNOT_CONTAIN_PAIR_DELIMITER = "Key cannot contain value of " + InputNames.PAIR_DELIMITER + " input";
    public static final String KEY_CANNOT_CONTAIN_ENTRY_DELIMITER = "Key cannot contain value of " + InputNames.ENTRY_DELIMITER + " input";
    public static final String VALUE_CANNOT_CONTAIN_MAP_START = "Value cannot contain " + InputNames.MAP_START;
    public static final String VALUE_CANNOT_CONTAIN_MAP_END = "Value cannot contain " + InputNames.MAP_END;
    public static final String VALUE_CANNOT_CONTAIN_PAIR_DELIMITER = "Value cannot contain " + InputNames.PAIR_DELIMITER;
    public static final String VALUE_CANNOT_CONTAIN_ENTRY_DELIMITER = "Value cannot contain " + InputNames.ENTRY_DELIMITER;
    public static final String NULL_OR_EMPTY_PAIR_DELIMITER = "Value of " + InputNames.PAIR_DELIMITER + " was null or empty.";
    public static final String NULL_OR_EMPTY_ENTRY_DELIMITER = "Value of " + InputNames.ENTRY_DELIMITER + " was null or empty.";
    public static final String NULL_MAP_START = "Value of " + InputNames.MAP_START + " was null.";
    public static final String NULL_MAP_END = "Value of " + InputNames.MAP_END + " was null.";
    public static final String ENTRY_AND_PAIR_DELIMITER_MUST_BE_DISTINCT = InputNames.ENTRY_DELIMITER + " and " +
            InputNames.PAIR_DELIMITER + " must be distinct.";
    public static final String PAIR_DELIMITER_CANNOT_CONTAIN_ENTRY_DELIMITER = InputNames.PAIR_DELIMITER + " cannot contain " + InputNames.ENTRY_DELIMITER;
    public static final String NO_MAP_PROVIDED = "No map provided";
    public static final String INVALID_MAP = "Invalid map provided";
    public static final String DUPLICATE_KEY = "Duplicate key found in map.";
    public static final String MAP_EXPECTED_TO_START_WITH_MAP_START = "Map was expected to start with value of " + InputNames.MAP_START + " input.";
    public static final String MAP_EXPECTED_TO_END_WITH_MAP_END = "Map was expected to end with value of " + InputNames.MAP_END + " input.";
    public static final String MISSING_PAIR_DELIMITER = "Missing key-value pair delimiter in map.";
    public static final String PAIR_DELIMITER_APPEARS_MORE_THAN_ONCE = "Key-value pair delimiter appears more than once in entry.";


    private ExceptionsMsgs() {

    }
}
