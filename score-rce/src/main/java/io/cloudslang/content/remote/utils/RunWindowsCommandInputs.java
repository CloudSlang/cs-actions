package io.cloudslang.content.remote.utils;

/**
 * Created by pasternd on 07/06/2016.
 */
public class RunWindowsCommandInputs {

    private String hostname;
    private String command;
    private String username;
    private String password;

    public RunWindowsCommandInputs(RunWindowsCommandInputsBuilder builder) {
        this.hostname = builder.host;
        this.command = builder.command;
        this.username = builder.username;
        this.password = builder.password;
    }

    public String getHostname() {
        return hostname;
    }

    public String getCommand() {
        return command;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public static class RunWindowsCommandInputsBuilder {
        private String host;
        private String username;
        private String password;
        private String command;

        public RunWindowsCommandInputs build() {
            return new RunWindowsCommandInputs(this);
        }

        public RunWindowsCommandInputsBuilder withHostname(String inputValue) {
            host = inputValue;
            return this;
        }

        public RunWindowsCommandInputsBuilder withUsername(String inputValue) throws Exception {
            username = inputValue;
            return this;
        }

        public RunWindowsCommandInputsBuilder withPassword(String inputValue) throws Exception {
            password = inputValue;
            return this;
        }

        public RunWindowsCommandInputsBuilder withCommand(String inputValue) throws Exception {
            command = inputValue;
            return this;
        }
    }

}
