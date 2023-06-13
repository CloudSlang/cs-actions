package io.cloudslang.content.vmware.commons.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.vmware.commons.entities.DeployTemplateFromLibraryInputs;
import io.cloudslang.content.vmware.commons.services.DeployTemplateFromLibraryService;

import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.vmware.commons.constants.Inputs.*;
import static io.cloudslang.content.vmware.commons.utils.Descriptions.*;

public class DeployTemplateFromLibrary {
    @Action(name = "Clone Virtual Machine from Content Library",
            outputs = {
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC),
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, description = FAILURE_DESC)
            })
    public Map<String, String> execute(@Param(value = HOST, description = HOST_DESC,required = true) String host,
                                       @Param(value = USERNAME,description = USERNAME_DESC, required = true) String username,
                                       @Param(value = PASSWORD,description=PASSWORD_DESC, encrypted = true) String password,
                                       @Param(value = VM_IDENTIFIER_TYPE ,description=VM_IDENTIFIER_TYPE_DESC) String vmIdentifierType,
                                       @Param(value = VM_NAME, required = true) String vmName,
                                       @Param(value = VM_SOURCE,description = VM_SOURCE_DESC, required = true) String vmSource,
                                       @Param(value = VM_FOLDER, description = VM_FOLDER_DESC) String vmFolder,
                                       @Param(value = VM_RESOURCE_POOL, description = VM_RESOURCE_POOL_DESC) String vmResourcePool,
                                       @Param(value = DATASTORE, description = DATASTORE_DESC) String datastore,
                                       @Param(value = CLUSTER ,description=CLUSTER_DESC) String cluster,
                                       @Param(value = DESCRIPTION, description = DESCRIPTION_DESC ) String description){


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
                .build();
        try {
            return DeployTemplateFromLibraryService.execute(deployTemplateFromLibraryInputs);
        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }
    }
}
