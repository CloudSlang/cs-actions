package io.cloudslang.content.jclouds.factory;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.InputsWrapper;
import io.cloudslang.content.jclouds.factory.helpers.NetworkHelper;
import io.cloudslang.content.jclouds.factory.helpers.VolumeHelper;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 9/5/2016.
 */
public class ParamsMapFactory {
    private ParamsMapFactory() {
    }

    public static Map<String, String> getQueryApiParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap;
        if (StringUtils.isBlank(wrapper.getCommonInputs().getQueryParams())) {
            switch (wrapper.getAction()) {
                case Constants.QueryApiActions.ALLOCATE_ADDRESS:
                    queryParamsMap = new NetworkHelper().getAllocateAddressQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.ATTACH_NETWORK_INTERFACE:
                    queryParamsMap = new NetworkHelper().getAttachNetworkInterfaceQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.CREATE_VOLUME:
                    queryParamsMap = new VolumeHelper().getCreateVolumeQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.CREATE_NETWORK_INTERFACE:
                    queryParamsMap = new NetworkHelper().getCreateNetworkInterfaceQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.DELETE_NETWORK_INTERFACE:
                    queryParamsMap = new NetworkHelper().getDeleteNetworkInterfaceQueryParamsMap(wrapper);
                    break;
                case Constants.QueryApiActions.DETACH_NETWORK_INTERFACE:
                    queryParamsMap = new NetworkHelper().getDetachNetworkInterfaceQueryParamsMap(wrapper);
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
}