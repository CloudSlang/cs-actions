package com.hp.score.content.ssh.utils.simulator;

import com.hp.score.content.ssh.utils.simulator.visualization.IShellVisualizer;
import com.hp.score.content.ssh.utils.simulator.visualization.ScreenEmulator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 *
 *
 *
 */
public class ShellSimulator {
    ScriptRunner simulator;
    String exception;
    HashMap<IShellVisualizer, InputStream> visualizers;

    /**
     * @param script
     * @param matchTimeout
     * @param readTimeout
     * @param sleepTimeout
     * @param characterDelay
     * @param newLineCharacter
     * @throws Exception
     */
    public ShellSimulator(String script,
                          long matchTimeout,
                          long readTimeout,
                          long sleepTimeout,
                          long characterDelay,
                          char[] newLineCharacter)
            throws Exception {
        simulator = new ScriptRunner(script,
                matchTimeout,
                readTimeout,
                sleepTimeout,
                characterDelay,
                newLineCharacter);
        exception = "";
        visualizers = new HashMap<>();
    }


    /**
     * constructor
     *
     * @param script           command
     * @param matchTimeout
     * @param readTimeout
     * @param sleepTimeout
     * @param characterDelay   delay in milseconds for user input
     * @param newLineCharacter
     * @param charSet          a characterSet name , for example UTF-8, SJIS
     * @throws Exception
     */
    public ShellSimulator(String script,
                          long matchTimeout,
                          long readTimeout,
                          long sleepTimeout,
                          long characterDelay,
                          char[] newLineCharacter,
                          String charSet)
            throws Exception {
        simulator = new ScriptRunner(script,
                matchTimeout,
                readTimeout,
                sleepTimeout,
                characterDelay,
                newLineCharacter,
                charSet);
        exception = "";
        visualizers = new HashMap<IShellVisualizer, InputStream>();
    }

    /**
     * @param script
     * @param matchTimeout
     * @param readTimeout
     * @param sleepTimeout
     * @param characterDelay
     * @throws Exception
     */
    public ShellSimulator(String script, long matchTimeout, long readTimeout, long sleepTimeout, long characterDelay) throws Exception {
        this(script, matchTimeout, readTimeout, sleepTimeout, characterDelay, new char[]{'\n'});
    }

    /**
     * @param script
     * @param matchTimeout
     * @param readTimeout
     * @param sleepTimeout
     * @param characterDelay
     * @param newLineCharacter
     * @param charset          input character set name, for example UTF-8, SJIS
     * @param closeStreams     Set to false if using a cached SSH shell channel
     * @throws Exception
     */
    public ShellSimulator(String script, long matchTimeout, long readTimeout, long sleepTimeout, long characterDelay, char[] newLineCharacter, String charset, boolean closeStreams) throws Exception {
        simulator = new ScriptRunner(script,
                matchTimeout,
                readTimeout,
                sleepTimeout,
                characterDelay,
                newLineCharacter,
                charset,
                closeStreams);
        exception = "";
        visualizers = new HashMap<IShellVisualizer, InputStream>();
    }

    /**
     * @param script
     * @param matchTimeout
     * @param readTimeout
     * @param sleepTimeout
     * @param characterDelay
     * @param charset        input character set name, for example UTF-8, SJIS
     * @param closeStreams   Set to false if using a cached SSH shell channel
     * @throws Exception
     */
    public ShellSimulator(String script, long matchTimeout, long readTimeout, long sleepTimeout, long characterDelay, String charset, boolean closeStreams) throws Exception {
        this(script, matchTimeout, readTimeout, sleepTimeout, characterDelay, new char[]{'\n'}, charset, closeStreams);
    }

    public IShellVisualizer addVisualizer(String name) throws Exception {
        IShellVisualizer vis;
        if (name == null || name.length() == 0 || name.toLowerCase().equals("basic"))
            vis = new ScreenEmulator();
        else if (name.toLowerCase().equals("none"))
            return null;
        else
            throw new Exception("The specified visualizer is of unknown type");
        addVisualizer(vis);
        return vis;
    }

    private PipedInputStream registerVisualizer(final IShellVisualizer vis) throws IOException {
        final PipedInputStream in = new PipedInputStream();
        simulator.addPipe(in);
        return in;
    }

    public void addVisualizer(final IShellVisualizer visualizer) throws IOException {
        visualizers.put(visualizer, registerVisualizer(visualizer));
    }

    public void setStreams(InputStream in, OutputStream out) {
        simulator.in = in;
        simulator.out = out;
    }

    public String getOutput() {
        return simulator.getoutput();
    }

    public String getException() {
        exception += simulator.getException();
        if (!simulator.completed())
            exception = "Script did not fully finish, had: " + simulator.getCommandsLeft() + " commands left " + exception;
        return exception;
    }

    public boolean completed() {
        return simulator.completed();
    }

    public void run(long timeout) {
        long startTime = System.currentTimeMillis();
        simulator.start();
        // connect channel to host
        Iterator<IShellVisualizer> visuals = visualizers.keySet().iterator();
        while (visuals.hasNext()) {
            final IShellVisualizer curr = visuals.next();
            Thread t = new Thread("Visualizer Thread") {
                public void run() {
                    try {
                        curr.run(visualizers.get(curr));
                    } finally {
                        try {
                            visualizers.get(curr).close();
                        } catch (IOException e) {
                            ;
                        }
                    }
                }
            };
            t.start();
        }
        while (simulator.isAlive()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                ;
            }
            long time = System.currentTimeMillis();
            if (time - startTime > timeout) {
                exception += "timeout after: " + (time - startTime) + "ms\n";
                simulator.terminate();
                break;
            }
        }
        while (simulator.isAlive())
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                ;
            } finally {
                visuals = visualizers.keySet().iterator();
                while (visuals.hasNext()) {
                    IShellVisualizer curr = visuals.next();
                    try {
                        visualizers.get(curr).close();
                    } catch (IOException e) {
                        ;
                    }
                }
            }
    }
}



