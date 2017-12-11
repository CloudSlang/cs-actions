/*
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
*/
package io.cloudslang.content.couchbase.factory;

import io.cloudslang.content.couchbase.entities.inputs.InputsWrapper;

import static io.cloudslang.content.couchbase.entities.constants.Constants.Api.BUCKETS;
import static io.cloudslang.content.couchbase.entities.constants.Constants.Api.CLUSTER;
import static io.cloudslang.content.couchbase.entities.constants.Constants.Api.NODES;
import static io.cloudslang.content.couchbase.entities.constants.Constants.Api.VIEWS;
import static io.cloudslang.content.couchbase.entities.constants.Constants.ErrorMessages.UNSUPPORTED_COUCHBASE_API;
import static io.cloudslang.content.couchbase.entities.couchbase.CouchbaseApi.CONTROLLER;
import static io.cloudslang.content.couchbase.entities.couchbase.CouchbaseApi.POOLS_DEFAULT;
import static io.cloudslang.content.couchbase.entities.couchbase.NodesUri.getNodesUriValue;
import static io.cloudslang.content.couchbase.entities.couchbase.SuffixUri.getSuffixUriValue;
import static io.cloudslang.content.couchbase.entities.couchbase.ViewsUri.getViewsUriValue;
import static io.cloudslang.content.couchbase.factory.buckets.BucketsUriFactory.getBucketsUriValue;
import static io.cloudslang.content.couchbase.factory.cluster.ClusterUriFactory.getClusterUriValue;
import static io.cloudslang.content.couchbase.factory.views.ViewsUriFactory.getViewsUriSuffix;
import static io.cloudslang.content.couchbase.utils.InputsUtil.appendTo;
import static org.apache.commons.lang3.StringUtils.EMPTY;

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
                return appendTo(POOLS_DEFAULT.getValue(), getBucketsUriValue(wrapper), action);
            case CLUSTER:
                return getClusterUriValue(wrapper) + getSuffixUriValue(action);
            case NODES:
                return appendTo(CONTROLLER.getValue() + getNodesUriValue(action), EMPTY, action);
            case VIEWS:
                return appendTo(POOLS_DEFAULT.getValue() + getViewsUriValue(action), getViewsUriSuffix(wrapper), action);
            default:
                throw new RuntimeException(UNSUPPORTED_COUCHBASE_API);
        }
    }
}