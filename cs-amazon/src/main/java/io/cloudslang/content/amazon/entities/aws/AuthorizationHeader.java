package io.cloudslang.content.amazon.entities.aws;

/**
 * Created by Mihai Tusa.
 * 8/8/2016.
 */
public class AuthorizationHeader {
    private final String signedHeaders;
    private final String signature;

    public AuthorizationHeader(String signedHeaders, String signature) {
        this.signedHeaders = signedHeaders;
        this.signature = signature;
    }

    public String getAuthorizationHeader() {
        return signedHeaders;
    }

    public String getSignature() {
        return signature;
    }
}