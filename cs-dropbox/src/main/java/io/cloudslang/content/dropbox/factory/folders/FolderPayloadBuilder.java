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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.cloudslang.content.dropbox.entities.inputs.InputsWrapper;

import static io.cloudslang.content.dropbox.entities.constants.Constants.FolderActions.CREATE_FOLDER;
import static io.cloudslang.content.dropbox.entities.constants.Constants.FolderActions.DELETE_FILE_OR_FOLDER;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.http.client.methods.HttpPost.METHOD_NAME;

/**
 * Created by TusaM
 * 5/31/2017.
 */
public class FolderPayloadBuilder {
    private static final Gson GSON = new GsonBuilder().create();

    public static String getFolderPayload(InputsWrapper wrapper) {
        String action = wrapper.getCommonInputs().getAction();
        if (METHOD_NAME.equalsIgnoreCase(wrapper.getHttpClientInputs().getMethod())) {
            switch (action) {
                case CREATE_FOLDER:
                    return GSON.toJson(new FolderHelper().getCreateFolderPayload(wrapper), FolderPayload.class);
                case DELETE_FILE_OR_FOLDER:
                    return GSON.toJson(new FolderHelper().getDeleteFileOrFolderPayload(wrapper), FolderPayload.class);
                default:
                    return EMPTY;
            }
        }

        return EMPTY;
    }
}