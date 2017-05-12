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

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import org.jetbrains.annotations.NotNull;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.database.utils.SQLUtils.getStrColumns;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by victor on 10.01.2017.
 */
public class SQLUtilsTest {

    public static final String SQL_STATE = "s1000";
    //    public static final String MSSQL_URL = "jdbc:jtds:sqlserver://";
    public static final String DB_SERVER_IPV6_LITERAL = "2001-0db8-85a3-0042-1000-8a2e-0370-7334.ipv6-literal.net";
    public static String CUSTOM_URL = "jdbc:h2:tcp://localhost/~/test";
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testProcessNullTerminatedString() {
        String value = SQLUtils.processNullTerminatedString("\0");
        assertEquals("null", value);

        value = SQLUtils.processNullTerminatedString("value\0");
        assertEquals("value", value);

        value = SQLUtils.processNullTerminatedString("value");
        assertEquals("value", value);

        value = SQLUtils.processNullTerminatedString(null);
        assertEquals("null", value);

        value = SQLUtils.processNullTerminatedString("");
        assertEquals("null", value);
    }

    @Test
    public void testGetIPv4OrIPv6WithSquareBracketsHost() {
        String host = SQLUtils.getIPv4OrIPv6WithSquareBracketsHost("localhost");
        assertEquals("localhost", host);

        host = SQLUtils.getIPv4OrIPv6WithSquareBracketsHost("2001:db8:85a3:0:0:8a2e:370:7334");
        assertEquals("[2001:db8:85a3:0:0:8a2e:370:7334]", host);
    }

    @Test
    public void testGetIPv4OrIPv6WithSquareBracketsHostInvalid() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("host [2001-0db8-85a3-0042-1000-8a2e-0370-7334.ipv6-literal.net  not valid");
        SQLUtils.getIPv4OrIPv6WithSquareBracketsHost("[" + DB_SERVER_IPV6_LITERAL);
    }

    @Test
    public void testGetIPv4OrIPv6WithSquareBracketsHostNull() {
        expectedEx.expect(Exception.class);
        expectedEx.expectMessage("host   not valid");
        SQLUtils.getIPv4OrIPv6WithSquareBracketsHost(null);
    }

    @Test
    public void testToString() {
        final String toString = SQLUtils.toString(new SQLException("test ex"));
        assertTrue(toString.startsWith("java.sql.SQLException: test ex"));
    }

    @Test
    public void testComputeSessionId() {
        final String sessionIdInput = SQLUtils.computeSessionId("sessionIdInput");
        assertEquals("SQLQuery:920c83186d589d993633e3e0ddf6f25b7815cccd241b5a8ef1569558b07764b7", sessionIdInput);
    }


    @Test
    public void testProcessDumpException() throws SQLException {

        SQLException sqlException = new SQLException("dump is complete") {
            @Override
            public String getSQLState() {
                return SQL_STATE;
            }
        };
        final String dumpException = SQLUtils.processDumpException(sqlException);
        assertEquals("dump is complete", dumpException);
    }

    @Test
    public void testProcessDumpExceptionNoState() throws SQLException {
        expectedEx.expect(SQLException.class);
        expectedEx.expectMessage("test");
        SQLUtils.processDumpException(new SQLException("test"));
    }

    @Test
    public void testProcessDumpExceptionNoMessage() throws SQLException {
        expectedEx.expect(SQLException.class);

        SQLException sqlException = new SQLException() {
            @Override
            public String getSQLState() {
                return SQL_STATE;
            }
        };
        SQLUtils.processDumpException(sqlException);
    }

    @Test
    public void testProcessLoadException() throws SQLException {

        SQLException sqlException = new SQLException("load is complete") {
            @Override
            public String getSQLState() {
                return SQL_STATE;
            }

        };
        final String loadException = SQLUtils.processLoadException(sqlException);
        assertEquals("load is complete", loadException);
    }

    @Test
    public void testProcessLoadExceptionNoState() throws SQLException {
        expectedEx.expect(SQLException.class);
        expectedEx.expectMessage("test");
        SQLUtils.processLoadException(new SQLException("test"));
    }

    @Test
    public void testProcessLoadExceptionNoMessage() throws SQLException {
        expectedEx.expect(SQLException.class);

        SQLException sqlException = new SQLException() {
            @Override
            public String getSQLState() {
                return SQL_STATE;
            }
        };
        SQLUtils.processLoadException(sqlException);
    }

    @Test
    public void getStrColumnsValid() throws Exception {
        final String aKey = "aKey";

        final GlobalSessionObject<Map<String, Object>> globalSessionObject = new GlobalSessionObject<>();
        final Map<String, Object> stringMap = new HashMap<>();
        stringMap.put(aKey, aKey);
        globalSessionObject.setResource(new SQLSessionResource(stringMap));

        assertThat(getStrColumns(globalSessionObject, aKey), is(aKey));
    }

}