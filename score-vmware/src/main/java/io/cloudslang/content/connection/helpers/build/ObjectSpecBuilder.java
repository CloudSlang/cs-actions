package io.cloudslang.content.connection.helpers.build;

import com.vmware.vim25.*;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Mihai Tusa.
 * 10/20/2015.
 */
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
