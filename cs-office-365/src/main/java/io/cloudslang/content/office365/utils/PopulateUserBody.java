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

package io.cloudslang.content.office365.utils;

import com.google.gson.Gson;
import io.cloudslang.content.office365.entities.CreateUserInputs;
import io.cloudslang.content.office365.entities.createUserModels.CreateUserBody;
import io.cloudslang.content.office365.entities.createUserModels.CreateUserBodyWithImmutableId;
import io.cloudslang.content.office365.entities.createUserModels.PasswordProfile;
import io.cloudslang.content.utils.StringUtilities;

public class PopulateUserBody {

    public static String populateUserBody(CreateUserInputs createUserInputs) {

        if (StringUtilities.isEmpty(createUserInputs.getOnPremisesImmutableId())) {

            CreateUserBody userBody = new CreateUserBody();
            processUserBody(userBody, createUserInputs);

            return new Gson().toJson(userBody);
        } else {
            CreateUserBodyWithImmutableId userBody = new CreateUserBodyWithImmutableId();
            userBody.setOnPremisesImmutableId(createUserInputs.getOnPremisesImmutableId());
            processUserBody(userBody, createUserInputs);

            return new Gson().toJson(userBody);
        }
    }

    private static void processUserBody(CreateUserBody userBody, CreateUserInputs createUserInputs) {

        userBody.setAccountEnabled(Boolean.parseBoolean(createUserInputs.getAccountEnabled()));
        userBody.setDisplayName(createUserInputs.getDisplayName());
        userBody.setMailNickname(createUserInputs.getMailNickname());
        userBody.setUserPrincipalName(createUserInputs.getUserPrincipalName());

        PasswordProfile passwordProfile = new PasswordProfile();
        passwordProfile.setForceChangePasswordNextSignIn(Boolean.parseBoolean(createUserInputs.getForceChangePassword()));
        passwordProfile.setPassword(createUserInputs.getAssignedPassword());

        userBody.setPasswordProfile(passwordProfile);
    }
}
