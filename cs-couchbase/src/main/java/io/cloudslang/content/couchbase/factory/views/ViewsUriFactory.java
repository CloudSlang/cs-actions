/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.couchbase.factory.views;

import io.cloudslang.content.couchbase.entities.inputs.InputsWrapper;

import static io.cloudslang.content.couchbase.entities.constants.Constants.ViewsActions.GET_DESIGN_DOCS_INFO;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Created by TusaM
 * 4/28/2017.
 */
public class ViewsUriFactory {
    private ViewsUriFactory() {
        // prevent instantiation
    }

    public static String getViewsUri(InputsWrapper wrapper) {
        switch (wrapper.getCommonInputs().getAction()) {
            case GET_DESIGN_DOCS_INFO:
                return wrapper.getBucketInputs().getBucketName();
            default:
                return EMPTY;
        }
    }
}