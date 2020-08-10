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

public class AddKeyInput {
    private final String map;
    private final String key;
    private final String value;


    private AddKeyInput(Builder builder) {
        this.map = builder.map;
        this.key = builder.key;
        this.value = builder.value;
    }


    public String getMap() {
        return map;
    }


    public String getKey() {
        return key;
    }


    public String getValue() {
        return value;
    }


    public static class Builder {
        private String map;
        private String key;
        private String value;


        public Builder map(String map) {
            this.map = map;
            return this;
        }


        public Builder key(String key) {
            this.key = key;
            return this;
        }


        public Builder value(String value) {
            this.value = value;
            return this;
        }


        public AddKeyInput build() {
            return new AddKeyInput(this);
        }
    }
}
