package io.cloudslang.content.jclouds.utilities;

import io.cloudslang.content.jclouds.entities.constants.ErrorConstants;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.ListRegionsInputs;
import io.cloudslang.content.jclouds.entities.inputs.ServerIdentificationInputs;

/**
 * Created by persdana on 7/13/2015.
 */
public final class InputsValidator {
    private static final String DEFAULT_DELIMITER = ";" + System.lineSeparator();

    public static void validateCommonInputs(CommonInputs commonInputs) throws Exception {
        String provider = commonInputs.getProvider().getProviderStr();
        if(provider == null || provider.isEmpty()) {
            throw new Exception(ErrorConstants.PROVIDER_NOT_SPECIFIED);
        }

        String endpoint = commonInputs.getEndpoint();
        if (endpoint == null || endpoint.isEmpty()) {
            throw new Exception(ErrorConstants.ENDPOINT_NOT_SPECIFIED);
        }
    }

    public static void validateServerIdentificationInputs(ServerIdentificationInputs serverIdentificationInputs) throws Exception {
        String serverId = serverIdentificationInputs.getServerId();
        if (serverId == null || serverId.isEmpty()) {
            throw new Exception(ErrorConstants.SERVER_ID_NOT_SPECIFIED);
        }
        serverIdentificationInputs.setServerId(serverId.trim());
    }

    public static void validateListRegionsInputs(ListRegionsInputs listRegionsInputs) throws Exception {
        String delimiter = listRegionsInputs.getDelimiter();
        if(delimiter == null || delimiter.isEmpty()) {
            listRegionsInputs.setDelimiter(DEFAULT_DELIMITER);
        }
    }
}
