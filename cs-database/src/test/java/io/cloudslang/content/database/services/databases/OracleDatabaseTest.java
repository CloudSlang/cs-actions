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
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by vranau on 12/10/2014.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({OracleDatabase.class})
@PowerMockIgnore({"javax.management.*", "org.apache.commons.logging.*"})
public class OracleDatabaseTest {
    public static final String ORACLE_URL = "jdbc:oracle:thin:@";
    public static final String DB_SERVER = "localhost";
    public static final int DB_PORT = 30;
    public static final String SLASH = "/";
    public static final String DB_NAME = "testDB";
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();
    private OracleDatabase oracleDatabase = null;

    @Before
    public void setUp() {
        oracleDatabase = new OracleDatabase();
    }

    @Test
    public void testSetUpConnectionOracleWithoutTns() throws Exception {
        //set up method needs a SLASH before dbName (if dbName is provided)
        final SQLInputs sqlInputs = new SQLInputs();
        sqlInputs.setDbName(SLASH + DB_NAME);
        sqlInputs.setDbServer(DB_SERVER);
        sqlInputs.setDbPort(DB_PORT);
//        sqlInputs.setDbUrls(new ArrayList<String>());
        final List<String> dbUrls = oracleDatabase.setUp(sqlInputs);
        assertEquals(2, dbUrls.size());
        assertEquals(ORACLE_URL + "//" + DB_SERVER + ":" + DB_PORT + "/" + DB_NAME, dbUrls.get(0));
        assertEquals(ORACLE_URL + DB_SERVER + ":" + DB_PORT + ":" + DB_NAME, dbUrls.get(1));
    }

}
