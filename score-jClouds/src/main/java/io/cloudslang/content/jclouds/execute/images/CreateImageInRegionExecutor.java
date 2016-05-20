package io.cloudslang.content.jclouds.execute.images;

import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.ImageInputs;
import io.cloudslang.content.jclouds.factory.ImageFactory;
import io.cloudslang.content.jclouds.services.ImageService;
import io.cloudslang.content.jclouds.utils.OutputsUtil;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 5/4/2016.
 */
public class CreateImageInRegionExecutor {
    public Map<String, String> execute(CommonInputs commonInputs, ImageInputs imageInputs) throws Exception {
        ImageService imageService = ImageFactory.getImageService(commonInputs);
        String response = imageService.createImageInRegion(imageInputs.getCustomInputs().getRegion(),
                imageInputs.getImageName(), imageInputs.getCustomInputs().getServerId(), imageInputs.getImageDescription(),
                imageInputs.isImageNoReboot());

        return OutputsUtil.getResultsMap(response);
    }
}