/*
 * Copyright 2022-2023 Open Text
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

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.database.entities.OracleCloudInputs;
import io.cloudslang.content.database.service.OracleCloudScriptService;
import io.cloudslang.content.database.utils.InputsValidation;
import io.cloudslang.content.database.utils.Utils;
import io.cloudslang.content.utils.OutputUtilities;
import org.apache.commons.lang3.StringUtils;

import java.sql.DriverManager;
import java.util.List;
import java.util.Map;

import static io.cloudslang.content.constants.BooleanValues.FALSE;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.database.constants.DBExceptionValues.NO_SQL_SCRIPT;
import static io.cloudslang.content.database.entities.OracleCloudInputs.getSqlCommands;
import static io.cloudslang.content.database.utils.Constants.*;
import static io.cloudslang.content.database.utils.Inputs.*;
import static io.cloudslang.content.database.utils.Outputs.UPDATE_COUNT;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class SQLScript {

    @Action(name = "SQL Script",
            outputs = {
                    @Output(RETURN_CODE),
                    @Output(RETURN_RESULT),
                    @Output(EXCEPTION),
                    @Output(UPDATE_COUNT),
            },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = RETURN_CODE, value = SUCCESS,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = ResponseNames.FAILURE, field = RETURN_CODE, value = FAILURE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
            })
    public Map<String, String> execute(@Param(value = CONNECTION_STRING, required = true) String connectionString,
                                       @Param(value = WALLET_PATH) String walletPath,
                                       @Param(value = USERNAME, required = true) String username,
                                       @Param(value = PASSWORD, required = true, encrypted = true) String password,
                                       @Param(value = OVERWRITE_WALLET) String overwriteWallet,
                                       @Param(value = DELIMITER) String delimiter,
                                       @Param(value = SQL_COMMANDS) String sqlCommands,
                                       @Param(value = SCRIPT_FILE_NAME) String scriptFileName,
                                       @Param(value = TRUST_STORE) String trustStore,
                                       @Param(value = TRUST_STORE_PASSWORD, encrypted = true) String trustStorePassword,
                                       @Param(value = KEYSTORE) String keystore,
                                       @Param(value = KEYSTORE_PASSWORD, encrypted = true) String keystorePassword,
                                       @Param(value = EXECUTION_TIMEOUT) String executionTimeout,
                                       @Param(value = GLOBAL_SESSION_OBJECT) GlobalSessionObject<Map<String, Object>> globalSessionObject) {

        username = defaultIfEmpty(username, EMPTY);
        password = defaultIfEmpty(password, EMPTY);
        trustStore = defaultIfEmpty(trustStore, EMPTY);
        trustStorePassword = defaultIfEmpty(trustStorePassword, EMPTY);
        executionTimeout = defaultIfEmpty(executionTimeout, DEFAULT_TIMEOUT);
        overwriteWallet = defaultIfEmpty(overwriteWallet, FALSE);
        delimiter = defaultIfEmpty(delimiter, SEMICOLON);
        walletPath = defaultIfEmpty(Utils.unzip(walletPath, Boolean.parseBoolean(overwriteWallet)), EMPTY);

        final List<String> preInputsValidation = InputsValidation.verifySqlScript(walletPath, trustStore, keystore, overwriteWallet, executionTimeout);
        if (!preInputsValidation.isEmpty())
            return getFailureResultsMap(StringUtils.join(preInputsValidation, NEW_LINE));

        walletPath = defaultIfEmpty((!StringUtils.isEmpty(walletPath)) ? Utils.unzip(walletPath, Boolean.parseBoolean(overwriteWallet)) : EMPTY, EMPTY);

        OracleCloudInputs sqlInputs = new OracleCloudInputs.OracleCloudInputsBuilder()
                .connectionString(connectionString)
                .username(username)
                .password(password)
                .walletPath(walletPath)
                .sqlCommands(getSqlCommands(sqlCommands, scriptFileName, delimiter))
                .delimiter(delimiter)
                .trustStore(trustStore)
                .trustStorePassword(trustStorePassword)
                .keyStore(keystore)
                .keyStorePassword(keystorePassword)
                .executionTimeout(Integer.parseInt(executionTimeout))
                .build();

        try {
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            final List<String> commands = sqlInputs.getSqlCommands();
            if (!commands.isEmpty()) {
                final String res = OracleCloudScriptService.executeSqlScript(commands, sqlInputs);
                final Map<String, String> result = getSuccessResultsMap(res);
                result.put(UPDATE_COUNT, String.valueOf(sqlInputs.getIUpdateCount()));
                return result;
            }
            return getFailureResultsMap(NO_SQL_SCRIPT);
        } catch (Exception e) {
            return OutputUtilities.getFailureResultsMap(e);
        }
    }
}
