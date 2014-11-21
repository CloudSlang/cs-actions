package org.eclipse.score.content.ssh.utils.simulator.elements;

import org.eclipse.score.content.ssh.utils.simulator.SafeMatcher;

public class ExpectLastLine extends Expect {

    public boolean match(String toMatch, long timeout) throws Exception {
        toMatch = toMatch.replace("\r", "\n");
        String[] lines = toMatch.trim().split("\n");
        toMatch = lines[lines.length - 1];
        matchError(toMatch, timeout);
        return SafeMatcher.match(toMatch, command, timeout);
    }

}