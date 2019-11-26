package io.cloudslang.content.hashicorp.terraform.utils;


import io.cloudslang.content.constants.OutputNames;

public class Outputs extends OutputNames {
    public static class CommonOutputs {
        public static final String DOCUMENT = "document";
        public static final String ATTACHMENT_ID = "attachment_id";
    }

    public static class AuthorizationOutputs {
        public static final String AUTH_TOKEN = "authToken";
        public static final String AUTH_TOKEN_TYPE = "authTokenType";
    }

    public static class GetAttachmentsOutputs {
        public static final String CONTENT_NAME = "contentName";
        public static final String CONTENT_TYPE = "contentType";
        public static final String CONTENT_BYTES = "contentBytes";
        public static final String CONTENT_SIZE = "contentSize";
    }


}
