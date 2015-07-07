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
@PrepareForTest({ListRegionsExecutor.class, ComputeFactory.class})
public class ListRegionsExecutorTest {
    private static final String INVALID_ENDPOINT_EXCEPTION_MESSAGE = "invalid endpoint";
    private static final String DELIMITER = ";;";
    private static final String DEFAULT_DELIMITER = ";\n";

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Spy
    private ListRegionsExecutor toTest = new ListRegionsExecutor();

    @Mock
    ComputeService computeServiceMock;

    private ListServersInputs getInputsForAmazon() {
        ListServersInputs serverIdentificationInputs = new ListServersInputs();
        serverIdentificationInputs.setProvider("ec2");
        serverIdentificationInputs.setEndpoint("https://ec2.amazonaws.com");
        serverIdentificationInputs.setIdentity("AKIAIQHVQ4UM7SOXXXXXX");
        serverIdentificationInputs.setCredential("R1ZRPK4HPXU6cyBi1XY/IkYqQ+qXXXXXXX");
        serverIdentificationInputs.setDelimiter(DELIMITER);
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
        ListServersInputs inputs = getInputsForAmazon();
        doNothing().when(toTest).processInputs(inputs);
        toTest.delimiter = DELIMITER;
        PowerMockito.mockStatic(ComputeFactory.class);
        PowerMockito.doReturn(computeServiceMock).when(ComputeFactory.class, "getComputeService", inputs);
        Set<String> regions = new HashSet<>();
        regions.add("reg1");
        regions.add("reg2");
        Mockito.doReturn(regions).when(computeServiceMock).listRegions();

        Map<String, String> result = toTest.execute(inputs);

        Assert.assertEquals(result.get(Outputs.RETURN_CODE), "0");
        Assert.assertEquals(result.get(Outputs.RETURN_RESULT), "reg1" + DELIMITER + "reg2");
        verify(toTest).processInputs(inputs);
        verify(computeServiceMock).listRegions();
        verifyNoMoreInteractions(computeServiceMock);
    }

    @Test
    public void testExecuteNegativeScenario() throws Exception {
        exception.expect(ResourceNotFoundException.class);
        exception.expectMessage(INVALID_ENDPOINT_EXCEPTION_MESSAGE);

        ListServersInputs intputs = getInputsForAmazon();
        doNothing().when(toTest).processInputs(intputs);
        toTest.delimiter = DELIMITER;
        PowerMockito.mockStatic(ComputeFactory.class);
        PowerMockito.doReturn(computeServiceMock).when(ComputeFactory.class, "getComputeService", intputs);
        ResourceNotFoundException toThrow = new ResourceNotFoundException(INVALID_ENDPOINT_EXCEPTION_MESSAGE);
        Mockito.doThrow(toThrow).when(computeServiceMock).listRegions();

        toTest.execute(intputs);
    }

    /**
     * Test processInputs method. Positive scenario.
     * @throws Exception
     */
    @Test
    public void testProcessInputs() throws Exception {
        ListServersInputs inputs = getInputsForAmazon();
        inputs.setDelimiter(DELIMITER);
        toTest.processInputs(inputs);

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
