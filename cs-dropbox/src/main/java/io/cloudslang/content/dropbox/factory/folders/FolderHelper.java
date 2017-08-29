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

class FolderHelper {
    FolderPayload getCreateFolderPayload(InputsWrapper wrapper) {
        FolderPayload payload = new FolderPayload();
        payload.setAutorename(wrapper.getFolderInputs().isAutoRename());
        payload.setPath(wrapper.getFolderInputs().getCreateFolderPath());

        return payload;
    }

    FolderPayload getDeleteFileOrFolderPayload(InputsWrapper wrapper) {
        FolderPayload payload = new FolderPayload();
        payload.setAutorename(null);
        payload.setPath(wrapper.getFolderInputs().getDeleteFileOrFolderPath());

        return payload;
    }
}