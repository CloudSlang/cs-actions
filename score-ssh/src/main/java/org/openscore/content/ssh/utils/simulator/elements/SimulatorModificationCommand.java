package org.openscore.content.ssh.utils.simulator.elements;

import org.openscore.content.ssh.utils.simulator.IScriptRunner;

import java.util.HashMap;

public abstract class SimulatorModificationCommand extends ScriptElement {

    public static final String FLAG_CAPTURE = "captureOutput";
    public static HashMap<String, Class<? extends SimulatorModificationCommand>> sends = new HashMap<String, Class<? extends SimulatorModificationCommand>>();

    static {
        sends.put(FLAG_CAPTURE.toLowerCase(), SetSimulatorCapture.class);
    }

    protected IScriptRunner runner;

    public static SimulatorModificationCommand getInstance(String line, IScriptRunner simulator) throws Exception {
        SimulatorModificationCommand command;

        String flag = getKey(line);
        if (sends.containsKey(flag)) {
            command = sends.get(flag).newInstance();
        } else
            throw new Exception("unsupported send operation: " + line + " flag was: " + flag);

        command.runner = simulator;
        command.setCommand(line, flag);

        return command;
    }

    public abstract void execute();
}