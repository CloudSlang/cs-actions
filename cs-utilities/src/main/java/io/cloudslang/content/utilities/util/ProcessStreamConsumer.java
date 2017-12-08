package io.cloudslang.content.utilities.util;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.concurrent.Callable;

import static java.lang.Thread.currentThread;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.io.IOUtils.closeQuietly;


public class ProcessStreamConsumer implements Callable<ProcessResponseEntity> {
    private static final int ERROR_CODE = -1;
    private final Process process;

    public ProcessStreamConsumer(Process process) {
        this.process = process;
    }

    @Override
    public ProcessResponseEntity call() throws Exception {
        String stdOut = "";
        String stdErr = "";
        int exitCode = ERROR_CODE;
        boolean timedOut = false;
        try {
            exitCode = process.waitFor();

            stdOut = IOUtils.toString(process.getInputStream(), UTF_8);
            stdErr = IOUtils.toString(process.getErrorStream(), UTF_8);
        } catch (InterruptedException ignored) {
            timedOut = true;
            currentThread().interrupt();
        } catch (IOException ignored) {
        } finally {
            closeQuietly(process.getInputStream());
            closeQuietly(process.getErrorStream());
        }

        return new ProcessResponseEntity(stdOut, stdErr, exitCode, timedOut);
    }
}