package io.cloudslang.content.jclouds.execute.images;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.ImageInputs;
import io.cloudslang.content.jclouds.factory.ImageFactory;
import io.cloudslang.content.jclouds.services.ImageService;
import io.cloudslang.content.jclouds.utils.OutputsUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by Mihai Tusa.
 * 5/18/2016.
 */
public class RemoveLaunchPermissionsFromImageExecutor {
    public Map<String, String> execute(CommonInputs commonInputs, ImageInputs imageInputs) throws Exception {
        ImageService imageService = ImageFactory.getImageService(commonInputs);

        Set<String> userIds = getStringsSet(imageInputs.getUserIdsString(), commonInputs.getDelimiter());
        Set<String> userGroups = getStringsSet(imageInputs.getUserGroupsString(), commonInputs.getDelimiter());

        if (userIds == null && userGroups == null) {
            throw new RuntimeException(Constants.ErrorMessages.BOTH_PERMISSIONS_INPUTS_EMPTY);
        }

        String response = imageService.removeLaunchPermissionsFromImage(imageInputs.getCustomInputs().getRegion(), userIds,
                userGroups, imageInputs.getCustomInputs().getImageId());

        return OutputsUtil.getResultsMap(response);
    }

    private Set<String> getStringsSet(String input, String delimiter) {
        if (StringUtils.isBlank(input)) {
            return null;
        }
        return new HashSet<>(Arrays.asList(input.split(Pattern.quote(delimiter))));
    }
}