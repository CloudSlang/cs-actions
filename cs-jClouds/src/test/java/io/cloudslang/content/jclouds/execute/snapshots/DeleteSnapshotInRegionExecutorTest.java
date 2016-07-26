package io.cloudslang.content.jclouds.execute.snapshots;

import io.cloudslang.content.jclouds.entities.constants.Outputs;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.entities.inputs.VolumeInputs;
import io.cloudslang.content.jclouds.factory.SnapshotFactory;
import io.cloudslang.content.jclouds.services.SnapshotService;
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
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by Mihai Tusa.
 * 7/19/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({DeleteSnapshotInRegionExecutor.class, SnapshotFactory.class})
public class DeleteSnapshotInRegionExecutorTest {
    private DeleteSnapshotInRegionExecutor toTest;

    @Mock
    private SnapshotService snapshotServiceMock;

    @Before
    public void init() {
        mockStatic(SnapshotFactory.class);

        toTest = new DeleteSnapshotInRegionExecutor();
    }

    @After
    public void tearDown() {
        toTest = null;
    }

    @Test
    public void testExecute() throws Exception {
        when(SnapshotFactory.getSnapshotService(any(CommonInputs.class))).thenReturn(snapshotServiceMock);

        Map<String, String> result = toTest.execute(getCommonInputs(), getVolumeInputs());

        verify(snapshotServiceMock, times(1)).deleteSnapshotInRegion(eq("testRegion"), eq("snap-abcdef12"), eq(false));

        assertNotNull(result);
        assertEquals("0", result.get(Outputs.RETURN_CODE));
        assertEquals("Delete snapshot started successfully.", result.get("returnResult"));
    }

    private CommonInputs getCommonInputs() throws Exception {
        return new CommonInputs.CommonInputsBuilder().withDebugMode("").build();
    }

    private CustomInputs getCustomInputs() throws Exception {
        return new CustomInputs.CustomInputsBuilder().withRegion("testRegion").build();
    }

    private VolumeInputs getVolumeInputs() throws Exception {
        return new VolumeInputs.VolumeInputsBuilder()
                .withCustomInputs(getCustomInputs())
                .withSnapshotId("snap-abcdef12")
                .build();
    }
}