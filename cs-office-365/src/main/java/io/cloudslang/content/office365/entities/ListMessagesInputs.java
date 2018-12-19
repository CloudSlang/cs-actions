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

public class ListMessagesInputs {

    private final String folderId;
    private final Office365CommonInputs commonInputs;

    @java.beans.ConstructorProperties({"folderId", "commonInputs"})
    public ListMessagesInputs(String folderId, Office365CommonInputs commonInputs) {
        this.folderId = folderId;
        this.commonInputs = commonInputs;
    }

    @NotNull
    public static ListMessagesInputsBuilder builder() {
        return new ListMessagesInputsBuilder();
    }

    @NotNull
    public String getFolderId() {
        return folderId;
    }

    @NotNull
    public Office365CommonInputs getCommonInputs() {
        return this.commonInputs;
    }

    public static class ListMessagesInputsBuilder {

        private String folderId = EMPTY;
        private Office365CommonInputs commonInputs;

        ListMessagesInputsBuilder() {
        }

        @NotNull
        public ListMessagesInputs.ListMessagesInputsBuilder folderId(@NotNull final String folderId) {
            this.folderId = folderId;
            return this;
        }

        @NotNull
        public ListMessagesInputs.ListMessagesInputsBuilder commonInputs(@NotNull final Office365CommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public ListMessagesInputs build() {
            return new ListMessagesInputs(folderId, commonInputs);
        }
    }

}
