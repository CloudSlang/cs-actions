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

public class ListAttachmentsInputs {
    private final String messageId;
    private final Office365CommonInputs commonInputs;

    @java.beans.ConstructorProperties({"messageId","commonInputs"})
    public ListAttachmentsInputs(String messageId, Office365CommonInputs commonInputs){
        this.messageId = messageId;
        this.commonInputs = commonInputs;
    }

    public static ListAttachmentsInputsBuilder builder(){
        return new ListAttachmentsInputsBuilder();
    }

    @NotNull
    public String getMessageId() {
        return messageId;
    }

    @NotNull
    public  Office365CommonInputs getCommonInputs(){
        return this.commonInputs;
    }

    public static class ListAttachmentsInputsBuilder {
        private String messageId = EMPTY;
        private Office365CommonInputs commonInputs;

    ListAttachmentsInputsBuilder(){
    }

    @NotNull
        public ListAttachmentsInputs.ListAttachmentsInputsBuilder messageId(@NotNull final String messageId){
            this.messageId = messageId;
            return this;
    }

    @NotNull
        public ListAttachmentsInputs.ListAttachmentsInputsBuilder commonInputs(@NotNull final Office365CommonInputs commonInputs){
        this.commonInputs = commonInputs;
        return this;
    }

    public ListAttachmentsInputs build() { return  new ListAttachmentsInputs(messageId,commonInputs); }
    }

}
