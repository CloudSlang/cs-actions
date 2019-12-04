package io.cloudslang.content.hashicorp.terraform.utils;

import io.cloudslang.content.constants.InputNames;


public class Inputs extends InputNames {

    public static class CommonInputs {
        public static final String AUTH_TOKEN = "authToken";
        public static final String PROXY_HOST = "proxyHost";
        public static final String PROXY_PORT = "proxyPort";
        public static final String PROXY_USERNAME = "proxyUsername";
        public static final String PROXY_PASSWORD = "proxyPassword";
        public static final String REQUEST_BODY = "requestBody";
        public static final String EXECUTION_TIMEOUT = "executionTimeout";
        public static final String POLLING_INTERVAL = "pollingInterval";
        public static final String ASYNC = "async";
        public static final String ORGANIZATION_NAME = "organizationName";

    }


    public static class ApplyRunInputs {
        public static final String RUN_ID = "runId";
        public static final String RUN_COMMENT = "runComment";
    }

    public static class CreateVariableInputs {
        public static final String VARIABLE_NAME = "variableName";
        public static final String VARIABLE_VALUE = "variableValue";
        public static final String VARIABLE_CATEGORY = "variableCategory";
        public static final String SENSITIVE = "sensitive";
        public static final String HCL = "hcl";
    }


}
