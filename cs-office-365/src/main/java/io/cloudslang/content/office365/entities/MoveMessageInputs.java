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

public class MoveMessageInputs {
    private final String messageId;
    private final Office365CommonInputs commonInputs;
    private final String destinationId;

    @java.beans.ConstructorProperties({"messageId", "destinationId", "commonInputs"})
    public MoveMessageInputs(String messageId, String destinationId, Office365CommonInputs commonInputs) {
        this.messageId = messageId;
        this.commonInputs = commonInputs;
        this.destinationId = destinationId;
    }

    @NotNull
    public static MoveMessageInputs.MoveMessageInputsBuilder builder() {
        return new MoveMessageInputs.MoveMessageInputsBuilder();
    }

    @NotNull
    public String getMessageId() {
        return messageId;
    }

    @NotNull
    public String getDestinationId() {
        return destinationId;
    }

    @NotNull
    public Office365CommonInputs getCommonInputs() {
        return this.commonInputs;
    }

    public static class MoveMessageInputsBuilder {
        private String messageId = EMPTY;
        private Office365CommonInputs commonInputs;
        private String destinationId = EMPTY;

        MoveMessageInputsBuilder() {
        }

        @NotNull
        public MoveMessageInputs.MoveMessageInputsBuilder messageId(@NotNull final String messageId) {
            this.messageId = messageId;
            return this;
        }

        public MoveMessageInputs.MoveMessageInputsBuilder destinationId(@NotNull final String destinationId) {
            this.destinationId = destinationId;
            return this;
        }

        @NotNull
        public MoveMessageInputs.MoveMessageInputsBuilder commonInputs(@NotNull final Office365CommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public MoveMessageInputs build() {
            return new MoveMessageInputs(messageId, destinationId, commonInputs);
        }
    }

}
