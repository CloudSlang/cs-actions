package org.eclipse.score.content.ssh.utils.simulator;

import org.eclipse.score.content.ssh.utils.simulator.elements.AlwaysOn;
import org.eclipse.score.content.ssh.utils.simulator.elements.Expect;
import org.eclipse.score.content.ssh.utils.simulator.elements.Send;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;

class ScriptRunner extends Thread implements IScriptRunner {

    private static final String DEFAULT_CHARACTER_SET = "UTF-8";
    public InputStream in;
    public OutputStream out;
    ScriptModel parser;
    boolean captureOutput;
    private List<String> stdout;
    private List<PipedOutputStream> pipes;
    private String current;
    private char[] toSend;
    private String exception;
    private int postCompleteReads;
    private long readTimeout;
    private long lastReadTime;
    private long matchTimeout;
    private long sleepTime;
    private long characterDelay;
    private boolean closeStreams;
    private String characterSet = DEFAULT_CHARACTER_SET;

    /**
     * ScriptRunner Constructor
     *
     * @param script
     * @param matchTimeout
     * @param readTimeout
     * @param sleepTimeout
     * @param characterDelay
     * @param newLineCharacter
     * @throws Exception
     */
    public ScriptRunner(String script, long matchTimeout, long readTimeout, long sleepTimeout, long characterDelay, char[] newLineCharacter) throws Exception {
        parser = new ScriptModel(script, newLineCharacter);
        stdout = new ArrayList<>();
        pipes = new ArrayList<>();
        current = "";
        toSend = new char[0];
        postCompleteReads = 0;
        this.readTimeout = readTimeout;
        this.matchTimeout = matchTimeout;
        sleepTime = sleepTimeout;
        this.characterDelay = characterDelay;
        exception = "";
        captureOutput = true;
        closeStreams = true;
        this.characterSet = DEFAULT_CHARACTER_SET;
    }

    /**
     * constructor to allow user's characterSet
     *
     * @param script           the command
     * @param matchTimeout
     * @param readTimeout
     * @param sleepTimeout
     * @param characterDelay   delay in milseconds for user input
     * @param newLineCharacter
     * @param charSet          input character set name, for example UTF-8, SJIS
     * @throws Exception
     */
    public ScriptRunner(String script,
                        long matchTimeout,
                        long readTimeout,
                        long sleepTimeout,
                        long characterDelay,
                        char[] newLineCharacter,
                        String charSet) throws Exception {
        this(script,
                matchTimeout,
                readTimeout,
                sleepTimeout,
                characterDelay,
                newLineCharacter);
        this.characterSet = charSet;
    }

    /**
     * @param script
     * @param matchTimeout
     * @param readTimeout
     * @param sleepTimeout
     * @param characterDelay
     * @param newLineCharacter
     * @param charset          input character set name, for example UTF-8, SJIS
     * @param closeStreams     If using a cached shell channel, closing the streams would cause the channel to close.
     * @throws Exception
     */
    public ScriptRunner(String script, long matchTimeout, long readTimeout, long sleepTimeout, long characterDelay, char[] newLineCharacter, String charset, boolean closeStreams) throws Exception {
        this(script, matchTimeout, readTimeout, sleepTimeout, characterDelay, newLineCharacter, charset);
        this.closeStreams = closeStreams;
    }

    public void addPipe(java.io.PipedInputStream in) throws IOException {
        if( in != null ) {
            pipes.add(new PipedOutputStream(in));
        }
    }

    private void pipe(byte[] read, int offset, int length) throws IOException {
        for (PipedOutputStream pipe : pipes) {
            pipe.write(read, offset, length);
        }
    }

    public String getOutput() {
        StringBuilder output = new StringBuilder();
        while (stdout.size() > 0) {
            output.append(stdout.remove(0));
        }
        return output.toString();
    }

    public void setCaptureOutput(boolean enableCapture) {
        this.captureOutput = enableCapture;
    }

    /**
     * Returns the number of commands left in the ScriptModel (which parses the script)
     */
    public int getCommandsLeft() {
        return parser.getCommandsLeft();
    }

    /**
     * @return true if no more commands are left for the ScriptModel to process
     */
    public boolean noMoreCommandsLeft() {
        return getCommandsLeft() <= 0;
    }

    /**
     * @return a string of all the exceptions that occurred while running the script
     */
    public String getException() {
        return exception.trim();
    }

    /**
     * @param exception
     */
    private synchronized void addException(Exception exception) {
        //exception.printStackTrace();
        this.exception += "\n" + exception + "\n Instructions left: " + getCommandsLeft() + "\n";
    }

    private synchronized void clearMatchExceptions() {
        String[] split = exception.split("\n");
        exception = "";
        for (String aSplit : split) {
            if (!aSplit.contains("match") && aSplit.length() > 0)
                exception += aSplit;
        }
        this.exception = "";
    }

    public void terminate() {
        addException(new Exception("timedOut"));
        for (int count = 0; count < 10; count++) {
            incrementPostCompleteReads();
        }
    }

