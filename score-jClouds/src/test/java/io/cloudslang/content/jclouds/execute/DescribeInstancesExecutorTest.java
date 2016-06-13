package io.cloudslang.content.jclouds.execute;

import io.cloudslang.content.jclouds.entities.constants.Outputs;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.entities.inputs.InstanceInputs;
import io.cloudslang.content.jclouds.execute.instances.DescribeInstancesExecutor;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by Mihai Tusa.
 * 6/8/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({DescribeInstancesExecutor.class, ComputeFactory.class})
public class DescribeInstancesExecutorTest {
    private DescribeInstancesExecutor toTest;
    private AmazonInputs inputs;

    @Mock
    private ComputeService computeServiceMock;

    @Before
    public void init() {
        mockStatic(ComputeFactory.class);

        toTest = new DescribeInstancesExecutor();
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

        doReturn(new HashSet<>()).when(computeServiceMock).describeInstancesInRegion(getCommonInputs(inputs), getInstanceInputs());

        Map<String, String> result = toTest.execute(getCommonInputs(inputs), getInstanceInputs());

        verify(computeServiceMock, times(1)).describeInstancesInRegion(any(CommonInputs.class), any(InstanceInputs.class));

        assertNotNull(result);
        assertEquals("0", result.get(Outputs.RETURN_CODE));
        assertEquals("[]", result.get(Outputs.RETURN_RESULT));
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

    private InstanceInputs getInstanceInputs() {
        return new InstanceInputs.InstanceInputsBuilder()
                .withCustomInputs(new CustomInputs.CustomInputsBuilder().build())
                .build();
    }
}
