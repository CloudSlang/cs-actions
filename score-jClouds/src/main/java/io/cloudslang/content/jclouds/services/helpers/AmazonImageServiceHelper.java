package io.cloudslang.content.jclouds.services.helpers;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import io.cloudslang.content.jclouds.entities.BlockDeviceMappingFilters;
import io.cloudslang.content.jclouds.entities.CommonFilters;
import io.cloudslang.content.jclouds.entities.ImageFilters;
import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.ImageInputs;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import org.jclouds.ec2.options.DescribeImagesOptions;

/**
 * Created by Mihai Tusa.
 * 5/10/2016.
 */
public class AmazonImageServiceHelper {
    public DescribeImagesOptions getDescribeImagesOptions(ImageInputs imageInputs, String delimiter) {
        String[] imageIds = InputsUtil.getStringsArray(imageInputs.getImageIdsString(), Constants.Miscellaneous.EMPTY, delimiter);
        String[] owners = InputsUtil.getStringsArray(imageInputs.getOwnersString(), Constants.Miscellaneous.EMPTY, delimiter);

        if (Constants.Miscellaneous.EMPTY.equals(imageInputs.getCustomInputs().getIdentityId())
                && imageIds == null && owners == null) {
            return DescribeImagesOptions.NONE;
        }

        return getPopulatedDescribeImagesOptions(imageInputs.getCustomInputs().getIdentityId(), imageIds, owners);
    }

    public Multimap<String, String> getImageFiltersMap(ImageInputs imageInputs) {
        Multimap<String, String> filtersMap = ArrayListMultimap.create();
        updateFiltersMap(imageInputs, filtersMap);

        return filtersMap;
    }

    private void updateFiltersMap(ImageInputs imageInputs, Multimap<String, String> filtersMap) {
        setRelevantFilters(imageInputs, filtersMap);

        Utils.updateFiltersMapEntry(filtersMap, BlockDeviceMappingFilters.DELETE_ON_TERMINATION.getValue(),
                imageInputs.getCustomInputs().getDeleteOnTermination());
        Utils.updateFiltersMapEntry(filtersMap, BlockDeviceMappingFilters.DEVICE_NAME.getValue(),
                imageInputs.getCustomInputs().getBlockMappingDeviceName());
        Utils.updateFiltersMapEntry(filtersMap, BlockDeviceMappingFilters.SNAPSHOT_ID.getValue(),
                imageInputs.getCustomInputs().getBlockDeviceMappingSnapshotId());

        Utils.updateFiltersMapEntry(filtersMap, ImageFilters.DESCRIPTION.getValue(), imageInputs.getDescription());
        Utils.updateFiltersMapEntry(filtersMap, ImageFilters.ID.getValue(), imageInputs.getCustomInputs().getImageId());
    }

    private void setRelevantFilters(ImageInputs imageInputs, Multimap<String, String> filtersMap) {
        Utils.addFiltersMapRelevantEntry(filtersMap, CommonFilters.ARCHITECTURE.getValue(),
                imageInputs.getCustomInputs().getArchitecture());

        Utils.addFiltersMapRelevantEntry(filtersMap, BlockDeviceMappingFilters.VOLUME_SIZE.getValue(),
                imageInputs.getCustomInputs().getVolumeSize());
        Utils.addFiltersMapRelevantEntry(filtersMap, BlockDeviceMappingFilters.VOLUME_TYPE.getValue(),
                imageInputs.getCustomInputs().getVolumeType());

        Utils.addFiltersMapRelevantEntry(filtersMap, ImageFilters.TYPE.getValue(),
                imageInputs.getCustomInputs().getVolumeType());
    }

    private DescribeImagesOptions getPopulatedDescribeImagesOptions(String identityId, String[] imageIds, String[] owners) {
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