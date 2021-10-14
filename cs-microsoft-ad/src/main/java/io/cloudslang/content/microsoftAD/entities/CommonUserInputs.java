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
package io.cloudslang.content.microsoftAD.entities;

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class CommonUserInputs {

    private final String accountEnabled;
    private final String displayName;
    private final String onPremisesImmutableId;
    private final String mailNickname;
    private final String forceChangePassword;
    private final String password;
    private final String body;

    private final AzureActiveDirectoryCommonInputs commonInputs;

    @java.beans.ConstructorProperties({"accountEnabled", "displayName", "onPremisesImmutableId", "mailNickname",
            "forceChangePassword", "password", "commonInputs", "body"})

    public CommonUserInputs(String accountEnabled, String displayName, String onPremisesImmutableId, String mailNickname,
                            String forceChangePassword, String password, AzureActiveDirectoryCommonInputs commonInputs,
                            String body) {
        this.accountEnabled = accountEnabled;
        this.displayName = displayName;
        this.onPremisesImmutableId = onPremisesImmutableId;
        this.mailNickname = mailNickname;
        this.forceChangePassword = forceChangePassword;
        this.password = password;
        this.commonInputs = commonInputs;
        this.body = body;
    }

    @NotNull
    public static CommonUserInputsBuilder builder() {
        return new CommonUserInputsBuilder();
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
    public String getForceChangePassword() {
        return forceChangePassword;
    }

    @NotNull
    public String getPassword() {
        return password;
    }

    @NotNull
    public AzureActiveDirectoryCommonInputs getCommonInputs() {
        return commonInputs;
    }

    @NotNull
    public String getBody() {
        return body;
    }

    public static class CommonUserInputsBuilder {
        private String accountEnabled = EMPTY;
        private String displayName = EMPTY;
        private String onPremisesImmutableId = EMPTY;
        private String mailNickname = EMPTY;
        private String forceChangePassword = EMPTY;
        private String password = EMPTY;
        private AzureActiveDirectoryCommonInputs commonInputs;
        private String body = EMPTY;

        CommonUserInputsBuilder() {
        }

        @NotNull
        public CommonUserInputs.CommonUserInputsBuilder accountEnabled(@NotNull final String accountEnabled) {
            this.accountEnabled = accountEnabled;
            return this;
        }

        @NotNull
        public CommonUserInputs.CommonUserInputsBuilder displayName(@NotNull final String displayName) {
            this.displayName = displayName;
            return this;
        }

        @NotNull
        public CommonUserInputs.CommonUserInputsBuilder onPremisesImmutableId(@NotNull final String onPremisesImmutableId) {
            this.onPremisesImmutableId = onPremisesImmutableId;
            return this;
        }

        @NotNull
        public CommonUserInputs.CommonUserInputsBuilder mailNickname(@NotNull final String mailNickname) {
            this.mailNickname = mailNickname;
            return this;
        }

        @NotNull
        public CommonUserInputs.CommonUserInputsBuilder forceChangePassword(@NotNull final String forceChangePassword) {
            this.forceChangePassword = forceChangePassword;
            return this;
        }

        @NotNull
        public CommonUserInputs.CommonUserInputsBuilder password(@NotNull final String password) {
            this.password = password;
            return this;
        }

        @NotNull
        public CommonUserInputs.CommonUserInputsBuilder commonInputs(@NotNull final AzureActiveDirectoryCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        @NotNull
        public CommonUserInputs.CommonUserInputsBuilder body(@NotNull final String body) {
            this.body = body;
            return this;
        }

        public CommonUserInputs build() {
            return new CommonUserInputs(accountEnabled, displayName, onPremisesImmutableId, mailNickname,
                     forceChangePassword, password, commonInputs, body);

        }
    }
}
