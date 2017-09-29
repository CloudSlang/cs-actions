/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
*/
package io.cloudslang.content.database.services.databases;

import io.cloudslang.content.database.utils.SQLInputs;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class PSQLDatabaseTest {

    private static final String DB_NAME = "dbName";
    private static final String DB_SERVER = "dbServer";
    private static final int DB_PORT = 5432;
    private static final String DB_SERVER_IPV6_LITERAL = "2001-0db8-85a3-0042-1000-8a2e-0370-7334.ipv6-literal.net";

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testSetUpNoDbName() throws ClassNotFoundException, SQLException {
        PostgreSqlDatabase pSqlDatabase = new PostgreSqlDatabase();
        final SQLInputs sqlInputs = SQLInputs.builder().build();
        sqlInputs.setDbName(EMPTY);
        sqlInputs.setDbServer(DB_SERVER);
        sqlInputs.setDbPort(DB_PORT);
//        sqlInputs.setDbUrls(new ArrayList<String>());

        final List<String> dbUrls = pSqlDatabase.setUp(sqlInputs);
        assertEquals("jdbc:postgresql://dbServer:5432", dbUrls.get(0));
        assertEquals(1, dbUrls.size());
    }

    @Test
    public void testSetUpNoServerName() throws ClassNotFoundException, SQLException {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("host   not valid");
        PostgreSqlDatabase pSqlDatabase = new PostgreSqlDatabase();
        final SQLInputs sqlInputs = SQLInputs.builder().build();
        sqlInputs.setDbName(DB_NAME);
        sqlInputs.setDbServer(null);
        sqlInputs.setDbPort(DB_PORT);
//        sqlInputs.setDbUrls(new ArrayList<String>());
        pSqlDatabase.setUp(sqlInputs);
    }

    @Test
    public void testSetUpAll() throws ClassNotFoundException, SQLException {
        PostgreSqlDatabase pSqlDatabase = new PostgreSqlDatabase();
        final SQLInputs sqlInputs = SQLInputs.builder().build();
        sqlInputs.setDbName(DB_NAME);
        sqlInputs.setDbServer(DB_SERVER);
        sqlInputs.setDbPort(DB_PORT);
//        sqlInputs.setDbUrls(new ArrayList<String>());
        final List<String> dbUrls = pSqlDatabase.setUp(sqlInputs);

        assertEquals("jdbc:postgresql://dbServer:5432/dbName", dbUrls.get(0));
        assertEquals(1, dbUrls.size());
    }

    @Test
    public void testSetUpAllIPV6LIteral() throws ClassNotFoundException, SQLException {
        PostgreSqlDatabase pSqlDatabase = new PostgreSqlDatabase();
        final SQLInputs sqlInputs = SQLInputs.builder().build();
        sqlInputs.setDbName(DB_NAME);
        sqlInputs.setDbServer(DB_SERVER_IPV6_LITERAL);
        sqlInputs.setDbPort(DB_PORT);
//        sqlInputs.setDbUrls(new ArrayList<String>());
        final List<String> dbUrls = pSqlDatabase.setUp(sqlInputs);

        assertEquals("jdbc:postgresql://2001-0db8-85a3-0042-1000-8a2e-0370-7334.ipv6-literal.net:5432/dbName", dbUrls.get(0));
        assertEquals(1, dbUrls.size());
    }
}
