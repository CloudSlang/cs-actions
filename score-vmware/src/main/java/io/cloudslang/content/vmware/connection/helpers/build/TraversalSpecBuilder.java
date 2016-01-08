package io.cloudslang.content.vmware.connection.helpers.build;

import com.vmware.vim25.SelectionSpec;
import com.vmware.vim25.TraversalSpec;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Mihai Tusa.
 * 10/20/2015.
 */
public class TraversalSpecBuilder extends TraversalSpec {
    private void init() {
        if (selectSet == null) {
            selectSet = new ArrayList<>();
        }
    }

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

    public TraversalSpecBuilder selectSet(final SelectionSpec... selectionSpecs) {
        init();
        this.selectSet.addAll(Arrays.asList(selectionSpecs));
        return this;
    }
}