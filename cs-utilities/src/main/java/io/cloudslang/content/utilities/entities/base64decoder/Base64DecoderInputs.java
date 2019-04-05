/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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
package io.cloudslang.content.utilities.entities.base64decoder;

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class Base64DecoderInputs {

    private final String filePath;
    private final String contentBytes;

    @java.beans.ConstructorProperties({"filePath", "contentBytes"})
    public Base64DecoderInputs(String filePath, String contentBytes) {
        this.filePath = filePath;
        this.contentBytes = contentBytes;
    }

    @NotNull
    public static Base64DecoderInputsBuilder builder() {
        return new Base64DecoderInputsBuilder();
    }

    @NotNull
    public String getFilePath() {
        return filePath;
    }

    @NotNull
    public String getContentBytes() {
        return contentBytes;
    }

    public static class Base64DecoderInputsBuilder {

        private String filePath = EMPTY;
        private String contentBytes = EMPTY;

        Base64DecoderInputsBuilder() {
        }

        @NotNull
        public Base64DecoderInputs.Base64DecoderInputsBuilder filePath(@NotNull final String filePath) {
            this.filePath = filePath;
            return this;
        }


        @NotNull
        public Base64DecoderInputs.Base64DecoderInputsBuilder contentBytes(@NotNull final String contentBytes) {
            this.contentBytes = contentBytes;
            return this;
        }

        public Base64DecoderInputs build() {
            return new Base64DecoderInputs(filePath, contentBytes);
        }
    }

}
