package io.cloudslang.content.jclouds.services.helpers;

import io.cloudslang.content.jclouds.entities.aws.AWSApiAction;
import io.cloudslang.content.jclouds.entities.aws.AuthorizationHeader;
import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.AWSInputsWrapper;
import io.cloudslang.content.jclouds.services.AmazonSignatureService;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 8/12/2016.
 */
public class AWSApiNetworkServiceHelper {
    private static final String NETWORK_INTERFACE_ID = "NetworkInterfaceId";
    private static final String DEVICE_ID = "DeviceIndex";
    private static final String ATTACHMENT_ID = "AttachmentId";
    private static final String FORCE = "Force";

    public Map<String, String> getAttachNetworkInterfaceQueryParamsMap(AWSInputsWrapper inputs) {
        Map<String, String> queryParamsMap = new HashMap<>();
        queryParamsMap.put(Constants.AWSParams.ACTION, AWSApiAction.ATTACH_NETWORK_INTERFACE.getValue());
        queryParamsMap.put(Constants.AWSParams.VERSION, inputs.getVersion());
        queryParamsMap.put(NETWORK_INTERFACE_ID, inputs.getNetworkInterfaceId());
        queryParamsMap.put(Constants.AWSParams.INSTANCE_ID, inputs.getCustomInputs().getInstanceId());
        queryParamsMap.put(DEVICE_ID, inputs.getDeviceIndex());

        return queryParamsMap;
    }

    public Map<String, String> getDetachNetworkInterfaceQueryParamsMap(AWSInputsWrapper inputs) {
        Map<String, String> queryParamsMap = new HashMap<>();
        queryParamsMap.put(Constants.AWSParams.ACTION, AWSApiAction.DETACH_NETWORK_INTERFACE.getValue());
        queryParamsMap.put(Constants.AWSParams.VERSION, inputs.getVersion());
        queryParamsMap.put(ATTACHMENT_ID, inputs.getAttachmentId());

        if (inputs.isForceDetach()) {
            queryParamsMap.put(FORCE, Constants.Miscellaneous.SET_FLAG);
        }

        return queryParamsMap;
    }
}