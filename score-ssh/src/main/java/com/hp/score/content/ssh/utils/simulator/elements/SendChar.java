package com.hp.score.content.ssh.utils.simulator.elements;

public class SendChar extends Send {
    public void set(String command) {
        int i = Integer.parseInt(command.trim());
        this.command = new char[]{(char) i};
    }
}