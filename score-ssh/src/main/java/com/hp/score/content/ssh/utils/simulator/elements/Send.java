package com.hp.score.content.ssh.utils.simulator.elements;

import java.util.Arrays;
import java.util.HashMap;

public class Send extends ScriptElement {
    public static final String FLAG_SEND = "send";
    public static final String FLAG_SEND_CHAR = "sendchar";
    public static HashMap<String, Class<? extends Send>> sends = new HashMap<>();

    static {
        sends.put(FLAG_SEND, Send.class);
        sends.put(FLAG_SEND_CHAR, SendChar.class);
    }

    protected char[] command;
    long toWait;
    char[] newLineCharacters;

    public static Send getInstance(String line, char[] newLineCharacters) throws Exception {
        Send parsed;
        String flag = getKey(line);
        if (sends.containsKey(flag)) {
            parsed = sends.get(flag).newInstance();
        } else
            throw new Exception("unsupported send operation: " + line + " flag was: " + flag);
        if (newLineCharacters == null) {
            parsed.newLineCharacters = new char[0];
        } else {
            parsed.newLineCharacters = Arrays.copyOf(newLineCharacters, newLineCharacters.length);
        }
        parsed.setCommand(line, flag);
        return parsed;
    }

    public void set(String command) {
        if (newLineCharacters == null)
            newLineCharacters = new char[]{'\n'};
        this.command = (command.trim() + new String(newLineCharacters)).toCharArray();
    }

    public char[] get() {
        return command;
    }

    public void setWaitTime(long ToWait) {
        this.toWait = ToWait;
    }

    public boolean waitIfNeeded() throws InterruptedException {
        if (toWait != 0) {
            Thread.sleep(toWait);
            toWait = 0;
            return true;
        }
        return false;
    }
}