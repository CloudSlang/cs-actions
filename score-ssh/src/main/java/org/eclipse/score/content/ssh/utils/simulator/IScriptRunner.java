package org.eclipse.score.content.ssh.utils.simulator;

public interface IScriptRunner extends Runnable {
    public String getOutput();

    public int getCommandsLeft();

    public boolean noMoreCommandsLeft();

    public String getException();

    public void setCaptureOutput(boolean enableCapture);
}
