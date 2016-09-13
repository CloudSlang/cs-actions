package io.cloudslang.content.jclouds.services;

import org.jclouds.ec2.domain.Attachment;
import org.jclouds.ec2.domain.Volume;

/**
 * Created by Mihai Tusa.
 * 6/16/2016.
 */
public interface VolumeService {
    void deleteVolumeInRegion(String region, String volumeId, boolean isDebugMode);

    Attachment attachVolumeInRegion(String region, String volumeId, String instanceId, String device, boolean isDebugMode)
            throws Exception;

    void detachVolumeInRegion(String region, String volumeId, String instanceId, String device, boolean force, boolean isDebugMode);
}
