package org.eclipse.score.content.ssh.utils.simulator.visualization;


public abstract class ScreenTask {

    public abstract void process(int x, int y, ByteWrapper val);

    public boolean completed() {
        return false;
    }

    public boolean nextLine() {
        return false;
    }
}
