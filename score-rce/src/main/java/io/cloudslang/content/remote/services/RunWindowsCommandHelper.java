package io.cloudslang.content.remote.services;

import io.cloudslang.content.remote.constants.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by pasternd on 08/06/2016.
 */
public class RunWindowsCommandHelper {

    public String getCommandResult(File file) throws IOException {
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

    public int executeCommand(String command){
        try {
            Runtime r = Runtime.getRuntime();
            Process p = r.exec(command);
            p.waitFor();
        } catch (Exception e) {
            System.out.println(e);
        }
        return 0;
    }


}
