package io.cloudslang.content.hashicorp.terraform.utils;

import io.cloudslang.content.constants.InputNames;

public class Inputs extends InputNames {
    public static class CommonInputs{

        public static final String AUTH_TOKEN = "authToken";
        public static final String PROXY_HOST = "proxyHost";
        public static final String PROXY_PORT = "proxyPort";
        public static final String PROXY_USERNAME = "proxyUsername";
        public static final String PROXY_PASSWORD = "proxyPassword";
        public static final String REQUEST_BODY = "requestBody";
        public static final String EXECUTION_TIMEOUT = "executionTimeout";
        public static final String POLLING_INTERVAL = "pollingInterval";
        public static final String ASYNC = "async";

    }




 public static class GetRunDetailInputs{
     public static final String RUN_ID = "runId";
     public static final String RUN_ID_DESC = "runIdDescription";

}

public static class CreateWorkspaceInputs{
        public static final String WORKSPACE_NAME="workspaceName";
}

}
