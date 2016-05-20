package io.cloudslang.content.jclouds.services.helpers;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import org.jclouds.ec2.options.DescribeImagesOptions;

/**
 * Created by Mihai Tusa.
 * 5/10/2016.
 */
public class AmazonImageServiceHelper {
    public DescribeImagesOptions getDescribeImagesOptions(String identityId, String[] imageIds, String[] owners) {
        if (Constants.Miscellaneous.EMPTY.equals(identityId) && imageIds == null && owners == null) {
            return DescribeImagesOptions.NONE;
        }

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