package io.cloudslang.content.utils;

import com.hp.oo.sdk.content.plugin.SessionResource;

import java.util.Map;

public class IteratorSessionResource extends SessionResource<Map<String, Object>> {

    private Map<String, Object> iteratorMap;

    IteratorSessionResource(final Map<String, Object> iteratorMap) {
        this.iteratorMap = iteratorMap;
    }

    @Override
    public Map<String, Object> get() {
        return iteratorMap;
    }

    @Override
    public void release() {
        iteratorMap = null;
    }
}

