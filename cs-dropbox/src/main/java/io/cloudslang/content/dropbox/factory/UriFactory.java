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

import static io.cloudslang.content.dropbox.entities.constants.Constants.Api.FILES;
import static io.cloudslang.content.dropbox.entities.constants.Constants.Api.FOLDERS;
import static io.cloudslang.content.dropbox.entities.constants.Constants.ErrorMessages.UNSUPPORTED_DROPBOX_API;
import static io.cloudslang.content.dropbox.entities.constants.Constants.Values.VERSION_2;
import static io.cloudslang.content.dropbox.entities.dropbox.ApiVersion.TWO;
import static io.cloudslang.content.dropbox.entities.dropbox.ApiVersion.getApiVersion;
import static io.cloudslang.content.dropbox.entities.dropbox.DropboxApi.getApi;
import static io.cloudslang.content.dropbox.entities.dropbox.SuffixUri.getSuffixUri;
import static io.cloudslang.content.dropbox.utils.InputsUtil.getInputWithDefaultValue;
import static io.cloudslang.content.dropbox.utils.InputsUtil.toAppend;
import static java.lang.String.valueOf;

/**
 * Created by TusaM
 * 5/29/2017.
 */
public class UriFactory {
    private UriFactory() {
        // prevent instantiation
    }

    public static String getUri(InputsWrapper wrapper) {
        String action = wrapper.getCommonInputs().getAction();
        String apiVersion = getInputWithDefaultValue(wrapper.getCommonInputs().getVersion(), getApiVersion(TWO.name()));
        String suffixUriKey = apiVersion.contains(valueOf(VERSION_2)) ? action + VERSION_2 : action;

        switch (wrapper.getCommonInputs().getApi()) {
            case FOLDERS:
                return toAppend(apiVersion + getApi(FILES), getSuffixUri(suffixUriKey));
            default:
                throw new RuntimeException(UNSUPPORTED_DROPBOX_API);
        }
    }
}