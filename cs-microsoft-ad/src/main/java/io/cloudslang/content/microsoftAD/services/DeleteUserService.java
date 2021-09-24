/*
 * (c) Copyright 2021 Micro Focus, L.P.
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
package io.cloudslang.content.microsoftAD.services;

import io.cloudslang.content.microsoftAD.entities.DeleteUserInputs;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static io.cloudslang.content.microsoftAD.services.HttpCommons.httpDelete;
import static io.cloudslang.content.microsoftAD.utils.Constants.DELETE_USER_REQUEST_URL;


public class DeleteUserService {

    public static Map<String, String> deleteUser(@NotNull final DeleteUserInputs deleteUserInputs) throws Exception {

        return httpDelete(deleteUserInputs.getCommonInputs(), getDeleteUserUrl(deleteUserInputs));

    }


    @NotNull
    public static String getDeleteUserUrl(@NotNull final DeleteUserInputs deleteUserInputs) {
        String finalUrl = DELETE_USER_REQUEST_URL;

        if (!deleteUserInputs.getCommonInputs().getUserPrincipalName().isEmpty())
            finalUrl = finalUrl + deleteUserInputs.getCommonInputs().getUserPrincipalName();
        else
            finalUrl = finalUrl + deleteUserInputs.getCommonInputs().getUserId();

        return finalUrl;
    }

}
