

package io.cloudslang.content.hashicorp.terraform.utils;

import io.cloudslang.content.constants.OutputNames;

public class Outputs extends OutputNames {
    public static class CommonOutputs {
        public static final String DOCUMENT = "document";

    }

    public static class ListOAuthClientOutputs {
        public static final String OAUTH_TOKEN_ID = "oauthTokenId";
    }

    public static class CreateWorkspaceOutputs {
        public static final String WORKSPACE_ID = "workspaceId";
    }

    public static class CreateOrganizationOutputs {
        public static final String ORGANIZATION_ID = "organizationId";
    }

    public static class CreateRunOutputs {
        public static final String RUN_ID = "runId";

    }

    public static class CreateVariableOutputs {
        public static final String VARIABLE_ID = "variableId";
    }

    public static class CreateWorkspaceVariableOutputs {
        public static final String WORKSPACE_VARIABLE_ID = "workspaceVariableId";
    }


    public static class ListWorkspacesOutputs {
        public static final String WORKSPACE_LIST = "workspaceList";
    }

    public static class ListOrganizationsOutputs {
        public static final String ORGANIZATION_NAME_LIST = "organizationNameList";
    }


    public static class GetCurrentStateVersionOutputs {
        public static final String STATE_VERSION_ID = "stateVersionId";
        public static final String HOSTED_STATE_DOWNLOAD_URL = "hostedStateDownloadUrl";
    }

}
