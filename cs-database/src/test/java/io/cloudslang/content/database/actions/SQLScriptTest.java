/***********************************************************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
***********************************************************************************************************************/
package io.cloudslang.content.database.actions;

import io.cloudslang.content.database.services.SQLScriptService;
import io.cloudslang.content.database.utils.SQLInputs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.database.constants.DBDefaultValues.AUTH_SQL;
import static io.cloudslang.content.database.constants.DBOtherValues.CONCUR_READ_ONLY;
import static io.cloudslang.content.database.constants.DBOtherValues.MSSQL_DB_TYPE;
import static io.cloudslang.content.database.constants.DBOtherValues.TYPE_FORWARD_ONLY;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by victor on 13.02.2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({SQLScript.class, SQLScriptService.class})
public class SQLScriptTest {

    @Spy
    private SQLScript sqlScript = new SQLScript();

    @Test
    public void executeFailValidation() throws Exception {
        final Map<String, String> resultMap = new SQLScript().execute(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY);
        assertThat(resultMap.get(RETURN_CODE), is(FAILURE));
        assertThat(resultMap.get(RETURN_RESULT), is("dbServerName can't be empty\nusername input is empty.\npassword input is empty.\ndatabase input is empty.\nOnly one of the sqlCommands and scriptFileName can be specified"));
    }

    @Test
    public void executeSuccess() throws Exception {
        final String res = "result";

        mockStatic(SQLScriptService.class);

        final List<String> anyList = any(List.class);

        when(SQLScriptService.executeSqlScript(anyList, any(SQLInputs.class))).thenReturn(res);

        final Map<String, String> resultMap = sqlScript.execute("1", MSSQL_DB_TYPE, "username", "Password", "someInstance", "123", "db",
                AUTH_SQL, EMPTY, EMPTY, EMPTY, "something", EMPTY, EMPTY, TYPE_FORWARD_ONLY, CONCUR_READ_ONLY);

        verifyStatic();
        assertThat(resultMap.get(RETURN_CODE), is(SUCCESS));
        assertThat(resultMap.get(RETURN_RESULT), is(res));
    }



}