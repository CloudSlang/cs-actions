



package io.cloudslang.content.oracle.oci.services;


import io.cloudslang.content.oracle.oci.entities.inputs.OCICommonInputs;
import org.junit.Test;

import static io.cloudslang.content.oracle.oci.services.InstanceImpl.*;
import static org.junit.Assert.assertEquals;

public class InstanceImplTest {

    private static final String EXPECTED_LIST_INSTANCES_PATH = "/20160918/instances/";
    private static final String EXPECTED_CREATE_INSTANCES_PATH = "/20160918/instances/";
    private static final String EXPECTED_GET_INSTANCE_PATH = "/20160918/instances/myinstance";
    private static final String EXPECTED_TERMINATE_INSTANCE_PATH = "/20160918/instances/myinstance";
    private static final String EXPECTED_UPDATE_INSTANCE_PATH = "/20160918/instances/myinstance";
    private static final String EXPECTED_ACTION_INSTANCE_PATH = "/20160918/instances/myinstance";
    private static final String EXPECTED_GET_INSTANCE_DEFAULT_CREDENTIALS_PATH = "/20160918/instances/myinstance/defaultCredentials";
    private final String REGION = "ap-hyderabad-1";
    private final String INSTANCE_ID = "myinstance";
    private final OCICommonInputs ociCommonInputs = OCICommonInputs.builder()
            .region(REGION)
            .instanceId(INSTANCE_ID)
            .build();

    @Test
    public void getlistInstancesPathTest() {
        String path = listInstancesPath();
        assertEquals(EXPECTED_LIST_INSTANCES_PATH, path);
    }

    @Test
    public void getInstanceDetailsPathTest() {
        String path = getInstanceDetailsPath(INSTANCE_ID);
        assertEquals(EXPECTED_GET_INSTANCE_PATH, path);

    }

    @Test
    public void getDefaultCredentialsTest() {
        String path = getInstanceDefaultCredentialsPath(INSTANCE_ID);
        assertEquals(EXPECTED_GET_INSTANCE_DEFAULT_CREDENTIALS_PATH, path);

    }


    @Test
    public void getTerminateInstancePathTest() {
        String path = getInstanceDetailsPath(INSTANCE_ID);
        assertEquals(EXPECTED_TERMINATE_INSTANCE_PATH, path);

    }

    @Test
    public void getCreateInstancePathTest() {
        String path = listInstancesPath();
        assertEquals(EXPECTED_CREATE_INSTANCES_PATH, path);
    }

    @Test
    public void getUpdateInstancePathTest() {
        String path = getInstanceDetailsPath(INSTANCE_ID);
        assertEquals(EXPECTED_UPDATE_INSTANCE_PATH, path);

    }


    @Test
    public void getInstanceActionPathTest() {
        String path = getInstanceDetailsPath(INSTANCE_ID);
        assertEquals(EXPECTED_ACTION_INSTANCE_PATH, path);

    }
}
