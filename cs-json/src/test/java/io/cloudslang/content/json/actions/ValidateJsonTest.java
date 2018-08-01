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
package io.cloudslang.content.json.actions;

import io.cloudslang.content.constants.OtherValues;
import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.json.utils.ValidationUtils;
import org.junit.Test;

import java.util.Map;

import static io.cloudslang.content.json.utils.Constants.ValidationMessages.*;
import static org.junit.Assert.*;

/**
 * Created by Daniel Manciu
 * Date 24/7/2018
 */
public class ValidateJsonTest {

    private static ValidateJson validateJson = new ValidateJson();

    @Test
    public void testValidateJsonSimpleSuccess() {
        String jsonObject = "{\n" +
                "  \"first_name\": \"George\",\n" +
                "  \"last_name\": \"Washington\",\n" +
                "  \"birthday\": \"22-02-1732\"}";

        final Map<String, String> result = validateJson.execute(jsonObject, null, null, null, null, null);
        assertEquals(VALID_JSON, result.get(OutputNames.RETURN_RESULT));
        assertEquals(ReturnCodes.SUCCESS, result.get(OutputNames.RETURN_CODE));
        assertNull(result.get(OutputNames.EXCEPTION));
    }

    @Test
    public void testValidateJsonComplexSuccess() {
        String jsonObject = "{\n" +
                "  \"first_name\": \"George\",\n" +
                "  \"last_name\": \"Washington\",\n" +
                "  \"birthday\": \"22-02-1732\",\n" +
                "  \"address\": {\n" +
                "    \"street_address\": \"3200 Mount Vernon Memorial Highway\",\n" +
                "    \"city\": \"Mount Vernon\",\n" +
                "    \"state\": \"Virginia\",\n" +
                "    \"country\": \"United States\"\n" +
                "  }\n" +
                "}";

        final Map<String, String> result = validateJson.execute(jsonObject, null, null, null, null, null);
        assertEquals(VALID_JSON, result.get(OutputNames.RETURN_RESULT));
        assertEquals(ReturnCodes.SUCCESS, result.get(OutputNames.RETURN_CODE));
        assertNull(result.get(OutputNames.EXCEPTION));
    }

    @Test
    public void testValidateJsonSimpleQuotesSuccess() {
        String jsonObject = "{\n" +
                "  \'first_name\': \'George\',\n" +
                "  \'last_name\': \'Washington\',\n" +
                "  \'birthday\': \'22-02-1732\'}";

        final Map<String, String> result = validateJson.execute(jsonObject, null, null, null, null, null);
        assertEquals(VALID_JSON, result.get(OutputNames.RETURN_RESULT));
        assertEquals(ReturnCodes.SUCCESS, result.get(OutputNames.RETURN_CODE));
        assertNull(result.get(OutputNames.EXCEPTION));
    }

    @Test
    public void testValidateJsonExtraDataFailure() {
        String jsonObject = "{\"firstName\":\"Darth\", \"lastName\":\"Vader\"}, {\"firstName\":\"Luke\", \"lastName\":\"Skywalker\"}";

        final Map<String, String> result = validateJson.execute(jsonObject, null, null, null, null, null);
        assertTrue(result.get(OutputNames.EXCEPTION).contains("input has trailing data after first JSON Text"));
        assertEquals(ReturnCodes.FAILURE, result.get(OutputNames.RETURN_CODE));
        assertEquals(result.get(OutputNames.RETURN_RESULT), result.get(OutputNames.EXCEPTION));
    }


    @Test
    public void testNullInputFailure() {
        final Map<String, String> result = validateJson.execute(null, null, null, null, null, null);
        assertEquals(EMPTY_JSON_PROVIDED, result.get(OutputNames.RETURN_RESULT));
        assertEquals(ReturnCodes.FAILURE, result.get(OutputNames.RETURN_CODE));
    }

    @Test
    public void testEmptyJsonStringFailure() {
        String jsonObject = "";

        final Map<String, String> result = validateJson.execute(jsonObject, null, null, null, null, null);
        assertEquals(EMPTY_JSON_PROVIDED, result.get(OutputNames.RETURN_RESULT));
        assertEquals(ReturnCodes.FAILURE, result.get(OutputNames.RETURN_CODE));
    }

