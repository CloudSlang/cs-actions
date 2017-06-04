/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.dropbox.factory.folders;

import io.cloudslang.content.dropbox.entities.inputs.InputsWrapper;

import static io.cloudslang.content.dropbox.entities.constants.Constants.FolderActions.CREATE_FOLDER;
import static io.cloudslang.content.dropbox.entities.constants.Constants.HttpClientInputsValues.APPLICATION_JSON;
import static io.cloudslang.content.dropbox.entities.constants.Constants.HttpClientInputsValues.AUTHORIZATION_HEADER_PREFIX;
import static io.cloudslang.content.dropbox.entities.constants.Constants.Miscellaneous.BLANK;

/**
 * Created by TusaM
 * 5/31/2017.
 */
public class FolderHeadersBuilder {
    private FolderHeadersBuilder() {
        // prevent instantiation
    }

    public static void setFolderHeaders(InputsWrapper wrapper) {
        switch (wrapper.getCommonInputs().getAction()) {
            case CREATE_FOLDER:
                wrapper.getHttpClientInputs().setHeaders(AUTHORIZATION_HEADER_PREFIX + BLANK + wrapper.getCommonInputs().getAccessToken());
                wrapper.getHttpClientInputs().setContentType(APPLICATION_JSON);
                break;
            default:
                break;
        }
    }
}