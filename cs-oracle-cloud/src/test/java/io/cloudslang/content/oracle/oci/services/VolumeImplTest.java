package io.cloudslang.content.oracle.oci.services;

import org.junit.Test;

import static io.cloudslang.content.oracle.oci.services.VolumeImpl.detachVolumePath;
import static org.junit.Assert.assertEquals;

public class VolumeImplTest {

    private static final String EXPECTED_DETACH_VOLUME_PATH = "/20160918/volumeAttachments/abc1234";

    private final String VOLUME_ATTACHMENT_ID = "abc1234";

    @Test
    public void detachVolumeTest() {
        String path = detachVolumePath(VOLUME_ATTACHMENT_ID);
        assertEquals(EXPECTED_DETACH_VOLUME_PATH, path);

    }
}
