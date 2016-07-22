package io.cloudslang.content.jclouds.execute.volumes;

import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.VolumeInputs;
import io.cloudslang.content.jclouds.factory.VolumeFactory;
import io.cloudslang.content.jclouds.services.VolumeService;
import io.cloudslang.content.jclouds.utils.OutputsUtil;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 7/18/2016.
 */
public class DeleteVolumeInRegionExecutor {
    private static final String DELETE_VOLUME_PROCESS_STARTED = "Delete volume process started successfully.";

    public Map<String, String> execute(CommonInputs commonInputs, VolumeInputs volumeInputs) throws Exception {
        VolumeService volumeService = VolumeFactory.getVolumeService(commonInputs);
        volumeService.deleteVolumeInRegion(volumeInputs.getCustomInputs().getRegion(),
                volumeInputs.getCustomInputs().getVolumeId(), commonInputs.getWithExecutionLogs());

        return OutputsUtil.getResultsMap(DELETE_VOLUME_PROCESS_STARTED);
    }
}