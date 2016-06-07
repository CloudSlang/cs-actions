package io.cloudslang.content.remote.utils;

/**
 * Created by pasternd on 07/06/2016.
 */
public class RunWindowsCommandInputs {

    private String hostname;
    private String command;
    private String username;
    private String password;

    public RunWindowsCommandInputs(String hostname,  String username, String password, String command) {
        this.hostname = hostname;
        this.command = command;
        this.username = username;
        this.password = password;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
