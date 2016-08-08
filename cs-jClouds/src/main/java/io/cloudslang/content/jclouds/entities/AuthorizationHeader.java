package io.cloudslang.content.jclouds.entities;

import io.cloudslang.content.jclouds.entities.constants.Constants;

/**
 * Created by Mihai Tusa.
 * 8/8/2016.
 */
public class AuthorizationHeader {
    private String signingAlgorithm;
    private String amzCredential;
    private String signedHeaders;
    private String signature;

    public AuthorizationHeader() {
        signingAlgorithm = Constants.Miscellaneous.EMPTY;
        amzCredential = Constants.Miscellaneous.EMPTY;
        signedHeaders = Constants.Miscellaneous.EMPTY;
        signature = Constants.Miscellaneous.EMPTY;
    }

    public AuthorizationHeader(String signingAlgorithm, String amzCredential, String signedHeaders, String signature) {
        this.signingAlgorithm = signingAlgorithm;
        this.amzCredential = amzCredential;
        this.signedHeaders = signedHeaders;
        this.signature = signature;
    }

    public String getAuthorizationHeader() {
        return signingAlgorithm + " Credential=" + amzCredential + ",SignedHeaders=" + signedHeaders + ",Signature=" + signature;
    }

    public String getSignature() {
        return signature;
    }
}