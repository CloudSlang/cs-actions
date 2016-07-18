package io.cloudslang.content.jclouds.services;

import org.jclouds.ec2.domain.Attachment;
import org.jclouds.ec2.domain.Volume;

/**
 * Created by Mihai Tusa.
 * 6/16/2016.
 */
public interface VolumeService {
    Volume createVolumeInAvailabilityZone(String region, String availabilityZone, String snapshotId, String volumeType,
                                          String size, String iops, boolean encrypted);

    void deleteVolumeInRegion(String region, String volumeId);

    Attachment attachVolumeInRegion(String region, String volumeId, String instanceId, String device) throws Exception;

    void detachVolumeInRegion(String region, String volumeId, String instanceId, String device, boolean force);
}
