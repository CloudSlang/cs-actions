package io.cloudslang.content.remote.constants;

/**
 * Created by pasternd on 08/06/2016.
 */
public class Constants {

    public static final String WMI = "wmic /node:\"";
    public static final String CREATE_FILE_COM = " process call create \"cmd.exe fsutil file createnew ";
    public static final String CALL_CMD_COM = "\" process call create \"cmd.exe /c ";
    public static final String USER = "\" /user:" ;
    public static final String PASSWORD = " /password:";
    public static final String BACK_SLASH = " \"";
    public static final String END_FILE_COM = " 0\"";
    public static final String EMPTY_STR = "";
    public static final String START_PATH_FILE = "\\\\";
    public static final String FILENAME = "\\C$\\Temp\\emptyfile.txt";
    public static final String WMI_RES_PUT_COM = " > ";
    public static final String INVALID_PARAMETERS = "Invalid parameters";
    public static final String BACK_SLASH_WITHOUT_SPACE = "\"";


}
