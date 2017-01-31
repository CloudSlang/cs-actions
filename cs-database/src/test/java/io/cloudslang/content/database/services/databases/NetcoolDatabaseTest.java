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

/**
 * Created by vranau on 12/10/2014.
 */
public class NetcoolDatabaseTest {

    public static final String DB_NAME = "/dbName";
    public static final String DB_SERVER = "dbServer";
    public static final int DB_PORT = 30;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testSetUp() throws ClassNotFoundException, SQLException {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("Could not locate either jconn2.jar or jconn3.jar file in the classpath");
        final NetcoolDatabase netcoolDatabase = new NetcoolDatabase();
        final SQLInputs sqlInputs = new SQLInputs();
        sqlInputs.setDbName(DB_NAME);
        sqlInputs.setDbServer(null);
        sqlInputs.setDbPort(DB_PORT);
        sqlInputs.setDbUrls(new ArrayList<String>());
        netcoolDatabase.setUp(sqlInputs);
    }
}
