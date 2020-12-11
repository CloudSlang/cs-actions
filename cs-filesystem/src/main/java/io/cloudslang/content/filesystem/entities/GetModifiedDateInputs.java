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

public class GetModifiedDateInputs {

    private String source;
    private String threshold;
    private String localeLang;
    private String localeCountry;

    public GetModifiedDateInputs(final String source, final String threshold, final String localeLang, final String localeCountry) {
        this.source = source;
        this.threshold = threshold;
        this.localeLang = localeLang;
        this.localeCountry = localeCountry;
    }

    public static GetModifiedDateInputsBuilder builder() {return new GetModifiedDateInputsBuilder();}

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getThreshold() {
        return threshold;
    }

    public void setThreshold(String treshold) {
        this.threshold = treshold;
    }

    public String getLocaleLang() {
        return localeLang;
    }

    public void setLocaleLang(String localeLang) {
        this.localeLang = localeLang;
    }

    public String getLocaleCountry() {
        return localeCountry;
    }

    public void setLocaleCountry(String localeCountry) {
        this.localeCountry = localeCountry;
    }

    public static class GetModifiedDateInputsBuilder {

        private String source = EMPTY;
        private String threshold = EMPTY;
        private String localeLang = EMPTY;
        private String localeCountry = EMPTY;

        GetModifiedDateInputsBuilder(){}

        public GetModifiedDateInputsBuilder source(final String source) {
            this.source = source;
            return this;
        }

        public GetModifiedDateInputsBuilder threshold(final String threshold) {
            this.threshold = threshold;
            return this;
        }

        public GetModifiedDateInputsBuilder localeLang(final String localeLang) {
            this.localeLang = localeLang;
            return this;
        }

        public GetModifiedDateInputsBuilder localeCountry(final String localeCountry) {
            this.localeCountry = localeCountry;
            return this;
        }

        public GetModifiedDateInputs build() {
            return new GetModifiedDateInputs(source, threshold, localeLang, localeCountry);
        }
    }
}
