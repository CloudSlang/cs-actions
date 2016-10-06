package io.cloudslang.content.jclouds.factory.helpers;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.InputsWrapper;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.ATTRIBUTE;
import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.EXECUTABLE_BY;
import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.FILTER_NAME;
import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.FILTER_VALUE;
import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.INSTANCE_ID;
import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.NO_REBOOT;
import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.OPERATION_TYPE;
import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.USER_GROUP;
import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.USER_ID;


/**
 * Created by Tirla Alin.
 * Date: 30/9/2016.
 */
public class ImageHelper {

    public static final String BLOCK_DEVICE_MAPPING_DEVICE_NAME = "block-device-mapping.device-name";
    public static final String BLOCK_DEVICE_MAPPING_SNAPSHOT_ID = "block-device-mapping.snapshot-id";
    public static final String BLOCK_DEVICE_MAPPING_VOLUME_SIZE = "block-device-mapping.volume-size";
    public static final String BLOCK_DEVICE_MAPPING_VOLUME_TYPE = "block-device-mapping.volume-type";
    public static final String HYPERVISOR = "hypervisor";
    public static final String IMAGE_ID = "image-id";
    public static final String KERNEL_ID = "kernel-id";
    public static final String OWNER_ALIAS = "owner-alias";
    public static final String OWNER_ID = "owner-id";
    public static final String PLATFORM = "platform";
    public static final String PRODUCT_CODE = "product-code";
    public static final String PRODUCT_CODE_TYPE = "product-code.type";
    public static final String RAMDISK_ID = "ramdisk-id";
    public static final String ROOT_DEVICE_NAME = "root-device-name";
    public static final String ROOT_DEVICE_TYPE = "root-device-type";
    public static final String STATE_REASON_CODE = "state-reason-code";
    public static final String STATE_REASON_MESSAGE = "state-reason-message";
    public static final String TAG_KEY = "tag-key";
    public static final String TAG_VALUE = "tag-value";
    public static final String VIRTUALIZATION_TYPE = "virtualization-type";
    public static final String DESCRIPTION = "description";
    public static final String IS_PUBLIC = "is-public";
    public static final String IMAGE_TYPE = "image-type";
    public static final String MANIFEST_LOCATION = "manifest-location";
    public static final String NAME = "name";
    public static final String STATE = "state";
    public static final String ARCHITECTURE = "architecture";
    public static final String BLOCK_DEVICE_MAPPING_DELETE_ON_TERMINATION = "block-device-mapping.delete-on-termination";

    public Map<String, String> getDeregisterImageQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(),
                wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(Constants.AwsParams.IMAGE_ID, wrapper.getCustomInputs().getImageId());

