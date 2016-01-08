package io.cloudslang.content.vmware.connection.helpers.build;

import com.vmware.vim25.SelectionSpec;

/**
 * Created by Mihai Tusa.
 * 10/20/2015.
 */
public class SelectionSpecBuilder extends SelectionSpec {
    public SelectionSpecBuilder name(final String name) {
        this.setName(name);
        return this;
    }
}