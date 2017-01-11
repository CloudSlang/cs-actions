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
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * Created by vranau on 12/10/2014.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({OracleDatabase.class})
@PowerMockIgnore({"javax.management.*", "org.apache.commons.logging.*"})
public class OracleDatabaseTest {
    public static final String TNS_PATH = "tnsPath";
    public static final String ORACLE_URL = "jdbc:oracle:thin:@";
    public static final String TNS_ENTRY = "tnsEntry";
    public static final String DB_SERVER = "localhost";
    public static final String DB_PORT = "30";
    public static final String SLASH = "/";
    public static final String DB_NAME = "testDB";
    private ArrayList<String> dbUrls = null;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    private OracleDatabase oracleDatabase = null;

    @Mock
    private java.io.File tnsPathMock;
    @Mock
    private java.io.File tnsFileOraMock;

    @Before
    public void setUp() {
        oracleDatabase = new OracleDatabase();
        dbUrls = new ArrayList<>();
    }

    @Test
    public void testSetUpConnectionOracleEmptyTnsPath() throws Exception {
        checkExpectedSQLException("Empty TNSPath for Oracle.");
        oracleDatabase.setUp(null, null, null, null, null, null);
    }

    @Test
    public void testSetUpConnectionOracleNotExistingTnsPath() throws Exception {
        checkExpectedSQLException("Invalid TNSPath for Oracle : not existing");
        oracleDatabase.setUp(null, null, null, null, "not existing", null);
    }

    @Test
    public void testSetUpConnectionOracleNotExistingTnsFile() throws Exception {
        checkExpectedSQLException("Failed to find tnsnames.ora file from :" + TNS_PATH);

        whenNew(File.class).withArguments(TNS_PATH).thenReturn(tnsPathMock);
        when(tnsPathMock.exists()).thenReturn(true);
        when(tnsPathMock.isDirectory()).thenReturn(true);

        whenNew(File.class).withArguments(TNS_PATH + File.separator + "tnsnames.ora").thenReturn(tnsFileOraMock);
        when(tnsFileOraMock.exists()).thenReturn(false);
        oracleDatabase.setUp(null, null, null, null, TNS_PATH, null);
    }

    @Test
    public void testSetUpConnectionOracleNotDirectory() throws Exception {
        whenNew(File.class).withArguments(TNS_PATH).thenReturn(tnsPathMock);
        when(tnsPathMock.exists()).thenReturn(true);
        checkExpectedSQLException("Invalid TNSPath for Oracle, TNSPath is not a directory: " + TNS_PATH);
        oracleDatabase.setUp(null, null, null, null, TNS_PATH, null);
    }

    @Test
    public void testSetUpConnectionOracleWithTns() throws Exception {
        String originalTnsPath = System.getProperty("oracle.net.tns_admin");
        try {
            whenNew(File.class).withArguments(TNS_PATH).thenReturn(tnsPathMock);
            when(tnsPathMock.exists()).thenReturn(true);
            when(tnsPathMock.isDirectory()).thenReturn(true);

            whenNew(File.class).withArguments(TNS_PATH + File.separator + "tnsnames.ora").thenReturn(tnsFileOraMock);
            when(tnsFileOraMock.exists()).thenReturn(true);
            oracleDatabase.setUp(null, null, null, dbUrls, TNS_PATH, TNS_ENTRY);
            assertEquals(1, dbUrls.size());
            assertEquals(ORACLE_URL + TNS_ENTRY, dbUrls.get(0));
            assertEquals(TNS_PATH, System.getProperty("oracle.net.tns_admin"));
        } finally {
            if (originalTnsPath != null) {
                System.setProperty("oracle.net.tns_admin", originalTnsPath);
            } else {
                System.setProperty("oracle.net.tns_admin", "");
            }
        }
    }

    @Test
    public void testSetUpConnectionOracleWithoutTns() throws Exception {
        //set up method needs a SLASH before dbName (if dbName is provided)
        oracleDatabase.setUp(SLASH + DB_NAME, DB_SERVER, DB_PORT, dbUrls, TNS_PATH, TNS_ENTRY);
        assertEquals(2, dbUrls.size());
        assertEquals(ORACLE_URL + "//" + DB_SERVER + ":" + DB_PORT + "/" + DB_NAME, dbUrls.get(0));
        assertEquals(ORACLE_URL + DB_SERVER + ":" + DB_PORT + ":" + DB_NAME, dbUrls.get(1));
    }

    private void checkExpectedSQLException(String message) {
        expectedEx.expect(SQLException.class);
        expectedEx.expectMessage(message);
    }
}