        return queryParamsMap;
    }

    public Map<String, String> getCreateImageQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(),
                wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(INSTANCE_ID, wrapper.getImageInputs().getCustomInputs().getInstanceId());
        queryParamsMap.put(Constants.AwsParams.NAME, wrapper.getImageInputs().getImageName());
        InputsUtil.setOptionalMapEntry(queryParamsMap, Constants.AwsParams.DESCRIPTION, wrapper.getImageInputs().getDescription(),
                StringUtils.isNotBlank(wrapper.getImageInputs().getDescription()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, NO_REBOOT, String.valueOf(wrapper.getImageInputs().isImageNoReboot()),
                StringUtils.isNotBlank(String.valueOf(wrapper.getImageInputs().isImageNoReboot())));

        return queryParamsMap;
    }

    public Map<String, String> getDescribeImageAttributeQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(),
                wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(Constants.AwsParams.IMAGE_ID, wrapper.getCustomInputs().getImageId());
        queryParamsMap.put(ATTRIBUTE, wrapper.getCustomInputs().getAttribute());

        return queryParamsMap;
    }

    public Map<String, String> getModifyImageAttributeQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        Set<String> userIds = InputsUtil.getStringsSet(wrapper.getImageInputs().getUserIdsString(), wrapper.getCommonInputs().getDelimiter());
        Set<String> userGroupIds = InputsUtil.getStringsSet(wrapper.getImageInputs().getUserGroupsString(), wrapper.getCommonInputs().getDelimiter());
        if (userIds == null && userGroupIds == null) {
            throw new RuntimeException(Constants.ErrorMessages.BOTH_PERMISSION_INPUTS_EMPTY);
        }
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(),
                wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(ATTRIBUTE, wrapper.getImageInputs().getCustomInputs().getAttribute());
        queryParamsMap.put(OPERATION_TYPE, wrapper.getImageInputs().getCustomInputs().getOperationType());
        queryParamsMap.put(Constants.AwsParams.IMAGE_ID, wrapper.getImageInputs().getCustomInputs().getImageId());
        putCollectionInQueryMap(queryParamsMap, USER_ID, userIds);
        putCollectionInQueryMap(queryParamsMap, USER_GROUP, userGroupIds);

        return queryParamsMap;
    }

    private void putCollectionInQueryMap(Map<String, String> queryParamsMap, String paramName, Collection<String> set) {
        int step;
        Iterator<String> iterator = set.iterator();
        for (step = 1; iterator.hasNext(); step++) {
            String curValue = iterator.next();
            queryParamsMap.put(String.format("%s.%d", paramName, step), curValue);
        }
    }

    public Map<String, String> getDescribeImagesQueryParamsMap(InputsWrapper wrapper) {
        int currentIndex = 1;
        Map<String, String> queryParamsMap = new HashMap<>();
        Set<String> imageIds = InputsUtil.getStringsSet(wrapper.getImageInputs().getImageIdsString(), wrapper.getCommonInputs().getDelimiter());
        Set<String> ownerIds = InputsUtil.getStringsSet(wrapper.getImageInputs().getOwnersString(), wrapper.getCommonInputs().getDelimiter());
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(),
                wrapper.getCommonInputs().getVersion());
        InputsUtil.setOptionalMapEntry(queryParamsMap, EXECUTABLE_BY + ".1", String.valueOf(wrapper.getImageInputs().getCustomInputs().getIdentityId()),
                StringUtils.isNotBlank(String.valueOf(wrapper.getImageInputs().getCustomInputs().getIdentityId())));
        currentIndex = appendOptionalFilters(queryParamsMap, ARCHITECTURE, currentIndex, wrapper.getImageInputs().getCustomInputs().getArchitecture());
        currentIndex = appendOptionalFilters(queryParamsMap, BLOCK_DEVICE_MAPPING_DELETE_ON_TERMINATION, currentIndex, wrapper.getImageInputs().getCustomInputs().getDeleteOnTermination());
        currentIndex = appendOptionalFilters(queryParamsMap, BLOCK_DEVICE_MAPPING_DEVICE_NAME, currentIndex, wrapper.getImageInputs().getCustomInputs().getBlockMappingDeviceName());
        currentIndex = appendOptionalFilters(queryParamsMap, BLOCK_DEVICE_MAPPING_SNAPSHOT_ID, currentIndex, wrapper.getImageInputs().getCustomInputs().getBlockDeviceMappingSnapshotId());
        currentIndex = appendOptionalFilters(queryParamsMap, BLOCK_DEVICE_MAPPING_VOLUME_SIZE, currentIndex, wrapper.getImageInputs().getCustomInputs().getVolumeSize());
        currentIndex = appendOptionalFilters(queryParamsMap, BLOCK_DEVICE_MAPPING_VOLUME_TYPE, currentIndex, wrapper.getImageInputs().getCustomInputs().getVolumeType());
        currentIndex = appendOptionalFilters(queryParamsMap, HYPERVISOR, currentIndex, wrapper.getImageInputs().getCustomInputs().getHypervisor());
        currentIndex = appendOptionalFilters(queryParamsMap, IMAGE_ID, currentIndex, wrapper.getImageInputs().getCustomInputs().getImageId());
        currentIndex = appendOptionalFilters(queryParamsMap, KERNEL_ID, currentIndex, wrapper.getImageInputs().getCustomInputs().getKernelId());
        currentIndex = appendOptionalFilters(queryParamsMap, OWNER_ALIAS, currentIndex, wrapper.getImageInputs().getCustomInputs().getOwnerAlias());
        currentIndex = appendOptionalFilters(queryParamsMap, OWNER_ID, currentIndex, wrapper.getImageInputs().getCustomInputs().getOwnerId());
        currentIndex = appendOptionalFilters(queryParamsMap, PLATFORM, currentIndex, wrapper.getImageInputs().getCustomInputs().getPlatform());
        currentIndex = appendOptionalFilters(queryParamsMap, PRODUCT_CODE, currentIndex, wrapper.getImageInputs().getCustomInputs().getProductCode());
        currentIndex = appendOptionalFilters(queryParamsMap, PRODUCT_CODE_TYPE, currentIndex, wrapper.getImageInputs().getCustomInputs().getProductCodeType());
        currentIndex = appendOptionalFilters(queryParamsMap, RAMDISK_ID, currentIndex, wrapper.getImageInputs().getCustomInputs().getRamdiskId());
        currentIndex = appendOptionalFilters(queryParamsMap, ROOT_DEVICE_NAME, currentIndex, wrapper.getImageInputs().getCustomInputs().getRootDeviceName());
        currentIndex = appendOptionalFilters(queryParamsMap, ROOT_DEVICE_TYPE, currentIndex, wrapper.getImageInputs().getCustomInputs().getRootDeviceType());
        currentIndex = appendOptionalFilters(queryParamsMap, STATE_REASON_CODE, currentIndex, wrapper.getImageInputs().getCustomInputs().getStateReasonCode());
        currentIndex = appendOptionalFilters(queryParamsMap, STATE_REASON_MESSAGE, currentIndex, wrapper.getImageInputs().getCustomInputs().getStateReasonMessage());
        currentIndex = appendOptionalFilters(queryParamsMap, TAG_KEY, currentIndex, wrapper.getImageInputs().getCustomInputs().getKeyTagsString());
        currentIndex = appendOptionalFilters(queryParamsMap, TAG_VALUE, currentIndex, wrapper.getImageInputs().getCustomInputs().getValueTagsString());
        currentIndex = appendOptionalFilters(queryParamsMap, VIRTUALIZATION_TYPE, currentIndex, wrapper.getImageInputs().getCustomInputs().getVirtualizationType());
        currentIndex = appendOptionalFilters(queryParamsMap, DESCRIPTION, currentIndex, wrapper.getImageInputs().getDescription());
        putCollectionInQueryMap(queryParamsMap, Constants.AwsParams.IMAGE_ID, imageIds);
        putCollectionInQueryMap(queryParamsMap, Constants.AwsParams.OWNER, ownerIds);
        currentIndex = appendOptionalFilters(queryParamsMap, IMAGE_TYPE, currentIndex, wrapper.getImageInputs().getType());
        currentIndex = appendOptionalFilters(queryParamsMap, IS_PUBLIC, currentIndex, wrapper.getImageInputs().getIsPublic());
        currentIndex = appendOptionalFilters(queryParamsMap, MANIFEST_LOCATION, currentIndex, wrapper.getImageInputs().getManifestLocation());
        currentIndex = appendOptionalFilters(queryParamsMap, NAME, currentIndex, wrapper.getImageInputs().getImageName());
        appendOptionalFilters(queryParamsMap, STATE, currentIndex, wrapper.getImageInputs().getState());

        return queryParamsMap;
    }

    private int appendOptionalFilters(Map<String, String> queryParamsMap, String filterName, int filterIndex, String filterValue) {
        boolean isNotBlankInput = StringUtils.isNotBlank(filterValue);
        if(isNotBlankInput) {
            queryParamsMap.put(String.format(FILTER_NAME, filterIndex), filterName);
            queryParamsMap.put(String.format(FILTER_VALUE, filterIndex), filterValue);
            return filterIndex + 1;
        } else {
            return filterIndex;
        }
    }

    public Map<String, String> getResetImageAttributeQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(),
                wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(ATTRIBUTE, wrapper.getCustomInputs().getAttribute());
        queryParamsMap.put(Constants.AwsParams.IMAGE_ID, wrapper.getCustomInputs().getImageId());

        return queryParamsMap;
    }
}
