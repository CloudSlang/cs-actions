package io.cloudslang.content.jclouds.execute.snapshots;

import io.cloudslang.content.jclouds.entities.constants.Outputs;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.entities.inputs.VolumeInputs;
import io.cloudslang.content.jclouds.factory.SnapshotFactory;
import io.cloudslang.content.jclouds.services.SnapshotService;
import org.jclouds.ec2.domain.Snapshot;
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
@PrepareForTest({CreateSnapshotInRegionExecutor.class, SnapshotFactory.class})
public class CreateSnapshotInRegionExecutorTest {
    private CreateSnapshotInRegionExecutor toTest;

    @Mock
    private SnapshotService snapshotServiceMock;

    @Mock
    private Snapshot snapshotMock;

    @Before
    public void init() {
        mockStatic(SnapshotFactory.class);

        toTest = new CreateSnapshotInRegionExecutor();
    }

    @After
    public void tearDown() {
        toTest = null;
    }

    @Test
    public void testExecute() throws Exception {
        when(SnapshotFactory.getSnapshotService(any(CommonInputs.class))).thenReturn(snapshotServiceMock);
        when(snapshotServiceMock.createSnapshotInRegion(anyString(), anyString(), anyString())).thenReturn(snapshotMock);

        Map<String, String> result = toTest.execute(getCommonInputs(), getVolumeInputs());

        verify(snapshotServiceMock, times(1))
                .createSnapshotInRegion(eq("testRegion"), eq("vol-abcdef12"), eq("somethingHere"));

        assertNotNull(result);
        assertNotNull(result.get("returnResult"));
        assertEquals("0", result.get(Outputs.RETURN_CODE));
    }

    private CommonInputs getCommonInputs() throws Exception {
        return new CommonInputs.CommonInputsBuilder().build();
    }

    private CustomInputs getCustomInputs() throws Exception {
        return new CustomInputs.CustomInputsBuilder().withRegion("testRegion").withVolumeId("vol-abcdef12").build();
    }

    private VolumeInputs getVolumeInputs() throws Exception {
        return new VolumeInputs.VolumeInputsBuilder()
                .withCustomInputs(getCustomInputs())
                .withDescription("somethingHere")
                .build();
    }
}