/*
 * (c) Copyright 2022 Micro Focus, L.P.
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
package io.cloudslang.content.amazon.actions.rds;

import com.amazonaws.services.rds.AmazonRDS;
import com.amazonaws.services.rds.model.DBInstance;
import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.amazon.entities.constants.Outputs;
import io.cloudslang.content.amazon.entities.validators.Validator;
import io.cloudslang.content.amazon.factory.RDSClientBuilder;
import io.cloudslang.content.amazon.services.AmazonRDSService;
import io.cloudslang.content.constants.DefaultValues;

import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Constants.AWSRDSActions.DELETE_AUTOMATED_BACKUPS_CONST;
import static io.cloudslang.content.amazon.entities.constants.Constants.AWSRDSActions.SKIP_FINAL_SNAPSHOT_CONST;
import static io.cloudslang.content.amazon.entities.constants.Descriptions.Common.*;
import static io.cloudslang.content.amazon.entities.constants.Descriptions.DBInstanceCommon.*;
import static io.cloudslang.content.amazon.entities.constants.Descriptions.DeleteDBInstanceAction.*;
import static io.cloudslang.content.amazon.entities.constants.Descriptions.ProvisionProductAction.*;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.*;
import static io.cloudslang.content.amazon.entities.constants.Inputs.DBInstanceCommonInputs.*;
import static io.cloudslang.content.amazon.entities.constants.Inputs.DeleteDBInstanceInputs.*;
import static io.cloudslang.content.amazon.utils.OutputsUtil.getSuccessResultMapDBInstance;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class DeleteDBInstance {
    @Action(name = DELETE_DB_INSTANCE, description = DELETE_DB_INSTANCE_DESC,
            outputs = {
                    @Output(value = Outputs.RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = Outputs.RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = Outputs.EXCEPTION, description = EXCEPTION_DESC)


            },
            responses = {
                    @Response(text = Outputs.SUCCESS, field = Outputs.RETURN_CODE, value = Outputs.SUCCESS_RETURN_CODE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED, description = SUCCESS_DESC),
                    @Response(text = Outputs.FAILURE, field = Outputs.RETURN_CODE, value = Outputs.FAILURE_RETURN_CODE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, description = FAILURE_DESC)
            }
    )
    public Map<String, String> execute(@Param(value = ACCESS_KEY_ID, required = true, description = ACCESS_KEY_ID_DESC) final String accessKeyID,
                                       @Param(value = ACCESS_KEY, required = true, encrypted = true, description = ACCESS_KEY_DESC) final String accessKey,
                                       @Param(value = REGION, required = true, description = REGION_DESC) String region,
                                       @Param(value = DB_INSTANCE_IDENTIFIER, required = true, description = DB_INSTANCE_IDENTIFIER_DESC) String dbInstanceIdentifier,
                                       @Param(value = SKIP_FINAL_SNAPSHOT, description = SKIP_FINAL_SNAPSHOT_DESC) String skipFinalSnapshot,
                                       @Param(value = FINAL_DB_SNAPSHOT_IDENTIFIER, description = FINAL_DB_SNAPSHOT_IDENTIFIER_DESC) String finalDBSnapshotIdentifier,
                                       @Param(value = DELETE_AUTOMATED_BACKUPS, description = DELETE_AUTOMATED_BACKUPS_DESC) String deleteAutomatedBackups,
                                       @Param(value = PROXY_HOST, description = PROXY_HOST_DESC) final String proxyHost,
                                       @Param(value = PROXY_PORT, description = PROXY_PORT_DESC) final String proxyPort,
                                       @Param(value = PROXY_USERNAME, description = PROXY_USERNAME_DESC) final String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, encrypted = true, description = PROXY_PASSWORD_DESC) final String proxyPassword,
                                       @Param(value = CONNECT_TIMEOUT, description = CONNECT_TIMEOUT_DESC) String connectTimeout,
                                       @Param(value = EXECUTION_TIMEOUT, description = EXECUTION_TIMEOUT_DESC) String execTimeout,
                                       @Param(value = ASYNC, description = ASYNC_DESC) String async
    ) {


        final String proxyPortVal = defaultIfEmpty(proxyPort, DefaultValues.PROXY_PORT);
        final String connectTimeoutVal = defaultIfEmpty(connectTimeout, DefaultValues.CONNECT_TIMEOUT);
        final String execTimeoutVal = defaultIfEmpty(execTimeout, DefaultValues.EXEC_TIMEOUT);
        final String asyncVal = defaultIfEmpty(async, DefaultValues.ASYNC);
        final String skipFinalSnapshotVal = defaultIfEmpty(skipFinalSnapshot, SKIP_FINAL_SNAPSHOT_CONST);
        final String deleteAutomatedBackupsVal = defaultIfEmpty(deleteAutomatedBackups, DELETE_AUTOMATED_BACKUPS_CONST);


        Validator validator = new Validator()
                .validatePort(proxyPortVal, PROXY_PORT)
                .validateInt(connectTimeoutVal, CONNECT_TIMEOUT)
                .validateInt(execTimeoutVal, EXECUTION_TIMEOUT)
                .validateBoolean(asyncVal, ASYNC)
                .validateBoolean(skipFinalSnapshotVal, SKIP_FINAL_SNAPSHOT)
                .validateBoolean(deleteAutomatedBackupsVal, DELETE_AUTOMATED_BACKUPS);

        if (validator.hasErrors()) {
            return getFailureResultsMap(validator.getErrors());
        }

        final Integer proxyPortImp = Integer.valueOf(proxyPortVal);
        final Integer connectTimeoutImp = Integer.valueOf(connectTimeoutVal);
        final Integer execTimeoutImp = Integer.valueOf(execTimeoutVal);
        final boolean asyncImp = Boolean.parseBoolean(asyncVal);
        final boolean skipFinalSnapshotImp = Boolean.parseBoolean(skipFinalSnapshotVal);
        final boolean deleteAutomatedBackupsImp = Boolean.parseBoolean(deleteAutomatedBackupsVal);

        try {

            final AmazonRDS amazonRDS = RDSClientBuilder.getRDSClientBuilder(accessKeyID, accessKey,
                    proxyHost, proxyPortImp, proxyUsername, proxyPassword, connectTimeoutImp, execTimeoutImp, region, asyncImp);

            final DBInstance result = AmazonRDSService.deleteRDSInstance(dbInstanceIdentifier, skipFinalSnapshotImp, finalDBSnapshotIdentifier
                    , deleteAutomatedBackupsImp, amazonRDS);

            return getSuccessResultMapDBInstance(result);


        } catch (Exception e) {
            return getFailureResultsMap(e);
        }
    }
}
