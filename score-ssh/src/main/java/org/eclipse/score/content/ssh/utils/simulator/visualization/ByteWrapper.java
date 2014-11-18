package org.eclipse.score.content.ssh.utils.simulator.visualization;

public class ByteWrapper {
    private byte value;
    private boolean isChanged;
    private ScreenEmulator screenEmulator;

    ByteWrapper(ScreenEmulator aEmu) {
        char empty = ' ';
        screenEmulator = aEmu;
        value = (byte) (empty & 0xFF);
    }

    public byte getValue() {
        return value;
    }

    public void setValue(byte aByteVal) {
        if (isChanged) {
            screenEmulator.snapshotBuffer(true);
        }
        value = aByteVal;
        isChanged = true;
    }

    void clearChanged() {
        isChanged = false;
    }

    /**
     * reset the value to empty, indicate if it is changed or not
     */
    public void clear() {
        char empty = ' ';
        if (value != (byte) (empty & 0xFF)) {
            if (isChanged)
                setValue((byte) (empty & 0xFF));
            else
                value = (byte) (empty & 0xFF);
        } else {
            isChanged = false;
        }
    }
}
