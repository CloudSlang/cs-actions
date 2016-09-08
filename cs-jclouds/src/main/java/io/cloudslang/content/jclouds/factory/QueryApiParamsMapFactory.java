package io.cloudslang.content.jclouds.factory;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.AwsInputsWrapper;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 9/5/2016.
 */
public class QueryApiParamsMapFactory {
    private static final String ATTACHMENT_ID = "AttachmentId";
    private static final String DEVICE_ID = "DeviceIndex";
    private static final String DOMAIN = "Domain";
    private static final String FORCE = "Force";
    private static final String NETWORK_INTERFACE_ID = "NetworkInterfaceId";

    private static final String AVAILABILITY_ZONE = "AvailabilityZone";
    private static final String ENCRYPTED = "Encrypted";
    private static final String IOPS = "Iops";
    private static final String KMS_KEY_ID = "KmsKeyId";
    private static final String SIZE = "Size";
    private static final String SNAPSHOT_ID = "SnapshotId";
    private static final String VOLUME_TYPE = "VolumeType";

    private QueryApiParamsMapFactory() {
    }

    public static Map<String, String> getQueryApiParamsMap(AwsInputsWrapper wrapper) {
        Map<String, String> queryParamsMap;
        if (StringUtils.isBlank(wrapper.getCommonInputs().getQueryParams())) {
            switch (wrapper.getAction()) {
                case Constants.QueryApiActions.ALLOCATE_ADDRESS:
                    queryParamsMap = getAllocateAddressQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.ATTACH_NETWORK_INTERFACE:
                    queryParamsMap = getAttachNetworkInterfaceQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.CREATE_VOLUME:
                    queryParamsMap = getCreateVolumeQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.DELETE_NETWORK_INTERFACE:
                    queryParamsMap = getDeleteNetworkInterfaceQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.DETACH_NETWORK_INTERFACE:
                    queryParamsMap = getDetachNetworkInterfaceQueryParamsMap(wrapper);
                    break;
                default:
                    throw new RuntimeException(Constants.ErrorMessages.UNSUPPORTED_QUERY_API);
            }
        } else {
            queryParamsMap = InputsUtil.getHeadersOrQueryParamsMap(new HashMap<String, String>(),
                    wrapper.getCommonInputs().getQueryParams(), Constants.Miscellaneous.AMPERSAND,
                    Constants.Miscellaneous.EQUAL, false);
        }

        return queryParamsMap;
    }

    private static Map<String, String> getDeleteNetworkInterfaceQueryParamsMap(AwsInputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        queryParamsMap.put(Constants.AwsParams.ACTION, wrapper.getAction());
        queryParamsMap.put(Constants.AwsParams.VERSION, wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(NETWORK_INTERFACE_ID, wrapper.getNetworkInputs().getNetworkInterfaceId());

        return queryParamsMap;
    }

    private static Map<String, String> getDetachNetworkInterfaceQueryParamsMap(AwsInputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        queryParamsMap.put(Constants.AwsParams.ACTION, wrapper.getAction());
        queryParamsMap.put(Constants.AwsParams.VERSION, wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(ATTACHMENT_ID, wrapper.getCustomInputs().getAttachmentId());

        setOptionalMapEntry(queryParamsMap, FORCE, Constants.Miscellaneous.SET_FLAG,
                wrapper.getNetworkInputs().isForceDetach());

        return queryParamsMap;
    }

    private static Map<String, String> getCreateVolumeQueryParamsMap(AwsInputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        queryParamsMap.put(Constants.AwsParams.ACTION, wrapper.getAction());
        queryParamsMap.put(Constants.AwsParams.VERSION, wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(AVAILABILITY_ZONE, wrapper.getCustomInputs().getAvailabilityZone());

        String volumeType = Constants.Miscellaneous.NOT_RELEVANT.equals(wrapper.getCustomInputs().getVolumeType()) ?
                Constants.Miscellaneous.STANDARD : wrapper.getCustomInputs().getVolumeType();
        queryParamsMap.put(VOLUME_TYPE, volumeType);

        setOptionalMapEntry(queryParamsMap, KMS_KEY_ID, wrapper.getCustomInputs().getKmsKeyId(),
                StringUtils.isNotBlank(wrapper.getCustomInputs().getKmsKeyId()));
        setOptionalMapEntry(queryParamsMap, SIZE, wrapper.getVolumeInputs().getSize(),
                StringUtils.isNotBlank(wrapper.getVolumeInputs().getSize()));
        setOptionalMapEntry(queryParamsMap, SNAPSHOT_ID, wrapper.getVolumeInputs().getSnapshotId(),
                StringUtils.isNotBlank(wrapper.getVolumeInputs().getSnapshotId()));
        setOptionalMapEntry(queryParamsMap, ENCRYPTED, Constants.Miscellaneous.SET_FLAG,
                wrapper.getVolumeInputs().isEncrypted());
        setOptionalMapEntry(queryParamsMap, IOPS, wrapper.getVolumeInputs().getIops(),
                !Constants.Miscellaneous.NOT_RELEVANT.equals(wrapper.getVolumeInputs().getIops()));

        return queryParamsMap;
    }

    private static Map<String, String> getAttachNetworkInterfaceQueryParamsMap(AwsInputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        queryParamsMap.put(Constants.AwsParams.ACTION, wrapper.getAction());
        queryParamsMap.put(Constants.AwsParams.VERSION, wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(Constants.AwsParams.INSTANCE_ID, wrapper.getCustomInputs().getInstanceId());
        queryParamsMap.put(NETWORK_INTERFACE_ID, wrapper.getNetworkInputs().getNetworkInterfaceId());
        queryParamsMap.put(DEVICE_ID, wrapper.getNetworkInputs().getDeviceIndex());

        return queryParamsMap;
    }

    private static Map<String, String> getAllocateAddressQueryParamsMap(AwsInputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        queryParamsMap.put(Constants.AwsParams.ACTION, wrapper.getAction());
        queryParamsMap.put(Constants.AwsParams.VERSION, wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(DOMAIN, wrapper.getCustomInputs().getDomain());

        return queryParamsMap;
    }

    private static void setOptionalMapEntry(Map<String, String> inputMap, String key, String value, boolean condition) {
        if (condition) {
            inputMap.put(key, value);
        }
    }
}