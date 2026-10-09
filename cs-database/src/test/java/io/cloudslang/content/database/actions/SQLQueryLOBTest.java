/*
 * Copyright 2019-2024 Open Text
 * This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */




package io.cloudslang.content.database.actions;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import io.cloudslang.content.database.constants.DBResponseNames;
import io.cloudslang.content.database.constants.DBReturnCodes;
import io.cloudslang.content.database.services.SQLQueryLobService;
import io.cloudslang.content.database.utils.SQLInputs;
import io.cloudslang.content.database.utils.SQLInputsUtils;
import io.cloudslang.content.database.utils.SQLSessionResource;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.database.constants.DBDefaultValues.AUTH_SQL;
import static io.cloudslang.content.database.constants.DBOtherValues.*;
import static io.cloudslang.content.database.constants.DBReturnCodes.NO_MORE;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
/**
 * Created by victor on 13.02.2017.
 */
@org.junit.jupiter.api.extension.ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
@org.mockito.junit.jupiter.MockitoSettings(strictness = org.mockito.quality.Strictness.LENIENT)
public class SQLQueryLOBTest {

    @Spy
    private final SQLQueryLOB sqlQueryLOB = new SQLQueryLOB();

    @Test
    public void execute() throws Exception {
        final Map<String, String> resultMap = new SQLQueryLOB().execute(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, null);
        assertThat(resultMap.get(RETURN_CODE), is(FAILURE));
        assertThat(resultMap.get(RETURN_RESULT), is("dbServerName can't be empty\nusername input is empty.\npassword input is empty.\ndatabase input is empty.\ntrustStore or trustStorePassword is mandatory if trustAllRoots is false\ncommand input is empty."));
    }

    @Test
    public void executeSuccessNoMore() throws Exception {
        final String aKey = "akey";
        final GlobalSessionObject<Map<String, Object>> globalSessionObject = new GlobalSessionObject<>();
        final Map<String, Object> stringMap = new HashMap<>();
        stringMap.put(aKey, aKey);
        globalSessionObject.setResource(new SQLSessionResource(stringMap));

        try (org.mockito.MockedStatic<SQLInputsUtils> inputsUtils = org.mockito.Mockito.mockStatic(SQLInputsUtils.class)) {
        inputsUtils.when(() -> SQLInputsUtils.getSqlKey(any(SQLInputs.class))).thenReturn(aKey);
        inputsUtils.when(() -> SQLInputsUtils.getOrDefaultGlobalSessionObj(any(GlobalSessionObject.class))).thenReturn(globalSessionObject);
        final Map<String, String> resultMap = sqlQueryLOB.execute("1", MSSQL_DB_TYPE, "username", "Password", "someInstance", "123", "db",
                AUTH_SQL, EMPTY, EMPTY, "something", "true", EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, TYPE_FORWARD_ONLY, CONCUR_READ_ONLY, globalSessionObject);

        inputsUtils.verify(() -> SQLInputsUtils.getSqlKey(any(SQLInputs.class)));
        inputsUtils.verify(() -> SQLInputsUtils.getOrDefaultGlobalSessionObj(any(GlobalSessionObject.class)));
        assertThat(resultMap.get(RETURN_CODE), is(NO_MORE));
        assertThat(resultMap.get(RETURN_RESULT), is(DBResponseNames.NO_MORE));
        }
    }

    @Test
    public void executeSuccessHasMore() throws Exception {
        final String aKey = "akey";
        final GlobalSessionObject<Map<String, Object>> globalSessionObject = new GlobalSessionObject<>();
        final Map<String, Object> stringMap = new HashMap<>();
        final List<String> aList = new ArrayList<>();
        aList.add("a");
        stringMap.put(aKey, aList);
        globalSessionObject.setResource(new SQLSessionResource(stringMap));

        try (org.mockito.MockedStatic<SQLInputsUtils> inputsUtils = org.mockito.Mockito.mockStatic(SQLInputsUtils.class)) {
        inputsUtils.when(() -> SQLInputsUtils.getSqlKey(any(SQLInputs.class))).thenReturn(aKey);
        inputsUtils.when(() -> SQLInputsUtils.getOrDefaultGlobalSessionObj(any(GlobalSessionObject.class))).thenReturn(globalSessionObject);
        final Map<String, String> resultMap = sqlQueryLOB.execute("1", MSSQL_DB_TYPE, "username", "Password", "someInstance", "123", "db",
                AUTH_SQL, EMPTY, EMPTY, "something", "true", EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, TYPE_FORWARD_ONLY, CONCUR_READ_ONLY, globalSessionObject);

        inputsUtils.verify(() -> SQLInputsUtils.getSqlKey(any(SQLInputs.class)));
        inputsUtils.verify(() -> SQLInputsUtils.getOrDefaultGlobalSessionObj(any(GlobalSessionObject.class)));
        assertThat(resultMap.get(RETURN_CODE), is(SUCCESS));
        assertThat(resultMap.get(RETURN_RESULT), is("a"));
        }
    }

    //GlobalSessionObject was implemented
    //In CloudSlang the object is instantiated by default and cannot be null
    @Test
    @Disabled
    public void executeSuccessNoGlobalSessionFailure() throws Exception {
        try (org.mockito.MockedStatic<SQLQueryLobService> service = org.mockito.Mockito.mockStatic(SQLQueryLobService.class)) {
        service.when(() -> SQLQueryLobService.executeSqlQueryLob(any(SQLInputs.class))).thenReturn(true);

        final Map<String, String> resultMap = sqlQueryLOB.execute("1", MSSQL_DB_TYPE, "username", "Password", "someInstance", "123", "db",
                AUTH_SQL, EMPTY, EMPTY, "something", "true", EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, TYPE_FORWARD_ONLY, CONCUR_READ_ONLY, null);

        service.verify(() -> SQLQueryLobService.executeSqlQueryLob(any(SQLInputs.class)));
        assertThat(resultMap.get(RETURN_CODE), is(DBReturnCodes.NO_MORE));
        }
    }

}