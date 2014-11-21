package org.eclipse.score.content.ssh.entities;

/**
 * @author ioanvranauhp
 *         Date: 10/29/14
 */
public class CommandResult {
    private String standardOutput;
    private String standardError;
    private int exitCode;

    public CommandResult() {
    }

    public String getStandardOutput() {
        return standardOutput;
    }

    public void setStandardOutput(String standardOutput) {
        this.standardOutput = standardOutput;
    }

    public String getStandardError() {
        return standardError;
    }

    public void setStandardError(String standardError) {
        this.standardError = standardError;
    }

    public int getExitCode() {
        return exitCode;
    }

    public void setExitCode(int exitCode) {
        this.exitCode = exitCode;
    }
}
