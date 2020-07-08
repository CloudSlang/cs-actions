package io.cloudslang.content.oracle.oci.services;

import org.junit.Test;

import static io.cloudslang.content.oracle.oci.services.VnicImpl.*;
import static org.junit.Assert.assertEquals;

public class VnicImplTest {


    private static final String EXPECTED_DETACH_VNIC_PATH = "/20160918/vnicAttachments/abc1234";
    private static final String EXPECTED_ATTACH_VNIC_PATH = "/20160918/vnicAttachments/";
    private static final String EXPECTED_GET_VNIC_ATTACHMENT_DERTAILS_PATH = "/20160918/vnicAttachments/abc1234";
    private static final String EXPECTED_GET_VNIC_DETAILS_PATH = "/20160918/vnics/abc1234";
    private static final String EXPECTED_LIST_VNIC_ATTACHMENTS_PATH = "/20160918/vnicAttachments/";

    private final String VNIC_ATTACHMENT_ID = "abc1234";

    @Test
    public void detachVolumeTest() {
        String path = vnicPath(VNIC_ATTACHMENT_ID);
        assertEquals(EXPECTED_DETACH_VNIC_PATH, path);

    }

    @Test
    public void attachVolumeTest() {
        String path = listVnicAttachmentsPath();
        assertEquals(EXPECTED_ATTACH_VNIC_PATH, path);

    }

    @Test
    public void getVnicAttachmentDetailsTest() {
        String path = vnicPath(VNIC_ATTACHMENT_ID);
        assertEquals(EXPECTED_GET_VNIC_ATTACHMENT_DERTAILS_PATH, path);

    }

    @Test
    public void getVnicDetailsTest() {
        String path = getVnicDetailsPath(VNIC_ATTACHMENT_ID);
        assertEquals(EXPECTED_GET_VNIC_DETAILS_PATH, path);

    }

    
    @Test
    public void getListVnicAttachmentsTest() {
        String path = listVnicAttachmentsPath();
        assertEquals(EXPECTED_LIST_VNIC_ATTACHMENTS_PATH, path);
    }
}
