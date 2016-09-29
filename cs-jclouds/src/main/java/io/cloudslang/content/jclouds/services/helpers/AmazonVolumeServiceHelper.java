package io.cloudslang.content.jclouds.services.helpers;

import org.apache.commons.lang3.StringUtils;
import org.jclouds.ec2.options.DetachVolumeOptions;

/**
 * Created by Mihai Tusa.
 * 6/22/2016.
 */
public class AmazonVolumeServiceHelper {
    public DetachVolumeOptions[] getDetachVolumeOptions(String instanceId, String device) {
        if (StringUtils.isBlank(instanceId) && StringUtils.isBlank(device)) {
            return null;
        }

        DetachVolumeOptions[] optionsArray = new DetachVolumeOptions[2];
        DetachVolumeOptions detachVolumeOptions;

        if (StringUtils.isNotBlank(instanceId)) {
            detachVolumeOptions = DetachVolumeOptions.Builder.fromInstance(instanceId);
            optionsArray[0] = detachVolumeOptions;
        }
        if (StringUtils.isNotBlank(device)) {
            detachVolumeOptions = DetachVolumeOptions.Builder.fromDevice(device);
            optionsArray[1] = detachVolumeOptions;
        }

        return optionsArray;
    }
}