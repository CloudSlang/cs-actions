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
    private final String topQuery;
    private final String selectQuery;
    private final String oDataQuery;

    private final Office365CommonInputs commonInputs;

    @java.beans.ConstructorProperties({"folderId", "topQuery", "selectQuery", "oDataQuery", "commonInputs"})
    public ListMessagesInputs(String folderId, String topQuery, String selectQuery, String oDataQuery, Office365CommonInputs commonInputs) {
        this.folderId = folderId;
        this.topQuery = topQuery;
        this.selectQuery = selectQuery;
        this.oDataQuery = oDataQuery;
        this.commonInputs = commonInputs;
    }

    @NotNull
    public static ListMessagesInputsBuilder builder() {
        return new ListMessagesInputsBuilder();
    }

    @NotNull
    public String getTopQuery() {
        return topQuery;
    }

    @NotNull
    public String getSelectQuery() {
        return selectQuery;
    }

    @NotNull
    public String getoDataQuery() {
        return oDataQuery;
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
        private String topQuery = EMPTY;
        private String selectQuery = EMPTY;
        private String oDataQuery = EMPTY;
        private Office365CommonInputs commonInputs;

        ListMessagesInputsBuilder() {
        }

        @NotNull
        public ListMessagesInputs.ListMessagesInputsBuilder topQuery(@NotNull final String topQuery) {
            this.topQuery = topQuery;
            return this;
        }

        @NotNull
        public ListMessagesInputs.ListMessagesInputsBuilder selectQuery(@NotNull final String selectQuery) {
            this.selectQuery = selectQuery;
            return this;
        }

        @NotNull
        public ListMessagesInputs.ListMessagesInputsBuilder oDataQuery(@NotNull final String oDataQuery) {
            this.oDataQuery = oDataQuery;
            return this;
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
            return new ListMessagesInputs(folderId, topQuery, selectQuery, oDataQuery, commonInputs);
        }
    }

}
