package org.eclipse.score.content.ssh.utils.simulator.elements;

public class ExpectEndsWith extends Expect {

    public boolean match(String toMatch, long timeout) throws Exception {
        matchError(toMatch, timeout);
        return toMatch.trim().endsWith(command);
    }

}