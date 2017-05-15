/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.couchbase.factory.cluster;

import io.cloudslang.content.couchbase.entities.inputs.InputsWrapper;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.couchbase.entities.constants.Constants.Miscellaneous.BLANK_SPACE;
import static io.cloudslang.content.couchbase.entities.constants.Constants.Miscellaneous.COLON;
import static io.cloudslang.content.couchbase.entities.constants.Inputs.ClusterInputs.EJECTED_NODES;
import static io.cloudslang.content.couchbase.entities.constants.Inputs.ClusterInputs.KNOWN_NODES;
import static io.cloudslang.content.couchbase.utils.InputsUtil.getPayloadString;
import static io.cloudslang.content.couchbase.utils.InputsUtil.validateIfNotBothBlankInputs;
import static io.cloudslang.content.couchbase.utils.InputsUtil.validateRebalancingNodesPayloadInputs;

/**
 * Created by TusaM
 * 5/11/2017.
 */
public class ClusterHelper {
    public String getRebalancingNodesPayload(InputsWrapper wrapper) {
        return getPayloadString(getPayloadMap(wrapper), COLON, BLANK_SPACE, false);
    }

    private Map<String, String> getPayloadMap(InputsWrapper wrapper) {
        String ejectedNodesString = wrapper.getClusterInputs().getEjectedNodes();
        String knownNodesString = wrapper.getClusterInputs().getKnownNodes();
        validateIfNotBothBlankInputs(ejectedNodesString, knownNodesString, EJECTED_NODES, KNOWN_NODES);

        String delimiter = wrapper.getCommonInputs().getDelimiter();
        validateRebalancingNodesPayloadInputs(ejectedNodesString, delimiter);
        validateRebalancingNodesPayloadInputs(knownNodesString, delimiter);

        Map<String, String> payloadMap = new HashMap<>();
        payloadMap.put(EJECTED_NODES, wrapper.getClusterInputs().getEjectedNodes());
        payloadMap.put(KNOWN_NODES, wrapper.getClusterInputs().getKnownNodes());

        return payloadMap;
    }
}