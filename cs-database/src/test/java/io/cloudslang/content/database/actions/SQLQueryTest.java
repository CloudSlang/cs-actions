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

import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import io.cloudslang.content.database.constants.DBResponseNames;
import io.cloudslang.content.database.utils.SQLInputs;
import io.cloudslang.content.database.utils.SQLInputsUtils;
import io.cloudslang.content.database.utils.SQLSessionResource;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import org.mockito.Spy;



import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.constants.BooleanValues.FALSE;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.database.constants.DBDefaultValues.AUTH_SQL;
import static io.cloudslang.content.database.constants.DBOtherValues.*;
import static io.cloudslang.content.database.constants.DBReturnCodes.NO_MORE;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by victor on 13.02.2017.
 */
@ExtendWith(MockitoExtension.class)
public class SQLQueryTest {

    @Spy
    private final SQLQuery sqlQuery = new SQLQuery();

    @Test
    public void executeFailValidation() throws Exception {
        final Map<String, String> resultMap = new SQLQuery().execute(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, null);
        assertThat(resultMap.get(RETURN_CODE), is(FAILURE));
        assertThat(resultMap.get(RETURN_RESULT), is("dbServerName can't be empty\nusername input is empty.\npassword input is empty.\ndatabase input is empty.\ntrustStore or trustStorePassword is mandatory if trustAllRoots is false\ncommand input is empty."));
    }

    @Test
    public void executeSuccess() throws Exception {
        final String aKey = "akey";
        final GlobalSessionObject<Map<String, Object>> globalSessionObject = new GlobalSessionObject<>();
        final Map<String, Object> stringMap = new HashMap<>();
        stringMap.put(aKey, aKey);
        globalSessionObject.setResource(new SQLSessionResource(stringMap));

        try (MockedStatic<SQLInputsUtils> utilities = mockStatic(SQLInputsUtils.class)) {
            utilities.when(() -> SQLInputsUtils.getSqlKey(any(SQLInputs.class))).thenReturn(aKey);
            utilities.when(() -> SQLInputsUtils.getOrDefaultGlobalSessionObj(any(GlobalSessionObject.class)))
                    .thenReturn(globalSessionObject);
            final Map<String, String> resultMap = sqlQuery.execute("1", MSSQL_DB_TYPE, "username", "Password", "someInstance", "123", "db",
                    AUTH_SQL, EMPTY, EMPTY, "something", "true", EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, TYPE_FORWARD_ONLY,
                    CONCUR_READ_ONLY, FALSE, globalSessionObject);

            assertThat(resultMap.get(RETURN_CODE), is(NO_MORE));
            assertThat(resultMap.get(RETURN_RESULT), is(DBResponseNames.NO_MORE));
        }
    }

}