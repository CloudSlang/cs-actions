package io.cloudslang.content.amazon.factory;

import io.cloudslang.content.amazon.entities.inputs.InputsWrapper;
import io.cloudslang.content.amazon.factory.helpers.*;
import io.cloudslang.content.amazon.utils.InputsUtil;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;

import static io.cloudslang.content.amazon.entities.constants.Constants.QueryApiActions.*;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.AMPERSAND;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EQUAL;

/**
 * Created by Mihai Tusa.
 * 9/5/2016.
 */
public class ParamsMapBuilder {
    private static final String UNSUPPORTED_QUERY_API = "Unsupported Query API.";

    private ParamsMapBuilder() {
    }

    public static Map<String, String> getParamsMap(InputsWrapper wrapper) throws Exception {
        Map<String, String> queryParamsMap;
        if (isBlank(wrapper.getCommonInputs().getQueryParams())) {
            switch (wrapper.getCommonInputs().getAction()) {
                case ALLOCATE_ADDRESS:
                    queryParamsMap = new ElasticIpUtils().getAllocateAddressQueryParamsMap(wrapper);
                    break;
                case ASSOCIATE_ADDRESS:
                    queryParamsMap = new NetworkUtils().getAssociateAddressQueryParamsMap(wrapper);
                    break;
                case ATTACH_NETWORK_INTERFACE:
                    queryParamsMap = new NetworkUtils().getAttachNetworkInterfaceQueryParamsMap(wrapper);
                    break;
                case ATTACH_VOLUME:
                    queryParamsMap = new VolumeUtils().getAttachVolumeQueryParamsMap(wrapper);
                    break;
                case CREATE_IMAGE:
                    queryParamsMap = new ImageUtils().getCreateImageQueryParamsMap(wrapper);
                    break;
                case CREATE_NETWORK_INTERFACE:
                    queryParamsMap = new NetworkUtils().getCreateNetworkInterfaceQueryParamsMap(wrapper);
                    break;
                case CREATE_SNAPSHOT:
                    queryParamsMap = new SnapshotUtils().getCreateSnapshotQueryParamsMap(wrapper);
                    break;
                case CREATE_TAGS:
                    queryParamsMap = new TagUtils().getCreateTagsQueryParamsMap(wrapper);
                    break;
                case CREATE_VOLUME:
                    queryParamsMap = new VolumeUtils().getCreateVolumeQueryParamsMap(wrapper);
                    break;
                case DELETE_NETWORK_INTERFACE:
                    queryParamsMap = new NetworkUtils().getDeleteNetworkInterfaceQueryParamsMap(wrapper);
                    break;
                case DELETE_SNAPSHOT:
                    queryParamsMap = new SnapshotUtils().getDeleteSnapshotQueryParamsMap(wrapper);
                    break;
                case DELETE_VOLUME:
                    queryParamsMap = new VolumeUtils().getDeleteVolumeQueryParamsMap(wrapper);
                    break;
                case DESCRIBE_IMAGES:
                    queryParamsMap = new ImageUtils().getDescribeImagesQueryParamsMap(wrapper);
                    break;
                case DESCRIBE_IMAGE_ATTRIBUTE:
                    queryParamsMap = new ImageUtils().getDescribeImageAttributeQueryParamsMap(wrapper);
                    break;
                case DESCRIBE_REGIONS:
                    queryParamsMap = new RegionUtils().getDescribeRegionsQueryParamsMap(wrapper);
                    break;
                case DEREGISTER_IMAGE:
                    queryParamsMap = new ImageUtils().getDeregisterImageQueryParamsMap(wrapper);
                    break;
                case DETACH_NETWORK_INTERFACE:
                    queryParamsMap = new NetworkUtils().getDetachNetworkInterfaceQueryParamsMap(wrapper);
                    break;
                case DETACH_VOLUME:
                    queryParamsMap = new VolumeUtils().getDetachVolumeQueryParamsMap(wrapper);
                    break;
                case DISASSOCIATE_ADDRESS:
                    queryParamsMap = new NetworkUtils().getDisassociateAddressQueryParamsMap(wrapper);
                    break;
                case MODIFY_IMAGE_ATTRIBUTE:
                    queryParamsMap = new ImageUtils().getModifyImageAttributeQueryParamsMap(wrapper);
                    break;
                case REBOOT_INSTANCES:
                    queryParamsMap = new InstanceUtils().getRebootInstancesQueryParamsMap(wrapper);
                    break;
                case RELEASE_ADDRESS:
                    queryParamsMap = new ElasticIpUtils().getReleaseAddressQueryParamsMap(wrapper);
                    break;
                case RESET_IMAGE_ATTRIBUTE:
                    queryParamsMap = new ImageUtils().getResetImageAttributeQueryParamsMap(wrapper);
                    break;
                case RUN_INSTANCES:
                    queryParamsMap = new InstanceUtils().getRunInstancesQueryParamsMap(wrapper);
                    break;
                case START_INSTANCES:
                    queryParamsMap = new InstanceUtils().getStartInstancesQueryParamsMap(wrapper);
                    break;
                case STOP_INSTANCES:
                    queryParamsMap = new InstanceUtils().getStopInstancesQueryParamsMap(wrapper);
                    break;
                case TERMINATE_INSTANCES:
                    queryParamsMap = new InstanceUtils().getTerminateInstancesQueryParamsMap(wrapper);
                    break;
                default:
                    throw new RuntimeException(UNSUPPORTED_QUERY_API);
            }
        } else {
            queryParamsMap = InputsUtil.getHeadersOrQueryParamsMap(new HashMap<String, String>(),
                    wrapper.getCommonInputs().getQueryParams(), AMPERSAND, EQUAL, false);
        }

        return queryParamsMap;
    }
}