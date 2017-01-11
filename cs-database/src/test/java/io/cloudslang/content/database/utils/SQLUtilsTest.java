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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.SQLException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.*;

/**
 * Created by victor on 10.01.2017.
 */
public class SQLUtilsTest {

    public static final String SQL_STATE = "s1000";
    public static String CUSTOM_URL = "jdbc:h2:tcp://localhost/~/test";
    public static final String MSSQL_URL = "jdbc:jtds:sqlserver://";
    public static final String DB_SERVER_IPV6_LITERAL = "2001-0db8-85a3-0042-1000-8a2e-0370-7334.ipv6-literal.net";

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testProcessHostorTNSNullServerName() throws Exception {
        expectedEx.expect(Exception.class);
        expectedEx.expectMessage("Failed to getConnection. Please provide DBServerName if you use JDBC. Or please provide TNSEntry if you use TNS");
        SQLUtils.processHostorTNS(Constants.ORACLE_DB_TYPE, null, null);
    }

    @Test
    public void testProcessHostOrTNSOtherEmptyServerName() throws Exception {
        expectedEx.expect(Exception.class);
        expectedEx.expectMessage("Failed to getConnection. DBServerName is empty.");
        SQLUtils.processHostorTNS("other t", "", null);
    }

    @Test
    public void testProcessHostOrTNSOtherAllNull() throws Exception {
        SQLUtils.processHostorTNS(null, null, null);
    }

    @Test
    public void testProcessHostOrTNSOther() throws Exception {
        SQLUtils.processHostorTNS("other t", "localhost", "tnsEntry");
    }

    @Test
    public void testTrimRowStat() {
        boolean trimRowstat = SQLUtils.trimRowstat(CUSTOM_URL, "true");
        assertFalse(trimRowstat);

        trimRowstat = SQLUtils.trimRowstat(CUSTOM_URL, "false");
        assertFalse(trimRowstat);

        trimRowstat = SQLUtils.trimRowstat(MSSQL_URL, "false");
        assertFalse(trimRowstat);

        trimRowstat = SQLUtils.trimRowstat(MSSQL_URL, "true");
        assertTrue(trimRowstat);

        trimRowstat = SQLUtils.trimRowstat(null, null);
        assertFalse(trimRowstat);

        trimRowstat = SQLUtils.trimRowstat(null, "true");
        assertFalse(trimRowstat);

        trimRowstat = SQLUtils.trimRowstat(MSSQL_URL, null);
        assertTrue(trimRowstat);

        trimRowstat = SQLUtils.trimRowstat(CUSTOM_URL, null);
        assertFalse(trimRowstat);
    }

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
        String sessionIdInput = SQLUtils.computeSessionId("sessionIdInput");
        assertEquals("SQLQuery:920c83186d589d993633e3e0ddf6f25b7815cccd241b5a8ef1569558b07764b7", sessionIdInput);

        sessionIdInput = SQLUtils.computeSessionId(null);
        assertEquals(null, sessionIdInput);
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

}