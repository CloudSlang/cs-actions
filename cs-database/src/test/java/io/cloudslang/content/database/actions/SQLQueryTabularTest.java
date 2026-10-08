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

import io.cloudslang.content.database.services.SQLQueryTabularService;
import io.cloudslang.content.database.utils.SQLInputs;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import org.mockito.Spy;



import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.database.constants.DBDefaultValues.AUTH_SQL;
import static io.cloudslang.content.database.constants.DBOtherValues.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by victor on 13.02.2017.
 */
@ExtendWith(MockitoExtension.class)
public class SQLQueryTabularTest {

    @Spy
    private final SQLQueryTabular sqlQueryTabular = new SQLQueryTabular();

    @Test
    public void executeFailValidation() throws Exception {
        final Map<String, String> resultMap = new SQLQueryTabular().execute(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY);
        assertThat(resultMap.get(RETURN_CODE), is(FAILURE));
        assertThat(resultMap.get(RETURN_RESULT), is("dbServerName can't be empty\nusername input is empty.\npassword input is empty.\ndatabase input is empty.\ntrustStore or trustStorePassword is mandatory if trustAllRoots is false\ncommand input is empty."));
    }

    @Test
    public void executeSuccess() throws Exception {
        final String res = "result";

        try (MockedStatic<SQLQueryTabularService> service = mockStatic(SQLQueryTabularService.class)) {
            service.when(() -> SQLQueryTabularService.execSqlQueryTabular(any(SQLInputs.class))).thenReturn(res);
            final Map<String, String> resultMap = sqlQueryTabular.execute("1", MSSQL_DB_TYPE, "username", "Password", "someInstance", "123", "db",
                    AUTH_SQL, EMPTY, EMPTY, "something", "true", EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, TYPE_FORWARD_ONLY, CONCUR_READ_ONLY);
            assertThat(resultMap.get(RETURN_CODE), is(SUCCESS));
            assertThat(resultMap.get(RETURN_RESULT), is(res));
        }
    }
}