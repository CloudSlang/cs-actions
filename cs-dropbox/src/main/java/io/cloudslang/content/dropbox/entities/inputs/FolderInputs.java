/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package io.cloudslang.content.dropbox.entities.inputs;

import static io.cloudslang.content.dropbox.entities.constants.Constants.Miscellaneous.CREATE_FOLDER_PATH_REGEX;
import static io.cloudslang.content.dropbox.entities.constants.Constants.Miscellaneous.DELETE_FOLDER_PATH_REGEX;
import static io.cloudslang.content.dropbox.utils.InputsUtil.getValidPath;
import static java.lang.Boolean.valueOf;

/**
 * Created by TusaM
 * 5/31/2017.
 */
public class FolderInputs {
    private final String createFolderPath;
    private final String deleteFileOrFolderPath;

    private final boolean autoRename;

    private FolderInputs(Builder builder) {
        this.createFolderPath = builder.createFolderPath;
        this.deleteFileOrFolderPath = builder.deleteFileOrFolderPath;

        this.autoRename = builder.autoRename;
    }

    public String getCreateFolderPath() {
        return createFolderPath;
    }

    public String getDeleteFileOrFolderPath() {
        return deleteFileOrFolderPath;
    }

    public boolean isAutoRename() {
        return autoRename;
    }

    public static class Builder {
        private String createFolderPath;
        private String deleteFileOrFolderPath;

        private boolean autoRename;

        public FolderInputs build() {
            return new FolderInputs(this);
        }

        public Builder withCreateFolderPath(String input) {
            createFolderPath = getValidPath(input, CREATE_FOLDER_PATH_REGEX);
            return this;
        }

        public Builder withDeleteFileOrFolderPath(String input) {
            deleteFileOrFolderPath = getValidPath(input, DELETE_FOLDER_PATH_REGEX);
            return this;
        }

        public Builder withAutoRename(String input) {
            autoRename = valueOf(input);
            return this;
        }
    }
}