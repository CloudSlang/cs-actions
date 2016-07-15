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
