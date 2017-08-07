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

import com.vmware.vim25.ObjectSpec;
import com.vmware.vim25.PropertyFilterSpec;
import com.vmware.vim25.PropertySpec;

import java.util.ArrayList;

import static java.util.Arrays.asList;

public class PropertyFilterSpecBuilder extends PropertyFilterSpec {
    private void init() {
        if (propSet == null) {
            propSet = new ArrayList<>();
        }
        if (objectSet == null) {
            objectSet = new ArrayList<>();
        }
    }

    public PropertyFilterSpecBuilder propSet(final PropertySpec... propertySpecs) {
        init();
        this.propSet.addAll(asList(propertySpecs));
        return this;
    }

    public PropertyFilterSpecBuilder objectSet(final ObjectSpec... objectSpecs) {
        init();
        this.objectSet.addAll(asList(objectSpecs));
        return this;
    }
}
