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

import com.vmware.vim25.PropertySpec;

import java.util.ArrayList;

import static java.util.Arrays.asList;

public class PropertySpecBuilder extends PropertySpec {
    private void init() {
        if (pathSet == null) {
            pathSet = new ArrayList<>();
        }
    }

    public PropertySpecBuilder all(final Boolean all) {
        this.setAll(all);
        return this;
    }

    public PropertySpecBuilder type(final String type) {
        this.setType(type);
        return this;
    }

    public PropertySpecBuilder pathSet(final String... paths) {
        init();
        this.pathSet.addAll(asList(paths));
        return this;
    }
}
