package com.hp.score.content.ssh.utils.simulator.elements;

public class SetSimulatorCapture extends SimulatorModificationCommand {

    boolean enable;

    @Override
    public void execute() {
    }

    @Override
    public void set(String s) {
        enable = Boolean.parseBoolean(s);
    }

}
