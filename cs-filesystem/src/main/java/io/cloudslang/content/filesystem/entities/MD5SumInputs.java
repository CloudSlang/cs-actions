/*
 * (c) Copyright 2020 EntIT Software LLC, a Micro Focus company, L.P.
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
package io.cloudslang.content.filesystem.entities;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class MD5SumInputs {

    private String source;
    private String compareTo;

    public MD5SumInputs(String source, String compareTo) {
        this.source = source;
        this.compareTo = compareTo;
    }

    public static MD5SumInputsBuilder builder() {return new MD5SumInputsBuilder();}

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCompareTo() {
        return compareTo;
    }

    public void setCompareTo(String compareTo) {
        this.compareTo = compareTo;
    }

    public static class MD5SumInputsBuilder {

        private String source = EMPTY;
        private String compareTo = EMPTY;

        MD5SumInputsBuilder() {}

        public MD5SumInputsBuilder source(final String source) {
            this.source = source;
            return this;
        }

        public MD5SumInputsBuilder compareTo(final String compareTo) {
            this.compareTo = compareTo;
            return this;
        }

        public MD5SumInputs build() {
            return new MD5SumInputs(source, compareTo);
        }
    }
}
