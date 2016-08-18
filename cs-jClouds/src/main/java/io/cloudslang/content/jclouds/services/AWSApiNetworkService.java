package io.cloudslang.content.jclouds.services;

import io.cloudslang.content.jclouds.entities.inputs.AWSInputsWrapper;

import java.net.MalformedURLException;
import java.security.SignatureException;
import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 8/12/2016.
 */
public interface AWSApiNetworkService {
    Map<String, String> attachNetworkInterface(AWSInputsWrapper inputs) throws SignatureException, MalformedURLException;

    Map<String, String> detachNetworkInterface(AWSInputsWrapper inputs) throws MalformedURLException, SignatureException;
}