/*
 * Copyright 2021-2024 Open Text
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


package io.cloudslang.content.mail.entities;

import io.cloudslang.content.mail.constants.ExceptionMsgs;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SendMailInputTest {

    private static final String HEADERS_WITH_DIFFERENT_DELIMITERS = "Sensitivity;Company-Confidential|" +
            "message-type;Multiple Part|" +
            "Sensitivity;Personal";
    private static final String HEADERS_WITH_MISSING_COLUMNDELIMITER = "Sensitivity:Company-Confidential" +
            "message-type:Multiple Part\n" +
            "Sensitivity:Personal";
    private static final String VALID_ROW = "Sensitivity:Company-Confidential";
    private static final String EMPTY_ROW = ":";
    private static final String ROW_MISSING_HEADERNAME = "Sensitivity:";
    private static final String ROW_WITH_MORE_THAN_ONE_DELIMITER = "Sensitivity:Company:Confidential";
    private static final String ROW_WITH_NO_DELIMITER = "Sensitivity Company-Confidential";
    private static final String HEADER1 = "Sensitivity";
    private static final String HEADER1v = "Company-Confidential";
    private static final String HEADER2 = "message-type";
    private static final String HEADER2v = "Multiple Part";
    private static final String HEADER3 = "Sensitivity";
    private static final String HEADER3v = "Personal";
    private static final String VERTICAL_BAR = "|";
    private static final String DEFAULT_ROW_DELIMITER = "\n";
    private static final String DEFAULT_COLUMN_DELIMITER = ":";
    private static final String COLUMN_DELIMITER = ";";

    private static final String EMPTY_HEADERS = "'headers' input does not contain any values";
    private static final String HEADERS_WITH_MULTIPLE_DELIMITERS = "'headers' input has more than one column delimiter";
    private static final String HEADERS_WITH_MISSING_VALUES = "'headers' input is missing one of the header values";
    private static final String HEADERS_INPUT_HAS_NO_DELIMITER = "'headers' input has no column delimiter";

    private SendMailInput.Builder inputBuilder;
    @Spy
    private SendMailInput.Builder inputBuilderSpy = new SendMailInput.Builder();

    @BeforeEach
    public void setUp() throws Exception {
        inputBuilder = new SendMailInput.Builder();
    }

    @AfterEach
    public void tearDown() throws Exception {
        inputBuilder = null;
    }

    /**
     * Test extractHeaderNamesAndValues method for valid inputs.
     *
     * @throws Exception
     */
    @Test
    public void testExtractHeaderNamesAndValues() throws Exception {
        Object[] headerNamesAndValues = inputBuilder.extractHeaderNamesAndValues(HEADERS_WITH_DIFFERENT_DELIMITERS,
                VERTICAL_BAR, COLUMN_DELIMITER);
        ArrayList<String> headerNames = (ArrayList<String>) headerNamesAndValues[0];
        ArrayList<String> headerValues = (ArrayList<String>) headerNamesAndValues[1];

        Iterator namesIter = headerNames.iterator();
        Iterator valuesIter = headerValues.iterator();
        while (namesIter.hasNext() && valuesIter.hasNext()) {
            assertEquals(HEADER1, namesIter.next());
            assertEquals(HEADER1v, valuesIter.next());
            assertEquals(HEADER2, namesIter.next());
            assertEquals(HEADER2v, valuesIter.next());
            assertEquals(HEADER3, namesIter.next());
            assertEquals(HEADER3v, valuesIter.next());
        }
    }

    /**
     * Test extractHeaderNamesAndValues method for invalid inputs.
     *
     * @throws Exception
     */
    @Test
    public void testExtractHeaderNamesAndValueForInvalidValues() throws Exception {
        assertThrows(Exception.class, () -> inputBuilderSpy.extractHeaderNamesAndValues(HEADERS_WITH_MISSING_COLUMNDELIMITER,
                DEFAULT_ROW_DELIMITER, DEFAULT_COLUMN_DELIMITER));
        verify(inputBuilderSpy).validateRow(any(), any(), anyInt());
    }

    @Test
    public void testValidateDelimitersForValidValues() throws Exception {
        inputBuilder.validateDelimiters(DEFAULT_ROW_DELIMITER, COLUMN_DELIMITER);
    }

    @Test
    public void testValidateDelimitersWhenRowDelimiterIsASubstringOfColumnDelimiter() throws Exception {
        Exception exception = assertThrows(Exception.class, () ->
                inputBuilder.validateDelimiters(COLUMN_DELIMITER.substring(0, COLUMN_DELIMITER.length() - 1), COLUMN_DELIMITER));
        assertEquals(ExceptionMsgs.INVALID_ROW_DELIMITER, exception.getMessage());
    }

    @Test
    public void testValidateDelimitersWhenTheyAreEqual() throws Exception {
        Exception exception = assertThrows(Exception.class, () ->
                inputBuilder.validateDelimiters(DEFAULT_ROW_DELIMITER, DEFAULT_ROW_DELIMITER));
        assertEquals(ExceptionMsgs.INVALID_DELIMITERS, exception.getMessage());
    }

    /**
     * Test validateRow method with valid row value.
     *
     * @throws Exception
     */
    @Test
    public void testValidateRowWithValidInput() throws Exception {
        assertTrue(inputBuilder.validateRow(VALID_ROW, DEFAULT_COLUMN_DELIMITER, 0));
    }

    /**
     * Test validateRow method with empty values in the row.
     *
     * @throws Exception
     */
    @Test
    public void testValidateRowWithEmptyValues() throws Exception {
        Exception exception = assertThrows(Exception.class, () ->
                inputBuilder.validateRow(EMPTY_ROW, DEFAULT_COLUMN_DELIMITER, 0));
        assertTrue(exception.getMessage().contains(EMPTY_HEADERS));
    }

    /**
     * Test validateRow method with more than one column delimiter found in the row.
     *
     * @throws Exception
     */
    @Test
    public void testValidateRowWithMoreThanOneColumnDelimiter() throws Exception {
        Exception exception = assertThrows(Exception.class, () ->
                inputBuilder.validateRow(ROW_WITH_MORE_THAN_ONE_DELIMITER, DEFAULT_COLUMN_DELIMITER, 0));
        assertTrue(exception.getMessage().contains(HEADERS_WITH_MULTIPLE_DELIMITERS));
    }

    /**
     * Test validateRow method when one of the header values is missing.
     *
     * @throws Exception
     */
    @Test
    public void testValidateRowWithOneOfTheHeaderValuesMissing() throws Exception {
        Exception exception = assertThrows(Exception.class, () ->
                inputBuilder.validateRow(ROW_MISSING_HEADERNAME, DEFAULT_COLUMN_DELIMITER, 0));
        assertTrue(exception.getMessage().contains(HEADERS_WITH_MISSING_VALUES));
    }

    /**
     * Test validateRow method when the delimiter is missing.
     *
     * @throws Exception
     */
    @Test
    public void testValidateRowWithMissingDelimiter() throws Exception {
        Exception exception = assertThrows(Exception.class, () ->
                inputBuilder.validateRow(ROW_WITH_NO_DELIMITER, DEFAULT_COLUMN_DELIMITER, 0));
        assertTrue(exception.getMessage().contains(HEADERS_INPUT_HAS_NO_DELIMITER));
    }

}
