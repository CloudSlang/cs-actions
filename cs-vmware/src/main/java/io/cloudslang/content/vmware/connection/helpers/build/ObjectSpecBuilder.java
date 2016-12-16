/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.vmware.connection.helpers.build;

import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.ObjectSpec;
import com.vmware.vim25.SelectionSpec;

import java.util.ArrayList;
import java.util.Arrays;

public class ObjectSpecBuilder extends ObjectSpec {
    private void init() {
        if (selectSet == null) {
            selectSet = new ArrayList<>();
        }
    }

    public ObjectSpecBuilder obj(final ManagedObjectReference objectReference) {
        this.setObj(objectReference);
        return this;
    }

    public ObjectSpecBuilder skip(final Boolean skip) {
        this.setSkip(skip);
        return this;
    }

    public ObjectSpecBuilder selectSet(final SelectionSpec... selectionSpecs) {
        init();
        this.selectSet.addAll(Arrays.asList(selectionSpecs));
        return this;
    }
}
