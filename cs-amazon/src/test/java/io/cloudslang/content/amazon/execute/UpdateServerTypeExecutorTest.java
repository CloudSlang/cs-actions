package io.cloudslang.content.amazon.execute;

import io.cloudslang.content.amazon.entities.constants.Outputs;
import io.cloudslang.content.amazon.entities.inputs.CommonInputs;
import io.cloudslang.content.amazon.entities.inputs.CustomInputs;
import io.cloudslang.content.amazon.entities.inputs.InstanceInputs;
import io.cloudslang.content.amazon.execute.instances.UpdateInstanceTypeExecutor;
import io.cloudslang.content.amazon.factory.ComputeFactory;
import io.cloudslang.content.amazon.services.ComputeService;
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
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * Created by Mihai Tusa.
 * 3/15/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({UpdateInstanceTypeExecutor.class, ComputeFactory.class})
public class UpdateServerTypeExecutorTest {
    private UpdateInstanceTypeExecutor toTest;
    private AmazonInputs inputs;

    @Mock
    private ComputeService computeServiceMock;

    @Before
    public void init() {
        mockStatic(ComputeFactory.class);

        toTest = new UpdateInstanceTypeExecutor();
        inputs = AmazonInputs.getAmazonInstance();
    }

    @After
    public void tearDown() {
        toTest = null;
        inputs = null;
    }

    @Test
    public void execute() throws Exception {
        when(ComputeFactory.getComputeService(any(CommonInputs.class))).thenReturn(computeServiceMock);
        doReturn("Server updated successfully.").when(computeServiceMock).updateInstanceType(anyString(), anyString(),
                anyString(), anyLong(), anyLong(), anyBoolean());
        Map<String, String> results = toTest.execute(getCommonInputs(inputs), getInstanceInputs(inputs));

        verify(computeServiceMock, times(1)).updateInstanceType(anyString(), anyString(), anyString(), anyLong(),
                anyLong(), anyBoolean());

        assertNotNull(results);
        assertEquals("0", results.get(Outputs.RETURN_CODE));
        assertEquals("Server updated successfully.", results.get(Outputs.RETURN_RESULT));
    }

    private CommonInputs getCommonInputs(AmazonInputs inputs) throws Exception {
        return new CommonInputs.Builder()
                .withProvider(inputs.getProvider())
                .withEndpoint(inputs.getEndpoint())
                .withIdentity(inputs.getIdentity())
                .withCredential(inputs.getCredential())
                .withProxyHost(inputs.getProxyHost())
                .withProxyPort(inputs.getProxyPort())
                .build();
    }

    private CustomInputs getCustomInputs(AmazonInputs inputs) {
        return new CustomInputs.Builder()
                .withRegion(inputs.getRegion())
                .build();
    }

    private InstanceInputs getInstanceInputs(AmazonInputs inputs) {
        return new InstanceInputs.Builder()
                .withCustomInputs(getCustomInputs(inputs))
                .build();
    }
}
