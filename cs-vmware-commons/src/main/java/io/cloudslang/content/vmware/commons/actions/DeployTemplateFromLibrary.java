/*
 * (c) Copyright 2024 Open Text
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
/*
 * Copyright 2024 Open Text
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
package io.cloudslang.content.vmware.commons.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.utils.StringUtilities;
import io.cloudslang.content.vmware.commons.entities.DeployTemplateFromLibraryInputs;
import io.cloudslang.content.vmware.commons.services.DeployTemplateFromLibraryService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.vmware.commons.constants.Inputs.*;
import static io.cloudslang.content.vmware.commons.utils.Constants.DEFAULT_VM_IDENTIFIER_TYPE;
import static io.cloudslang.content.vmware.commons.utils.Constants.NEW_LINE;
import static io.cloudslang.content.vmware.commons.utils.Descriptions.*;
import static io.cloudslang.content.vmware.commons.utils.InputsValidation.verifyDeployTemplateFromLibraryInputs;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class DeployTemplateFromLibrary {
    @Action(name = "Deploy Template from Library",
            outputs = {
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC),
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, description = FAILURE_DESC)
            })
    public Map<String, String> execute(@Param(value = HOST, description = HOST_DESC, required = true) String host,
                                       @Param(value = USERNAME, description = USERNAME_DESC, required = true) String username,
                                       @Param(value = PASSWORD, description = PASSWORD_DESC, encrypted = true) String password,
                                       @Param(value = VM_IDENTIFIER_TYPE, description = VM_IDENTIFIER_TYPE_DESC) String vmIdentifierType,
                                       @Param(value = VM_NAME, description = VM_NAME_DESC, required = true) String vmName,
                                       @Param(value = VM_SOURCE, description = VM_SOURCE_DESC, required = true) String vmSource,
                                       @Param(value = VM_FOLDER, description = VM_FOLDER_DESC, required = true) String vmFolder,
                                       @Param(value = DATASTORE, description = DATASTORE_DESC, required = true) String datastore,
                                       @Param(value = VM_RESOURCE_POOL, description = VM_RESOURCE_POOL_DESC) String vmResourcePool,
                                       @Param(value = CLUSTER, description = CLUSTER_DESC) String cluster,
                                       @Param(value = HOST_SYSTEM, description = HOST_SYSTEM_DESC) String hostSystem,
                                       @Param(value = DESCRIPTION, description = DESCRIPTION_DESC) String description,
                                       @Param(value = TIMEOUT, description = TIMEOUT_DESC) String timeout) {

        vmIdentifierType = defaultIfEmpty(vmIdentifierType, DEFAULT_VM_IDENTIFIER_TYPE);
        timeout = defaultIfEmpty(timeout,DEFAULT_VM_IDENTIFIER_TYPE);

        final List<String> exceptionMessages =verifyDeployTemplateFromLibraryInputs(timeout,vmIdentifierType);
        if (!exceptionMessages.isEmpty()) {
            return getFailureResultsMap(StringUtilities.join(exceptionMessages, NEW_LINE));
        }

        DeployTemplateFromLibraryInputs deployTemplateFromLibraryInputs = new DeployTemplateFromLibraryInputs.CloneVmInputsBuilder()
                .host(host)
                .username(username)
                .password(password)
                .vmIdentifierType(vmIdentifierType)
                .vmName(vmName)
                .vmFolder(vmFolder)
                .vmResourcePool(vmResourcePool)
                .datastore(datastore)
                .vmSource(vmSource)
                .description(description)
                .cluster(cluster)
                .hostSystem(hostSystem)
                .timeout(timeout)
                .build();
        try {
            return DeployTemplateFromLibraryService.execute(deployTemplateFromLibraryInputs);
        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }
    }
}
