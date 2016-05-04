package io.cloudslang.content.jclouds.services;

import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;

/**
 * Created by Mihai Tusa.
 * 5/4/2016.
 */
public interface ImageService {
    String createImageInRegion(CommonInputs commonInputs, CustomInputs customInputs) throws Exception;
}