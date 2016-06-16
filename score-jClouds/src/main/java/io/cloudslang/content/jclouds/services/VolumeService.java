package io.cloudslang.content.jclouds.services;

import org.jclouds.ec2.domain.Attachment;

/**
 * Created by Mihai Tusa.
 * 6/16/2016.
 */
public interface VolumeService {
    Attachment attachVolumeInRegion(String region, String volumeId, String instanceId, String device) throws Exception;
}