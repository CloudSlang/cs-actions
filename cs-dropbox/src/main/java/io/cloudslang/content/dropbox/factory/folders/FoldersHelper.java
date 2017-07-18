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

/**
 * Created by TusaM
 * 5/31/2017.
 */
public class FoldersHelper {
    private static final Gson gson = new GsonBuilder().create();

    public String getCreateFolderPayload(InputsWrapper wrapper) {
        return gson.toJson(getPayloadObj(wrapper), CreateFolderPayload.class);
    }

    private CreateFolderPayload getPayloadObj(InputsWrapper wrapper) {
        CreateFolderPayload payloadObj = new CreateFolderPayload();
        payloadObj.setAutorename(wrapper.getFolderInputs().getAutorename());
        payloadObj.setPath(wrapper.getFolderInputs().getPath());

        return payloadObj;
    }
}