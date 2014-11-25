package org.eclipse.score.content.ssh.utils.simulator.elements;

public class ExpectEndsWith extends Expect {

    public boolean match(String toMatch, long timeout) throws Exception {
        if(toMatch != null) {
            matchError(toMatch, timeout);
            return toMatch.trim().endsWith(command);
        } else {
            return false;
        }
    }

}