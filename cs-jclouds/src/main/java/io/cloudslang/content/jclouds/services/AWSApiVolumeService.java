package io.cloudslang.content.jclouds.services;

import io.cloudslang.content.jclouds.entities.inputs.AWSInputsWrapper;

import java.net.MalformedURLException;
import java.security.SignatureException;
import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 8/16/2016.
 */
public interface AWSApiVolumeService {
    Map<String, String> createVolume(AWSInputsWrapper inputs) throws MalformedURLException, SignatureException;
}