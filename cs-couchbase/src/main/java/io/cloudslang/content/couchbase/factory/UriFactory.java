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

import io.cloudslang.content.couchbase.entities.couchbase.ClusterUri;
import io.cloudslang.content.couchbase.entities.couchbase.CouchbaseApi;
import io.cloudslang.content.couchbase.entities.couchbase.NodesUri;
import io.cloudslang.content.couchbase.entities.couchbase.ViewsUri;
import io.cloudslang.content.couchbase.entities.inputs.InputsWrapper;

import static io.cloudslang.content.couchbase.entities.constants.Constants.Api.BUCKETS;
import static io.cloudslang.content.couchbase.entities.constants.Constants.Api.CLUSTER;
import static io.cloudslang.content.couchbase.entities.constants.Constants.Api.NODES;
import static io.cloudslang.content.couchbase.entities.constants.Constants.Api.VIEWS;
import static io.cloudslang.content.couchbase.entities.constants.Constants.ErrorMessages.UNSUPPORTED_COUCHBASE_API;
import static io.cloudslang.content.couchbase.factory.buckets.BucketsUriFactory.getBucketsUri;
import static io.cloudslang.content.couchbase.factory.nodes.NodesUriFactory.getNodesUri;
import static io.cloudslang.content.couchbase.factory.views.ViewsUriFactory.getViewsUri;
import static io.cloudslang.content.couchbase.utils.InputsUtil.appendTo;

/**
 * Created by Mihai Tusa
 * 4/5/2017.
 */
public class UriFactory {
    private UriFactory() {
        // prevent instantiation
    }

    public static String getUri(InputsWrapper wrapper) {
        String action = wrapper.getCommonInputs().getAction();
        switch (wrapper.getCommonInputs().getApi()) {
            case BUCKETS:
                return appendTo(CouchbaseApi.BUCKETS.getValue(), getBucketsUri(wrapper), action);
            case CLUSTER:
                return ClusterUri.getValue(action);
            case NODES:
                return appendTo(NodesUri.getValue(action), getNodesUri(wrapper), action);
            case VIEWS:
                return appendTo(ViewsUri.getValue(action), getViewsUri(wrapper), action);
            default:
                throw new RuntimeException(UNSUPPORTED_COUCHBASE_API);
        }
    }
}