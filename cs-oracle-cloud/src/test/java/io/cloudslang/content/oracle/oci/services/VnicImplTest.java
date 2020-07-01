package io.cloudslang.content.oracle.oci.services;

import org.junit.Test;

import static io.cloudslang.content.oracle.oci.services.VnicImpl.vnicPath;
import static org.junit.Assert.assertEquals;

public class VnicImplTest {


    private static final String EXPECTED_DETACH_VNIC_PATH = "/20160918/vnicAttachments/abc1234";

    private final String VNIC_ATTACHMENT_ID = "abc1234";

    @Test
    public void detachVolumeTest() {
        String path = vnicPath(VNIC_ATTACHMENT_ID);
        assertEquals(EXPECTED_DETACH_VNIC_PATH, path);

    }
}
