package com.hp.score.content.ssh.utils.simulator.visualization;

public abstract class linearUpdateTask extends ScreenTask {
    int maxOffset;
    int offsetIndex;
    boolean completed;
    boolean nextLine;
    int newlines;

    public linearUpdateTask(int length) {
        this.maxOffset = length;
        completed = false;
        nextLine = false;
        newlines = 0;
        offsetIndex = 0;
    }

    /**
     * QCCR 92359 - make it to deal with byte instead of
     * char, since char number is from 0 - 255 while byte is from -128 ~ 127
     * so we can handle JP char
     */
    public abstract byte process(int offset);

    public final void process(int x, int y, ByteWrapper val) {
        nextLine = false;
        byte read = process(offsetIndex);

        if (read == (byte) ('\r' & 0xFF) || (read == (byte) ('\n' & 0xFF)
                && process(offsetIndex - 1) != (byte) ('\r' & 0xFF))) {
            nextLine = true;
        } else {
            val.setValue(read);
        }
        offsetIndex++;
        if (offsetIndex >= maxOffset)
            completed = true;
    }

    public boolean completed() {
        return completed;
    }

    public boolean nextLine() {
        return nextLine || completed;
    }

}
