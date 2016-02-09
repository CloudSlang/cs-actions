package io.cloudslang.content.json.services;

import org.junit.After;
import org.junit.Before;

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
}
