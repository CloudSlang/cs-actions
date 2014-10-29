package com.hp.score.content.ssh.utils.simulator.elements;

import com.hp.score.content.ssh.utils.simulator.SafeMatcher;

import java.util.HashMap;

public class Expect extends ScriptElement {
    public static final String FLAG_EXPECT = "expect";
    public static final String FLAG_EXPECT_ENDS_WITH = "expectendswith";
    public static final String FLAG_EXPECT_LAST_LINE = "expectlastline";
    public static HashMap<String, Class<? extends Expect>> expects = new HashMap<>();

    static {
        expects.put(FLAG_EXPECT, Expect.class);
        expects.put(FLAG_EXPECT_ENDS_WITH, ExpectEndsWith.class);
        expects.put(FLAG_EXPECT_LAST_LINE, ExpectLastLine.class);
    }

    protected String command;
    //	safeMatch.match(current.trim(),, matchTimeout)){
    long toWait;
    Expect error;

    public static Expect getInstance(String line) throws Exception {
        Expect parsed;
        String flag = getKey(line);
        if (expects.containsKey(flag)) {
            parsed = expects.get(flag).newInstance();
        } else
            throw new Exception("unsupported expect operation: " + line + " flag was: " + flag);
        parsed.setCommand(line, flag);
        return parsed;
    }

    public void matchError(String toMatch, long timeout) throws Exception {
        if (error != null)
            if (error.match(toMatch, timeout))
                throw new Exception("Error condition matched: " + error.command + " to: " + toMatch);
    }

    public boolean match(String toMatch, long timeout) throws Exception {
        matchError(toMatch, timeout);
        return SafeMatcher.match(toMatch, command, timeout);
    }

    public void set(String command) {
        this.command = command.trim();
    }

    public void setError(Expect error) {
        this.error = error;
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