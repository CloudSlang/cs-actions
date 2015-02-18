package org.openscore.content.json.actions;

import org.junit.Test;

import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by vranau
 * Date 2/17/2015.
 */
public class EditJsonTest {

    private static final String RETURN_RESULT = "returnResult";
    private EditJson editJson = new EditJson();

    @Test
    public void testInvalidAction() throws Exception {
        final String jsonPathQuery = "$.store.bicycle";
        final Map<String, String> result = editJson.execute(jsonFile, jsonPathQuery, "get1", "", "");
        assertEquals("Invalid action provided! Action should be one of the values: get insert add update delete ", result.get(RETURN_RESULT));
    }

    @Test
    public void testNullAction() throws Exception {
        final String jsonPathQuery = "$.store.bicycle";
        final Map<String, String> result = editJson.execute(jsonFile, jsonPathQuery, null, "", "");
        assertEquals("Empty action provided!", result.get(RETURN_RESULT));
    }

    @Test
    public void testEmptyAction() throws Exception {
        final String jsonPathQuery = "$.store.bicycle";
        final Map<String, String> result = editJson.execute(jsonFile, jsonPathQuery, "  ", "", "");
        assertEquals("Empty action provided!", result.get(RETURN_RESULT));
    }

    @Test
    public void testGetActionJson() throws Exception {
        final String jsonPathQuery = "$.store.bicycle";
        final Map<String, String> result = editJson.execute(jsonFile, jsonPathQuery, "get", "", "");
        assertEquals("{\"color\":\"red\",\"price\":19.95}", result.get(RETURN_RESULT));
    }

    @Test
    public void testGetActionArray() throws Exception {
        final String jsonPathQuery = "$.arrayTest";
        final Map<String, String> result = editJson.execute(jsonFile, jsonPathQuery, "get", "", "");
        assertEquals("[1,2,3]", result.get(RETURN_RESULT));
    }

    @Test
    public void testGetActionValue() throws Exception {
        final String jsonPathQuery = "$.store.book[0].category";
        final Map<String, String> result = editJson.execute(jsonFile, jsonPathQuery, "get", "", "");
        assertEquals("\"reference\"", result.get(RETURN_RESULT));
    }

    @Test
    public void testGetActionNullValues() throws Exception {
        final String jsonPathQuery = "$.store.book[0].category";
        final Map<String, String> result = editJson.execute(jsonFile, jsonPathQuery, "get", null, null);
        assertEquals("\"reference\"", result.get(RETURN_RESULT));
    }

    @Test
    public void testGetActionMultipleValues() throws Exception {
        final String jsonPathQuery = "$.store.book[*].author";
        final Map<String, String> result = editJson.execute(jsonFile, jsonPathQuery, "get", "", "");
        assertEquals("[\"Nigel Rees\",\"Evelyn Waugh\"]", result.get(RETURN_RESULT));
    }

    @Test
    public void testUpdateActionJsonWithNull() throws Exception {
        final String jsonPathQuery = "$.store.bicycle";
        final Map<String, String> result = editJson.execute(jsonFile, jsonPathQuery, "update", "", null);
        assertEquals("Null value provided for update action!", result.get(RETURN_RESULT));
    }

    @Test
    public void testUpdateActionJsonWithEmptyValue() throws Exception {
        final String jsonPathQuery = "$.store.bicycle";
        final Map<String, String> result = editJson.execute(jsonFile, jsonPathQuery, "update", "", "");
        assertEquals("{\"store\":{\"book\":[{\"category\":\"reference\",\"author\":\"Nigel Rees\",\"title\":" +
                "\"Sayings of the Century\",\"price\":8.95},{\"category\":\"fiction\",\"author\":\"Evelyn Waugh\"," +
                "\"title\":\"Sword of Honour\",\"price\":12.99}],\"bicycle\":\"\"},\"arrayTest\":[1,2,3]}", result.get(RETURN_RESULT));
    }

