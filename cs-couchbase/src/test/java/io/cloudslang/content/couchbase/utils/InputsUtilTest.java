/*
 * Copyright 2019-2024 Open Text
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




package io.cloudslang.content.couchbase.utils;

import io.cloudslang.content.couchbase.entities.couchbase.AuthType;
import io.cloudslang.content.couchbase.entities.couchbase.BucketType;
import io.cloudslang.content.couchbase.entities.couchbase.ConflictResolutionType;
import io.cloudslang.content.couchbase.entities.couchbase.EvictionPolicy;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static io.cloudslang.content.couchbase.utils.InputsUtil.getEnumValidValuesString;
import static io.cloudslang.content.couchbase.utils.InputsUtil.getPayloadString;
import static io.cloudslang.content.couchbase.utils.InputsUtil.getValidIntValue;
import static io.cloudslang.content.couchbase.utils.InputsUtil.getValidPort;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Created by TusaM
 * 4/24/2017.
 */
public class InputsUtilTest {
    @Test
    public void testGetEnumValidAuthTypeValuesString() {
        String toTest = getEnumValidValuesString(AuthType.class);

        assertEquals("none, sasl", toTest);
    }

    @Test
    public void testGetEnumValidBucketTypeValuesString() {
        String toTest = getEnumValidValuesString(BucketType.class);

        assertEquals("couchbase, membase", toTest);
    }

    @Test
    public void testGetEnumValidConflictResolutionTypeValuesString() {
        String toTest = getEnumValidValuesString(ConflictResolutionType.class);

        assertEquals("lww, seqno", toTest);
    }

    @Test
    public void testGetEnumValidEvictionPolicyValuesString() {
        String toTest = getEnumValidValuesString(EvictionPolicy.class);

        assertEquals("fullEviction, valueOnly", toTest);
    }

    @Test
    public void testGetValidIntValueExceedMaxValue() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> getValidIntValue("65536", 0, 65535, 80));
        assertEquals("The provided value: 65536 is not within valid range. " +
                "See operation inputs description section for details.", exception.getMessage());
    }

    @Test
    public void testGetValidIntValueBellowMinValue() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> getValidIntValue("-1", 0, 65535, 80));
        assertEquals("The provided value: -1 is not within valid range. " +
                "See operation inputs description section for details.", exception.getMessage());
    }

    @Test
    public void testGetValidIntValueEmptyInput() {
        int toTest = getValidIntValue("", 0, 65535, 80);

        assertEquals(80, toTest);
    }

    @Test
    public void testGetValidIntValueValidInput() {
        int toTest = getValidIntValue("11215", 0, 65535, 80);

        assertEquals(11215, toTest);
    }

    @Test
    public void testGetValidPortNoValue() {
        assertEquals(11215, getValidPort(""));
    }

    @Test
    public void testGetValidPort() {
        assertEquals(11211, getValidPort("11211"));
    }

    @Test
    public void testGetValidPortWrongValue() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> getValidPort("not integer"));
        assertEquals("Incorrect provided value: not integer input. " +
                "The value doesn't meet conditions for general purpose usage. See operation inputs description section for details.",
                exception.getMessage());
    }

    @Test
    public void testGetEmptyPayloadString() {
        assertEquals(EMPTY, getPayloadString(new HashMap<String, String>(), ",", "", true));
    }

    @Test
    public void testGetIntegerWrongInputValue() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> getValidIntValue("blah blah", 0, null, 3));
        assertEquals("The provided input value: blah blah is not integer.", exception.getMessage());
    }

    @Test
    public void testGetIntegerBellowAllowedMinimum() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> getValidIntValue("-10", 0, null, 3));
        assertEquals("The provided value: -10 is bellow minimum allowed. " +
                "See operation inputs description section for details.", exception.getMessage());
    }

    @Test
    public void testGetValidInt() {
        int toTest = getValidIntValue("8080", 0, null, 80);

        assertEquals(8080, toTest);
    }
}