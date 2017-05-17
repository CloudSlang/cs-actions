/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.couchbase.factory;

import io.cloudslang.content.couchbase.entities.inputs.InputsWrapper;
import io.cloudslang.content.couchbase.factory.buckets.BucketsHelper;
import io.cloudslang.content.couchbase.factory.cluster.ClusterHelper;
import io.cloudslang.content.couchbase.factory.nodes.NodesHelper;

import static io.cloudslang.content.couchbase.entities.constants.Constants.BucketActions.CREATE_OR_EDIT_BUCKET;
import static io.cloudslang.content.couchbase.entities.constants.Constants.ClusterActions.REBALANCING_NODES;
import static io.cloudslang.content.couchbase.entities.constants.Constants.NodeActions.FAIL_OVER_NODE;
import static io.cloudslang.content.couchbase.entities.constants.Constants.NodeActions.GRACEFUL_FAIL_OVER_NODE;
import static io.cloudslang.content.couchbase.entities.constants.Constants.NodeActions.SET_RECOVERY_TYPE;
import static org.apache.http.client.methods.HttpPost.METHOD_NAME;

/**
 * Created by TusaM
 * 4/14/2017.
 */
public class PayloadBuilder {
    private PayloadBuilder() {
        // prevent instantiation
    }

    public static void buildPayload(InputsWrapper wrapper) {
        if (METHOD_NAME.equalsIgnoreCase(wrapper.getHttpClientInputs().getMethod())) {
            switch (wrapper.getCommonInputs().getAction()) {
                case CREATE_OR_EDIT_BUCKET:
                    wrapper.getHttpClientInputs().setBody(new BucketsHelper().getCreateBucketPayload(wrapper));
                    break;
                case FAIL_OVER_NODE:
                    wrapper.getHttpClientInputs().setBody(new NodesHelper().getFailOverNodePayloadString(wrapper));
                    break;
                case GRACEFUL_FAIL_OVER_NODE:
                    wrapper.getHttpClientInputs().setBody(new NodesHelper().getFailOverNodePayloadString(wrapper));
                    break;
                case REBALANCING_NODES:
                    wrapper.getHttpClientInputs().setBody(new ClusterHelper().getRebalancingNodesPayload(wrapper));
                    break;
                case SET_RECOVERY_TYPE:
                    wrapper.getHttpClientInputs().setBody(new NodesHelper().getRecoveryTypePayloadString(wrapper));
                    break;
                default:
                    break;
            }
        }
    }
}