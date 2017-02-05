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
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static io.cloudslang.content.database.constants.DBOtherValues.*;
import static io.cloudslang.content.database.utils.SQLInputsUtils.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

/**
 * Created by victor on 02.02.2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(SQLUtils.class)
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
    public void getOrDefaultDBPortEmptyForDBType() throws Exception {
        assertEquals(DEFAULT_PORT_ORACLE.intValue(), getOrDefaultDBPort(EMPTY, ORACLE_DB_TYPE));
        assertEquals(DEFAULT_PORT_MSSQL.intValue(), getOrDefaultDBPort(EMPTY, MSSQL_DB_TYPE));
        assertEquals(DEFAULT_PORT_MYSQL.intValue(), getOrDefaultDBPort(EMPTY, MYSQL_DB_TYPE));
        assertEquals(DEFAULT_PORT_PSQL.intValue(), getOrDefaultDBPort(EMPTY, POSTGRES_DB_TYPE));
        assertEquals(DEFAULT_PORT_SYBASE.intValue(), getOrDefaultDBPort(EMPTY, SYBASE_DB_TYPE));
        assertEquals(DEFAULT_PORT_DB2.intValue(), getOrDefaultDBPort(EMPTY, DB2_DB_TYPE));
        assertEquals(DEFAULT_PORT_NETCOOL.intValue(), getOrDefaultDBPort(EMPTY, NETCOOL_DB_TYPE));
    }

    @Test
    public void getOrDefaultDBPortSimple() throws Exception {
        assertEquals(1234, getOrDefaultDBPort("1234", ORACLE_DB_TYPE));
        assertEquals(4321, getOrDefaultDBPort("4321", EMPTY));
    }

    @Test
    public void getOrDefaultDBPortInvalid() throws Exception {
        assertEquals(-1, getOrDefaultDBPort(EMPTY, EMPTY));
    }

    @Test
    public void getSqlCommandsInvalid() throws Exception {
        assertTrue(getSqlCommands(EMPTY, EMPTY, EMPTY).isEmpty());
        assertTrue(getSqlCommands(null, null, ", ").isEmpty());
        assertTrue(getSqlCommands(null, null, null).isEmpty());
    }

    @Test
    public void getSqlCommandsCommandStr() throws Exception {
        assertThat(Arrays.asList("a", "b", "c", "d", "e", "f", "g"), is(getSqlCommands("a,b,c,d,e,f,g", EMPTY, ",")));
        assertThat(Collections.<String>emptyList(), is(getSqlCommands(EMPTY, EMPTY, ",")));
        assertThat(Collections.singletonList("a,b,c,d,e,f,g"), is(getSqlCommands("a,b,c,d,e,f,g", EMPTY, "|")));
    }

    @Test
    public void getSqlCommandsScriptFile() throws Exception {
        final List<String> commandsScript = Arrays.asList("a", "b", "c", "d", "e", "f", "g");
        final String scriptName = "someFile";
        PowerMockito.mockStatic(SQLUtils.class);

        given(SQLUtils.readFromFile(scriptName)).willReturn(commandsScript);
        assertThat(commandsScript, is(getSqlCommands(EMPTY, scriptName, ",")));
        PowerMockito.verifyStatic();
    }

    @Test
    public void getDbUrlsValid() throws Exception {
        final List<String> dbUrls = getDbUrls("www.google.com");
        assertEquals(1, dbUrls.size());
        assertEquals("www.google.com", dbUrls.get(0));
    }

    @Test
    public void getDbUrlsEmpty() throws Exception {
        assertEquals(0, getDbUrls(EMPTY).size());
        assertEquals(0, getDbUrls(null).size());
    }

    @Test
    public void getOrDefaultDBPoolingProperties() throws Exception {

    }

    @Test
    public void getResultSetConcurrencySimple() throws Exception {
        assertEquals(ResultSet.CONCUR_READ_ONLY, getResultSetConcurrency(CONCUR_READ_ONLY));
        assertEquals(ResultSet.CONCUR_UPDATABLE, getResultSetConcurrency(CONCUR_UPDATABLE));
    }

    @Test
    public void getResultSetConcurrencyInvalid() throws Exception {
        assertEquals(-1000000, getResultSetConcurrency(EMPTY));
        assertEquals(-1000000, getResultSetConcurrency(null));
        assertEquals(-1000000, getResultSetConcurrency("123"));
    }

    @Test
    public void getResultSetTypeSimple() throws Exception {
        assertEquals(ResultSet.TYPE_FORWARD_ONLY, getResultSetType(TYPE_FORWARD_ONLY));
        assertEquals(ResultSet.TYPE_SCROLL_INSENSITIVE, getResultSetType(TYPE_SCROLL_INSENSITIVE));
        assertEquals(ResultSet.TYPE_SCROLL_SENSITIVE, getResultSetType(TYPE_SCROLL_SENSITIVE));
    }

    @Test
    public void getResultSetTypeInvalid() throws Exception {
        assertEquals(-1000000, getResultSetType(EMPTY));
        assertEquals(-1000000, getResultSetType(null));
        assertEquals(-1000000, getResultSetType("123"));
    }

    @Test
    public void getResultSetTypeForDbTypeDB2() throws Exception {
        assertEquals(ResultSet.TYPE_FORWARD_ONLY, getResultSetTypeForDbType(null, DB2_DB_TYPE));
        assertEquals(ResultSet.TYPE_FORWARD_ONLY, getResultSetTypeForDbType(EMPTY, DB2_DB_TYPE));
        assertEquals(ResultSet.TYPE_FORWARD_ONLY, getResultSetTypeForDbType(TYPE_SCROLL_INSENSITIVE, DB2_DB_TYPE));
    }

    @Test
    public void getResultSetTypeForDbTypeSimple() throws Exception {
        assertEquals(ResultSet.TYPE_FORWARD_ONLY, getResultSetTypeForDbType(TYPE_FORWARD_ONLY, ORACLE_DB_TYPE));
        assertEquals(ResultSet.TYPE_SCROLL_INSENSITIVE, getResultSetTypeForDbType(TYPE_SCROLL_INSENSITIVE, MYSQL_DB_TYPE));
        assertEquals(ResultSet.TYPE_SCROLL_SENSITIVE, getResultSetTypeForDbType(TYPE_SCROLL_SENSITIVE, MSSQL_DB_TYPE));
    }

    @Test
    public void getResultSetTypeForDbTypeInvalid() throws Exception {
        assertEquals(-1000000, getResultSetTypeForDbType(EMPTY, EMPTY));
        assertEquals(-1000000, getResultSetTypeForDbType(null, null));
        assertEquals(-1000000, getResultSetTypeForDbType("123", "123"));

    }

    @Test
    public void notInCollectionIgnoreCaseTrue() throws Exception {
        assertTrue(notInCollectionIgnoreCase("1", Arrays.asList("a", "b", "c")));
        assertTrue(notInCollectionIgnoreCase("2", Collections.<String>emptyList()));
        assertTrue(notInCollectionIgnoreCase("1", Collections.<String>emptySet()));
    }

    @Test
    public void notInCollectionIgnoreCaseFalse() throws Exception {
        assertFalse(notInCollectionIgnoreCase("1", Arrays.asList("a", "1", "c")));
        assertFalse(notInCollectionIgnoreCase("A", Arrays.asList("a", "1", "c")));
        assertFalse(notInCollectionIgnoreCase("C", Arrays.asList("a", "1", "c")));
    }

    @Test
    public void inCollectionIgnoreCaseTrue() throws Exception {
        assertTrue(inCollectionIgnoreCase("1", Arrays.asList("a", "1", "c")));
        assertTrue(inCollectionIgnoreCase("A", Arrays.asList("a", "1", "c")));
        assertTrue(inCollectionIgnoreCase("C", Arrays.asList("a", "1", "c")));
    }

    @Test
    public void inCollectionIgnoreCaseFalse() throws Exception {
        assertFalse(inCollectionIgnoreCase("1", Arrays.asList("a", "b", "c")));
        assertFalse(inCollectionIgnoreCase("2", Collections.<String>emptyList()));
        assertFalse(inCollectionIgnoreCase("1", Collections.<String>emptySet()));
    }

    @Test
    public void getDbTypeSimple() throws Exception {
        assertEquals(ORACLE_DB_TYPE, getDbType("oraCle "));
        assertEquals(MSSQL_DB_TYPE, getDbType("mssql"));
        assertEquals(MYSQL_DB_TYPE, getDbType(" mySqL "));
        assertEquals("1s", getDbType("1s"));
    }

    @Test
    public void getSqlKey() throws Exception {

    }

}