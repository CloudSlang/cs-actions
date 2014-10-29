package com.hp.score.content.ssh.utils.simulator.visualization;


public abstract class ScreenTask {
    //QCCR 92359 - make it to deal with byte instead of
    //char, since char number is from 0 - 255 while byte is from -128 ~ 127
    public abstract void process(int x, int y, ByteWrapper val);

    public boolean completed() {
        return false;
    }

    public boolean nextLine() {
        return false;
    }
}
