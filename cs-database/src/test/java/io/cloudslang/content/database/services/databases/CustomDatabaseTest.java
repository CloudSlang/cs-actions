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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Created by vranau on 12/10/2014.
 */
public class CustomDatabaseTest {
    public static final String CUSTOM_CLASS_DRIVER = "org.h2.Driver";

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testSetUp() throws ClassNotFoundException {
        CustomDatabase customDatabase = new CustomDatabase();
        customDatabase.setUp(CUSTOM_CLASS_DRIVER);
    }

    @Test
    public void testSetUpNoClassName() throws ClassNotFoundException {

        expectedEx.expect(ClassNotFoundException.class);
        expectedEx.expectMessage("No db class name provided");
        CustomDatabase customDatabase = new CustomDatabase();
        customDatabase.setUp(null);
    }
}
