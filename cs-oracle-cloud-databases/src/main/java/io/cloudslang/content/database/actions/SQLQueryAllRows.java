/*
 * (c) Copyright 2022 Micro Focus
 * All rights reserved. This program and the accompanying materials
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

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.database.entities.OracleCloudInputs;
import io.cloudslang.content.database.service.OracleCloudQueryAllRowsService;
import io.cloudslang.content.database.utils.InputsValidation;
import io.cloudslang.content.database.utils.Utils;
import io.cloudslang.content.utils.OutputUtilities;
import org.apache.commons.lang3.StringUtils;

import java.sql.DriverManager;
import java.util.List;
import java.util.Map;

import static io.cloudslang.content.constants.BooleanValues.FALSE;
import static io.cloudslang.content.constants.OtherValues.COMMA_DELIMITER;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.database.utils.Constants.*;
import static io.cloudslang.content.database.utils.Inputs.*;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class SQLQueryAllRows {

    @Action(name = "SQL Query All Rows",
            outputs = {
                    @Output(RETURN_CODE),
                    @Output(RETURN_RESULT),
                    @Output(EXCEPTION)
            },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = RETURN_CODE, value = SUCCESS,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = ResponseNames.FAILURE, field = RETURN_CODE, value = FAILURE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
            })
    public Map<String, String> execute(@Param(value = CONNECTION_STRING, required = true) String connectionString,
                                       @Param(value = USERNAME, required = true) String username,
                                       @Param(value = PASSWORD, required = true, encrypted = true) String password,
                                       @Param(value = COMMAND, required = true) String command,
                                       @Param(value = WALLET_PATH) String walletPath,
                                       @Param(value = OVERWRITE) String overwrite,
                                       @Param(value = COL_DELIMITER) String colDelimiter,
                                       @Param(value = ROW_DELIMITER) String rowDelimiter,
                                       @Param(value = SESSION_KEY) String sessionKey,
                                       @Param(value = TRUST_STORE) String trustStore,
                                       @Param(value = TRUST_STORE_PASSWORD, encrypted = true) String trustStorePassword,
                                       @Param(value = KEYSTORE) String keystore,
                                       @Param(value = KEYSTORE_PASSWORD, encrypted = true) String keystorePassword,
                                       @Param(value = EXECUTION_TIMEOUT) String executionTimeout,
                                       @Param(value = RESULT_SET_TYPE) String resultSetType,
                                       @Param(value = RESULT_SET_CONCURRENCY) String resultSetConcurrency,
                                       @Param(value = GLOBAL_SESSION_OBJECT) GlobalSessionObject<Map<String, Object>> globalSessionObject) {

        username = defaultIfEmpty(username, EMPTY);
        password = defaultIfEmpty(password, EMPTY);
        overwrite = defaultIfEmpty(overwrite, FALSE);
        colDelimiter = defaultIfEmpty(colDelimiter,COMMA_DELIMITER);
        rowDelimiter = defaultIfEmpty(rowDelimiter, NEW_LINE);
        sessionKey = defaultIfEmpty(sessionKey,EMPTY);
        trustStore = defaultIfEmpty(trustStore, EMPTY);
        trustStorePassword = defaultIfEmpty(trustStorePassword, EMPTY);
        executionTimeout = defaultIfEmpty(executionTimeout, DEFAULT_TIMEOUT);
        resultSetType = defaultIfEmpty(resultSetType, TYPE_FORWARD_ONLY);
        resultSetConcurrency = defaultIfEmpty(resultSetConcurrency, CONCUR_READ_ONLY);
        walletPath = defaultIfEmpty(Utils.unzip(walletPath, Boolean.parseBoolean(overwrite)), EMPTY);

        final List<String> preInputsValidation = InputsValidation.verifySqlQuery(walletPath, trustStore, keystore, overwrite, executionTimeout, resultSetConcurrency, resultSetType);


        if (!preInputsValidation.isEmpty()) {
            return getFailureResultsMap(StringUtils.join(preInputsValidation, NEW_LINE));
        }

        OracleCloudInputs sqlInputs = new OracleCloudInputs.OracleCloudInputsBuilder()
                .connectionString(connectionString)
                .username(username)
                .password(password)
                .walletPath(walletPath)
                .sqlCommand(command)
                .colDelimiter(colDelimiter)
                .rowDelimiter(rowDelimiter)
                .key(sessionKey)
                .trustStore(trustStore)
                .trustStorePassword(trustStorePassword)
                .keyStore(keystore)
                .keyStorePassword(keystorePassword)
                .executionTimeout(Integer.parseInt(executionTimeout))
                .resultSetConcurrency(InputsValidation.concurValues.get(resultSetConcurrency))
                .resultSetType(InputsValidation.typeValues.get(resultSetType))
                .build();

        try {
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            final String queryResult = OracleCloudQueryAllRowsService.execQueryAllRows(sqlInputs);
            return OutputUtilities.getSuccessResultsMap(queryResult);
        } catch (Exception e) {
            return OutputUtilities.getFailureResultsMap(e);
        }
    }
}
