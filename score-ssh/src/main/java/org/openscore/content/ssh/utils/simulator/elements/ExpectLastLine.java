package org.openscore.content.ssh.utils.simulator.elements;

import org.openscore.content.ssh.utils.simulator.SafeMatcher;

public class ExpectLastLine extends Expect {

    public boolean match(String toMatch, long timeout) throws Exception {
        if( toMatch != null ) {
            toMatch = toMatch.replace("\r", "\n");
            String[] lines = toMatch.trim().split("\n");
            toMatch = lines[lines.length - 1];
            matchError(toMatch, timeout);
            return SafeMatcher.match(toMatch, command, timeout);
        } else {
            return false;
        }
    }

}