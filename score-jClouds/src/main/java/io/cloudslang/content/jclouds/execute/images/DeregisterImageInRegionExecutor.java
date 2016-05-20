package io.cloudslang.content.jclouds.execute.images;

import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.factory.ImageFactory;
import io.cloudslang.content.jclouds.services.ImageService;
import io.cloudslang.content.jclouds.utils.OutputsUtil;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 5/5/2016.
 */
public class DeregisterImageInRegionExecutor {
    public Map<String, String> execute(CommonInputs commonInputs, CustomInputs customInputs) throws Exception {
        ImageService imageService = ImageFactory.getImageService(commonInputs);
        String response = imageService.deregisterImageInRegion(customInputs.getRegion(), customInputs.getImageId());

        return OutputsUtil.getResultsMap(response);
    }
}