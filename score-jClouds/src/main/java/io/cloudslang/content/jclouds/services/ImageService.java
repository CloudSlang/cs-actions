package io.cloudslang.content.jclouds.services;

import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;

/**
 * Created by Mihai Tusa.
 * 5/4/2016.
 */
public interface ImageService {
    String createImageInRegion(CustomInputs customInputs) throws Exception;

    String deregisterImageInRegion(CustomInputs customInputs) throws Exception;
}