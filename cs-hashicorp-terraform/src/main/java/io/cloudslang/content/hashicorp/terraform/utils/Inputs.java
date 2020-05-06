/*
 * (c) Copyright 2020 Micro Focus, L.P.
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

package io.cloudslang.content.hashicorp.terraform.utils;

import io.cloudslang.content.constants.InputNames;

public class Inputs extends InputNames {

    public static class CommonInputs {
        public static final String AUTH_TOKEN = "authToken";
        public static final String ORGANIZATION_NAME = "organizationName";
        public static final String PROXY_HOST = "proxyHost";
        public static final String PROXY_PORT = "proxyPort";
        public static final String PROXY_USERNAME = "proxyUsername";
        public static final String PROXY_PASSWORD = "proxyPassword";
        public static final String REQUEST_BODY = "requestBody";
        public static final String TERRAFORM_VERSION = "terraformVersion";
        public static final String PAGE_NUMBER = "pageNumber";
        public static final String PAGE_SIZE = "pageSize";
    }

    public static class ApplyRunInputs {
        public static final String RUN_ID = "runId";
        public static final String RUN_COMMENT = "runComment";
    }

    public static class ApplyDetailsInputs {
        public static final String APPLY_ID = "applyId";
    }

    public static class CreateWorkspaceInputs {
        public static final String WORKSPACE_NAME = "workspaceName";
        public static final String WORKSPACE_DESCRIPTION = "workspaceDescription";
        public static final String AUTO_APPLY = "autoApply";
        public static final String FILE_TRIGGERS_ENABLED = "fileTriggersEnabled";
        public static final String WORKING_DIRECTORY = "workingDirectory";
        public static final String TRIGGER_PREFIXES = "triggerPrefixes";
        public static final String QUEUE_ALL_RUNS = "queueAllRuns";
        public static final String SPECULATIVE_ENABLED = "speculativeEnabled";
        public static final String INGRESS_SUBMODULES = "ingressSubmodules";
        public static final String VCS_REPO_ID = "vcsRepoId";
        public static final String VCS_BRANCH_NAME = "vcsBranchName";
    }

    public static class CreateVariableInputs {
        public static final String VARIABLE_NAME = "variableName";
        public static final String VARIABLE_VALUE = "variableValue";
        public static final String SENSITIVE_VARIABLE_NAME = "sensitiveVariableName";
        public static final String SENSITIVE_VARIABLE_VALUE = "sensitiveVariableValue";
        public static final String SENSITIVE_REQUEST_BODY = "sensitiveRequestBody";
        public static final String VARIABLE_CATEGORY = "variableCategory";
        public static final String SENSITIVE = "sensitive";
        public static final String HCL = "hcl";
        public static final String VARIABLES_JSON = "variablesJson";
        public static final String SENSITIVE_VARIABLES_JSON = "sensitiveVariablesJson";
    }

    public static class CreateWorkspaceVariableInputs {
        public static final String WORKSPACE_VARIABLE_NAME = "workspaceVariableName";
        public static final String WORKSPACE_VARIABLE_VALUE = "workspaceVariableValue";
        public static final String SENSITIVE_WORKSPACE_VARIABLE_NAME = "sensitiveWorkspaceVariableName";
        public static final String SENSITIVE_WORKSPACE_VARIABLE_VALUE = "sensitiveWorkspaceVariableValue";
        public static final String SENSITIVE_REQUEST_BODY = "sensitiveRequestBody";
        public static final String WORKSPACE_VARIABLE_CATEGORY = "workspaceVariableCategory";
        public static final String SENSITIVE = "sensitive";
        public static final String HCL = "hcl";
        public static final String WORKSPACE_VARIABLES_JSON = "workspaceVariablesJson";
        public static final String SENSITIVE_WORKSPACE_VARIABLES_JSON = "sensitiveWorkspaceVariablesJson";
    }

    public static class CreateRunInputs {
        public static final String RUN_MESSAGE = "runMessage";
        public static final String IS_DESTROY = "isDestroy";
    }

    public static class GetRunDetailInputs {
        public static final String RUN_ID = "runId";
        public static final String RUN_ID_DESC = "runIdDescription";
    }

    public static class PlanDetailInputs {
        public static final String PLAN_ID = "planId";
        public static final String PLAN_ID_DESC = "planIdDescription";
    }

}
