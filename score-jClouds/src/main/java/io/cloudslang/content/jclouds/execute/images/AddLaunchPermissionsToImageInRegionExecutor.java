package io.cloudslang.content.jclouds.execute.images;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.ImageInputs;
import io.cloudslang.content.jclouds.factory.ImageFactory;
import io.cloudslang.content.jclouds.services.ImageService;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import io.cloudslang.content.jclouds.utils.OutputsUtil;

import java.util.Map;
import java.util.Set;

/**
 * Created by Mihai Tusa.
 * 5/10/2016.
 */
public class AddLaunchPermissionsToImageInRegionExecutor {
    public Map<String, String> execute(CommonInputs commonInputs, ImageInputs imageInputs) throws Exception {
        ImageService imageService = ImageFactory.getImageService(commonInputs);

        Set<String> userIds = InputsUtil.getStringsSet(imageInputs.getUserIdsString(), commonInputs.getDelimiter());
        Set<String> userGroups = InputsUtil.getStringsSet(imageInputs.getUserGroupsString(), commonInputs.getDelimiter());

        if (userIds == null && userGroups == null) {
            throw new RuntimeException(Constants.ErrorMessages.BOTH_PERMISSIONS_INPUTS_EMPTY);
        }

        String response = imageService.addLaunchPermissionsToImage(imageInputs.getCustomInputs().getRegion(), userIds,
                userGroups, imageInputs.getCustomInputs().getImageId());

        return OutputsUtil.getResultsMap(response);
    }
}