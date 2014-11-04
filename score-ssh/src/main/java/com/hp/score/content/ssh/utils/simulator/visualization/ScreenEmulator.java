package com.hp.score.content.ssh.utils.simulator.visualization;

import com.jcraft.jcterm.Connection;
import com.jcraft.jcterm.Emulator;
import com.jcraft.jcterm.EmulatorVT100;
import com.jcraft.jcterm.Term;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


public class ScreenEmulator implements Term, IShellVisualizer {
    public static final String STR_FRAME = "Frame";
    ByteWrapper[][] buffer;
    final ArrayList<Element> frames;

    public ScreenEmulator() {
        initBuffer();
        frames = new ArrayList<>();
    }

    public void run(final InputStream in) {
        start(new Connection() {

            public InputStream getInputStream() {
                return in;
            }

            public java.io.OutputStream getOutputStream() {
                return null;
            }

            public void requestResize(Term term) {

            }

            public void close() {

            }
        });
    }

    public String getXMLSummary() {
        this.snapshotBuffer(true);
        Element d = DocumentFactory.getInstance().createElement("Frames");
        d.setText("\n");

        synchronized (frames) {
            for (Element curr : frames) {
                d.add(curr);
                d.addText("\n");
            }
        }
        return d.asXML();
    }

    private void initBuffer() {
        buffer = new ByteWrapper[24][80];
        for (int row = 0; row < buffer.length; row++)
            for (int col = 0; col < buffer[0].length; col++) {
                buffer[row][col] = new ByteWrapper(this);
            }
    }

    /**
     * It will transfer the buffer which gets the PipedInputStream from Emulator
     * into a string separated by "\n". buffer is a 2 dementional array. each row
     * represents one part of the string. For example, the string is like
     * "string for row[0]\n string from row[1]\n string from row[2]...."
     * Emulator updates the buffer[24][80] into a VT100 screen displayable format
     * each cell in buffer is a object ByteWrapper
     *
     * @param clearChanges indicate whether need to clear changes
     * @return a converted string which represents the stream ni a VT100 screen
     * displayable format.
     */
    public String buffAsString(boolean clearChanges) {
        String s = null;
        byte[][] byteArray = toByteArray(clearChanges);
        for (byte[] curr : byteArray) {
            try {
                if (s == null) {
                    s = new String(curr, "UTF-8").trim();
                } else {
                    s += "\n" + new String(curr, "UTF-8").trim();
                }
            } catch (UnsupportedEncodingException e) {
                //do nothing
            }
        }
        return s;
    }

    String snapshotBuffer(boolean autoStore) {
        String s = buffAsString(autoStore);
        if (s.trim().length() == 0)
            return s;
        if (frames.size() > 0 && frames.get(frames.size() - 1).getName().equals(STR_FRAME)) {
            String old = frames.get(frames.size() - 1).getTextTrim();
            if (s.trim().equals(old) || old.trim().endsWith(s.trim()) || old.trim().startsWith(s.trim()))
                return s;
        }
        if (autoStore) {
            Element curr = DocumentFactory.getInstance().createElement(STR_FRAME);
            curr.addAttribute("index", "" + (frames.size() + 1));
            curr.setText(s.trim());
            frames.add(curr);
        }
        return s;
    }

    /**
     * @return a 2 Dementional byte[24][80 array which is in screen presentable format
     * based on buffer, buffer is a 2 demontional ByteWrapper array.
     */
    private byte[][] toByteArray(final boolean resetChanges) {
        final byte[][] converted = new byte[buffer.length][buffer[0].length];
        runTask(new ScreenTask() {
            public void process(int row, int col, ByteWrapper val) {
                converted[row][col] = val.getValue();
                if (resetChanges)
                    val.clearChanged();
            }
        }, 0, 0, buffer.length, buffer[0].length);
        return converted;
    }

    private synchronized void runTask(ScreenTask task, int minrow, int mincol, int maxrow, int maxcol) {
        runTask(task, minrow, mincol, maxrow, maxcol, false, false);
    }

