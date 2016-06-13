package io.cloudslang.content.remote.utils;

import io.cloudslang.content.remote.constants.Constants;

import java.io.*;

/**
 * Created by pasternd on 07/06/2016.
 */
public class CommandUtils {

    public static String getTempFileForResults(String hostname){
        return Constants.START_PATH_FILE + hostname + Constants.FILENAME;
    }

    public static String getWmiComCreateFile(String hostname, String username, String password, String tempFileForResults){
        return Constants.WMI+ hostname + Constants.USER + username + Constants.PASSWORD + password + Constants.CREATE_FILE_COM + tempFileForResults + Constants.END_FILE_COM;
    }

    public static String getWmiComExecuteWindCom(String hostname, String command, String tempFileForResults){
        return Constants.WMI + hostname + Constants.CALL_CMD_COM + command + Constants.WMI_RES_PUT_COM + tempFileForResults + Constants.BACK_SLASH;
    }

    public static String getCommandResultFromFile(File file) throws IOException {
        String result = Constants.EMPTY_STR;

        BufferedReader br = new BufferedReader(new FileReader(file));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            result = sb.toString();

        } finally {
            br.close();
            file.delete();
        }
        return result;
    }

}
