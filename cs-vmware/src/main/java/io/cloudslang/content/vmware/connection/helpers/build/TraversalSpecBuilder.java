/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.vmware.connection.helpers.build;

import com.vmware.vim25.TraversalSpec;

public class TraversalSpecBuilder extends TraversalSpec {
    public TraversalSpecBuilder name(final String name) {
        this.setName(name);
        return this;
    }

    public TraversalSpecBuilder path(final String path) {
        this.setPath(path);
        return this;
    }

    public TraversalSpecBuilder skip(final Boolean skip) {
        this.setSkip(skip);
        return this;
    }

    public TraversalSpecBuilder type(final String type) {
        this.setType(type);
        return this;
    }
}
