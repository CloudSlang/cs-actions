package io.cloudslang.content.jclouds.execute;

import io.cloudslang.content.jclouds.entities.constants.Outputs;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.execute.instances.UpdateServerTypeExecutor;
import io.cloudslang.content.jclouds.factory.ComputeFactory;
import io.cloudslang.content.jclouds.services.ComputeService;
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
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * Created by Mihai Tusa.
 * 3/15/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({UpdateServerTypeExecutor.class, ComputeFactory.class})
public class UpdateServerTypeExecutorTest {
    private UpdateServerTypeExecutor toTest;
    private AmazonInputs inputs;

    @Mock
    private ComputeService computeServiceMock;

    @Before
    public void init() {
        mockStatic(ComputeFactory.class);

        toTest = new UpdateServerTypeExecutor();
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
        when(ComputeFactory.getComputeService(any(CommonInputs.class))).thenReturn(computeServiceMock);
        doReturn("Server updated successfully.").when(computeServiceMock)
                .updateInstanceType(anyString(), anyString(), anyString(), anyLong(), anyLong());
        Map<String, String> results = toTest.execute(getCommonInputs(inputs), getCustomInputs(inputs));

        verify(computeServiceMock, times(1)).updateInstanceType(anyString(), anyString(), anyString(), anyLong(), anyLong());

        assertNotNull(results);
        assertEquals("0", results.get(Outputs.RETURN_CODE));
        assertEquals("Server updated successfully.", results.get(Outputs.RETURN_RESULT));
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
                .build();
    }
}