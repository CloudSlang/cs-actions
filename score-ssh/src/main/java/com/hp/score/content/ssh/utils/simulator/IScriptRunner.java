package com.hp.score.content.ssh.utils.simulator;

public interface IScriptRunner extends Runnable {
    public String getoutput();

    public int getCommandsLeft();

    public boolean completed();

    public String getException();

    public void setCaptureOutput(boolean enableCapture);
}
