package org.eclipse.score.content.ssh.utils.simulator.elements;

public class SendChar extends Send {

    /**
     * This method sets a command in a char format. If a bad char is provided
     * then an empty char will be set.
     * @param command
     */
    public void set(String command) {
        int i = 0;
        if( command!= null ) {
            try {
                i = Integer.parseInt(command.trim());
            } catch (NumberFormatException ex) {
                i = 0;
            }
        }
        this.command = new char[]{(char) i};
    }
}