    @Test
    public void testUpdateActionValue() throws Exception {
        final String jsonPathQuery = "$.store.book[0].category";
        final Map<String, String> result = editJson.execute(jsonFile, jsonPathQuery, "update", "", "newCategory");
        assertEquals("{\"store\":{\"book\":[{\"category\":\"newCategory\",\"author\":\"Nigel Rees\",\"title\":" +
                "\"Sayings of the Century\",\"price\":8.95},{\"category\":\"fiction\",\"author\":\"Evelyn Waugh\"," +
                "\"title\":\"Sword of Honour\",\"price\":12.99}],\"bicycle\":{\"color\":\"red\",\"price\":19.95}}," +
                "\"arrayTest\":[1,2,3]}", result.get(RETURN_RESULT));
    }

    @Test
    public void testUpdateActionSpacedValue() throws Exception {
        final String jsonPathQuery = "$.arrayTest";
        final Map<String, String> result = editJson.execute(jsonFile, jsonPathQuery, "update", "new Author", "new Author value");
        assertTrue(result.get(RETURN_RESULT).startsWith("com.google.gson.stream.MalformedJsonException"));
    }

    @Test
    public void testUpdateActionSpacedValueInQuotes() throws Exception {
        final String jsonPathQuery = "$.arrayTest";
        final Map<String, String> result = editJson.execute(jsonFile, jsonPathQuery, "update", "new Author", "\"new Author value\"");
        assertEquals("{\"store\":{\"book\":[{\"category\":\"reference\",\"author\":\"Nigel Rees\",\"title\":" +
                "\"Sayings of the Century\",\"price\":8.95},{\"category\":\"fiction\",\"author\":\"Evelyn Waugh\"," +
                "\"title\":\"Sword of Honour\",\"price\":12.99}],\"bicycle\":{\"color\":\"red\",\"price\":19.95}}," +
                "\"arrayTest\":\"new Author value\"}", result.get(RETURN_RESULT));
    }

    @Test
    public void testUpdateActionMultipleValues() throws Exception {
        final String jsonPathQuery = "$.store.book[*].author";
        final Map<String, String> result = editJson.execute(jsonFile, jsonPathQuery, "update", "", "newAuthor");
        assertEquals("{\"store\":{\"book\":[{\"category\":\"reference\",\"author\":\"newAuthor\",\"title\":" +
                "\"Sayings of the Century\",\"price\":8.95},{\"category\":\"fiction\",\"author\":\"newAuthor\"," +
                "\"title\":\"Sword of Honour\",\"price\":12.99}],\"bicycle\":{\"color\":\"red\",\"price\":19.95}}," +
                "\"arrayTest\":[1,2,3]}", result.get(RETURN_RESULT));
    }

    @Test
    public void testInsertActionJsonNullValueAndName() throws Exception {
        final String jsonPathQuery = "$.store.bicycle";
        final Map<String, String> result = editJson.execute(jsonFile, jsonPathQuery, "insert", null, null);
        assertEquals("Null value provided for insert action!", result.get(RETURN_RESULT));
    }

    @Test
    public void testInsertActionJsonNullName() throws Exception {
        final String jsonPathQuery = "$.store.bicycle";
        final Map<String, String> result = editJson.execute(jsonFile, jsonPathQuery, "insert", null, "");
        assertEquals("Empty name provided for insert action!", result.get(RETURN_RESULT));
    }

    @Test
    public void testInsertActionJsonEmptyValue() throws Exception {
        final String jsonPathQuery = "$.store.bicycle";
        final Map<String, String> result = editJson.execute(jsonFile, jsonPathQuery, "insert", "newName", "");
        assertEquals("{\"store\":{\"book\":[{\"category\":\"reference\",\"author\":\"Nigel Rees\",\"title\":" +
                "\"Sayings of the Century\",\"price\":8.95},{\"category\":\"fiction\",\"author\":\"Evelyn Waugh\"," +
                "\"title\":\"Sword of Honour\",\"price\":12.99}],\"bicycle\":{\"color\":\"red\",\"price\":19.95," +
                "\"newName\":\"\"}},\"arrayTest\":[1,2,3]}", result.get(RETURN_RESULT));
    }

