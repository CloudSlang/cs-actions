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

package io.cloudslang.content.couchbase.utils;

import io.cloudslang.content.couchbase.entities.couchbase.AuthType;
import io.cloudslang.content.couchbase.entities.couchbase.BucketType;
import io.cloudslang.content.couchbase.entities.couchbase.ConflictResolutionType;
import io.cloudslang.content.couchbase.entities.couchbase.EvictionPolicy;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;

import static io.cloudslang.content.couchbase.utils.InputsUtil.getEnumValues;
import static io.cloudslang.content.couchbase.utils.InputsUtil.getPayloadString;
import static io.cloudslang.content.couchbase.validate.Validators.getValidIntValue;
import static io.cloudslang.content.couchbase.validate.Validators.getValidPort;
import static io.cloudslang.content.couchbase.utils.TestUtils.setExpectedExceptions;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.Assert.assertEquals;

/**
 * Created by TusaM
 * 4/24/2017.
 */
public class InputsUtilTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testGetEnumValidAuthTypeValuesString() {
        String toTest = getEnumValues(AuthType.class);

        assertEquals("none, sasl", toTest);
    }

    @Test
    public void testGetEnumValidBucketTypeValuesString() {
        String toTest = getEnumValues(BucketType.class);

        assertEquals("couchbase, membase", toTest);
    }

    @Test
    public void testGetEnumValidConflictResolutionTypeValuesString() {
        String toTest = getEnumValues(ConflictResolutionType.class);

        assertEquals("lww, seqno", toTest);
    }

    @Test
    public void testGetEnumValidEvictionPolicyValuesString() {
        String toTest = getEnumValues(EvictionPolicy.class);

        assertEquals("fullEviction, valueOnly", toTest);
    }

    @Test
    public void testGetValidIntValueExceedMaxValue() {
        setExpectedExceptions(RuntimeException.class, exception, "The provided value: 65536 is not within valid range. " +
                "See operation inputs description section for details.");

        getValidIntValue("65536", 0, 65535, 80);
    }

    @Test
    public void testGetValidIntValueBellowMinValue() {
        setExpectedExceptions(RuntimeException.class, exception, "The provided value: -1 is not within valid range. " +
                "See operation inputs description section for details.");

        getValidIntValue("-1", 0, 65535, 80);
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
        setExpectedExceptions(IllegalArgumentException.class, exception, "Incorrect provided value: not integer input. " +
                "The value doesn't meet conditions for general purpose usage. See operation inputs description section for details.");

        getValidPort("not integer");
    }

    @Test
    public void testGetEmptyPayloadString() {
        assertEquals(EMPTY, getPayloadString(new HashMap<>(), ",", "", true));
    }

    @Test
    public void testGetIntegerWrongInputValue() {
        setExpectedExceptions(RuntimeException.class, exception, "The provided input value: blah blah is not integer.");

        getValidIntValue("blah blah", 0, null, 3);
    }

    @Test
    public void testGetIntegerBellowAllowedMinimum() {
        setExpectedExceptions(RuntimeException.class, exception, "The provided value: -10 is bellow minimum allowed. " +
                "See operation inputs description section for details.");

        getValidIntValue("-10", 0, null, 3);
    }

    @Test
    public void testGetValidInt() {
        int toTest = getValidIntValue("8080", 0, null, 80);

        assertEquals(8080, toTest);
    }
}