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

package io.cloudslang.content.office365.entities;

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class CreateUserInputs {

    private final String accountEnabled;
    private final String displayName;
    private final String onPremisesImmutableId;
    private final String mailNickname;
    private final String userPrincipalName;
    private final String forceChangePassword;
    private final String assignedPassword;
    private final String oDataQuery;

    private final Office365CommonInputs commonInputs;

    @java.beans.ConstructorProperties({"accountEnabled", "displayName", "onPremisesImmutableId", "mailNickname",
            "userPrincipalName", "forceChangePassword", "assignedPassword", "commonInputs", "oDataQuery"})

    public CreateUserInputs(String accountEnabled, String displayName, String onPremisesImmutableId, String mailNickname,
                            String userPrincipalName, String forceChangePassword, String assignedPassword, Office365CommonInputs commonInputs, String oDataQuery) {
        this.accountEnabled = accountEnabled;
        this.displayName = displayName;
        this.onPremisesImmutableId = onPremisesImmutableId;
        this.mailNickname = mailNickname;
        this.userPrincipalName = userPrincipalName;
        this.forceChangePassword = forceChangePassword;
        this.assignedPassword = assignedPassword;
        this.commonInputs = commonInputs;
        this.oDataQuery = oDataQuery;
    }

    @NotNull
    public static CreateUserInputsBuilder builder() {
        return new CreateUserInputsBuilder();
    }

    @NotNull
    public String getAccountEnabled() {
        return accountEnabled;
    }

    @NotNull
    public String getDisplayName() {
        return displayName;
    }

    @NotNull
    public String getOnPremisesImmutableId() {
        return onPremisesImmutableId;
    }

    @NotNull
    public String getMailNickname() {
        return mailNickname;
    }

    @NotNull
    public String getUserPrincipalName() {
        return userPrincipalName;
    }

    @NotNull
    public String getForceChangePassword() {
        return forceChangePassword;
    }

    @NotNull
    public String getAssignedPassword() {
        return assignedPassword;
    }

    @NotNull
    public Office365CommonInputs getCommonInputs() {
        return commonInputs;
    }

    @NotNull
    public String getoDataQuery() {
        return oDataQuery;
    }

    public static class CreateUserInputsBuilder {
        private String accountEnabled = EMPTY;
        private String displayName = EMPTY;
        private String onPremisesImmutableId = EMPTY;
        private String mailNickname = EMPTY;
        private String userPrincipalName = EMPTY;
        private String forceChangePassword = EMPTY;
        private String assignedPassword = EMPTY;
        private Office365CommonInputs commonInputs;
        private String oDataQuery = EMPTY;

        CreateUserInputsBuilder() {
        }

        @NotNull
        public CreateUserInputs.CreateUserInputsBuilder accountEnabled(@NotNull final String accountEnabled) {
            this.accountEnabled = accountEnabled;
            return this;
        }

        @NotNull
        public CreateUserInputs.CreateUserInputsBuilder displayName(@NotNull final String displayName) {
            this.displayName = displayName;
            return this;
        }

        @NotNull
        public CreateUserInputs.CreateUserInputsBuilder onPremisesImmutableId(@NotNull final String onPremisesImmutableId) {
            this.onPremisesImmutableId = onPremisesImmutableId;
            return this;
        }

        @NotNull
        public CreateUserInputs.CreateUserInputsBuilder mailNickname(@NotNull final String mailNickname) {
            this.mailNickname = mailNickname;
            return this;
        }

        @NotNull
        public CreateUserInputs.CreateUserInputsBuilder userPrincipalName(@NotNull final String userPrincipalName) {
            this.userPrincipalName = userPrincipalName;
            return this;
        }

        @NotNull
        public CreateUserInputs.CreateUserInputsBuilder forceChangePassword(@NotNull final String forceChangePassword) {
            this.forceChangePassword = forceChangePassword;
            return this;
        }

        @NotNull
        public CreateUserInputs.CreateUserInputsBuilder assignedPassword(@NotNull final String assignedPassword) {
            this.assignedPassword = assignedPassword;
            return this;
        }

        @NotNull
        public CreateUserInputs.CreateUserInputsBuilder commonInputs(@NotNull final Office365CommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        @NotNull
        public CreateUserInputs.CreateUserInputsBuilder oDataQuery(@NotNull final String oDataQuery) {
            this.oDataQuery = oDataQuery;
            return this;
        }

        public CreateUserInputs build() {
            return new CreateUserInputs(accountEnabled, displayName, onPremisesImmutableId, mailNickname,
                    userPrincipalName, forceChangePassword, assignedPassword, commonInputs, oDataQuery);

        }
    }
}
