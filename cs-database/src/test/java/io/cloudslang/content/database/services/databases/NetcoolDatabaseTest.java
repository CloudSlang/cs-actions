/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.database.services.databases;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.SQLException;
import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

/**
 * Created by vranau on 12/10/2014.
 */
public class NetcoolDatabaseTest {

    public static final String DB_NAME = "/dbName";
    public static final String DB_SERVER = "dbServer";
    public static final String DB_PORT = "30";

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();
    private ArrayList<String> dbUrls = null;

    @Before
    public void setUp() {
        dbUrls = new ArrayList<>();
    }

    @Test
    public void testSetUp() throws ClassNotFoundException, SQLException {
        expectedEx.expect(ClassNotFoundException.class);
        expectedEx.expectMessage("Could not locate either jconn2.jar or jconn3.jar file in the classpath");
        NetcoolDatabase metcoolDatabase = new NetcoolDatabase();
        metcoolDatabase.setUp(DB_NAME, null, DB_PORT, null);
    }

    @Test
    public void testSetUpNoDbName() throws ClassNotFoundException, SQLException {
        expectedEx.expect(SQLException.class);
        expectedEx.expectMessage("No database provided!");
        NetcoolDatabase netcoolDatabase = new NetcoolDatabase();
        netcoolDatabase.setUp(null, DB_SERVER, DB_PORT, dbUrls);
        assertEquals("jdbc:jtds:sybase://dbServer:30;prepareSQL=1;useLOBs=false;TDS=4.2;", dbUrls.get(0));
    }

    @Test
    public void testSetUpNoDbPort() throws ClassNotFoundException, SQLException {
        expectedEx.expect(SQLException.class);
        expectedEx.expectMessage("No port provided!");
        NetcoolDatabase netcoolDatabase = new NetcoolDatabase();
        netcoolDatabase.setUp(DB_NAME, DB_SERVER, null, dbUrls);
    }

}
