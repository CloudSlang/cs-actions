package io.cloudslang.content.jclouds.execute.volumes;

import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.VolumeInputs;
import io.cloudslang.content.jclouds.factory.VolumeFactory;
import io.cloudslang.content.jclouds.services.VolumeService;
import io.cloudslang.content.jclouds.utils.OutputsUtil;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 6/24/2016.
 */
public class DetachVolumeInRegionExecutor {
    private static final String DETACH_VOLUME_PROCESS_STARTED = "Detach volume process started successfully.";

    public Map<String, String> execute(CommonInputs commonInputs, VolumeInputs volumeInputs) throws Exception {
        VolumeService volumeService = VolumeFactory.getVolumeService(commonInputs);
        volumeService.detachVolumeInRegion(volumeInputs.getCustomInputs().getRegion(), volumeInputs.getCustomInputs().getVolumeId(),
                volumeInputs.getCustomInputs().getInstanceId(), volumeInputs.getDeviceName(), volumeInputs.isForce(),
                commonInputs.isDebugMode());

        return OutputsUtil.getResultsMap(DETACH_VOLUME_PROCESS_STARTED);
    }
}