    private synchronized void runTask(ScreenTask task, int minrow, int mincol, int maxrow, int maxcol, boolean reverseRowOrder, boolean reverseColOrder) {
        try {
            int col = (reverseColOrder) ? maxcol - 1 : mincol;
            int row = (reverseRowOrder) ? maxrow - 1 : minrow;
            while (row < maxrow && row >= minrow) {
                while (col < maxcol && col >= mincol) {

                    task.process(row, col, buffer[row][col]);
                    if (task.nextLine()) {
                        break;
                    }
                    col = (reverseColOrder) ? col - 1 : col + 1;
                }
                col = 0;
                if (task.completed()) {
                    break;
                }
                row = (reverseRowOrder) ? row - 1 : row + 1;
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void runTask(ScreenTask task, int minrow, int mincol) {
        runTask(task, minrow, mincol, buffer.length, buffer[0].length);
    }

    private byte get(int row, int col) {
        return buffer[row][col].getValue();
    }

    //All the following methods are implementation for interface Term
    public void beep() {
        snapshotBuffer(true);
        Element curr = DocumentFactory.getInstance().createElement("Beep");
        curr.addAttribute("lastFrameIndex", "" + frames.size());
        frames.add(curr);
    }

    public void clear() {
        this.snapshotBuffer(true);
        initBuffer();
    }

    public void clear_area(int col, int row, int maxCol, int maxRow) {
        runTask(new ScreenTask() {
            public void process(int row, int col, ByteWrapper val) {
                val.clear();
            }
        }, row, col, maxRow - 1, maxCol);
    }

    /**
     * drawBytes will be called when Emulator determines the bytes from offset
     * till offset + length are ascii.
     *
     * @param arg0,  byte[1024] array from Emulator,
     * @param offset will tell where is the starting byte in the arg0
     * @param length length indicates how long the string will be.
     * @param mincol is equivalent to x in Emulator , meaning starting col
     * @param minrow is equivalent to y in Emulator, meaning starting row
     */
    public void drawBytes(final byte[] arg0, final int offset, int length, int mincol, int minrow) {
        //minrow equivalent to y in Emulator.java, however the y starts with 1
        //so minus 1 to make row starting from 0 for buffer
        minrow--;
        int maxRow = (mincol + length) / buffer[0].length + minrow + 1;
        int maxCol = mincol + length;

        runTask
                (
                        new LinearUpdateTask(length) {
                            public byte process(int col) {
                                return (byte) (arg0[col + offset]);
                            }
                        }, minrow, mincol, maxRow, maxCol
                );
    }

    /**
     * drawString will be called when Emulator determines the bytes are minus bytes
     * non-ascii. It is called for each multi-bytes char.
     *
     * @param arg0, a UTF-8 encoded string from Emulator. Usually the string is
     *              multiple bytes. For example 3 bytes JP char.
     * @param col   is equivalent to x in Emulator , meaning starting col
     * @param row   is equivalent to y in Emulator, meaning starting row
     */
    public void drawString(final String arg0, int col, int row) {
        //row equivalent to y in Emulator.java, however the y starts with 1
        //so minus 1 to make row starting from 0 for buffer
        row--;
        byte[] argBytes = null;
        try {
            argBytes = arg0.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            //do nothing;
        }
        final byte[] argBytesFinal = argBytes;

        runTask
                (new LinearUpdateTask(argBytesFinal.length) {
                    public byte process(int offset) {
                        return argBytesFinal[offset];
                    }
                }, row, col);
    }

    public void draw_cursor() {
        //NOT IMPLEMENTED
    }

    public int getCharHeight() {
        return 1;
    }

    public int getCharWidth() {
        return 1;
    }

    public Object getColor(int arg0) {
        return null;
    }

    public int getColumnCount() {
        return buffer[0].length;
    }

    public int getRowCount() {
        return buffer.length;
    }

    public int getTermHeight() {
        return getCharHeight() * getRowCount();
    }

    public int getTermWidth() {
        return getCharWidth() * getColumnCount();
    }

    public void redraw(int arg0, int arg1, int arg2, int arg3, final boolean isSnapshot) {
        //NOT IMPLEMENTED
    }

    public void redraw(int arg0, int arg1, int arg2, int arg3) {
        //redraw(arg0, arg1, arg2, arg3, false);
    }

    public void resetAllAttributes() {
    }

    public void scroll_area(int icol, int irow, int w, int h, final int dcol, final int drow) {
        final ScreenEmulator emu = this;
        runTask(new ScreenTask() {
            public void process(int row, int col, ByteWrapper val) {
                int orow = row - drow;
                int ocol = col - dcol;
                if (emu.getColumnCount() > ocol &&
                        emu.getRowCount() > orow && orow >= 0 && ocol >= 0) {
                    val.setValue(get(orow, ocol));
                }
            }
        }, irow, icol, irow + h, icol + w, drow > 0, dcol > 0);
        runTask(new ScreenTask() {
            public void process(int row, int col, ByteWrapper val) {
                char empty = ' ';
                int orow = row + drow;
                int ocol = col + dcol;
                if (emu.getColumnCount() <= ocol || emu.getRowCount() <= orow
                        || ocol < 0 || orow < 0) {
                    val.setValue((byte) (empty & 0xFF));
                }
            }
        }, irow, icol, irow + h, icol + w, drow > 0, dcol > 0);
    }

    public void setBackGround(Object arg0) {
        //NOT IMPLEMENTED
    }

    public void setBold() {
        //NOT IMPLEMENTED
    }

    public void setCursor(int arg0, int arg1) {
        //NOT IMPLEMENTED
    }

    public void setDefaultBackGround(Object arg0) {
        //NOT IMPLEMENTED
    }

    public void setDefaultForeGround(Object arg0) {
        //NOT IMPLEMENTED
    }

    public void setForeGround(Object arg0) {
        //NOT IMPLEMENTED
    }

    public void setReverse() {
        //NOT IMPLEMENTED
    }

    public void setUnderline() {
        //NOT IMPLEMENTED
    }

    public void start(Connection connection) {
        Emulator emulator = new EmulatorVT100(this, connection.getInputStream());
        emulator.reset();
        emulator.start();
        redraw(0, 0, getTermWidth(), getTermHeight());
    }

}
