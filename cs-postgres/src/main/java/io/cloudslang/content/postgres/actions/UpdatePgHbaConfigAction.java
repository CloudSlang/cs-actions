/*
 * (c) Copyright 2019 Micro Focus, L.P.
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

package io.cloudslang.content.postgres.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.postgres.services.ConfigService;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.postgres.utils.Constants.*;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;


public class UpdatePgHbaConfigAction {

    /**
     * Updates the Postgres config pg_hba.config
     *
     * @param installationPath The full path to the PostgreSQL configuration file in the local machine to be updated.
     * @param allowedHosts     A wildcard or a comma-separated list of hostnames or IPs (IPv4 or IPv6).
     * @param allowedUsers     A comma-separated list of PostgreSQL users. If no value is specified for this input,
     *                         all users will have access to the server.
     * @return A map with strings as keys and strings as values that contains: outcome of the action (or failure message
     * and the exception if there is one), returnCode of the operation and the ID of the request
     */
    @Action(name = "Update pg_hba.config",
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
            @Param(value = ALLOWED_HOSTS) String allowedHosts,
            @Param(value = ALLOWED_USERS) String allowedUsers
    ) {

        try {
            if (allowedHosts == null && allowedUsers == null) {
                return getSuccessResultsMap("No changes in pg_hba.conf");
            }

            if (allowedHosts == null || allowedHosts.trim().length() == 0) {
                allowedHosts = "localhost";
            }
            allowedHosts = allowedHosts.replace("\'", "").trim();

            if (allowedUsers == null || allowedUsers.trim().length() == 0) {
                allowedUsers = "all";
            } else {
                allowedUsers = allowedUsers.replace("\'", "").trim();
            }

            ConfigService.changeProperty(installationPath, allowedHosts.split(";"), allowedUsers.split(";"));

            return getSuccessResultsMap("Updated pg_hba.conf successfully");
        } catch (Exception e) {
            return getFailureResultsMap("Failed to update pg_hba.conf", e);
        }
    }

}
