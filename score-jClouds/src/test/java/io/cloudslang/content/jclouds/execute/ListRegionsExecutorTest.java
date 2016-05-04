package io.cloudslang.content.jclouds.execute;

import io.cloudslang.content.jclouds.entities.constants.Outputs;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.execute.regions.ListRegionsExecutor;
import io.cloudslang.content.jclouds.factory.ComputeFactory;
import io.cloudslang.content.jclouds.services.ComputeService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by persdana on 7/7/2015.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ListRegionsExecutor.class, ComputeFactory.class})
public class ListRegionsExecutorTest {
    private ListRegionsExecutor toTest;
    private AmazonInputs inputs;

    @Mock
    private ComputeService computeServiceMock;

    @Before
    public void init() {
        mockStatic(ComputeFactory.class);

        toTest = new ListRegionsExecutor();
        inputs = AmazonInputs.getAmazonInstance();
    }

    @After
    public void tearDown() {
        toTest = null;
        inputs = null;
    }

    /**
     * Tests the execute method. Positive scenario.
     *
     * @throws Exception
     */
    @Test
    public void testExecute() throws Exception {
        when(ComputeFactory.getComputeService(any(CommonInputs.class))).thenReturn(computeServiceMock);

        Set<String> regions = getRegions();
        doReturn(regions).when(computeServiceMock).listRegions();

        Map<String, String> result = toTest.execute(getCommonInputs(inputs));

        verify(computeServiceMock, times(1)).listRegions();

        assertNotNull(result);
        assertEquals("0", result.get(Outputs.RETURN_CODE));
        assertEquals("reg1;;reg2", result.get(Outputs.RETURN_RESULT));
    }

    private Set<String> getRegions() {
        Set<String> regions = new HashSet<>();
        regions.add("reg1");
        regions.add("reg2");
        return regions;
    }

    private CommonInputs getCommonInputs(AmazonInputs inputs) throws Exception {
        return new CommonInputs.CommonInputsBuilder()
                .withProvider(inputs.getProvider())
                .withEndpoint(inputs.getEndpoint())
                .withIdentity(inputs.getIdentity())
                .withCredential(inputs.getCredential())
                .withProxyHost(inputs.getProxyHost())
                .withProxyPort(inputs.getProxyPort())
                .withDelimiter(inputs.getDelimiter())
                .build();
    }
}