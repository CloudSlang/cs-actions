package io.cloudslang.content.jclouds.execute.snapshots;

import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.VolumeInputs;
import io.cloudslang.content.jclouds.factory.SnapshotFactory;
import io.cloudslang.content.jclouds.services.SnapshotService;
import io.cloudslang.content.jclouds.utils.OutputsUtil;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 6/29/2016.
 */
public class DeleteSnapshotInRegionExecutor {
    private static final String DELETE_SNAPSHOT_STARTED = "Delete snapshot started successfully.";

    public Map<String, String> execute(CommonInputs commonInputs, VolumeInputs volumeInputs) throws Exception {
        SnapshotService snapshotService = SnapshotFactory.getSnapshotService(commonInputs);
        snapshotService.deleteSnapshotInRegion(volumeInputs.getCustomInputs().getRegion(), volumeInputs.getSnapshotId(),
                commonInputs.isDebugMode());

        return OutputsUtil.getResultsMap(DELETE_SNAPSHOT_STARTED);
    }
}
