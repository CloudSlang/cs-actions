package io.cloudslang.content.remote.services;

import io.cloudslang.content.remote.constants.Constants;
import io.cloudslang.content.remote.constants.Outputs;
import io.cloudslang.content.remote.utils.RunWindowsCommandInputs;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by pasternd on 08/06/2016.
 */
@PrepareForTest(RunWindowsCommandService.class)
public class RunWindowsCommandServiceTest {
    RunWindowsCommandHelper runWindowsCommandHelper;
    RunWindowsCommandService runWindowsCommandService;
    RunWindowsCommandInputs runWindowsCommandInputs;

    @Before
    public void init() throws Exception {
        runWindowsCommandHelper = new RunWindowsCommandHelper();
        runWindowsCommandService = new RunWindowsCommandService();
        runWindowsCommandInputs =  new RunWindowsCommandInputs.RunWindowsCommandInputsBuilder()
                .withHostname("")
                .withUsername("")
                .withPassword("")
                .withCommand("")
                .build();
    }
    @After
    public void tearDown() {
        runWindowsCommandHelper = null;
        runWindowsCommandService = null;
    }

    @Test
    public void testExecuteWithInvalidParameters() throws Exception {
        Map <String, String> resultMap = runWindowsCommandService.execute(runWindowsCommandInputs);
        assertNotNull(resultMap);
        assertEquals(resultMap.get(Outputs.ERROR_MESSAGE), Constants.INVALID_PARAMETERS);
    }

}