    @Test
    public void testInsertActionArray() throws Exception {
        final String jsonPathQuery = "$.arrayTest[0]";
        final Map<String, String> result = editJson.execute(jsonFile, jsonPathQuery, "insert", "newName", "1");
        assertEquals("Can only add properties to a map", result.get(RETURN_RESULT));
    }

    @Test
    public void testInsertActionArrayAsNewItem() throws Exception {
        final String jsonPathQuery = "$.store.book[0]";
        final Map<String, String> result = editJson.execute(jsonFile, jsonPathQuery, "insert", "newArray", "[1,2,3]");
        assertEquals("{\"store\":{\"book\":[{\"category\":\"reference\",\"author\":\"Nigel Rees\",\"title\":" +
                "\"Sayings of the Century\",\"price\":8.95,\"newArray\":[1,2,3]},{\"category\":\"fiction\"," +
                "\"author\":\"Evelyn Waugh\",\"title\":\"Sword of Honour\",\"price\":12.99}],\"bicycle\":{\"color\":\"red\"," +
                "\"price\":19.95}},\"arrayTest\":[1,2,3]}", result.get(RETURN_RESULT));
    }

    @Test
    public void testInsertActionNewJsonObject() throws Exception {
        final String jsonPathQuery = "$.store";
        String newBook = "{" +
                "            \"category\":\"fiction\",\n" +
                "            \"author\":\"test1\",\n" +
                "            \"title\":\"title1\",\n" +
                "            \"price\":13\n" +
                "         }";
        final Map<String, String> result = editJson.execute(jsonFile, jsonPathQuery, "insert", "newCar", newBook);
        assertEquals("{\"store\":{\"book\":[{\"category\":\"reference\",\"author\":\"Nigel Rees\",\"title\":" +
                "\"Sayings of the Century\",\"price\":8.95},{\"category\":\"fiction\",\"author\":\"Evelyn Waugh\"," +
                "\"title\":\"Sword of Honour\",\"price\":12.99}],\"bicycle\":{\"color\":\"red\",\"price\":19.95}," +
                "\"newCar\":{\"category\":\"fiction\",\"author\":\"test1\",\"title\":\"title1\",\"price\":13}}," +
                "\"arrayTest\":[1,2,3]}", result.get(RETURN_RESULT));
    }

    @Test
    public void testInsertActionMultipleValues() throws Exception {
        final String jsonPathQuery = "$.store.book[*].author";
        final Map<String, String> result = editJson.execute(jsonFile, jsonPathQuery, "insert", "newAuthor", "newAuthor_value");
        assertEquals("Can only add properties to a map", result.get(RETURN_RESULT));
    }

    @Test
    public void testInsertActionSpacedValue() throws Exception {
        final String jsonPathQuery = "$.store";
        final Map<String, String> result = editJson.execute(jsonFile, jsonPathQuery, "insert", "new Author", "new Author value");
        assertTrue(result.get(RETURN_RESULT).startsWith("com.google.gson.stream.MalformedJsonException"));
    }

    @Test
    public void testInsertActionSpacedValueInQuotes() throws Exception {
        final String jsonPathQuery = "$.store";
        final Map<String, String> result = editJson.execute(jsonFile, jsonPathQuery, "insert", "new Author", "\"new Author value\"");
        assertEquals("{\"store\":{\"book\":[{\"category\":\"reference\",\"author\":\"Nigel Rees\",\"title\":" +
                "\"Sayings of the Century\",\"price\":8.95},{\"category\":\"fiction\",\"author\":\"Evelyn Waugh\"," +
                "\"title\":\"Sword of Honour\",\"price\":12.99}],\"bicycle\":{\"color\":\"red\",\"price\":19.95}," +
                "\"new Author\":\"new Author value\"},\"arrayTest\":[1,2,3]}", result.get(RETURN_RESULT));
    }

