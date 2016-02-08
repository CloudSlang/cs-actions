package io.cloudslang.content.jclouds.execute;

import io.cloudslang.content.jclouds.entities.inputs.ListRegionsInputs;
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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

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

    /**
     * Tests the execute method. Positive scenario.
     * @throws Exception
     */
    @Test
    public void testExecute() throws Exception {
        ListRegionsInputs inputs = Inputs.getListRegionsInputsForAmazon();
        PowerMockito.mockStatic(ComputeFactory.class);
        PowerMockito.doReturn(computeServiceMock).when(ComputeFactory.class, "getComputeService", inputs);
        Set<String> regions = new HashSet<>();
        regions.add("reg1");
        regions.add("reg2");
        Mockito.doReturn(regions).when(computeServiceMock).listRegions();

        Map<String, String> result = toTest.execute(inputs);

        Assert.assertEquals(result.get(Outputs.RETURN_CODE), "0");
        Assert.assertEquals(result.get(Outputs.RETURN_RESULT), "reg1" + DELIMITER + "reg2");
        verify(computeServiceMock).listRegions();
        verifyNoMoreInteractions(computeServiceMock);
    }

    @Test
    public void testExecuteNegativeScenario() throws Exception {
        exception.expect(ResourceNotFoundException.class);
        exception.expectMessage(INVALID_ENDPOINT_EXCEPTION_MESSAGE);

        ListRegionsInputs intputs = Inputs.getListRegionsInputsForAmazon();
        PowerMockito.mockStatic(ComputeFactory.class);
        PowerMockito.doReturn(computeServiceMock).when(ComputeFactory.class, "getComputeService", intputs);
        ResourceNotFoundException toThrow = new ResourceNotFoundException(INVALID_ENDPOINT_EXCEPTION_MESSAGE);
        Mockito.doThrow(toThrow).when(computeServiceMock).listRegions();

        toTest.execute(intputs);
    }
}
