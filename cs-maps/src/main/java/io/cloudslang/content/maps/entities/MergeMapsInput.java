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

import org.apache.commons.lang3.StringUtils;

public class MergeMapsInput {

    private final String map1;
    private final String map1PairDelimiter;
    private final String map1EntryDelimiter;
    private final String map1Start;
    private final String map1End;
    private final String map2;
    private final String map2PairDelimiter;
    private final String map2EntryDelimiter;
    private final String map2Start;
    private final String map2End;

    private MergeMapsInput(Builder builder) {
        this.map1 = builder.map1;
        this.map1PairDelimiter = builder.map1PairDelimiter;
        this.map1EntryDelimiter = builder.map1EntryDelimiter;
        this.map1Start = builder.map1Start;
        this.map1End = builder.map1End;

        this.map2 = builder.map2;
        this.map2PairDelimiter = builder.map2PairDelimiter;
        this.map2EntryDelimiter = builder.map2EntryDelimiter;
        this.map2Start = builder.map2Start;
        this.map2End = builder.map2End;

    }

    public String getMap1() {
        return map1;
    }

    public String getMap2() {
        return map2;
    }

    public String getMap1PairDelimiter() {
        return map1PairDelimiter;
    }

    public String getMap1EntryDelimiter() {
        return map1EntryDelimiter;
    }

    public String getMap1Start() {
        return map1Start;
    }

    public String getMap1End() {
        return map1End;
    }

    public String getMap2PairDelimiter() {
        return map2PairDelimiter;
    }

    public String getMap2EntryDelimiter() {
        return map2EntryDelimiter;
    }

    public String getMap2Start() {
        return map2Start;
    }

    public String getMap2End() {
        return map2End;
    }

    public static class Builder {
        private String map1;
        private String map2;
        private String map1PairDelimiter;
        private String map1EntryDelimiter;
        private String map1Start;
        private String map1End;
        private String map2PairDelimiter;
        private String map2EntryDelimiter;
        private String map2Start;
        private String map2End;

        public Builder map1(String map1) {
            this.map1 = map1;
            return this;
        }

        public Builder map2(String map2) {
            this.map2 = map2;
            return this;
        }

        public Builder map1PairDelimiter(String map1PairDelimiter) {
            this.map1PairDelimiter = map1PairDelimiter;
            return this;
        }


        public Builder map1EntryDelimiter(String map1EntryDelimiter ) {
            this.map1EntryDelimiter = map1EntryDelimiter;
            return this;
        }


        public Builder map1Start(String map1Start) {
            this.map1Start = StringUtils.defaultString(map1Start);
            return this;
        }


        public Builder map1End(String map1End) {
            this.map1End = StringUtils.defaultString(map1End);
            return this;
        }

        public Builder map2PairDelimiter(String map2PairDelimiter) {
            this.map2PairDelimiter = map2PairDelimiter;
            return this;
        }


        public Builder map2EntryDelimiter(String map2EntryDelimiter ) {
            this.map2EntryDelimiter = map2EntryDelimiter;
            return this;
        }


        public Builder map2Start(String map2Start) {
            this.map2Start = StringUtils.defaultString(map2Start);
            return this;
        }


        public Builder map2End(String map2End) {
            this.map2End = StringUtils.defaultString(map2End);
            return this;
        }


        public MergeMapsInput build() {
            return new MergeMapsInput(this);
        }
    }
}
