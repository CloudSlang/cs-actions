/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.azure.utils;

public class DescriptionConstants {

    public static class OperationDescription {
        public static final String GET_AUTH_TOKEN_DESC = "This operation retrieves the authentication Bearer token for Azure.";
    }

    public static class InputsDescription {
        public static final String USERNAME_DESC = "The username used to authenticate to the Azure Management Service.";
        public static final String PASSWORD_DESC = "The password used to authenticate to the Azure Management Service.";
        public static final String CLIENT_ID_DESC = "The client ID.";
        public static final String LOGIN_AUTHORITY_DESC = "URL of the login authority that should be used when retrieving the Authentication Token.\n" +
                "Default: \"https://login.windows.net/common\"";
        public static final String RESOURCE_DESCRIPTION = "Resource URl for which the Authentication Token is intended.\n" +
                "Default: \"https://management.azure.com/\"";
        public static final String PROXY_HOST_DESC = "Proxy server used to access the web site.";
        public static final String PROXY_PORT_DESC = "The port to connect to the proxy server. Enter both proxyHost and proxyPort only if you use proxy server.\n" +
                "Default: \"8080\"";
        public static final String PROXY_USERNAME_DESC = "User name used when connecting to the proxy.\n" +
                "Default: \"\"";
        public static final String PROXY_PASSWORD_DESC = "The proxy server password associated with the proxyUsername input value.\n" +
                "Default: \"\"";
    }

    public static class OutputsDescription {
        public static final String RETURN_RESULT_DESC = "The authorization Bearer token for Azure.";
        public static final String RETURN_CODE_DESC = "0 if success, -1 otherwise.";
        public static final String EXCEPTION_DESC = "An exception in case there was an error.";
    }

    public static class ResultsDescription {
        public static final String SUCCESS_DESC = "The operation completed successfully.";
        public static final String FAILURE_DESC = "An error occurred during execution.";
    }
}
