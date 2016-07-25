package io.cloudslang.content.jclouds.execute.volumes;

import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.VolumeInputs;
import io.cloudslang.content.jclouds.factory.VolumeFactory;
import io.cloudslang.content.jclouds.services.VolumeService;
import io.cloudslang.content.jclouds.utils.OutputsUtil;
import org.jclouds.ec2.domain.Attachment;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 6/24/2016.
 */
public class AttachVolumeInRegionExecutor {
    public Map<String, String> execute(CommonInputs commonInputs, VolumeInputs volumeInputs) throws Exception {
        VolumeService volumeService = VolumeFactory.getVolumeService(commonInputs);
        Attachment attachment = volumeService.attachVolumeInRegion(volumeInputs.getCustomInputs().getRegion(),
                volumeInputs.getCustomInputs().getVolumeId(), volumeInputs.getCustomInputs().getInstanceId(),
                volumeInputs.getDeviceName(), commonInputs.isDebugMode());

        return OutputsUtil.getResultsMap(attachment.toString());
    }
}
