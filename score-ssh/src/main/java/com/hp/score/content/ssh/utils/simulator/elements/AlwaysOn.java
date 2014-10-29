package com.hp.score.content.ssh.utils.simulator.elements;

import com.hp.score.content.ssh.utils.simulator.ScriptLines;

public class AlwaysOn {
    Expect expect;
    Send send;

    public static AlwaysOn getInstance(ScriptLines script, char[] newLineCharacter) throws Exception {
        AlwaysOn command = new AlwaysOn();
        String line = script.getCurrentLine();
        line = line.substring(line.indexOf(" "));
        command.setCommand(Send.getInstance(line, newLineCharacter));

        if (!script.nextLine())
            throw new Exception("Always statement started on last line: always statements must have 'on' starting the next line");
        line = script.getCurrentLine();
        if (!line.startsWith("on"))
            throw new Exception("always statements must have 'on' starting the next line");
        line = line.substring(line.indexOf(" "));
        command.setExpect(Expect.getInstance(line));
        return command;
    }

    public void setCommand(Send command) {
        this.send = command;
    }

    public char[] get() {
        return send.get();
    }

    public boolean match(String toMatch, long timeout) throws Exception {
        return expect.match(toMatch, timeout);
    }

    public void setExpect(Expect command) {
        this.expect = command;
    }
}