    @Test
    public void testDeleteActionJson() throws Exception {
        final String jsonPathQuery = "$.store.bicycle";
        final Map<String, String> result = editJson.execute(jsonFile, jsonPathQuery, "delete", "", "");
        assertEquals("{\"store\":{\"book\":[{\"category\":\"reference\",\"author\":\"Nigel Rees\",\"title\":" +
                "\"Sayings of the Century\",\"price\":8.95},{\"category\":\"fiction\",\"author\":\"Evelyn Waugh\"," +
                "\"title\":\"Sword of Honour\",\"price\":12.99}]},\"arrayTest\":[1,2,3]}", result.get(RETURN_RESULT));
    }

    @Test
    public void testDeleteActionArray() throws Exception {
        final String jsonPathQuery = "$.arrayTest";
        final Map<String, String> result = editJson.execute(jsonFile, jsonPathQuery, "delete", "", "");
        assertEquals("{\"store\":{\"book\":[{\"category\":\"reference\",\"author\":\"Nigel Rees\",\"title\":" +
                        "\"Sayings of the Century\",\"price\":8.95},{\"category\":\"fiction\",\"author\":\"Evelyn Waugh\"," +
                        "\"title\":\"Sword of Honour\",\"price\":12.99}],\"bicycle\":{\"color\":\"red\",\"price\":19.95}}}",
                result.get(RETURN_RESULT));
    }

    @Test
    public void testDeleteActionValue() throws Exception {
        final String jsonPathQuery = "$.store.book[0].category";
        final Map<String, String> result = editJson.execute(jsonFile, jsonPathQuery, "delete", "", "");
        assertEquals("{\"store\":{\"book\":[{\"author\":\"Nigel Rees\",\"title\":\"Sayings of the Century\"," +
                "\"price\":8.95},{\"category\":\"fiction\",\"author\":\"Evelyn Waugh\",\"title\":\"Sword of Honour\"," +
                "\"price\":12.99}],\"bicycle\":{\"color\":\"red\",\"price\":19.95}},\"arrayTest\":[1,2,3]}", result.get(RETURN_RESULT));
    }

    @Test
    public void testDeleteActionNullValue() throws Exception {
        final String jsonPathQuery = "$.store.book[0].category";
        final Map<String, String> result = editJson.execute(jsonFile, jsonPathQuery, "delete", null, null);
        assertEquals("{\"store\":{\"book\":[{\"author\":\"Nigel Rees\",\"title\":\"Sayings of the Century\"," +
                "\"price\":8.95},{\"category\":\"fiction\",\"author\":\"Evelyn Waugh\",\"title\":\"Sword of Honour\"," +
                "\"price\":12.99}],\"bicycle\":{\"color\":\"red\",\"price\":19.95}},\"arrayTest\":[1,2,3]}", result.get(RETURN_RESULT));
    }

        @Test
    public void testDeleteActionMultipleValues() throws Exception {
        final String jsonPathQuery = "$.store.book[*].author";
        final Map<String, String> result = editJson.execute(jsonFile, jsonPathQuery, "delete", "", "");
        assertEquals("{\"store\":{\"book\":[{\"category\":\"reference\",\"title\":\"Sayings of the Century\"," +
                "\"price\":8.95},{\"category\":\"fiction\",\"title\":\"Sword of Honour\",\"price\":12.99}],\"bicycle\":" +
                "{\"color\":\"red\",\"price\":19.95}},\"arrayTest\":[1,2,3]}", result.get(RETURN_RESULT));
    }

    @Test
    public void testAddActionEmpty() throws Exception {
        final String jsonPathQuery = "$.arrayTest";
        final Map<String, String> result = editJson.execute(jsonFile, jsonPathQuery, "add", "", "");
        assertEquals("{\"store\":{\"book\":[{\"category\":\"reference\",\"author\":\"Nigel Rees\",\"title\":" +
                "\"Sayings of the Century\",\"price\":8.95},{\"category\":\"fiction\",\"author\":\"Evelyn Waugh\"," +
                "\"title\":\"Sword of Honour\",\"price\":12.99}],\"bicycle\":{\"color\":\"red\",\"price\":19.95}}," +
                "\"arrayTest\":[1,2,3,\"\"]}", result.get(RETURN_RESULT));
    }

