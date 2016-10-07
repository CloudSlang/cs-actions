package io.cloudslang.content.jclouds.factory;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.InputsWrapper;
import io.cloudslang.content.jclouds.factory.helpers.ElasticIpUtils;
import io.cloudslang.content.jclouds.factory.helpers.ImageUtils;
import io.cloudslang.content.jclouds.factory.helpers.InstanceUtils;
import io.cloudslang.content.jclouds.factory.helpers.NetworkUtils;
import io.cloudslang.content.jclouds.factory.helpers.SnapshotUtils;
import io.cloudslang.content.jclouds.factory.helpers.VolumeUtils;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

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
        if (StringUtils.isBlank(wrapper.getCommonInputs().getQueryParams())) {
            switch (wrapper.getCommonInputs().getAction()) {
                case Constants.QueryApiActions.ALLOCATE_ADDRESS:
                    queryParamsMap = new ElasticIpUtils().getAllocateAddressQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.ASSOCIATE_ADDRESS:
                    queryParamsMap = new NetworkUtils().getAssociateAddressQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.ATTACH_NETWORK_INTERFACE:
                    queryParamsMap = new NetworkUtils().getAttachNetworkInterfaceQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.ATTACH_VOLUME:
                    queryParamsMap = new VolumeUtils().getAttachVolumeQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.CREATE_IMAGE:
                    queryParamsMap = new ImageUtils().getCreateImageQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.CREATE_NETWORK_INTERFACE:
                    queryParamsMap = new NetworkUtils().getCreateNetworkInterfaceQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.CREATE_SNAPSHOT:
                    queryParamsMap = new SnapshotUtils().getCreateSnapshotQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.CREATE_VOLUME:
                    queryParamsMap = new VolumeUtils().getCreateVolumeQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.DELETE_NETWORK_INTERFACE:
                    queryParamsMap = new NetworkUtils().getDeleteNetworkInterfaceQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.DELETE_SNAPSHOT:
                    queryParamsMap = new SnapshotUtils().getDeleteSnapshotQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.DELETE_VOLUME:
                    queryParamsMap = new VolumeUtils().getDeleteVolumeQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.DESCRIBE_IMAGES:
                    queryParamsMap = new ImageUtils().getDescribeImagesQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.DESCRIBE_IMAGE_ATTRIBUTE:
                    queryParamsMap = new ImageUtils().getDescribeImageAttributeQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.DEREGISTER_IMAGE:
                    queryParamsMap = new ImageUtils().getDeregisterImageQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.DETACH_NETWORK_INTERFACE:
                    queryParamsMap = new NetworkUtils().getDetachNetworkInterfaceQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.DETACH_VOLUME:
                    queryParamsMap = new VolumeUtils().getDetachVolumeQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.DISASSOCIATE_ADDRESS:
                    queryParamsMap = new NetworkUtils().getDisassociateAddressQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.MODIFY_IMAGE_ATTRIBUTE:
                    queryParamsMap = new ImageUtils().getModifyImageAttributeQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.REBOOT_INSTANCES:
                    queryParamsMap = new InstanceUtils().getRebootInstancesQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.RELEASE_ADDRESS:
                    queryParamsMap = new ElasticIpUtils().getReleaseAddressQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.RESET_IMAGE_ATTRIBUTE:
                    queryParamsMap = new ImageUtils().getResetImageAttributeQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.RUN_INSTANCES:
                    queryParamsMap = new InstanceUtils().getRunInstancesQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.START_INSTANCES:
                    queryParamsMap = new InstanceUtils().getStartInstancesQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.STOP_INSTANCES:
                    queryParamsMap = new InstanceUtils().getStopInstancesQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.TERMINATE_INSTANCES:
                    queryParamsMap = new InstanceUtils().getTerminateInstancesQueryParamsMap(wrapper);
                    break;
                default:
                    throw new RuntimeException(UNSUPPORTED_QUERY_API);
            }
        } else {
            queryParamsMap = InputsUtil.getHeadersOrQueryParamsMap(new HashMap<String, String>(),
                    wrapper.getCommonInputs().getQueryParams(), Constants.Miscellaneous.AMPERSAND,
                    Constants.Miscellaneous.EQUAL, false);
        }

        return queryParamsMap;
    }
}