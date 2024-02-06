/*
 * Copyright 2020-2024 Open Text
 * This program and the accompanying materials
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

package io.cloudslang.content.filesystem.entities;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class GetChildrenInputs {

    private String source;
    private String delimiter;

    public GetChildrenInputs(String source, String delimiter) {
        this.source = source;
        this.delimiter = delimiter;
    }

    public static GetChildrenInputsBuilder builder() {return new GetChildrenInputsBuilder();}

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public static class GetChildrenInputsBuilder {

        private String source = EMPTY;
        private String delimiter = EMPTY;

        GetChildrenInputsBuilder(){}

        public GetChildrenInputsBuilder source(final String source) {
            this.source = source;
            return this;
        }

        public GetChildrenInputsBuilder delimiter(final String delimiter) {
            this.delimiter = delimiter;
            return this;
        }

        public GetChildrenInputs build() {
            return new GetChildrenInputs(source, delimiter);
        }
    }

}
