package io.cloudslang.content.database.utils;

import com.hp.oo.sdk.content.plugin.SessionResource;

import java.util.Map;

public class SQLSessionResource extends SessionResource<Map<String, Object>> {

    private Map<String, Object> sqlConnectionMap;

    public SQLSessionResource(final Map<String, Object> sqlConnectionMap) {
        this.sqlConnectionMap = sqlConnectionMap;
    }

    @Override
    public Map<String, Object> get() {
        return sqlConnectionMap;
    }

    @Override
    public void release() {
        sqlConnectionMap = null;
    }
}
