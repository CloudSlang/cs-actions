package io.cloudslang.content.utilities.util;

public class ProcessResponseEntity {
    private final String stdout;
    private final String stderr;
    private final int exitCode;
    private final boolean timeout;

    public ProcessResponseEntity(final String stdout, final String stderr, final int exitCode, boolean timeout) {
        this.exitCode = exitCode;
        this.stdout = stdout;
        this.stderr = stderr;
        this.timeout = timeout;
    }

    public String getStdout() {
        return stdout;
    }

    public String getStderr() {
        return stderr;
    }

    public boolean isTimeout() {
        return timeout;
    }

    public int getExitCode() {
        return exitCode;
    }
}
