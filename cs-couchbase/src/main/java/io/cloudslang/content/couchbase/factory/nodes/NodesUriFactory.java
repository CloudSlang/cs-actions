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

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Created by TusaM
 * 5/9/2017.
 */
public class NodesUriFactory {
    private NodesUriFactory() {
        // prevent instantiation
    }

    public static String getNodesUri(InputsWrapper wrapper) {
        switch (wrapper.getCommonInputs().getAction()) {
            default:
                return EMPTY;
        }
    }
}