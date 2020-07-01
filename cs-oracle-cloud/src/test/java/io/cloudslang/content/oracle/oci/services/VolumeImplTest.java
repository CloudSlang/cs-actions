package io.cloudslang.content.oracle.oci.services;

import io.cloudslang.content.oracle.oci.entities.inputs.OCIAttachVolumeInputs;
import io.cloudslang.content.oracle.oci.entities.inputs.OCICommonInputs;
import org.junit.Test;

import static io.cloudslang.content.oracle.oci.services.VolumeImpl.*;
import static org.junit.Assert.assertEquals;

public class VolumeImplTest {

    private static final String EXPECTED_ATTACH_VOLUME_PATH = "/20160918/volumeAttachments";
    private static final String EXPECTED_ATTACH_VOLUME_REQUEST_BODY = "{\"displayName\":\"test\"," +
            "\"instanceId\":\"1234\",\"volumeId\":\"abcd\",\"type\":\"iscsi\",\"isReadOnly\":false," +
            "\"isShareable\":false}";
    private static final String EXPECTED_GET_VOLUME_ATTACHMENT_DETAILS_PATH = "/20160918/volumeAttachments/abc1234";
    private static final String EXPECTED_DETACH_VOLUME_PATH = "/20160918/volumeAttachments/abc1234";

    private final String VOLUME_ID = "abcd";
    private final String INSTANCE_ID = "1234";
    private final String VOLUME_ATTACHMENT_ID = "abc1234";

    private final OCIAttachVolumeInputs ociAttachVolumeInputs = OCIAttachVolumeInputs.builder()
            .volumeType("iscsi")
            .displayName("test")
            .isReadOnly("false")
            .isShareable("false")
            .commonInputs(OCICommonInputs.builder()
                    .instanceId(INSTANCE_ID)
                    .volumeId(VOLUME_ID)
                    .build()).build();

    @Test
    public void attachVolumeTest() {
        String path = attachVolumePath();
        assertEquals(EXPECTED_ATTACH_VOLUME_PATH, path);
    }

    @Test
    public void attachVolumeRequestBodyTest() {
        final String requestBody = attachVolumeRequestBody(ociAttachVolumeInputs);
        assertEquals(EXPECTED_ATTACH_VOLUME_REQUEST_BODY, requestBody);
    }

    @Test
    public void getVolumeAttachmentDetailsTest() {
        String path = getVolumeAttachmentDetailsPath(VOLUME_ATTACHMENT_ID);
        assertEquals(EXPECTED_GET_VOLUME_ATTACHMENT_DETAILS_PATH, path);
    }

    @Test
    public void detachVolumeTest() {
        String path = detachVolumePath(VOLUME_ATTACHMENT_ID);
        assertEquals(EXPECTED_DETACH_VOLUME_PATH, path);
    }
}
