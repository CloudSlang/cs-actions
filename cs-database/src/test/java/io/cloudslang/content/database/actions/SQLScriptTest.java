/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.database.actions;

import io.cloudslang.content.database.services.SQLScriptService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.database.constants.DBDefaultValues.AUTH_SQL;
import static io.cloudslang.content.database.constants.DBOtherValues.CONCUR_READ_ONLY;
import static io.cloudslang.content.database.constants.DBOtherValues.MSSQL_DB_TYPE;
import static io.cloudslang.content.database.constants.DBOtherValues.TYPE_FORWARD_ONLY;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by victor on 13.02.2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({SQLScript.class, SQLScriptService.class})
public class SQLScriptTest {

    @Mock
    private SQLScript sqlScript;

    @Test
    public void executeFailValidation() throws Exception {
        final Map<String, String> resultMap = new SQLScript().execute(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY);
        assertThat(resultMap.get(RETURN_CODE), is(FAILURE));
        assertThat(resultMap.get(RETURN_RESULT), is("dbServerName can't be empty\n" +
                "username input is empty.\n" +
                "password input is empty.\n" +
                "database input is empty.\n" +
                "trustStore or trustStorePassword is mandatory if trustAllRoots is false\n" +
                "Only one of the sqlCommands and scriptFileName can be specified"));
    }

    @Test
    public void executeSuccess() throws Exception {
//        final String res = "result";
//
//        PowerMockito.doReturn(res).when(sqlScript).
//        final Map<String, String> resultMap = sqlScript.execute("1", MSSQL_DB_TYPE, "username", "Password", "someInstance", "123", "db",
//                AUTH_SQL, EMPTY, EMPTY, EMPTY, "something", EMPTY, "true", EMPTY, EMPTY, EMPTY, TYPE_FORWARD_ONLY, CONCUR_READ_ONLY);
//        assertThat(resultMap.get(RETURN_CODE), is(FAILURE));
//        assertThat(resultMap.get(RETURN_RESULT), is("dbServerName can't be empty\n" +
//                "username input is empty.\n" +
//                "password input is empty.\n" +
//                "database input is empty.\n" +
//                "trustStore or trustStorePassword is mandatory if trustAllRoots is false\n" +
//                "Only one of the sqlCommands and scriptFileName can be specified"));
    }



}