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

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudslang.content.utils.NumberUtilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static io.cloudslang.content.hashicorp.terraform.utils.Constants.Common.*;
import static io.cloudslang.content.hashicorp.terraform.utils.Inputs.CommonInputs.ORGANIZATION_NAME;
import static io.cloudslang.content.hashicorp.terraform.utils.Inputs.CreateVariableInputs.*;
import static io.cloudslang.content.hashicorp.terraform.utils.Outputs.CreateWorkspaceOutputs.WORKSPACE_ID;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.*;
import static io.cloudslang.content.utils.BooleanUtilities.isValid;
import static io.cloudslang.content.utils.OtherUtilities.isValidIpPort;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public final class InputsValidation {

    @NotNull
    public static List<String> verifyCommonInputs(@Nullable final String proxyPort,
                                                  @Nullable final String trust_all_roots,
                                                  @Nullable final String connectTimeout,
                                                  @Nullable final String socketTimeout,
                                                  @Nullable final String keepAlive,
                                                  @Nullable final String connectionsMaxPerRoute,
                                                  @Nullable final String connectionsMaxTotal) {

        final List<String> exceptionMessages = new ArrayList<>();
        addVerifyProxy(exceptionMessages, proxyPort, PROXY_PORT);
        addVerifyBoolean(exceptionMessages, trust_all_roots, TRUST_ALL_ROOTS);
        addVerifyNumber(exceptionMessages, connectTimeout, CONNECT_TIMEOUT);
        addVerifyNumber(exceptionMessages, socketTimeout, SOCKET_TIMEOUT);
        addVerifyBoolean(exceptionMessages, keepAlive, KEEP_ALIVE);
        addVerifyNumber(exceptionMessages, connectionsMaxPerRoute, CONNECTIONS_MAX_PER_ROUTE);
        addVerifyNumber(exceptionMessages, connectionsMaxTotal, CONNECTIONS_MAX_TOTAL);

        return exceptionMessages;
    }

    @NotNull
    public static List<String> verifyCreateVariableInputs(@Nullable final String workspaceId, @Nullable final String variableName, @Nullable final String variableValue, @Nullable final String variableCategory,
                                                          @Nullable final String requestBody) {

        final List<String> exceptionMessages = new ArrayList<>();
        if (requestBody.isEmpty()) {
            addVerifyString(exceptionMessages, workspaceId, WORKSPACE_ID);
            addVerifyString(exceptionMessages, variableName, VARIABLE_NAME);
            addVerifyString(exceptionMessages, variableValue, VARIABLE_VALUE);
            addVerifyString(exceptionMessages, variableCategory, VARIABLE_CATEGORY);
        } else {
            addVerifyRequestBody(exceptionMessages, requestBody);
        }
       return exceptionMessages;
    }

    @NotNull
    public static List<String> verifyCreateRunInputs(@Nullable final String workspaceId,
                                                      @Nullable final String requestBody) {

        final List<String> exceptionMessages = new ArrayList<>();
        if(requestBody.isEmpty()) {
            addVerifyString(exceptionMessages, workspaceId, WORKSPACE_ID);
        }else{
            addVerifyRequestBody(exceptionMessages,requestBody);
        }
        return exceptionMessages;
    }
  
     @NotNull
    public static List<String> verifyCreateWorkspaceInputs(@Nullable final String workspaceName, @Nullable final String vcsRepoId, @Nullable final String oauthTokenId,
                                                           @Nullable final String requestBody) {
         final List<String> exceptionMessages = new ArrayList<>();
         if (requestBody.isEmpty()) {
             addVerifyString(exceptionMessages, workspaceName, WORKSPACE_ID);
             addVerifyString(exceptionMessages, vcsRepoId, VARIABLE_NAME);
             addVerifyString(exceptionMessages, oauthTokenId, VARIABLE_VALUE);
         } else {
             addVerifyRequestBody(exceptionMessages, requestBody);
         }
         return exceptionMessages;
     }

         @NotNull
         public static List<String> verifyApplyRunRequestBody(@Nullable final String requestBody) {

             final List<String> exceptionMessages = new ArrayList<>();
             if (!requestBody.isEmpty()) {
                 addVerifyRequestBody(exceptionMessages, requestBody);
             }

             return exceptionMessages;
         }

         @NotNull
         private static List<String> addVerifyUserInputs(@NotNull List<String> exceptions, @Nullable final String organizationName) {
             if (isEmpty(organizationName)) {
                 exceptions.add(String.format(ORGANIZATION_NAME));
             }
             return exceptions;
         }


         @NotNull
         private static List<String> addVerifyNotNullOrEmpty(@NotNull List<String> exceptions, @Nullable final String input, @NotNull final String inputName) {
             if (isEmpty(input)) {
                 exceptions.add(String.format(EXCEPTION_NULL_EMPTY, inputName));
             }
             return exceptions;
         }

         @NotNull
         private static List<String> addVerifyProxy(@NotNull List<String> exceptions, @Nullable final String input, @NotNull final String inputName) {
             if (isEmpty(input)) {
                 exceptions.add(String.format(EXCEPTION_NULL_EMPTY, inputName));
             } else if (!isValidIpPort(input)) {
                 exceptions.add(String.format(EXCEPTION_INVALID_PROXY, inputName));
             }
             return exceptions;
         }

         @NotNull
         private static List<String> addVerifyString(@NotNull List<String> exceptions, @Nullable final String input, @NotNull final String inputName) {
             if (isEmpty(input)) {
                 exceptions.add(String.format(EXCEPTION_NULL_EMPTY, inputName));
             }
             return exceptions;
         }

         @NotNull
         private static List<String> addVerifyBoolean(@NotNull List<String> exceptions, @Nullable final String input, @NotNull final String inputName) {
             if (isEmpty(input)) {
                 exceptions.add(String.format(EXCEPTION_NULL_EMPTY, inputName));
             } else if (!isValid(input)) {
                 exceptions.add(String.format(EXCEPTION_INVALID_BOOLEAN, input, inputName));
             }
             return exceptions;
         }

         @NotNull
         private static List<String> addVerifyRequestBody(@NotNull List<String> exceptions, @Nullable final String input) {
             try {
                 ObjectMapper mapper = new ObjectMapper();
                 mapper.readTree(input);
             } catch (Exception exception) {

                 exceptions.add(exception.getMessage());
             }
             return exceptions;
         }


         @NotNull
         private static List<String> addVerifyNumber(@NotNull List<String> exceptions, @Nullable final String input, @NotNull final String inputName) {
             if (isEmpty(input)) {
                 exceptions.add(String.format(EXCEPTION_NULL_EMPTY, inputName));
             } else if (!NumberUtilities.isValidInt(input)) {
                 exceptions.add(String.format(EXCEPTION_INVALID_NUMBER, input, inputName));
             }
             return exceptions;
         }
}

