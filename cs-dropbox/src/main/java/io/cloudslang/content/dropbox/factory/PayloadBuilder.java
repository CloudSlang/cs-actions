/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.dropbox.factory;

import io.cloudslang.content.dropbox.entities.inputs.InputsWrapper;
import io.cloudslang.content.dropbox.factory.folders.FoldersHelper;

import static io.cloudslang.content.dropbox.entities.constants.Constants.FolderActions.CREATE_FOLDER;
import static org.apache.http.client.methods.HttpPost.METHOD_NAME;

/**
 * Created by TusaM
 * 5/31/2017.
 */
public class PayloadBuilder {
    private PayloadBuilder() {
        // prevent instantiation
    }

    public static void buildPayload(InputsWrapper wrapper) {
        if (METHOD_NAME.equalsIgnoreCase(wrapper.getHttpClientInputs().getMethod())) {
            switch (wrapper.getCommonInputs().getAction()) {
                case CREATE_FOLDER:
                    wrapper.getHttpClientInputs().setBody(new FoldersHelper().getCreateFolderPayload(wrapper));
                    break;
                default:
                    break;
            }
        }
    }
}