package io.cloudslang.content.jclouds.execute.volumes;

import io.cloudslang.content.jclouds.entities.constants.Outputs;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.entities.inputs.VolumeInputs;
import io.cloudslang.content.jclouds.factory.VolumeFactory;
import io.cloudslang.content.jclouds.services.VolumeService;
import org.jclouds.ec2.domain.Attachment;
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
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by Mihai Tusa.
 * 7/19/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AttachVolumeInRegionExecutor.class, VolumeFactory.class})
public class AttachVolumeInRegionExecutorTest {
    private AttachVolumeInRegionExecutor toTest;

    @Mock
    private VolumeService volumeServiceMock;

    @Mock
    private Attachment attachmentMock;

    @Before
    public void init() {
        mockStatic(VolumeFactory.class);

        toTest = new AttachVolumeInRegionExecutor();
    }

    @After
    public void tearDown() {
        toTest = null;
    }

    @Test
    public void testExecute() throws Exception {
        when(VolumeFactory.getVolumeService(any(CommonInputs.class))).thenReturn(volumeServiceMock);
        when(volumeServiceMock.attachVolumeInRegion(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(attachmentMock);

        Map<String, String> result = toTest.execute(getCommonInputs(), getVolumeInputs());

        verify(volumeServiceMock, times(1))
                .attachVolumeInRegion(eq("testRegion"), eq("vol-abcdef12"), eq("i-abcdef12"), eq("testName"));

        assertNotNull(result);
        assertNotNull(result.get("returnResult"));
        assertEquals("0", result.get(Outputs.RETURN_CODE));
    }

    private CommonInputs getCommonInputs() throws Exception {
        return new CommonInputs.CommonInputsBuilder().build();
    }

    private CustomInputs getCustomInputs() throws Exception {
        return new CustomInputs.CustomInputsBuilder()
                .withRegion("testRegion")
                .withVolumeId("vol-abcdef12")
                .withInstanceId("i-abcdef12")
                .build();
    }

    private VolumeInputs getVolumeInputs() throws Exception {
        return new VolumeInputs.VolumeInputsBuilder()
                .withCustomInputs(getCustomInputs())
                .withDeviceName("testName")
                .build();
    }
}
