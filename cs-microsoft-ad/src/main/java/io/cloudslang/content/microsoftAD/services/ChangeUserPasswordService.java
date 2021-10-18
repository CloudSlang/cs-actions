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

import com.google.gson.JsonObject;
import io.cloudslang.content.microsoftAD.entities.ChangeUserPasswordInputs;

import java.util.Map;

import static io.cloudslang.content.microsoftAD.services.HttpCommons.httpPost;
import static io.cloudslang.content.microsoftAD.utils.Constants.CHANGE_USER_PASSWORD_URL;
import static io.cloudslang.content.microsoftAD.utils.Inputs.ChangeUserPasswordInputs.CURRENT_PASSWORD;
import static io.cloudslang.content.microsoftAD.utils.Inputs.ChangeUserPasswordInputs.NEW_PASSWORD;

public class ChangeUserPasswordService {

    public static Map<String, String> changeUserPassword(ChangeUserPasswordInputs changeUserPasswordInputs) {
        JsonObject body = new JsonObject();
        body.addProperty(CURRENT_PASSWORD, changeUserPasswordInputs.getCurrentPassword());
        body.addProperty(NEW_PASSWORD, changeUserPasswordInputs.getNewPassword());

        return httpPost(changeUserPasswordInputs.getCommonInputs(), CHANGE_USER_PASSWORD_URL, body.toString());
    }
}
