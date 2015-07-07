package io.cloudslang.content.jclouds.execute;

import io.cloudslang.content.jclouds.entities.inputs.ServerIdentificationInputs;
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

import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by persdana on 7/7/2015.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ResumeServerExecutor.class, ComputeFactory.class})
public class ResumeServerExecutorTest {
    public static final String SERVER_ID_NOT_SPECIFIED = "The required serverId input is not specified!";
    private static final String INVALID_SERVER_ID_EXCEPTION_MESSAGE = "{\"itemNotFound\": {\"message\": \"Instance not found\", \"code\": 404}}";
    private static final String REGION = "us-east-1";
    private static final String SERVER_ID = "i-578dde87";

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Spy
    private ResumeServerExecutor toTest = new ResumeServerExecutor();

    @Mock
    ComputeService computeServiceMock;

    private ServerIdentificationInputs getInputsForAmazon() {
        ServerIdentificationInputs serverIdentificationInputs = new ServerIdentificationInputs();
        serverIdentificationInputs.setProvider("ec2");
        serverIdentificationInputs.setEndpoint("https://ec2.amazonaws.com");
        serverIdentificationInputs.setIdentity("AKIAIQHVQ4UM7SOXXXXXX");
        serverIdentificationInputs.setCredential("R1ZRPK4HPXU6cyBi1XY/IkYqQ+qXXXXXXX");
        serverIdentificationInputs.setRegion(REGION);
        serverIdentificationInputs.setServerId(SERVER_ID);
        serverIdentificationInputs.setProxyHost("proxy.abcde.com");
        serverIdentificationInputs.setProxyPort("8080");
        return serverIdentificationInputs;
    }

    /**
     * Tests the execute method. Positive scenario.
     * @throws Exception
     */
    @Test
    public void testExecute() throws Exception {
        ServerIdentificationInputs intputs = getInputsForAmazon();
        doNothing().when(toTest).processInputs(intputs);
        toTest.region = REGION;
        toTest.serverId = SERVER_ID;
        PowerMockito.mockStatic(ComputeFactory.class);
        PowerMockito.doReturn(computeServiceMock).when(ComputeFactory.class, "getComputeService", intputs);
        Mockito.doNothing().when(computeServiceMock).resume(REGION, SERVER_ID);

        Map<String, String> result = toTest.execute(intputs);

        Assert.assertEquals(result.get(Outputs.RETURN_CODE), "0");
        Assert.assertEquals(result.get(Outputs.RETURN_RESULT), "server resumed");
        verify(toTest).processInputs(intputs);
        verify(computeServiceMock).resume(REGION, SERVER_ID);
        verifyNoMoreInteractions(computeServiceMock);
    }

    @Test
    public void testExecuteNegativeScenario() throws Exception {
        exception.expect(ResourceNotFoundException.class);
        exception.expectMessage(INVALID_SERVER_ID_EXCEPTION_MESSAGE);

        ServerIdentificationInputs intputs = getInputsForAmazon();
        doNothing().when(toTest).processInputs(intputs);
        toTest.region = REGION;
        toTest.serverId = SERVER_ID;
        PowerMockito.mockStatic(ComputeFactory.class);
        PowerMockito.doReturn(computeServiceMock).when(ComputeFactory.class, "getComputeService", intputs);
        ResourceNotFoundException toThrow = new ResourceNotFoundException(INVALID_SERVER_ID_EXCEPTION_MESSAGE);
        Mockito.doThrow(toThrow).when(computeServiceMock).resume(REGION, SERVER_ID);

        toTest.execute(intputs);
    }

    /**
     * Test processInputs method. Positive scenario.
     * @throws Exception
     */
    @Test
    public void testProcessInputs() throws Exception {
        ServerIdentificationInputs inputs = getInputsForAmazon();
        toTest.processInputs(inputs);

        assertEquals(REGION, toTest.region);
        assertEquals(SERVER_ID, toTest.serverId);
    }

    /**
     * Test process inputs with null server id.
     * @throws Exception
     */
    @Test
    public void testProcessInputsWithNullServerId() throws Exception {
        exception.expect(Exception.class);
        exception.expectMessage(SERVER_ID_NOT_SPECIFIED);

        ServerIdentificationInputs inputs = getInputsForAmazon();
        inputs.setServerId(null);
        toTest.processInputs(inputs);
    }
}
