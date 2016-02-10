package io.cloudslang.content.json.services;


import io.cloudslang.content.json.exceptions.RemoveEmptyElementException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.assertEquals;

/**
 * Created by nane on 2/9/2016.
 */
public class JsonServiceTest {

    private JsonService jsonServiceUnderTest;
    private String jsonStringInput;
    private String expectedJsonStringOutput;
    private String actualJsonStringOutput;

    @Before
    public void setUp(){
        jsonServiceUnderTest =  new JsonService();
    }

    @After
    public void tearDown(){
        jsonServiceUnderTest =  null;
        jsonStringInput = null;
        expectedJsonStringOutput = null;
        actualJsonStringOutput = null;
    }


    @Test
    public void givenValidJsonStringWithDoubleQuotesThenSuccessfullyRemoveEmpty() throws RemoveEmptyElementException {
        jsonStringInput = "{\"removed1\":\"\", \"removed2\":[], \"removed3\":null, \"expected\":\"value\"} ";
        expectedJsonStringOutput ="{\"expected\":\"value\"}";
        actualJsonStringOutput = jsonServiceUnderTest.removeEmptyElementsJson(jsonStringInput);

        assertEquals(expectedJsonStringOutput,actualJsonStringOutput);
    }

    @Test
     public void givenValidJsonStringWithNewLineAndDoubleQuotesThenSuccessfullyRemoveEmpty() throws RemoveEmptyElementException {
        jsonStringInput = "{\"removed1\":\"\", \"removed2\":[], \n" +
                " \"removed3\":null, \"expected\":\"value\"} \n";
        expectedJsonStringOutput ="{\"expected\":\"value\"}";
        actualJsonStringOutput = jsonServiceUnderTest.removeEmptyElementsJson(jsonStringInput);

        assertEquals(expectedJsonStringOutput,actualJsonStringOutput);
    }

    @Test
    public void givenValidJsonStringWithEmptySpaceAndDoubleQuotesThenSuccessfullyRemoveEmpty() throws RemoveEmptyElementException {
        jsonStringInput = "{   \"removed1\":\"\", \"removed2\":[],  \"removed3\":null, \"expected\":\"value\"} ";
        expectedJsonStringOutput ="{\"expected\":\"value\"}";
        actualJsonStringOutput = jsonServiceUnderTest.removeEmptyElementsJson(jsonStringInput);

        assertEquals(expectedJsonStringOutput,actualJsonStringOutput);
    }

    @Test
    public void givenValidJsonStringWithSingleQuotesThenSuccessfullyRemoveEmpty() throws RemoveEmptyElementException {
        jsonStringInput = "{'removed1':'', 'removed2':[], 'removed3':null, 'expected':'value'} ";
        expectedJsonStringOutput ="{'expected':'value'}";
        actualJsonStringOutput = jsonServiceUnderTest.removeEmptyElementsJson(jsonStringInput);

        assertEquals(expectedJsonStringOutput,actualJsonStringOutput);
    }

    @Test
    public void givenValidJsonStringWithNewLineAndSingleQuotesThenSuccessfullyRemoveEmpty() throws RemoveEmptyElementException {
        jsonStringInput = "{'removed1':'', 'removed2':[], 'removed3':null, 'expected':'value'} ";
        expectedJsonStringOutput ="{'expected':'value'}";
        actualJsonStringOutput = jsonServiceUnderTest.removeEmptyElementsJson(jsonStringInput);

        assertEquals(expectedJsonStringOutput,actualJsonStringOutput);
    }

    @Test
    public void givenValidJsonStringWithEmptySpaceAndSingleQuotesThenSuccessfullyRemoveEmpty() throws RemoveEmptyElementException {
        jsonStringInput = "{    'removed1':'', 'removed2':[],  'removed3':null, 'expected':'value'} ";
        expectedJsonStringOutput ="{'expected':'value'}";
        actualJsonStringOutput = jsonServiceUnderTest.removeEmptyElementsJson(jsonStringInput);

        assertEquals(expectedJsonStringOutput,actualJsonStringOutput);
    }

    @Test
    public void givenValidComplexJsonStringDoubleQuotesThenSuccessfullyRemoveEmpty() throws RemoveEmptyElementException {
        jsonStringInput = "{ \n" +
                " \"expected1\": \"value\", \n" +
                " \"expected2\": 1, \n" +
                " \"remove1\": null, \n" +
                " \"remove2\": [], \n" +
                " \"remove3\": \"\", \n" +
                " \"expected\": { \n" +
                "  \"public\": [{ \n" +
                "   \"expected1\": \"value\", \n" +
                "   \"expected2\": 1, \n" +
                "   \"remove1\": null, \n" +
                "   \"remove2\": [], \n" +
                "   \"remove3\": \"\" \n" +
                "  }] \n" +
                " }, \n" +
                " \"links\": [{ \n" +
                "  \"expected1\": \"http://test.com\", \n" +
                "  \"expected2\": 1, \n" +
                "  \"remove1\": null, \n" +
                "  \"remove2\": [], \n" +
                "  \"remove3\": \"\" \n" +
                " }] \n" +
                "} \n";
        expectedJsonStringOutput ="{\"expected1\":\"value\",\"expected2\":1,\"expected\":{\"public\":[{\"expected1\":\"value\",\"expected2\":1}]},\"links\":[{\"expected1\":\"http://test.com\",\"expected2\":1}]}";
        actualJsonStringOutput = jsonServiceUnderTest.removeEmptyElementsJson(jsonStringInput);


        assertEquals(expectedJsonStringOutput, actualJsonStringOutput);
    }

    @Test
    public void givenValidComplexJsonStringSingleQuotesThenSuccessfullyRemoveEmpty() throws RemoveEmptyElementException {
        jsonStringInput = "{ \n" +
                " 'expected1': 'value', \n" +
                " 'expected2': 1, \n" +
                " 'remove1': null, \n" +
                " 'remove2': [], \n" +
                " 'remove3': '', \n" +
                " 'expected': { \n" +
                "  'public': [{ \n" +
                "   'expected1': 'value', \n" +
                "   'expected2': 1, \n" +
                "   'remove1': null, \n" +
                "   'remove2': [], \n" +
                "   'remove3': '' \n" +
                "  }] \n" +
                " }, \n" +
                " 'links': [{ \n" +
                "  'expected1': 'http://test.com', \n" +
                "  'expected2': 1, \n" +
                "  'remove1': null, \n" +
                "  'remove2': [], \n" +
                "  'remove3': '' \n" +
                " }] \n" +
                "} \n";
        expectedJsonStringOutput ="{'expected1':'value','expected2':1,'expected':{'public':[{'expected1':'value','expected2':1}]},'links':[{'expected1':'http://test.com','expected2':1}]}";
        actualJsonStringOutput = jsonServiceUnderTest.removeEmptyElementsJson(jsonStringInput);

        assertEquals(expectedJsonStringOutput,actualJsonStringOutput);
    }


    @Test
    public void givenURLJsonValueAndDoubleQuoteThenSuccessfullyRemoveEmpty() throws RemoveEmptyElementException {
        jsonStringInput = "{\"removed1\":\"\", \"removed2\":[], \"removed3\":null, \"expected\":\"http://test.com\"} ";
        expectedJsonStringOutput ="{\"expected\":\"http://test.com\"}";
        actualJsonStringOutput = jsonServiceUnderTest.removeEmptyElementsJson(jsonStringInput);

        assertEquals(expectedJsonStringOutput,actualJsonStringOutput);
    }



}
