/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.dropbox.entities.inputs;

import static io.cloudslang.content.dropbox.utils.InputsUtil.getValidPath;
import static java.lang.Boolean.valueOf;

/**
 * Created by TusaM
 * 5/31/2017.
 */
public class FolderInputs {
    private final String path;

    private final Boolean autorename;

    private FolderInputs(Builder builder) {
        this.path = builder.path;
        this.autorename = builder.autorename;
    }

    public String getPath() {
        return path;
    }

    public Boolean getAutorename() {
        return autorename;
    }

    public static class Builder {
        private String path;

        private Boolean autorename;

        public FolderInputs build() {
            return new FolderInputs(this);
        }

        public Builder withPath(String input) {
            path = getValidPath(input);
            return this;
        }

        public Builder withAutoRename(String input) {
            autorename = valueOf(input);
            return this;
        }
    }
}