package io.cloudslang.content.remote.utils;

import io.cloudslang.content.remote.constants.Constants;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.nio.file.Paths;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by pasternd on 08/06/2016.
 */
@PrepareForTest(CommandUtils.class)
public class CommandUtilsTest {
    private  String hostname = "";
    private  String username = "";
    private  String password = "";
    private  String command = "";
    private  String tempFileForResults = "";


    @Test
    public void getTempFileForResultsTest() throws Exception {
        String resultStr =  CommandUtils.getTempFileForResults(hostname);
        assertNotNull(resultStr);
        assertEquals(resultStr, Constants.START_PATH_FILE + hostname + Constants.FILENAME);
    }

    @Test
    public void getWmiComCreateFileTest() throws Exception {
        String resultStr = CommandUtils.getWmiComCreateFile(hostname,username,password,tempFileForResults);
        assertNotNull(resultStr);
        assertEquals(resultStr, Constants.WMI + hostname + Constants.USER + username + Constants.PASSWORD + Constants.BACK_SLASH_WITHOUT_SPACE + password + Constants.BACK_SLASH_WITHOUT_SPACE + Constants.CREATE_FILE_COM + tempFileForResults + Constants.END_FILE_COM);
    }

    @Test
    public void getWmiComExecuteWindComTest() throws Exception {
        String resultStr =  CommandUtils.getWmiComExecuteWindCom(hostname,username,password,command,tempFileForResults);
        assertNotNull(resultStr);
        assertEquals(resultStr, Constants.WMI + hostname +Constants.USER + username + Constants.PASSWORD + Constants.BACK_SLASH_WITHOUT_SPACE + password + Constants.CALL_CMD_COM + command + Constants.WMI_RES_PUT_COM + tempFileForResults + Constants.BACK_SLASH);
    }
}
