package io.cloudslang.content.remote.action;

import io.cloudslang.content.remote.constants.Constants;
import io.cloudslang.content.remote.constants.Outputs;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import java.io.*;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * Created by pasternd on 06/06/2016.
 */
@PrepareForTest(RunWindowsCommand.class)
public class RunWindowsCommandTest {

    private RunWindowsCommand runWindowsCommand;

    @Before
    public void init() {
        runWindowsCommand = new RunWindowsCommand();
    }

    @After
    public void tearDown() {
        runWindowsCommand = null;
    }

    @Test
    public void testRunWindowsCommandWithInvalidParameters() throws Exception {
        Map<String, String> resultMap = runWindowsCommand.run("", "", "", "");
        assertNotNull(resultMap);
        assertEquals(resultMap.get(Outputs.ERROR_MESSAGE), Constants.INVALID_PARAMETERS);
    }

}