    @Test
    public void testEmptyKeyJsonFailure() {
        String jsonObject = "{\"\" : \"empty key\"}";

        final Map<String, String> result = validateJson.execute(jsonObject, null, null, null, null, null);
        assertEquals(VALID_JSON, result.get(OutputNames.RETURN_RESULT));
        assertEquals(ReturnCodes.SUCCESS, result.get(OutputNames.RETURN_CODE));
        assertNull(result.get(OutputNames.EXCEPTION));
    }

    @Test
    public void testDuplicateKeyJsonFailure() {
        String jsonObject = "{\n" +
                "  \"first_name\": \"George\",\n" +
                "  \"last_name\": \"Washington\",\n" +
                "  \"last_name\": \"Daniel\"}";

        final Map<String, String> result = validateJson.execute(jsonObject, null, null, null, null, null);
        assertTrue(result.get(OutputNames.EXCEPTION).contains("Duplicate field"));
        assertEquals(ReturnCodes.FAILURE, result.get(OutputNames.RETURN_CODE));
        assertEquals(result.get(OutputNames.RETURN_RESULT), result.get(OutputNames.EXCEPTION));
    }

    @Test
    public void testUnquotedCharacterJsonFailure() {
        String jsonObject = "{\n" +
                "  \"first_name\": \"George\",\n" +
                "  \"last_name\": \"Washington,\n" +
                "  \"birthday\": \"22-02-1732\"}";

        final Map<String, String> result = validateJson.execute(jsonObject, null, null, null, null, null);
        assertEquals(ReturnCodes.FAILURE, result.get(OutputNames.RETURN_CODE));
        assertTrue(result.get(OutputNames.EXCEPTION).contains("Illegal unquoted character"));
        assertEquals(result.get(OutputNames.RETURN_RESULT), result.get(OutputNames.EXCEPTION));
    }

    @Test
    public void testEmptySchemaSuccess() {
        String jsonObject = "{\n" +
                "  \"first_name\": \"George\",\n" +
                "  \"last_name\": \"Washington\",\n" +
                "  \"birthday\": \"22-02-1732\"}";

        final Map<String, String> result = validateJson.execute(jsonObject, OtherValues.EMPTY_STRING, null, null, null, null);
        assertEquals(ReturnCodes.SUCCESS, result.get(OutputNames.RETURN_CODE));
    }

    @Test
    public void testValidateJsonAgainstSchemaSuccess() {
        String jsonObject = "{\n" +
                "  \"first_name\": \"George\",\n" +
                "  \"last_name\": \"Washington\",\n" +
                "  \"birthday\": \"22-02-1732\",\n" +
                "  \"address\": {\n" +
                "    \"street_address\": \"3200 Mount Vernon Memorial Highway\",\n" +
                "    \"city\": \"Mount Vernon\",\n" +
                "    \"state\": \"Virginia\",\n" +
                "    \"country\": \"United States\"\n" +
                "  }\n" +
                "}";

        String jsonSchemaObject = "{\n" +
                "  \"type\": \"object\",\n" +
                "  \"properties\": {\n" +
                "    \"first_name\": { \"type\": \"string\" },\n" +
                "    \"last_name\": { \"type\": \"string\" },\n" +
                "    \"birthday\": { \"type\": \"string\" },\n" +
                "    \"address\": {\n" +
                "      \"type\": \"object\",\n" +
                "      \"properties\": {\n" +
                "        \"street_address\": { \"type\": \"string\" },\n" +
                "        \"city\": { \"type\": \"string\" },\n" +
                "        \"state\": { \"type\": \"string\" },\n" +
                "        \"country\": { \"type\" : \"string\" }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";

        final Map<String, String> result = validateJson.execute(jsonObject, jsonSchemaObject, null, null, null, null);
        assertEquals(VALID_JSON_AGAINST_SCHEMA, result.get(OutputNames.RETURN_RESULT));
        assertEquals(ReturnCodes.SUCCESS, result.get(OutputNames.RETURN_CODE));
        assertNull(result.get(OutputNames.EXCEPTION));
    }

