package io.cloudslang.content.remote.utils;

/**
 * Created by pasternd on 07/06/2016.
 */
public class StringUtils {

    public static String getTempFileForResults(String hostname){
        return "\\\\" + hostname + "\\C$\\Temp\\emptyfile.txt";
    }

    public static String getWmiComCreateFile(String hostname, String username, String password, String tempFileForResults){
        return "wmic /node:\""+ hostname + "\" /user:"+ username + " /password:" + password + " process call create \"cmd.exe fsutil file createnew " + tempFileForResults + " 0\"";
    }

    public static String getWmiComExecuteWindCom(String hostname, String command, String tempFileForResults){
        return "wmic /node:\""+ hostname + "\" process call create \"cmd.exe /c " + command + " > " + tempFileForResults + " \"";
    }

}
