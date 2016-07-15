package io.cloudslang.content.jclouds.services;

import org.jclouds.ec2.domain.Snapshot;

/**
 * Created by Mihai Tusa.
 * 6/27/2016.
 */
public interface SnapshotService {
    Snapshot createSnapshotInRegion(String region, String volumeId, String description);

    void deleteSnapshotInRegion(String region, String snapshotId);
}
