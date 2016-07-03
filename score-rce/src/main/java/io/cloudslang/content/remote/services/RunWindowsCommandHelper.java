package io.cloudslang.content.remote.services;

import java.io.IOException;

/**
 * Created by pasternd on 08/06/2016.
 */
public class RunWindowsCommandHelper {

    public int executeCommand(String command) throws IOException, InterruptedException {
        Runtime r = Runtime.getRuntime();
        Process p = r.exec(command);
        p.waitFor();
        return 0;
    }


}
