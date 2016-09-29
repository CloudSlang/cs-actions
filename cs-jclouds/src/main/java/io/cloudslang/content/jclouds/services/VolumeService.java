package io.cloudslang.content.jclouds.services;

/**
 * Created by Mihai Tusa.
 * 6/16/2016.
 */
public interface VolumeService {
    void deleteVolumeInRegion(String region, String volumeId, boolean isDebugMode);
}
