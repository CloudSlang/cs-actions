package io.cloudslang.content.jclouds.execute.snapshots;

import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.VolumeInputs;
import io.cloudslang.content.jclouds.factory.SnapshotFactory;
import io.cloudslang.content.jclouds.services.SnapshotService;
import io.cloudslang.content.jclouds.utils.OutputsUtil;
import org.jclouds.ec2.domain.Snapshot;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 6/28/2016.
 */
public class CreateSnapshotInRegionExecutor {
    public Map<String, String> execute(CommonInputs commonInputs, VolumeInputs volumeInputs) throws Exception {
        SnapshotService snapshotService = SnapshotFactory.getSnapshotService(commonInputs);
        Snapshot snapshot = snapshotService.createSnapshotInRegion(volumeInputs.getCustomInputs().getRegion(),
                volumeInputs.getCustomInputs().getVolumeId(), volumeInputs.getDescription(), commonInputs.getWithExecutionLogs());

        return OutputsUtil.getResultsMap(snapshot.toString());
    }
}
