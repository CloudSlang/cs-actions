package io.cloudslang.content.jclouds.execute;

import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;

/**
 * Created by persdana on 7/13/2015.
 */
public class AmazonInputs {
    private static final String PROVIDER = "amazon";
    private static final String IDENTITY = "AKIAIQHVQ4UM7SOXXXXXX";
    private static final String CREDENTIAL = "AKIAIQHVQ4UM7SOXXXXXX";
    private static final String ENDPOINT = "https://ec2.amazonaws.com";
    private static final String PROXY_HOST = "proxy.abcde.com";
    private static final String PROXY_PORT = "8080";
    private static final String DELIMITER = ";;";

    private static final String REGION = "us-east-1";
    private static final String SERVER_ID = "i-578dde87";
    private static final String IMAGE_ID = "ami-4cfc1121";

    private String provider;
    private String identity;
    private String credential;
    private String endpoint;
    private String proxyHost;
    private String proxyPort;
    private String delimiter;

    private String region;
    private String serverId;

    private static CommonInputs commonInputs;
    private static CustomInputs customInputs;

    static {
        try {
            commonInputs = new CommonInputs.CommonInputsBuilder()
                    .withProvider(PROVIDER)
                    .withIdentity(IDENTITY)
                    .withCredential(CREDENTIAL)
                    .withEndpoint(ENDPOINT)
                    .withProxyHost(PROXY_HOST)
                    .withProxyPort(PROXY_PORT)
                    .withDelimiter(DELIMITER)
                    .build();

            customInputs = new CustomInputs.CustomInputsBuilder()
                    .withRegion(REGION)
                    .withInstanceId(SERVER_ID)
                    .withImageId(IMAGE_ID)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private AmazonInputs(CommonInputs commonInputs, CustomInputs customInputs) {
        this.provider = commonInputs.getProvider();
        this.identity = commonInputs.getIdentity();
        this.credential = commonInputs.getCredential();
        this.endpoint = commonInputs.getEndpoint();
        this.proxyHost = commonInputs.getProxyHost();
        this.proxyPort = commonInputs.getProxyPort();
        this.delimiter = commonInputs.getDelimiter();

        this.region = customInputs.getRegion();
        this.serverId = customInputs.getInstanceId();
    }

    static AmazonInputs getAmazonInstance() {
        return new AmazonInputs(commonInputs, customInputs);
    }

    String getProvider() {
        return provider;
    }

    String getIdentity() {
        return identity;
    }

    String getCredential() {
        return credential;
    }

    String getEndpoint() {
        return endpoint;
    }

    String getProxyHost() {
        return proxyHost;
    }

    String getProxyPort() {
        return proxyPort;
    }

    String getRegion() {
        return region;
    }

    String getServerId() {
        return serverId;
    }

    String getDelimiter() {
        return delimiter;
    }
}
