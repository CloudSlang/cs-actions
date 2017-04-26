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
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Created by vranau on 12/10/2014.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest(Class.class)
public class NetcoolDatabaseTest {

    @Spy
    private NetcoolDatabase netcoolDatabase = new NetcoolDatabase();

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testSetUpInvalid() throws Exception {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("Could not locate either jconn2.jar or jconn3.jar file in the classpath");
        final SQLInputs sqlInputs = SQLInputs.builder().build();
        sqlInputs.setDbName("dbName");
        sqlInputs.setDbServer(null);
        sqlInputs.setDbPort(30);
        netcoolDatabase.setUp(sqlInputs);
    }

    @Test
    public void testSetUpValid() throws Exception {

//todo
//        final SQLInputs sqlInputs = SQLInputs.builder().build();
//        sqlInputs.setDbName("dbName");
//        sqlInputs.setDbServer(null);
//        sqlInputs.setDbPort(30);
//
//        Class cls = any(Class.class);
//
//        mockStatic(Class.class);
//        given(Class.forName("com.sybase.jdbc3.jdbc.SybDriver")).willReturn(cls);
//
//        netcoolDatabase.setUp(sqlInputs);
//        verifyStatic();

    }
}
