package io.cloudslang.content.jclouds.execute.images;

import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.factory.ImageFactory;
import io.cloudslang.content.jclouds.services.ImageService;
import io.cloudslang.content.jclouds.utils.OutputsUtil;
import org.jclouds.ec2.domain.Permission;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 5/10/2016.
 */
public class GetLaunchPermissionForImageInRegionExecutor {
    public Map<String, String> execute(CommonInputs commonInputs, CustomInputs customInputs) throws Exception {
        ImageService imageService = ImageFactory.getImageService(commonInputs);

        Permission response = imageService.getLaunchPermissionForImage(customInputs.getRegion(), customInputs.getImageId());

        return OutputsUtil.getResultsMap(response.toString());
    }
}