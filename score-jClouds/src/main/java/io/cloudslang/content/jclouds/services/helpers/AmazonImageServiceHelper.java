package io.cloudslang.content.jclouds.services.helpers;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.ImageInputs;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import org.apache.commons.lang3.StringUtils;
import org.jclouds.ec2.options.DescribeImagesOptions;

/**
 * Created by Mihai Tusa.
 * 5/10/2016.
 */
public class AmazonImageServiceHelper {
    public DescribeImagesOptions getDescribeImagesOptions(ImageInputs imageInputs, String delimiter) {
        String[] imageIds = InputsUtil.getStringsArray(imageInputs.getImageIdsString(), Constants.Miscellaneous.EMPTY, delimiter);
        String[] owners = InputsUtil.getStringsArray(imageInputs.getOwnersString(), Constants.Miscellaneous.EMPTY, delimiter);

        if (StringUtils.isBlank(imageInputs.getCustomInputs().getIdentityId()) && imageIds == null && owners == null) {
            return DescribeImagesOptions.NONE;
        }

        return getPopulatedImagesOptions(imageInputs.getCustomInputs().getIdentityId(), imageIds, owners);
    }

    public Multimap<String, String> getImageFiltersMap(ImageInputs imageInputs, String delimiter) {
        Multimap<String, String> filtersMap = ArrayListMultimap.create();
        new Utils().updateImageFiltersMap(imageInputs, filtersMap, delimiter);

        return filtersMap;
    }

    private DescribeImagesOptions getPopulatedImagesOptions(String identityId, String[] imageIds, String[] owners) {
        DescribeImagesOptions options = new DescribeImagesOptions().executableBy(identityId);
        if (imageIds != null) {
            options.imageIds(imageIds);
        }
        if (owners != null) {
            options.ownedBy(owners);
        }

        return options;
    }
}