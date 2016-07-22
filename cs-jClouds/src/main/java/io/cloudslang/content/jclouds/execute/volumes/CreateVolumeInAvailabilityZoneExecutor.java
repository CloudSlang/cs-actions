package io.cloudslang.content.jclouds.execute.volumes;

import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.VolumeInputs;
import io.cloudslang.content.jclouds.factory.VolumeFactory;
import io.cloudslang.content.jclouds.services.VolumeService;
import io.cloudslang.content.jclouds.utils.OutputsUtil;
import org.jclouds.ec2.domain.Volume;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 6/22/2016.
 */
public class CreateVolumeInAvailabilityZoneExecutor {
    public Map<String, String> execute(CommonInputs commonInputs, VolumeInputs volumeInputs) throws Exception {
        VolumeService volumeService = VolumeFactory.getVolumeService(commonInputs);
        Volume volume = volumeService.createVolumeInAvailabilityZone(volumeInputs.getCustomInputs().getRegion(),
                volumeInputs.getCustomInputs().getAvailabilityZone(), volumeInputs.getSnapshotId(),
                volumeInputs.getCustomInputs().getVolumeType(), volumeInputs.getSize(), volumeInputs.getIops(),
                volumeInputs.isEncrypted(), commonInputs.getWithExecutionLogs());

        return OutputsUtil.getResultsMap(volume.toString());
    }
}