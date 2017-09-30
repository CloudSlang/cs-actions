/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package io.cloudslang.content.dropbox.factory.folders;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created by TusaM
 * 5/31/2017.
 */
public class FolderPayload {
    private String path;
    private Boolean autorename;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean getAutorename() {
        return autorename;
    }

    public void setAutorename(Boolean autorename) {
        this.autorename = autorename;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        FolderPayload that = (FolderPayload) o;

        return new EqualsBuilder()
                .append(path, that.path)
                .append(autorename, that.autorename)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(path)
                .append(autorename)
                .toHashCode();
    }
}