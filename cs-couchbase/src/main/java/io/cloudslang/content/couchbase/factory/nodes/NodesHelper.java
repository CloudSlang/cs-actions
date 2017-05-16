/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.couchbase.factory.nodes;

import io.cloudslang.content.couchbase.entities.inputs.InputsWrapper;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.couchbase.entities.constants.Constants.Miscellaneous.EQUAL;
import static io.cloudslang.content.couchbase.utils.InputsUtil.getPayloadString;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Created by TusaM
 * 5/9/2017.
 */
public class NodesHelper {
    private static final String OTP_NODE = "otpNode";

    public String getFailOverNodePayload(InputsWrapper wrapper) {
        return getPayloadString(getPayloadMap(wrapper), EQUAL, EMPTY, false);
    }

    private Map<String, String> getPayloadMap(InputsWrapper wrapper) {
        Map<String, String> payloadMap = new HashMap<>();
        payloadMap.put(OTP_NODE, wrapper.getNodeInputs().getInternalNodeIpAddress());

        return payloadMap;
    }
}