    @Test
    public void testValidateJsonAgainstSchemaFailure() {
        String jsonObject = "{\n" +
                "  \"first_name\": 1,\n" +
                "  \"last_name\": \"Washington\",\n" +
                "  \"birthday\": \"22-02-1732\",\n" +
                "  \"address\": {\n" +
                "    \"street_address\": \"3200 Mount Vernon Memorial Highway\",\n" +
                "    \"city\": \"Mount Vernon\",\n" +
                "    \"state\": \"Virginia\",\n" +
                "    \"country\": \"United States\"\n" +
                "  }\n" +
                "}";

        String jsonSchemaObject = "{\n" +
                "  \"type\": \"object\",\n" +
                "  \"properties\": {\n" +
                "    \"first_name\": { \"type\": \"string\" },\n" +
                "    \"last_name\": { \"type\": \"string\" },\n" +
                "    \"birthday\": { \"type\": \"string\" },\n" +
                "    \"address\": {\n" +
                "      \"type\": \"object\",\n" +
                "      \"properties\": {\n" +
                "        \"street_address\": { \"type\": \"string\" },\n" +
                "        \"city\": { \"type\": \"string\" },\n" +
                "        \"state\": { \"type\": \"string\" },\n" +
                "        \"country\": { \"type\" : \"string\" }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";

        final Map<String, String> result = validateJson.execute(jsonObject, jsonSchemaObject, null, null, null, null);
        assertEquals(INVALID_JSON_AGAINST_SCHEMA, result.get(OutputNames.RETURN_RESULT));
        assertEquals(ReturnCodes.FAILURE, result.get(OutputNames.RETURN_CODE));
    }

    @Test
    public void testValidateJsonWithSchemaFromFile() {
        String jsonObject = "{\n" +
                "  \"first_name\": \"George\",\n" +
                "  \"last_name\": \"Washington\",\n" +
                "  \"birthday\": \"22-02-1732\",\n" +
                "  \"address\": {\n" +
                "    \"street_address\": \"3200 Mount Vernon Memorial Highway\",\n" +
                "    \"city\": \"Mount Vernon\",\n" +
                "    \"state\": \"Virginia\",\n" +
                "    \"country\": \"United States\"\n" +
                "  }\n" +
                "}";

        String jsonSchemaFilePath = System.getProperty("user.dir") + "\\src\\test\\java\\resources\\test_valid_json_schema.json";
        final Map<String, String> result = validateJson.execute(jsonObject, jsonSchemaFilePath, null, null, null, null);
        assertEquals(VALID_JSON_AGAINST_SCHEMA, result.get(OutputNames.RETURN_RESULT));
        assertEquals(ReturnCodes.SUCCESS, result.get(OutputNames.RETURN_CODE));
        assertNull(result.get(OutputNames.EXCEPTION));
    }

    @Test
    public void testInvalidSchemaFromFile() {
        String jsonObject = "{\n" +
                "  \"first_name\": \"George\",\n" +
                "  \"last_name\": \"Washington\",\n" +
                "  \"birthday\": \"22-02-1732\",\n" +
                "  \"address\": {\n" +
                "    \"street_address\": \"3200 Mount Vernon Memorial Highway\",\n" +
                "    \"city\": \"Mount Vernon\",\n" +
                "    \"state\": \"Virginia\",\n" +
                "    \"country\": \"United States\"\n" +
                "  }\n" +
                "}";

        String jsonSchemaFilePath = System.getProperty("user.dir") + "\\src\\test\\java\\resources\\test_invalid_json_schema.json";
        final Map<String, String> result = validateJson.execute(jsonObject, jsonSchemaFilePath, null, null, null, null);
        assertEquals(ReturnCodes.FAILURE, result.get(OutputNames.RETURN_CODE));
        assertTrue(result.get(OutputNames.EXCEPTION).contains("invalid JSON Schema"));
        assertEquals(result.get(OutputNames.RETURN_RESULT), result.get(OutputNames.EXCEPTION));
    }

}