    @Test
    public void testAddActionJsonNull() throws Exception {
        final String jsonPathQuery = "$.arrayTest";
        final Map<String, String> result = editJson.execute(jsonFile, jsonPathQuery, "add", "", null);
        assertEquals("Null value provided for add action!", result.get(RETURN_RESULT));
    }

    @Test
    public void testAddActionJson() throws Exception {
        final String jsonPathQuery = "$.store.bicycle";
        final Map<String, String> result = editJson.execute(jsonFile, jsonPathQuery, "add", "", "");
        assertEquals("Can only add to an array", result.get(RETURN_RESULT));
    }

    @Test
    public void testAddActionArray() throws Exception {
        final String jsonPathQuery = "$.arrayTest";
        final Map<String, String> result = editJson.execute(jsonFile, jsonPathQuery, "add", "", "value");
        assertEquals("{\"store\":{\"book\":[{\"category\":\"reference\",\"author\":\"Nigel Rees\",\"title\":" +
                "\"Sayings of the Century\",\"price\":8.95},{\"category\":\"fiction\",\"author\":\"Evelyn Waugh\"," +
                "\"title\":\"Sword of Honour\",\"price\":12.99}],\"bicycle\":{\"color\":\"red\",\"price\":19.95}}," +
                "\"arrayTest\":[1,2,3,\"value\"]}", result.get(RETURN_RESULT));
    }

    @Test
    public void testAddActionValue() throws Exception {
        final String jsonPathQuery = "$.store.book[0].category";
        final Map<String, String> result = editJson.execute(jsonFile, jsonPathQuery, "add", "", "value");
        assertEquals("Can only add to an array", result.get(RETURN_RESULT));
    }

    @Test
    public void testAddActionMultipleValues() throws Exception {
        final String jsonPathQuery = "$.store.book[*].author";
        final Map<String, String> result = editJson.execute(jsonFile, jsonPathQuery, "add", "", "");
        assertEquals("Can only add to an array", result.get(RETURN_RESULT));
    }

    @Test
    public void testAddActionJsonValue() throws Exception {
        final String jsonPathQuery = "$.store.book";
        String newBook = "{" +
                "            \"category\":\"fiction\",\n" +
                "            \"author\":\"test1\",\n" +
                "            \"title\":\"title1\",\n" +
                "            \"price\":13\n" +
                "         }";
        final Map<String, String> result = editJson.execute(jsonFile, jsonPathQuery, "add", "", newBook);
        assertEquals("{\"store\":{\"book\":[{\"category\":\"reference\",\"author\":\"Nigel Rees\",\"title\":" +
                "\"Sayings of the Century\",\"price\":8.95},{\"category\":\"fiction\",\"author\":\"Evelyn Waugh\"," +
                "\"title\":\"Sword of Honour\",\"price\":12.99},{\"category\":\"fiction\",\"author\":\"test1\"," +
                "\"title\":\"title1\",\"price\":13}],\"bicycle\":{\"color\":\"red\",\"price\":19.95}}," +
                "\"arrayTest\":[1,2,3]}", result.get(RETURN_RESULT));
    }


    private static String jsonFile =
            "{" +
                    "    \n\"store\": {\n" +
                    "        \"book\": [\n" +
                    "            {\n" +
                    "                \"category\": \"reference\",\n" +
                    "                \"author\": \"Nigel Rees\",\n" +
                    "                \"title\": \"Sayings of the Century\",\n" +
                    "                \"price\": 8.95\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"category\": \"fiction\",\n" +
                    "                \"author\": \"Evelyn Waugh\",\n" +
                    "                \"title\": \"Sword of Honour\",\n" +
                    "                \"price\": 12.99\n" +
                    "            }" +
                    "        ],\n" +
                    "        \"bicycle\": {\n" +
                    "            \"color\": \"red\",\n" +
                    "            \"price\": 19.95\n" +
                    "        }\n" +
                    "    },\n" +
                    "    \"arrayTest\":[1,2,3] \n" +
                    "}";
}
