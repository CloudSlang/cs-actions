package io.cloudslang.content.postgres.actions;

import java.util.Map;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.postgres.services.ConfigService;

import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.postgres.utils.Constants.*;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;


public class UpdatePostgresConfigAction {

    /**
     * Updates the Postgres config postgresql.conf
     *
     * @param installationPath     The full path to the PostgreSQL configuration file in the local machine to be updated
     * @param listenAddresses     The list of addresses where the PostgreSQL database listens
     * @param port                 The port the PostgreSQL database should listen.
     * @param ssl                  Enable SSL connections.
     * @param sslCaFile            Name of the file containing the SSL server certificate authority (CA).
     * @param sslCertFile          Name of the file containing the SSL server certificate.
     * @param sslKeyFile           Name of the file containing the SSL server private key.
     * @param maxConnections       The maximum number of client connections allowed.
     * @param sharedBuffers        Determines how much memory is dedicated to PostgreSQL to use for caching data.
     * @param effectiveCacheSize   Effective cache size.
     * @param autovacuum           Enable/disable autovacuum. The autovacuum process takes care of several maintenance
     *                             chores inside your database that you really need.
     * @param workMem              Memory used for sorting and queries.
     *
     * @return A map with strings as keys and strings as values that contains: outcome of the action (or failure message
     * and the exception if there is one), returnCode of the operation and the ID of the request
     */
    @Action(name = "Update Property Value",
            outputs = {
                    @Output(RETURN_CODE),
                    @Output(RETURN_RESULT),
                    @Output(EXCEPTION),
                    @Output(STDERR)
            },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = RETURN_CODE, value = SUCCESS,
                            matchType = MatchType.COMPARE_EQUAL,
                            responseType = ResponseType.RESOLVED),
                    @Response(text = ResponseNames.FAILURE, field = RETURN_CODE, value = FAILURE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
            })
    public Map<String, String> execute(
            @Param(value = FILE_PATH, required = true) String installationPath,
            @Param(value = LISTEN_ADDRESSES, required = true) String listenAddresses,
            @Param(value = PORT) String port,
            @Param(value = SSL) String ssl,
            @Param(value = SSL_CA_FILE) String sslCaFile,
            @Param(value = SSL_CERT_FILE) String sslCertFile,
            @Param(value = SSL_KEY_FILE) String sslKeyFile,
            @Param(value = MAX_CONNECTIONS) String maxConnections,
            @Param(value = SHARED_BUFFERS) String sharedBuffers,
            @Param(value = EFFECTIVE_CACHE_SIZE) String effectiveCacheSize,
            @Param(value = AUTOVACUUM) String autovacuum,
            @Param(value = WORK_MEM) String workMem
    ) {

        try {
            Map<String, Object> keyValues = ConfigService.validateAndBuildKeyValuesMap(
                    listenAddresses, port, ssl, sslCaFile, sslCertFile, sslKeyFile, maxConnections, sharedBuffers, effectiveCacheSize, autovacuum, workMem);

            ConfigService.changeProperty(installationPath, keyValues);

            return getSuccessResultsMap("Updated postgresql.conf successfully");
        } catch (Exception e) {
            return getFailureResultsMap("Failed to update postgresql.conf", e);
        }
    }

}
