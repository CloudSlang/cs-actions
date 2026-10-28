

package io.cloudslang.content.oracle.oci.utils;

import com.hp.oo.sdk.content.plugin.SessionResource;

import java.util.Map;

public class CounterSessionResource extends SessionResource<Map<String, Object>> {

    private Map<String, Object> counterMap;

    CounterSessionResource(final Map<String, Object> counterMap) {
        this.counterMap = counterMap;
    }

    @Override
    public Map<String, Object> get() {
        return counterMap;
    }

    @Override
    public void release() {
        counterMap = null;
    }
}

