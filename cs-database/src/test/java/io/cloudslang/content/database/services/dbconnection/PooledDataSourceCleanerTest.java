/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
*/
package io.cloudslang.content.database.services.dbconnection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PooledDataSourceCleaner.class)
public class PooledDataSourceCleanerTest {

    private long interval = 60 * 60 * 12; //12 hours

    private DBConnectionManager aManager;
    private PooledDataSourceCleaner cleaner;

    /**
     * Will execute before each test.
     */
    @Before
    public void setUp() {
        aManager = PowerMockito.mock(DBConnectionManager.class);
        cleaner = new PooledDataSourceCleaner(aManager, interval);
    }

    /**
     * Will execute after each test.
     */
    @After
    public void tearDown() {
        aManager = null;
        cleaner = null;
    }

    /**
     * Test the void run() method with mocking of static void method.
     *
     * @throws Exception
     */
    @Test
    public void testRun() throws Exception {
        doNothing().when(aManager).cleanDataSources();
        doReturn(0).when(aManager).getDbmsPoolSize();
        PowerMockito.mockStatic(Thread.class);
        PowerMockito.doNothing().when(Thread.class, "sleep", any(Long.class));

        cleaner.run();
        verify(aManager, times(1)).cleanDataSources();
        verify(aManager, times(1)).getDbmsPoolSize();
    }
}