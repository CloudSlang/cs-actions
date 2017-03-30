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

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Created by victor on 15.02.2017.
 */
public class SQLInputsTest {

    @Test
    public void testSqlInputsEquals() throws Exception {
        final SQLInputs firstInput = SQLInputs.builder()
                .authenticationType(EMPTY)
                .colDelimiter(EMPTY)
                .databasePoolingProperties(null)
                .dbClass(EMPTY)
                .dbName(EMPTY)
                .dbPort(1)
                .dbServer(EMPTY)
                .dbType(EMPTY)
                .authenticationType(EMPTY)
                .colDelimiter(EMPTY)
                .dbUrl(EMPTY)
                .ignoreCase(true)
                .instance(EMPTY)
                .isNetcool(true)
                .username(EMPTY)
                .password(EMPTY)
                .key(EMPTY)
                .timeout(4)
                .strDelim(EMPTY)
                .sqlCommand(EMPTY)
                .trustStore(EMPTY)
                .trustAllRoots(true)
                .trustStorePassword(EMPTY)
                .rowDelimiter(EMPTY)
                .colDelimiter(EMPTY)
                .resultSetType(1)
                .resultSetConcurrency(2)
                .sqlCommands(null)
                .build();

        final SQLInputs secondInput = SQLInputs.builder()
                .authenticationType(EMPTY)
                .colDelimiter(EMPTY)
                .databasePoolingProperties(null)
                .dbClass(EMPTY)
                .dbName(EMPTY)
                .dbPort(1)
                .dbServer(EMPTY)
                .dbType(EMPTY)
                .authenticationType(EMPTY)
                .colDelimiter(EMPTY)
                .dbUrl(EMPTY)
                .ignoreCase(true)
                .instance(EMPTY)
                .isNetcool(true)
                .username(EMPTY)
                .password(EMPTY)
                .key(EMPTY)
                .timeout(4)
                .strDelim(EMPTY)
                .sqlCommand(EMPTY)
                .trustStore(EMPTY)
                .trustAllRoots(true)
                .trustStorePassword(EMPTY)
                .rowDelimiter(EMPTY)
                .colDelimiter(EMPTY)
                .resultSetType(1)
                .resultSetConcurrency(2)
                .sqlCommands(null)
                .build();

        firstInput.setDatabasePoolingProperties(null);

        final String firstStr = firstInput.toString();
        final String secodnStr = secondInput.toString();

        assertTrue(firstInput.hashCode() == secondInput.hashCode());
        assertThat(firstStr, is(secodnStr));
        assertTrue(firstInput.equals(secondInput));
    }

}