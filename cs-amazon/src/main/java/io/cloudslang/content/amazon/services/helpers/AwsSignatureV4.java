/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package io.cloudslang.content.amazon.services.helpers;

import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.AWS_REQUEST_VERSION;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.ENCODING;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.LINE_SEPARATOR;

/**
 * Created by Mihai Tusa.
 * 8/8/2016.
 */
public class AwsSignatureV4 {
    private static final String AWS_SIGNATURE_VERSION = "AWS4";
    private static final String AWS4_SIGNING_ALGORITHM = "AWS4-HMAC-SHA256";
    private static final String HASH_ALGORITHM = "SHA-256";
    private static final String HMAC_ALGORITHM = "HmacSHA256";

    private static final String CANONICAL_REQUEST_DIGEST_ERROR = "Failed to calculate the canonical request digest: ";
    private static final String DERIVED_SIGNING_ERROR = "Failed to calculate the derived signing key: ";
    private static final String REQUEST_PAYLOAD_DIGEST = "Failed to calculate the request payload digest: ";
    private static final String SIGNATURE_ERROR = "Failed to calculate the AWS signature: ";

    /**
     * Combines the inputs into a canonical (standardized format) request.
     * This method requires the payload's hash pre-calculated.
     *
     * @param httpRequestMethod    Request method.
     * @param canonicalURI         Request canonical URI.
     * @param canonicalQueryString Request query string.
     * @param canonicalHeaders     Canonical headers for the request. These headers will be signed.
     * @param signedHeaders        Column separated list of header names that will be signed.
     * @param requestPayload       Request payload's hash.
     * @return A string representing the canonical request.
     */
    public String getCanonicalRequest(String httpRequestMethod, String canonicalURI, String canonicalQueryString,
                                      String canonicalHeaders, String signedHeaders, String requestPayload) throws SignatureException {
        try {
            return httpRequestMethod + LINE_SEPARATOR + canonicalURI + LINE_SEPARATOR + canonicalQueryString + LINE_SEPARATOR +
                    canonicalHeaders + LINE_SEPARATOR + signedHeaders + LINE_SEPARATOR +
                    new String(Hex.encode(calculateHash(requestPayload)), ENCODING);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new SignatureException(REQUEST_PAYLOAD_DIGEST + e.getMessage());
        }
    }

    /**
     * Combines the inputs into a string with a fixed structure and calculates the canonical request digest.
     * This string can be used with the derived signing key to create an AWS signature.
     *
     * @param requestDate      Request date in YYYYMMDD'T'HHMMSS'Z' format.
     * @param credentialScope  Request credential scope.
     * @param canonicalRequest Canonical request.
     * @return A string that includes meta information about the request.
     */
    public String getStringToSign(String requestDate, String credentialScope, String canonicalRequest) throws SignatureException {
        try {
            return AWS4_SIGNING_ALGORITHM + LINE_SEPARATOR + requestDate + LINE_SEPARATOR + credentialScope + LINE_SEPARATOR +
                    new String(Hex.encode(calculateHash(canonicalRequest)), ENCODING);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new SignatureException(CANONICAL_REQUEST_DIGEST_ERROR + e.getMessage());
        }
    }

    /**
     * Derives a signing key from the AWS secret access key.
     *
     * @param secretAccessKey Amazon secret a access key.
     * @param dateStamp       Credential scope date stamp in "yyyyMMdd" format.
     * @param region          Amazon region name.
     * @param amazonApi       Amazon service name.
     * @return Signing key's bytes. This result is not encoded.
     */
    public byte[] getDerivedSigningKey(String secretAccessKey, String dateStamp, String region, String amazonApi)
            throws SignatureException {
        try {
            byte[] kSecret = (AWS_SIGNATURE_VERSION + secretAccessKey).getBytes(ENCODING);
            byte[] kDate = calculateHmacSHA256(dateStamp, kSecret);
            byte[] kRegion = calculateHmacSHA256(region, kDate);
            byte[] kService = calculateHmacSHA256(amazonApi, kRegion);

            return calculateHmacSHA256(AWS_REQUEST_VERSION, kService);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException e) {
            throw new SignatureException(DERIVED_SIGNING_ERROR + e.getMessage());
        }
    }

    /**
     * Calculates the AWS Signature Version 4 by signing (calculates the HmacSHA256) the string-to-sign with the derived key.
     *
     * @param stringToSign      String-to-sign the includes meta information about the request.
     * @param derivedSigningKey Signing key derived from the AWS secret access key.
     * @return AWS Signature Version 4.
     */
    public String getSignature(String stringToSign, byte[] derivedSigningKey) throws SignatureException {
        try {
            return new String(Hex.encode(calculateHmacSHA256(stringToSign, derivedSigningKey)), ENCODING);
        } catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
            throw new SignatureException(SIGNATURE_ERROR + e.getMessage());
        }
    }

    /**
     * Calculates the message digest (hash) for the data string.
     *
     * @param data String for which the digest will be calculated.
     * @return Digest's bytes. This result is not encoded.
     */
    private byte[] calculateHash(String data) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
        md.update(data.getBytes(ENCODING));
        return md.digest();
    }

    /**
     * Calculates the keyed-hash message authentication code for the data string.
     *
     * @param data String for which the HMAC will be calculated.
     * @param key  Key used for HMAC calculation.
     * @return HMAC's bytes. This result is not encoded.
     */
    private byte[] calculateHmacSHA256(String data, byte[] key)
            throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        Mac mac = Mac.getInstance(HMAC_ALGORITHM);
        mac.init(new SecretKeySpec(key, HMAC_ALGORITHM));
        return mac.doFinal(data.getBytes(ENCODING));
    }
}
