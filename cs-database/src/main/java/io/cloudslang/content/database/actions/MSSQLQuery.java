/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
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
import io.cloudslang.content.database.constants.DBReturnCodes;
import io.cloudslang.content.database.services.SQLQueryService;
import io.cloudslang.content.database.utils.SQLInputs;
import io.cloudslang.content.database.utils.SQLSessionResource;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.cloudslang.content.constants.BooleanValues.FALSE;
import static io.cloudslang.content.constants.BooleanValues.TRUE;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.database.constants.DBDefaultValues.*;
import static io.cloudslang.content.database.constants.DBInputNames.*;
import static io.cloudslang.content.database.constants.DBOtherValues.*;
import static io.cloudslang.content.database.constants.DBOutputNames.*;
import static io.cloudslang.content.database.constants.DBResponseNames.*;
import static io.cloudslang.content.database.services.databases.MSSqlDatabase.exportPathToAuthDll;
import static io.cloudslang.content.database.utils.Constants.AUTH_WINDOWS;
import static io.cloudslang.content.database.utils.Constants.JTDS_JDBC_DRIVER;
import static io.cloudslang.content.database.utils.SQLInputsUtils.*;
import static io.cloudslang.content.database.utils.SQLInputsValidator.*;
import static io.cloudslang.content.database.utils.SQLUtils.getRowsFromGlobalSessionMap;
import static io.cloudslang.content.database.utils.SQLUtils.getStrColumns;
import static io.cloudslang.content.utils.BooleanUtilities.toBoolean;
import static io.cloudslang.content.utils.NumberUtilities.toInteger;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class MSSQLQuery {

    /**
     * @param dbServerName              The hostname or ip address of the database server.
     * @param username                  The username to use when connecting to the server.
     * @param password                  The password to use when connecting to the server.
     * @param instance                  The name instance of MSSQL Server. Leave it blank for default instance.
     *                                  Example: MSSQLSERVER
     * @param dbPort                    The port to connect to.
     * @param databaseName              The name of the database to connect to.
     * @param authenticationType        The type of authentication used to access the database (applicable only to MSSQL type).
     *                                  Default: sql
     *                                  Values: sql, windows
     * @param dbClass                   The classname of the JDBC driver to use.
     * @param dbURL                     The url required to load up the driver and make your connection.
     * @param command                   The SQL query to execute.
     *                                  Example: "SELECT * FROM table"
     * @param trustAllRoots             Specifies whether to enable weak security over SSL/TSL. A certificate is trusted even if no trusted certification authority issued it.
     *                                  Default value: false
     *                                  Valid values: true, false
     *                                  Note: If trustAllRoots is set to 'false', a trustStore and a trustStorePassword must be provided.
     * @param trustStore                The pathname of the Java TrustStore file. This contains certificates from other parties that you expect to communicate with,
     *                                  or from Certificate Authorities that you trust to identify other parties.
     *                                  If the trustAllRoots input is set to 'true' this input is ignored.
     * @param trustStorePassword        The password associated with the trustStore file.
     * @param delimiter                 The delimiter to use between resulted values in "returnResult" and column names in "columnNames".
     * @param key                       The key to help keep multiple query results distinct.
     * @param timeout                   Seconds to wait before timing out the SQL command execution. When the default value is used, there
     *                                  is no limit on the amount of time allowed for a running command to complete.
     *                                  Default values: 0
     * @param databasePoolingProperties Properties for database pooling configuration. Pooling is disabled by default.
     *                                  Default: db.pooling.enable=false
     * @param resultSetType             the result set type. See JDBC folder description for more details.
     *                                  Valid values: TYPE_FORWARD_ONLY, TYPE_SCROLL_INSENSITIVE,TYPE_SCROLL_SENSITIVE.
     *                                  Default value: TYPE_SCROLL_INSENSITIVE except DB2 which is overridden to TYPE_FORWARD_ONLY
     * @param resultSetConcurrency      the result set concurrency. See JDBC folder description for more details.
     *                                  Valid values: CONCUR_READ_ONLY, CONCUR_UPDATABLE
     *                                  Default value: CONCUR_READ_ONLY
     * @param ignoreCase                If set to true the inputs' letters case will be ignored and converted to lowercase.
     *                                  Valid values: true, false
     *                                  Default value: true
     * @return It contains the data of one row, separated by the "delimiter".
     */
    @Action(name = "MSSQL Query",
            outputs = {
                    @Output(RETURN_CODE),
                    @Output(RETURN_RESULT),
                    @Output(EXCEPTION),
                    @Output(ROWS_LEFT),
                    @Output(COLUMN_NAMES),
                    @Output(SQL_QUERY)
            },
            responses = {
                    @Response(text = MORE_ITEMS, field = RETURN_CODE, value = SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.NO_ACTION_TAKEN),
                    @Response(text = NO_MORE_ITEMS, field = RETURN_CODE, value = DBReturnCodes.NO_MORE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.NO_ACTION_TAKEN),
                    @Response(text = ResponseNames.FAILURE, field = RETURN_CODE, value = FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
            })
    public Map<String, String> execute(@Param(value = MS_DB_SERVER_NAME, required = true) String dbServerName,
                                       @Param(value = MS_USERNAME) String username,
                                       @Param(value = MS_PASSWORD, encrypted = true) String password,
                                       @Param(value = INSTANCE) String instance,
                                       @Param(value = DB_PORT) String dbPort,
                                       @Param(value = MS_DATABASE_NAME, required = true) String databaseName,
                                       @Param(value = AUTHENTICATION_TYPE) String authenticationType,
                                       @Param(value = DB_CLASS) String dbClass,
                                       @Param(value = DB_URL) String dbURL,
                                       @Param(value = MS_COMMAND, required = true) String command,
                                       @Param(value = TRUST_ALL_ROOTS) String trustAllRoots,
                                       @Param(value = TRUST_STORE) String trustStore,
                                       @Param(value = TRUST_STORE_PASSWORD) String trustStorePassword,
                                       @Param(value = MS_DELIMITER, required = true) String delimiter,
                                       @Param(value = MS_KEY, required = true) String key,
                                       @Param(value = TIMEOUT) String timeout,
                                       @Param(value = DATABASE_POOLING_PROPERTIES) String databasePoolingProperties,
                                       @Param(value = RESULT_SET_TYPE) String resultSetType,
                                       @Param(value = RESULT_SET_CONCURRENCY) String resultSetConcurrency,
                                       @Param(value = IGNORE_CASE) String ignoreCase,
                                       @Param(value = GLOBAL_SESSION_OBJECT) GlobalSessionObject<Map<String, Object>> globalSessionObject) {
        String dbType =  MSSQL_DB_TYPE;
        String authLibraryPath = EMPTY;
        username = defaultIfEmpty(username, EMPTY);
        password = defaultIfEmpty(password, EMPTY);
        instance = defaultIfEmpty(instance, EMPTY);
        authenticationType = defaultIfEmpty(authenticationType, AUTH_SQL);
        trustAllRoots = defaultIfEmpty(trustAllRoots, FALSE);
        trustStore = defaultIfEmpty(trustStore, EMPTY);
        trustStorePassword = defaultIfEmpty(trustStorePassword, EMPTY);
        timeout = defaultIfEmpty(timeout, DEFAULT_TIMEOUT);
        resultSetType = defaultIfEmpty(resultSetType, TYPE_SCROLL_INSENSITIVE);
        resultSetConcurrency = defaultIfEmpty(resultSetConcurrency, CONCUR_READ_ONLY);
        ignoreCase = defaultIfEmpty(ignoreCase, TRUE);
        dbClass = getOrDefaultDBClassMSSQLQuery(dbClass, dbType, authenticationType);
        String windowsDomain = null;


        if (AUTH_WINDOWS.equalsIgnoreCase(authenticationType)&& username.contains("\\")){
             windowsDomain = username.substring(0, username.indexOf("\\"));
             username = username.substring(username.indexOf("\\") + 1);
        }

        final List<String> preInputsValidation = validateMSSqlQueryInputs(dbServerName, dbType, username, password, instance, dbPort,
                databaseName, authenticationType, command, trustAllRoots, trustStore, trustStorePassword,
                timeout, resultSetType, resultSetConcurrency, ignoreCase);

        if (!preInputsValidation.isEmpty()) {
            return getFailureResultsMap(StringUtils.join(preInputsValidation, NEW_LINE));
        }

        final boolean ignoreCaseBool = toBoolean(ignoreCase);
        dbType = getDbType(dbType);

        final SQLInputs sqlInputs = SQLInputs.builder()
                .dbServer(dbServerName)
                .dbType(dbType)
                .username(username)
                .password(password)
                .instance(getOrLower(instance, ignoreCaseBool))
                .dbPort(getOrDefaultDBPort(dbPort, dbType))
                .dbName(getOrLower(defaultIfEmpty(databaseName, EMPTY), ignoreCaseBool))
                .authenticationType(authenticationType)
                .dbClass(dbClass)
                .dbUrl(defaultIfEmpty(dbURL, EMPTY))
                .sqlCommand(command)
                .trustAllRoots(toBoolean(trustAllRoots))
                .trustStore(trustStore)
                .trustStorePassword(trustStorePassword)
                .authLibraryPath(authLibraryPath)
                .strDelim(delimiter)
                .key(key)
                .timeout(toInteger(timeout))
                .databasePoolingProperties(getOrDefaultDBPoolingProperties(databasePoolingProperties, EMPTY))
                .resultSetType(getResultSetTypeForDbType(resultSetType, dbType))
                .resultSetConcurrency(getResultSetConcurrency(resultSetConcurrency))
                .ignoreCase(ignoreCaseBool)
                .isNetcool(checkIsNetcool(dbType))
                .windowsDomain(windowsDomain)
                .build();


        try {

            final String aKey = getSqlKey(sqlInputs);
            final String strKeyCol = format(KEY_COLUMNS, aKey);

            globalSessionObject = getOrDefaultGlobalSessionObj(globalSessionObject);

            final Map<String, Object> globalMap = globalSessionObject.get();

            if (globalMap.containsKey(aKey)) {
                sqlInputs.setLRows(getRowsFromGlobalSessionMap(globalSessionObject, aKey));
                sqlInputs.setStrColumns(getStrColumns(globalSessionObject, strKeyCol));

            } else {
                if (AUTH_WINDOWS.equalsIgnoreCase(authenticationType)){
                    try {
                        sqlInputs.setAuthLibraryPath(exportPathToAuthDll());
                    } catch (Exception e) {
                        return getFailureResultsMap(e);
                    }
                }
                SQLQueryService.executeSqlQuery(sqlInputs);
            }

            Map<String, String> result = new HashMap<>();
            if (!sqlInputs.getLRows().isEmpty()) {
                final String getFirstRow = sqlInputs.getLRows().remove(0);

                result = getSuccessResultsMap(getFirstRow);
                result.put(COLUMN_NAMES, sqlInputs.getStrColumns());
                result.put(ROWS_LEFT, String.valueOf(sqlInputs.getLRows().size()));

                globalMap.put(aKey, sqlInputs.getLRows());
                globalMap.put(strKeyCol, sqlInputs.getStrColumns());

                globalSessionObject.setResource(new SQLSessionResource(globalMap));

            } else {
                result.put(SQL_QUERY, sqlInputs.getSqlCommand());
                result.put(RETURN_RESULT, NO_MORE);
                result.put(ROWS_LEFT, ZERO);
                result.put(RETURN_CODE, DBReturnCodes.NO_MORE);

                globalMap.put(aKey, null);
            }

            return result;
        } catch (Exception e) {
            final Map<String, String> failureMap = getFailureResultsMap(e);
            failureMap.put(ROWS_LEFT, ZERO);
            return failureMap;
        }
    }

}
