package io.cloudslang.content.httpclient.utils;

import io.cloudslang.content.constants.OutputNames;

public class Outputs extends OutputNames {
    public static class HTTPClientOutputs {
        public static final String STATUS_CODE = "statusCode";
        public static final String FINAL_LOCATION = "finalLocation";
        public static final String RESPONSE_HEADERS = "responseHeaders";
        public static final String PROTOCOL_VERSION = "protocolVersion";
        public static final String REASON_PHRASE = "reasonPhrase";
    }
}
