package io.cloudslang.content.hashicorp.terraform.services;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static io.cloudslang.content.hashicorp.terraform.services.RunImpl.getRunDetailsUrl;
import static org.junit.Assert.assertEquals;


@RunWith(PowerMockRunner.class)
@PrepareForTest(WorkspaceImpl.class)
public class RunImplTest {

    private static final String RUN_ID="test123";
    private final String EXPECTED_GET_RUN_DETAILS_PATH="https://app.terraform.io/api/v2/runs/test123";

    @Test
    public void getRunDetailsPathTest() throws Exception{
        final String path=getRunDetailsUrl(RUN_ID);
        assertEquals(EXPECTED_GET_RUN_DETAILS_PATH,path);
    }


}

