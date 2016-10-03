package io.cloudslang.content.jclouds.factory;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.InputsWrapper;
import io.cloudslang.content.jclouds.factory.helpers.*;
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
                    queryParamsMap = new ElasticIpHelper().getAllocateAddressQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.ASSOCIATE_ADDRESS:
                    queryParamsMap = new NetworkHelper().getAssociateAddressQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.ATTACH_NETWORK_INTERFACE:
                    queryParamsMap = new NetworkHelper().getAttachNetworkInterfaceQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.ATTACH_VOLUME:
                    queryParamsMap = new VolumeHelper().getAttachVolumeQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.CREATE_NETWORK_INTERFACE:
                    queryParamsMap = new NetworkHelper().getCreateNetworkInterfaceQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.CREATE_SNAPSHOT:
                    queryParamsMap = new SnapshotHelper().getCreateSnapshotQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.CREATE_VOLUME:
                    queryParamsMap = new VolumeHelper().getCreateVolumeQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.DELETE_NETWORK_INTERFACE:
                    queryParamsMap = new NetworkHelper().getDeleteNetworkInterfaceQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.DELETE_SNAPSHOT:
                    queryParamsMap = new SnapshotHelper().getDeleteSnapshotQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.DELETE_VOLUME:
                    queryParamsMap = new VolumeHelper().getDeleteVolumeQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.DETACH_NETWORK_INTERFACE:
                    queryParamsMap = new NetworkHelper().getDetachNetworkInterfaceQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.DETACH_VOLUME:
                    queryParamsMap = new VolumeHelper().getDetachVolumeQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.DISASSOCIATE_ADDRESS:
                    queryParamsMap = new NetworkHelper().getDisassociateAddressQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.REBOOT_INSTANCES:
                    queryParamsMap = new InstanceHelper().getRebootInstancesQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.RELEASE_ADDRESS:
                    queryParamsMap = new ElasticIpHelper().getReleaseAddressQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.RUN_INSTANCES:
                    queryParamsMap = new InstanceHelper().getRunInstancesQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.START_INSTANCES:
                    queryParamsMap = new InstanceHelper().getStartInstancesQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.STOP_INSTANCES:
                    queryParamsMap = new InstanceHelper().getStopInstancesQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.TERMINATE_INSTANCES:
                    queryParamsMap = new InstanceHelper().getTerminateInstancesQueryParamsMap(wrapper);
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