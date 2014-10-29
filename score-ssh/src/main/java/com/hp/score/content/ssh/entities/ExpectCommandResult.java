package com.hp.score.content.ssh.entities;

/**
 * @author ioanvranauhp
 * Date: 10/29/14
 */
public class ExpectCommandResult extends CommandResult {
    private String expectXmlOutputs;

    public String getExpectXmlOutputs() {
        return expectXmlOutputs;
    }

    public void setExpectXmlOutputs(String expectXmlOutputs) {
        this.expectXmlOutputs = expectXmlOutputs;
    }
}
