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
import org.junit.Test;

import static io.cloudslang.content.database.constants.DBOtherValues.*;
import static io.cloudslang.content.database.utils.SQLInputsUtils.getOrDefaultDBName;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.Assert.assertEquals;

/**
 * Created by victor on 02.02.2017.
 */
public class SQLInputsUtilsTest {

    @Test
    public void getOrDefaultDBNameMSSql() throws Exception {
        final String dbName = "testDB";
        assertEquals(dbName, getOrDefaultDBName(dbName, MSSQL_DB_TYPE));
    }
    @Test
    public void getOrDefaultDBNameSimple() throws Exception {
        final String dbName = "testDB";
        assertEquals(FORWARD_SLASH + dbName, getOrDefaultDBName(dbName, ORACLE_DB_TYPE));
    }

    @Test
    public void getOrDefaultDBNameEmpty() throws Exception {
        assertEquals(EMPTY, getOrDefaultDBName(EMPTY, ORACLE_DB_TYPE));
        assertEquals(EMPTY, getOrDefaultDBName(EMPTY, MSSQL_DB_TYPE));
    }

    @Test
    public void getOrDefaultDBPort() throws Exception {

    }

    @Test
    public void getSqlCommands() throws Exception {

    }

    @Test
    public void getDbUrls() throws Exception {

    }

    @Test
    public void getOrDefaultDBPoolingProperties() throws Exception {

    }

    @Test
    public void getResultSetConcurrency() throws Exception {

    }

    @Test
    public void getResultSetType() throws Exception {

    }

    @Test
    public void getResultSetTypeForDbType() throws Exception {

    }

    @Test
    public void notInCollectionIgnoreCase() throws Exception {

    }

    @Test
    public void inCollectionIgnoreCase() throws Exception {

    }

    @Test
    public void getDbType() throws Exception {

    }

    @Test
    public void getSqlKey() throws Exception {

    }

}