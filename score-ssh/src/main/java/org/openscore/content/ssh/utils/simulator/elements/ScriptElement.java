package org.openscore.content.ssh.utils.simulator.elements;

public abstract class ScriptElement {

    protected static String getKey(String line) {
        line = line.trim();
        if (!line.contains(" "))
            return line.toLowerCase();
        return line.substring(0, line.indexOf(' ')).toLowerCase();
    }

    public abstract void set(String s);

    protected void setCommand(String line, String flag) {
        String toSet = line.trim().substring(flag.length());
        set(toSet);
    }
}
