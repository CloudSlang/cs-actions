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

import io.cloudslang.content.database.utils.Constants;
import io.cloudslang.content.database.utils.SQLInputs;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static io.cloudslang.content.database.constants.DBDefaultValues.AUTH_SQL;
import static io.cloudslang.content.database.constants.DBOtherValues.BACK_SLASH;
import static io.cloudslang.content.database.utils.Constants.AUTH_WINDOWS;
import static io.cloudslang.content.database.utils.Constants.SQLSERVER_JDBC_DRIVER;
import static junit.framework.Assert.assertEquals;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Created by vranau on 12/10/2014.
 */
public class MSSqlDatabaseTest {

    public static final String DB_NAME = "dbName";
    public static final String DB_SERVER = "dbServer";
    public static final String INSTANCE = "instance";
    public static final String DB_SERVER_WITH_INSTANCE = DB_SERVER + BACK_SLASH + INSTANCE;
    public static final int DB_PORT = 1433;
    public static final String DB_SERVER_IPV6_LITERAL = "2001-0db8-85a3-0042-1000-8a2e-0370-7334.ipv6-literal.net";
    public static final String AUTH_TYPE = "authType";
    public static final String INVALID_AUTH_TYPE = "invalidAuthType";

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testSetUpInvalidAuthType() throws ClassNotFoundException, SQLException {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("Invalid authentication type for MS SQL : " + INVALID_AUTH_TYPE);
        MSSqlDatabase mSSqlDatabase = new MSSqlDatabase();

        final SQLInputs sqlInputs = getSqlInputsForMSSql(DB_NAME, DB_SERVER, INVALID_AUTH_TYPE, INSTANCE);


        final List<String> dbUrls = mSSqlDatabase.setUp(sqlInputs);
        assertEquals(1, dbUrls.size());
    }

    @Test
    public void testSetUpNoDbName() throws ClassNotFoundException, SQLException {
        MSSqlDatabase mSSqlDatabase = new MSSqlDatabase();
        final SQLInputs sqlInputs = getSqlInputsForMSSql(EMPTY, DB_SERVER, AUTH_WINDOWS, INSTANCE);


        final List<String> dbUrls = mSSqlDatabase.setUp(sqlInputs);

        assertEquals("jdbc:sqlserver://dbServer:1433;DatabaseName=;instance=instance;integratedSecurity=true;encrypt=true;trustServerCertificate=true", dbUrls.get(0));
        assertEquals(1, dbUrls.size());
    }

    @Test
    public void testSetUpNoServerName() throws ClassNotFoundException, SQLException {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("host   not valid");
        MSSqlDatabase mSSqlDatabase = new MSSqlDatabase();

        final SQLInputs sqlInputs = getSqlInputsForMSSql(DB_NAME, EMPTY, AUTH_TYPE, INSTANCE);

        mSSqlDatabase.setUp(sqlInputs);
    }

    @Test
    public void testSetUpAllAuthWindows() throws ClassNotFoundException, SQLException {
        MSSqlDatabase mSSqlDatabase = new MSSqlDatabase();

        final SQLInputs sqlInputs = getSqlInputsForMSSql(DB_NAME, DB_SERVER, AUTH_WINDOWS, INSTANCE);


        final List<String> dbUrls = mSSqlDatabase.setUp(sqlInputs);
        assertEquals("jdbc:sqlserver://dbServer:1433;DatabaseName=dbName;instance=instance;integratedSecurity=true;encrypt=true;trustServerCertificate=true", dbUrls.get(0));
        assertEquals(1, dbUrls.size());
    }

    @Test
    public void testSetUpAllAuthSQL() throws ClassNotFoundException, SQLException {
        MSSqlDatabase mSSqlDatabase = new MSSqlDatabase();

        final SQLInputs sqlInputs = getSqlInputsForMSSql(DB_NAME, DB_SERVER, AUTH_SQL, INSTANCE);


        final List<String> dbUrls = mSSqlDatabase.setUp(sqlInputs);
        assertEquals("jdbc:sqlserver://dbServer:1433;DatabaseName=dbName;instance=instance;encrypt=true;trustServerCertificate=true", dbUrls.get(0));
        assertEquals(1, dbUrls.size());
    }

    @Test
    public void testSetUpAllIPV6LIteral() throws ClassNotFoundException, SQLException {
        MSSqlDatabase mSSqlDatabase = new MSSqlDatabase();

        final SQLInputs sqlInputs = getSqlInputsForMSSql(DB_NAME, DB_SERVER_IPV6_LITERAL, AUTH_SQL, INSTANCE);

        final List<String> dbUrls = mSSqlDatabase.setUp(sqlInputs);
        assertEquals("jdbc:sqlserver://2001-0db8-85a3-0042-1000-8a2e-0370-7334.ipv6-literal.net:1433;DatabaseName=dbName;instance=instance;encrypt=true;trustServerCertificate=true", dbUrls.get(0));
        assertEquals(1, dbUrls.size());
    }

    @Test
    public void testSetUpAllAuthWindowsAndHostWithInstance() throws ClassNotFoundException, SQLException {
        MSSqlDatabase mSSqlDatabase = new MSSqlDatabase();

        final SQLInputs sqlInputs = getSqlInputsForMSSql(DB_NAME, DB_SERVER_WITH_INSTANCE, AUTH_WINDOWS, INSTANCE);

        final List<String> dbUrls = mSSqlDatabase.setUp(sqlInputs);
        assertEquals("jdbc:sqlserver://dbServer:1433;DatabaseName=dbName;instance=instance;integratedSecurity=true;encrypt=true;trustServerCertificate=true", dbUrls.get(0));
        assertEquals(1, dbUrls.size());
    }

    @Test
    public void testSetUpAllAuthWindowsAndHostWithNoInstance() throws ClassNotFoundException, SQLException {
        MSSqlDatabase mSSqlDatabase = new MSSqlDatabase();

        final SQLInputs sqlInputs = getSqlInputsForMSSql(DB_NAME, DB_SERVER, AUTH_WINDOWS, null);

        final List<String> dbUrls = mSSqlDatabase.setUp(sqlInputs);
        assertEquals("jdbc:sqlserver://dbServer:1433;DatabaseName=dbName;integratedSecurity=true;encrypt=true;trustServerCertificate=true", dbUrls.get(0));
        assertEquals(1, dbUrls.size());
    }

    @NotNull
    public SQLInputs getSqlInputsForMSSql(String dbName, String dbServer, String authWindows, String instance) {
        final SQLInputs sqlInputs = new SQLInputs();
        sqlInputs.setDbName(dbName);
        sqlInputs.setDbServer(dbServer);
        sqlInputs.setDbPort(DB_PORT);
//        sqlInputs.setDbUrls(new ArrayList<String>());
        sqlInputs.setAuthenticationType(authWindows);
        sqlInputs.setInstance(instance);
        sqlInputs.setDbClass(SQLSERVER_JDBC_DRIVER);
        sqlInputs.setTrustAllRoots(true);
        sqlInputs.setTrustStore(EMPTY);
        sqlInputs.setTrustStorePassword(EMPTY);
        return sqlInputs;
    }

}
