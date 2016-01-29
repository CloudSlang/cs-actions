package io.cloudslang.content.vmware.connection.helpers.build;

import com.vmware.vim25.PropertySpec;

import java.util.ArrayList;
import java.util.Arrays;

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
        this.pathSet.addAll(Arrays.asList(paths));
        return this;
    }
}

