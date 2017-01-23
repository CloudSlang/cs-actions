/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.database.utils;

import com.hp.oo.sdk.content.plugin.SessionResource;

import java.util.Map;

/**
 * Created by pinteae on 1/10/2017.
 */
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
