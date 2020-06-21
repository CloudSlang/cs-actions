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

package io.cloudslang.content.abbyy.utils;

import io.cloudslang.content.abbyy.entities.others.LocationId;
import io.cloudslang.content.abbyy.entities.others.Region;
import org.apache.commons.lang3.StringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
public class InputParserTest {

    private static final String INPUT_NAME = "dummy";

    @Rule
    public ExpectedException exception = ExpectedException.none();


    @Test
    public void parseDescription_descrIsNull_emptyStringReturned() {
        //Arrange
        final String descr = null;
        //Act
        String result = InputParser.parseDescription(descr);
        //Assert
        assertEquals(StringUtils.EMPTY, result);
    }


    @Test
    public void parseDescription_descrHasMaxNrOfChars_descrIsNotTrimmed() {
        //Arrange
        final String descr = StringUtils.repeat("-", 255);
        //Act
        String result = InputParser.parseDescription(descr);
        //Assert
        assertEquals(descr, result);
    }


    @Test
    public void parseDescription_descrHasMoreThanMaxNrOfChars_descrIsTrimmed() {
        //Arrange
        final String descr = StringUtils.repeat("-", 256);
        //Act
        String result = InputParser.parseDescription(descr);
        //Assert
        assertEquals(255, result.length());
    }


    @Test
    public void parseShort_valueIsNull_IllegalArgumentException() {
        //Arrange
        final String value = null;

        try {
            //Act
            InputParser.parseShort(value, INPUT_NAME);
            //Assert
            fail();
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains(INPUT_NAME));
        }
    }


    @Test
    public void parseShort_valueIsNaN_IllegalArgumentException() {
        //Arrange
        final String value = "not a number";

        try {
            //Act
            InputParser.parseShort(value, INPUT_NAME);
            //Assert
            fail();
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains(INPUT_NAME));
        }
    }


    @Test
    public void parseNonNegativeShort_valueIsValid_correspondingNumberReturned() {
        //Arrange
        final String value = "0";
        //Act
        short result = InputParser.parseShort(value, INPUT_NAME);
        //Assert
        assertEquals(Short.parseShort(value), result);
    }


    @Test
    public void parseInt_valueIsNull_IllegalArgumentException() {
        //Arrange
        final String value = null;

        try {
            //Act
            InputParser.parseInt(value, INPUT_NAME);
            //Assert
            fail();
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains(INPUT_NAME));
        }
    }


    @Test
    public void parseInt_valueIsNaN_IllegalArgumentException() {
        //Arrange
        final String value = "not a number";

        try {
            //Act
            InputParser.parseInt(value, INPUT_NAME);
            //Assert
            fail();
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains(INPUT_NAME));
        }
    }


    @Test
    public void parseInt_valueIsValid_correspondingNumberReturned() {
        //Arrange
        final String value = "0";
        //Act
        int result = InputParser.parseInt(value, INPUT_NAME);
        //Assert
        assertEquals(Integer.parseInt(value), result);
    }


    @Test
    public void parseBoolean_valueIsNull_IllegalArgumentException() {
        //Arrange
        final String value = null;

        try {
            //Act
            InputParser.parseBoolean(value, INPUT_NAME);
            //Assert
            fail();
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains(INPUT_NAME));
        }
    }


    @Test
    public void parseBoolean_valueIsNotABoolean_IllegalArgumentException() {
        //Arrange
        final String value = "dummy";

        try {
            //Act
            InputParser.parseBoolean(value, INPUT_NAME);
            //Assert
            fail();
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains(INPUT_NAME));
        }
    }


    @Test
    public void parseBoolean_valueIsWrongCaseTrue_IllegalArgumentException() {
        //Arrange
        final String value = "True";

        try {
            //Act
            InputParser.parseBoolean(value, INPUT_NAME);
            //Assert
            fail();
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains(INPUT_NAME));
        }
    }


    @Test
    public void parseBoolean_valueIsWrongCaseFalse_IllegalArgumentException() {
        //Arrange
        final String value = "False";

        try {
            //Act
            InputParser.parseBoolean(value, INPUT_NAME);
            //Assert
            fail();
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains(INPUT_NAME));
        }
    }


    @Test
    public void parseBoolean_valueIsCorrectCaseTrue_IllegalArgumentException() {
        //Arrange
        final String value = "true";
        //Act
        boolean result = InputParser.parseBoolean(value, INPUT_NAME);
        //Assert
        assertTrue(result);
    }


    @Test
    public void parseBoolean_valueIsCorrectCaseFalse_IllegalArgumentException() {
        //Arrange
        final String value = "false";
        //Act
        boolean result = InputParser.parseBoolean(value, INPUT_NAME);
        //Assert
        assertFalse(result);
    }


    @Test
    public void parseRegion_nullStr_IllegalArgumentException() throws Exception {
        //Arrange
        final String str = null;
        //Assert
        exception.expect(IllegalArgumentException.class);
        //Act
        InputParser.parseRegion(str);
    }


    @Test
    public void parseRegion_invalidLength_IllegalArgumentException() throws Exception {
        //Arrange
        final String str = "-1,-1,-1";
        //Assert
        exception.expect(IllegalArgumentException.class);
        //Act
        InputParser.parseRegion(str);
    }


    @Test
    public void parseRegion_coordinateIsNotNumber_NumberFormatException() throws Exception {
        //Arrange
        final String str = "-1,-1,-1,asd";
        //Assert
        exception.expect(NumberFormatException.class);
        //Act
        InputParser.parseRegion(str);
    }


    @Test
    public void parseRegion_coordinateIsInvalidNumber_IllegalArgumentException() throws Exception {
        //Arrange
        final String str = "-1,-1,-1,-2";
        //Assert
        exception.expect(IllegalArgumentException.class);
        //Act
        InputParser.parseRegion(str);
    }


    @Test
    public void parseRegion_illegalCoordinateCombination_IllegalArgumentException() throws Exception {
        //Arrange
        final String str = "2,-1,1,-1";
        //Assert
        exception.expect(IllegalArgumentException.class);
        //Act
        InputParser.parseRegion(str);
    }


    @Test
    public void parseRegion_validCoordinates_IllegalArgumentException() throws Exception {
        //Arrange
        final String str = "0,1,2,3";
        //Act
        Region result = InputParser.parseRegion(str);
        //Assert
        assertEquals(0, result.getLeft());
        assertEquals(1, result.getTop());
        assertEquals(2, result.getRight());
        assertEquals(3, result.getBottom());
    }


    @Test
    public void parseEnum_valueIsInvalid_IllegalArgumentException() {
        //Arrange
        final String value = null;

        try {
            //Act
            InputParser.parseEnum(value, LocationId.class, INPUT_NAME);
            //Assert
            fail();
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains(INPUT_NAME));
        }
    }


    @Test
    public void parseEnum_valueIsValid_IllegalArgumentException() {
        //Arrange
        final String value = LocationId.WEST_US.toString();
        //Act
        LocationId result = InputParser.parseEnum(value, LocationId.class, INPUT_NAME);
        //Assert
        assertEquals(LocationId.WEST_US, result);
    }
}