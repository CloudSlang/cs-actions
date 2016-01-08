package io.cloudslang.content.vmware.connection.helpers.build;

import com.vmware.vim25.ObjectSpec;
import com.vmware.vim25.PropertyFilterSpec;
import com.vmware.vim25.PropertySpec;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Mihai Tusa.
 * 10/20/2015.
 */
public class PropertyFilterSpecBuilder extends PropertyFilterSpec {
    private void init() {
        if (propSet == null) {
            propSet = new ArrayList<>();
        }
        if(objectSet == null) {
            objectSet = new ArrayList<>();
        }
    }

    public PropertyFilterSpecBuilder propSet(final PropertySpec... propertySpecs) {
        init();
        this.propSet.addAll(Arrays.asList(propertySpecs));
        return this;
    }

    public PropertyFilterSpecBuilder objectSet(final ObjectSpec... objectSpecs) {
        init();
        this.objectSet.addAll(Arrays.asList(objectSpecs));
        return this;
    }
}