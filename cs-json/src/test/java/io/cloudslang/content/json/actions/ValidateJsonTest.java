package io.cloudslang.content.json.actions;

import io.cloudslang.content.constants.OtherValues;
import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.json.utils.ValidationUtils;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import static io.cloudslang.content.json.utils.Constants.ValidationMessages.*;
import static org.junit.Assert.*;

/**
 * Created by Daniel Manciu
 * Date 24/7/2018
 */
public class ValidateJsonTest {

    private static ValidateJson validateJson = new ValidateJson();

    URI resource1;
    String validJson;
    URI resource2;
    String invalidJson;

    @Before
    public void setUp() throws Exception{
        resource1 = getClass().getResource("/test_valid_json_schema.json").toURI();
        validJson = FileUtils.readFileToString(new File(resource1));
        resource2 = getClass().getResource("/test_invalid_json_schema.json").toURI();
        invalidJson = FileUtils.readFileToString(new File(resource2));
    }

    @After
    public void tearDown(){
        resource1 = null;
        resource2 = null;
        validJson = null;
        invalidJson = null;
    }


    public ValidateJsonTest() throws URISyntaxException, IOException {
    }

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

        final Map<String, String> result = validateJson.execute(jsonObject, validJson, null, null, null, null);
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

        final Map<String, String> result = validateJson.execute(jsonObject, invalidJson, null, null, null, null);
        assertEquals(ReturnCodes.FAILURE, result.get(OutputNames.RETURN_CODE));
        assertTrue(result.get(OutputNames.EXCEPTION).contains("invalid JSON Schema"));
        assertEquals(result.get(OutputNames.RETURN_RESULT), result.get(OutputNames.EXCEPTION));
    }

}
