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



package io.cloudslang.content.utils;

import io.cloudslang.content.constants.ExceptionValues;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

/**
 * Created by victor on 04.09.2016.
 */
public class BooleanUtilitiesTest {

    @org.junit.Test
    public void isValidTrue() throws Exception {
        assertTrue(BooleanUtilities.isValid("true"));
        assertTrue(BooleanUtilities.isValid("TrUe"));
        assertTrue(BooleanUtilities.isValid("TRUE"));
        assertTrue(BooleanUtilities.isValid("FALSE"));
        assertTrue(BooleanUtilities.isValid("fALSE"));
        assertTrue(BooleanUtilities.isValid("false"));
    }

    @org.junit.Test
    public void isValidFalse() throws Exception {
        assertFalse(BooleanUtilities.isValid(""));
        assertFalse(BooleanUtilities.isValid("T"));
        assertFalse(BooleanUtilities.isValid("F"));
        assertFalse(BooleanUtilities.isValid("1"));
        assertFalse(BooleanUtilities.isValid("0"));

    }

    @org.junit.Test
    public void toBooleanValid() throws Exception {
        assertTrue(BooleanUtilities.toBoolean("true"));
        assertTrue(BooleanUtilities.toBoolean("TRUE"));
        assertTrue(BooleanUtilities.toBoolean("tRue"));
        assertTrue(BooleanUtilities.toBoolean("True"));
        assertTrue(BooleanUtilities.toBoolean("", true));
        assertTrue(BooleanUtilities.toBoolean(null, true));
        assertFalse(BooleanUtilities.toBoolean("false"));
        assertFalse(BooleanUtilities.toBoolean("False"));
        assertFalse(BooleanUtilities.toBoolean("fAlSe"));
        assertFalse(BooleanUtilities.toBoolean("FALSE"));
        assertFalse(BooleanUtilities.toBoolean(null, false));
    }

    @org.junit.Test
    public void toBooleanInvalid() throws Exception {
        try {
            BooleanUtilities.toBoolean("a");
        } catch (IllegalArgumentException iae) {
            assertEquals(iae.getMessage(), "a" + ExceptionValues.EXCEPTION_DELIMITER + ExceptionValues.INVALID_BOOLEAN_VALUE);
        }
        try {
            BooleanUtilities.toBoolean("b", true);
        } catch (IllegalArgumentException iae) {
            assertEquals(iae.getMessage(), "b" + ExceptionValues.EXCEPTION_DELIMITER + ExceptionValues.INVALID_BOOLEAN_VALUE);
        }
    }

}