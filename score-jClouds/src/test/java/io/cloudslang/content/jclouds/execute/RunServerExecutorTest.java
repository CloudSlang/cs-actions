package io.cloudslang.content.jclouds.execute;

import io.cloudslang.content.jclouds.entities.constants.Outputs;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.factory.ComputeFactory;
import io.cloudslang.content.jclouds.services.ComputeService;
import org.jclouds.ec2.domain.Reservation;
import org.jclouds.ec2.domain.RunningInstance;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * Created by Mihai Tusa.
 * 2/26/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({RunServerExecutor.class, ComputeFactory.class})
public class RunServerExecutorTest {
    private RunServerExecutor toTest;
    private AmazonInputs inputs;

    @Mock
    ComputeService computeServiceMock;

    @Mock
    Reservation<? extends RunningInstance> reservationsMock;

    @Before
    public void init() {
        mockStatic(ComputeFactory.class);

        toTest = new RunServerExecutor();
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
    public void execute() throws Exception {
        when(ComputeFactory.getComputeService(any(CommonInputs.class), (Class<?>) any(Class.class))).thenReturn(computeServiceMock);
        doReturn(reservationsMock).when(computeServiceMock).runServer(any(CommonInputs.class), any(CustomInputs.class));
        Map<String, String> results = toTest.execute(getCommonInputs(inputs), getCustomInputs(inputs));

        verify(computeServiceMock, times(1)).runServer(any(CommonInputs.class), any(CustomInputs.class));

        assertNotNull(results);
        assertEquals("0", results.get(Outputs.RETURN_CODE));
        assertEquals("reservation", results.get(Outputs.RETURN_RESULT));
    }

    private CommonInputs getCommonInputs(AmazonInputs inputs) throws Exception {
        return new CommonInputs.CommonInputsBuilder()
                .withProvider(inputs.getProvider())
                .withEndpoint(inputs.getEndpoint())
                .withIdentity(inputs.getIdentity())
                .withCredential(inputs.getCredential())
                .withProxyHost(inputs.getProxyHost())
                .withProxyPort(inputs.getProxyPort())
                .build();
    }

    private CustomInputs getCustomInputs(AmazonInputs inputs) {
        return new CustomInputs.CustomInputsBuilder()
                .withRegion(inputs.getRegion())
                .withAvailabilityZone("")
                .withImageRef("ami-4b91bb21")
                .withMinCount("")
                .withMaxCount("")
                .build();
    }
}