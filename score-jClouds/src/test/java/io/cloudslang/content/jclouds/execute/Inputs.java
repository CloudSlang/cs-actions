package io.cloudslang.content.jclouds.execute;


/**
 * Created by persdana on 7/13/2015.
 */
public class Inputs {

    public static final String PROVIDER = "amazon";
    public static final String IDENTITY = "AKIAIQHVQ4UM7SOXXXXXX";
    public static final String CREDENTIAL = "AKIAIQHVQ4UM7SOXXXXXX";
    public static final String ENDPOINT = "https://ec2.amazonaws.com";
    public static final String PROXY_HOST = "proxy.abcde.com";
    public static final String PROXY_PORT = "8080";
    public static final String REGION = "us-east-1";
    public static final String SERVER_ID = "i-578dde87";
    private static final String DELIMITER = ";;";

//    public static ServerIdentificationInputs getServerIdentificationInputsForAmazon() {
//        return new ServerIdentificationInputs(PROVIDER, IDENTITY,CREDENTIAL, ENDPOINT, PROXY_HOST, PROXY_PORT, REGION, SERVER_ID);
//    }
//
//    public static ListServersInputs getListServerInputsForAmazon() {
//        return new ListServersInputs(PROVIDER, IDENTITY, CREDENTIAL, ENDPOINT, PROXY_HOST, PROXY_PORT, REGION, DELIMITER);
//    }
//
//    public static ListRegionsInputs getListRegionsInputsForAmazon() {
//        return new ListRegionsInputs(PROVIDER, IDENTITY, CREDENTIAL, ENDPOINT, PROXY_HOST, PROXY_PORT, DELIMITER);
//    }
}