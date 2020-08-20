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
package io.cloudslang.content.maps.entities;

import io.cloudslang.content.maps.constants.DefaultInputValues;
import io.cloudslang.content.maps.exceptions.ValidationException;
import io.cloudslang.content.maps.utils.BuilderUtils;
import org.apache.commons.lang3.StringUtils;

public class GetValuesInput {
    private final String map;
    private final String pairDelimiter;
    private final String entryDelimiter;
    private final String mapStart;
    private final String mapEnd;
    private final String elementWrapper;
    private final boolean stripWhitespaces;
    private final String key;

    private GetValuesInput(Builder builder) {
        this.map = builder.map;
        this.pairDelimiter = builder.pairDelimiter;
        this.entryDelimiter = builder.entryDelimiter;
        this.mapStart = builder.mapStart;
        this.mapEnd = builder.mapEnd;
        this.elementWrapper = builder.elementWrapper;
        this.stripWhitespaces = builder.stripWhitespaces;
        this.key = builder.key;
    }


    public String getMap() {
        return map;
    }


    public String getPairDelimiter() {
        return pairDelimiter;
    }


    public String getEntryDelimiter() {
        return entryDelimiter;
    }


    public String getMapStart() {
        return mapStart;
    }


    public String getMapEnd() {
        return mapEnd;
    }


    public String getElementWrapper() {
        return elementWrapper;
    }


    public boolean isStripWhitespaces() {
        return stripWhitespaces;
    }

    public String getKey() {
        return key;
    }


    public static class Builder {
        private String map;
        private String pairDelimiter;
        private String entryDelimiter;
        private String mapStart;
        private String mapEnd;
        private String elementWrapper;
        private boolean stripWhitespaces;
        private String key;

        public Builder map(String map) {
            this.map = StringUtils.defaultString(map, DefaultInputValues.MAP);
            return this;
        }


        public Builder pairDelimiter(String pairDelimiter) {
            this.pairDelimiter = pairDelimiter;
            return this;
        }


        public Builder entryDelimiter(String entryDelimiter) {
            this.entryDelimiter = entryDelimiter;
            return this;
        }


        public Builder mapStart(String mapStart) {
            this.mapStart = StringUtils.defaultString(mapStart, DefaultInputValues.MAP_START);
            return this;
        }


        public Builder mapEnd(String mapEnd) {
            this.mapEnd = StringUtils.defaultString(mapEnd, DefaultInputValues.MAP_END);
            return this;
        }


        public Builder elementWrapper(String elementWrapper) {
            this.elementWrapper = StringUtils.defaultString(elementWrapper, DefaultInputValues.ELEMENT_WRAPPER);
            return this;
        }


        public Builder stripWhitespaces(String stripWhitespaces) throws ValidationException {
            this.stripWhitespaces = BuilderUtils.parseStripWhitespaces(stripWhitespaces);
            return this;
        }

        public Builder key(String key) {
            this.key = StringUtils.defaultString(key, StringUtils.EMPTY);
            return this;
        }

        public GetValuesInput build() {
            return new GetValuesInput(this);
        }
    }
}