    private synchronized void incrementPostCompleteReads() {
        postCompleteReads++;
    }

    private synchronized void resetPostCompleteReads() {
        postCompleteReads = 0;
    }

    public synchronized long getDeltaT() {
        return System.currentTimeMillis() - lastReadTime;
    }

    public void run() {
        long deltaT = 0;
        setReadTime();
        try {
            while (postCompleteReads < 5 && (deltaT = getDeltaT()) <= readTimeout) {
                boolean updated;
                updated = process();

                if (noMoreCommandsLeft()) {
                    clearMatchExceptions();
                    incrementPostCompleteReads();
                    if (updated)
                        resetPostCompleteReads();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        //don't care if we are interrupted.
                    }
                }

            }
            //if this is used for SSH shell ops, closing the output stream will close the SHELL channel
            //This isn't desired if multiple SSH Shell Operations are sharing the same SHELL channel.
            if (closeStreams) {
                if(in != null) {
                    in.close();
                }
                if(out != null) {
                    out.close();
                }
            }
        } catch (Exception e) {
            addException(e);
        }
        if (!noMoreCommandsLeft() && deltaT >= readTimeout)
            addException(new Exception("readTimeout at: " + deltaT + "ms"));
        if (captureOutput)
            this.stdout.add(current);
        current = "";
    }

    public boolean checkAlwaysHandlers() {
        try {
            AlwaysOn curr = parser.checkAlwaysHandlers(current, matchTimeout);
            if (curr != null) {
                if (captureOutput)
                    this.stdout.add(current);
                current = "";
                toSend = curr.get();
                clearMatchExceptions();
                return true;
            } else
                return false;
        } catch (Exception ex) {
            addException(ex);
            return false;
        }
    }

    boolean sendable() throws Exception {
        if (toSend != null && toSend.length > 0)  //there are commands to send.
            return true;
        if (noMoreCommandsLeft())
            return false;
        try {
            Expect e = parser.IsExceptAllowed(current, this, matchTimeout);
            if (e != null) {
                if (e.match(current.trim(), matchTimeout)) {
                    if (captureOutput)
                        this.stdout.add(current);
                    current = "";
                    clearMatchExceptions();
                    return sendable();
                }
            }
        } catch (Exception ex) {
            if (checkAlwaysHandlers())
                return true;
            else
                addException(ex);
        }
        Send s = parser.IsSendAllowed();
        if (s != null) {
            if (s.waitIfNeeded())
                setReadTime();
            toSend = s.get();
            return true;
        }
        return checkAlwaysHandlers();
    }

    /**
     * updates the last time the ScriptRunner has performed a read to current time.
     */
    public synchronized void setReadTime() {
        lastReadTime = System.currentTimeMillis();
    }

    /**
     * @param in
     * @return
     */
    public String removeAsciiEscapes(String in) {
        String out = in.replaceAll("" + (char) 27 + "\\[.*?m", "");//color codes
        //out = out.replaceAll(""+(char)27+"\\[\\d+;\\d+?H", "");//from windows terminal
        return out;
    }

    /**
     * @return
     * @throws Exception
     */
    public boolean process() throws Exception {
        boolean wasRead = false;
        if(in == null) {
            return false;
        }
        while (in.available() > 0) {
            wasRead = true;
            byte[] buff = new byte[in.available()]; //this is max size of buffer to avoid boundery condition
            int read;
            try {
                read = in.read(buff);
            } catch (java.net.SocketException e) {//telnet stream throws one on first read after stream is closed remotely.
                if (noMoreCommandsLeft())
                    read = in.read(buff);
                else
                    throw e;
            }
            if (read > 0) {
                //TODO: this should use a CharsetDecoder and a ByteBuffer instead.
                // A common helper method that does this is not tested and ready yet. 
                String cleanString = removeAsciiEscapes(new String(buff, 0, read, this.characterSet));
                current += cleanString;

                if (captureOutput && cleanString != null) {
                    byte[] outPipeToVisualizer = cleanString.getBytes("UTF-8");
                    if (outPipeToVisualizer != null && outPipeToVisualizer.length != 0) {
                        pipe(outPipeToVisualizer, 0, outPipeToVisualizer.length);
                        //pipe(buff, 0, read);
                    }
                }
            }

        }
        boolean sent = false;
        while (sendable()) {
            sent = true;
            byte[] bytes = new String(toSend).getBytes(this.characterSet);
            if (characterDelay > 0) {
                //delay each character write by sleepTime
                for (int i = 0; i < toSend.length; i++) {
                    out.write(bytes, i, 1);
                    out.flush();
                    try {
                        Thread.sleep(characterDelay);
                    } catch (InterruptedException e) {
                        //don't care if we are interrupted.
                    }
                }
            } else {
                //no delay
                out.write(bytes);
                out.flush();
            }

            toSend = new char[0];
        }

        if (wasRead || sent) {
            setReadTime();
            return true;
        } else {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                //don't care if we are interrupted.
            }
            return false;
        }
    }
}
