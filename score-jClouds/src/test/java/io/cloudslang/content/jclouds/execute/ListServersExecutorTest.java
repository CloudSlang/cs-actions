package io.cloudslang.content.jclouds.execute;

import io.cloudslang.content.jclouds.entities.inputs.ListServersInputs;
import io.cloudslang.content.jclouds.entities.outputs.Outputs;
import io.cloudslang.content.jclouds.factory.ComputeFactory;
import io.cloudslang.content.jclouds.services.ComputeService;
import org.jclouds.rest.ResourceNotFoundException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by persdana on 7/7/2015.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ListServersExecutor.class, ComputeFactory.class})
public class ListServersExecutorTest {
    private static final String DELIMITER = ";;";
    private static final String DEFAULT_DELIMITER = ";\n";
    private static final String INVALID_SERVER_ID_EXCEPTION_MESSAGE = "{\"itemNotFound\": {\"message\": \"Instance not found\", \"code\": 404}}";
    private static final String REGION = "us-east-1";

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Spy
    private ListServersExecutor toTest = new ListServersExecutor();

    @Mock
    ComputeService computeServiceMock;

    private ListServersInputs getInputsForAmazon() {
        ListServersInputs listServersInputs = new ListServersInputs();
        listServersInputs.setProvider("ec2");
        listServersInputs.setEndpoint("https://ec2.amazonaws.com");
        listServersInputs.setIdentity("AKIAIQHVQ4UM7SOXXXXXX");
        listServersInputs.setCredential("R1ZRPK4HPXU6cyBi1XY/IkYqQ+qXXXXXXX");
        listServersInputs.setRegion(REGION);
        listServersInputs.setProxyHost("proxy.abcde.com");
        listServersInputs.setProxyPort("8080");
        listServersInputs.setDelimiter(DELIMITER);
        return listServersInputs;
    }

    /**
     * Tests the execute method. Positive scenario.
     * @throws Exception
     */
    @Test
    public void testExecute() throws Exception {
        ListServersInputs intputs = getInputsForAmazon();
        doNothing().when(toTest).processInputs(intputs);
        toTest.region = REGION;
        toTest.delimiter = DELIMITER;
        PowerMockito.mockStatic(ComputeFactory.class);
        PowerMockito.doReturn(computeServiceMock).when(ComputeFactory.class, "getComputeService", intputs);
        Set<String> nodes = new HashSet<>();
        nodes.add("nod1");
        nodes.add("nod2");
        Mockito.doReturn(nodes).when(computeServiceMock).listNodes(REGION);

        Map<String, String> result = toTest.execute(intputs);

        Assert.assertEquals("0", result.get(Outputs.RETURN_CODE));
        Assert.assertEquals("nod2" + DELIMITER + "nod1", result.get(Outputs.RETURN_RESULT));
        verify(toTest).processInputs(intputs);
        verify(computeServiceMock).listNodes(REGION);
        verifyNoMoreInteractions(computeServiceMock);
    }

    @Test
    public void testExecuteNegativeScenario() throws Exception {
        exception.expect(ResourceNotFoundException.class);
        exception.expectMessage(INVALID_SERVER_ID_EXCEPTION_MESSAGE);

        ListServersInputs inputs = getInputsForAmazon();
        doNothing().when(toTest).processInputs(inputs);
        toTest.region = REGION;
        toTest.delimiter = DELIMITER;
        PowerMockito.mockStatic(ComputeFactory.class);
        PowerMockito.doReturn(computeServiceMock).when(ComputeFactory.class, "getComputeService", inputs);
        ResourceNotFoundException toThrow = new ResourceNotFoundException(INVALID_SERVER_ID_EXCEPTION_MESSAGE);
        Mockito.doThrow(toThrow).when(computeServiceMock).listNodes(REGION);

        toTest.execute(inputs);
    }

    /**
     * Test processInputs method. Positive scenario.
     * @throws Exception
     */
    @Test
    public void testProcessInputs() throws Exception {
        ListServersInputs inputs = getInputsForAmazon();
        toTest.processInputs(inputs);

        assertEquals(REGION, toTest.region);
        assertEquals(DELIMITER, toTest.delimiter);
    }

    /**
     * Test process inputs with null server id.
     * @throws Exception
     */
    @Test
    public void testProcessInputsWithNullServerId() throws Exception {
        ListServersInputs inputs = getInputsForAmazon();
        inputs.setDelimiter(null);
        toTest.processInputs(inputs);
        assertEquals(DEFAULT_DELIMITER, toTest.delimiter);
    }
}
