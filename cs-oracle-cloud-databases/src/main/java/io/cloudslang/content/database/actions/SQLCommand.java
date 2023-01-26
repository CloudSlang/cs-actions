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
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.database.entities.OracleCloudInputs;
import io.cloudslang.content.database.service.OracleCloudQueryService;
import io.cloudslang.content.database.utils.InputsValidation;
import io.cloudslang.content.database.utils.Outputs;
import io.cloudslang.content.database.utils.Utils;
import io.cloudslang.content.utils.StringUtilities;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.sql.DriverManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.cloudslang.content.constants.BooleanValues.FALSE;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.database.utils.Constants.*;
import static io.cloudslang.content.database.utils.Inputs.*;
import static io.cloudslang.content.database.utils.Outputs.*;
import static io.cloudslang.content.database.utils.Outputs.DBMS_OUTPUT;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;


public class SQLCommand {
    @Action(name = "SQL Command",
            outputs = {
                    @Output(RETURN_CODE),
                    @Output(RETURN_RESULT),
                    @Output(UPDATE_COUNT),
                    @Output(OUTPUT_TEXT),
                    @Output(DBMS_OUTPUT),
                    @Output(EXCEPTION),
            },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = RETURN_CODE, value = SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = ResponseNames.FAILURE, field = RETURN_CODE, value = FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
            })
    public Map<String, String> execute(@Param(value = CONNECTION_STRING, required = true) String connectionString,
                                       @Param(value = USERNAME, required = true) String username,
                                       @Param(value = PASSWORD, encrypted = true, required = true) String password,
                                       @Param(value = WALLET_PATH) String walletPath,
                                       @Param(value = COMMAND, required = true) String command,
                                       @Param(value = OVERWRITE_WALLET) String overwriteWallet,
                                       @Param(value = TRUST_STORE) String trustStore,
                                       @Param(value = TRUST_STORE_PASSWORD) String trustStorePassword,
                                       @Param(value = KEYSTORE) String keystore,
                                       @Param(value = KEYSTORE_PASSWORD) String keystorePassword,
                                       @Param(value = EXECUTION_TIMEOUT) String executionTimeout) {


        overwriteWallet = defaultIfEmpty(overwriteWallet, FALSE);
        executionTimeout = defaultIfEmpty(executionTimeout, DEFAULT_TIMEOUT);
        trustStore = defaultIfEmpty(trustStore, EMPTY);
        keystore = defaultIfEmpty(keystore, EMPTY);

        final List<String> exceptionMessages = InputsValidation.verifySqlCommand(walletPath, trustStore, keystore, overwriteWallet, executionTimeout);
        if (!exceptionMessages.isEmpty())
            return getFailureResultsMap(StringUtilities.join(exceptionMessages, NEW_LINE));

        walletPath = defaultIfEmpty((!StringUtils.isEmpty(walletPath)) ? Utils.unzip(walletPath, Boolean.parseBoolean(overwriteWallet)) : EMPTY, EMPTY);

        OracleCloudInputs oracleCloudInputs = new OracleCloudInputs.OracleCloudInputsBuilder()
                .connectionString(connectionString)
                .username(username)
                .password(password)
                .walletPath(walletPath)
                .sqlCommand(command)
                .trustStore(trustStore)
                .trustStorePassword(trustStorePassword)
                .keyStore(keystore)
                .keyStorePassword(keystorePassword)
                .executionTimeout(Integer.parseInt(executionTimeout))
                .build();

        try {
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            return OracleCloudQueryService.executeSqlCommand(oracleCloudInputs);

        } catch (Exception exception) {
            Map<String, String> results = new HashMap();
            results.put(RETURN_CODE, MINUS_1);
            try {
                results.put(RETURN_RESULT, ExceptionUtils.getRootCause(exception).toString());
            } catch (NullPointerException e) {
                results.put(RETURN_RESULT, exception.getMessage());
            }
            results.put(EXCEPTION, ExceptionUtils.getStackTrace(exception));
            return results;
        }
    }
}
