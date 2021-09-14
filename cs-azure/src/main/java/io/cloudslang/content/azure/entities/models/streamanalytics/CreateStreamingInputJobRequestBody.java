/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
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
 * (c) Copyright 2021 Micro Focus, L.P.
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
package io.cloudslang.content.azure.entities.models.streamanalytics;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.simple.JSONArray;

public class CreateStreamingInputJobRequestBody {
    CreateStreamingInputJobRequestBody.Properties properties;

    public CreateStreamingInputJobRequestBody.Properties getProperties() {
        return properties;
    }

    public void setProperties(CreateStreamingInputJobRequestBody.Properties properties) {
        this.properties = properties;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class Properties {

        @JsonProperty("type")
        public String sourceType;
        CreateStreamingInputJobRequestBody.Datasource datasource;
        CreateStreamingInputJobRequestBody.Serialization serialization;

        public String getSourceType() {
            return sourceType;
        }

        public void setSourceType(String sourceType) {
            this.sourceType = sourceType;
        }

        public CreateStreamingInputJobRequestBody.Datasource getDatasource() {
            return datasource;
        }

        public void setDatasource(CreateStreamingInputJobRequestBody.Datasource datasource) {
            this.datasource = datasource;
        }

        public CreateStreamingInputJobRequestBody.Serialization getSerialization() {
            return serialization;
        }

        public void setSerialization(CreateStreamingInputJobRequestBody.Serialization serialization) {
            this.serialization = serialization;
        }

    }


    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class Serialization {
        public String type;
        CreateStreamingInputJobRequestBody.SerializationProperties properties;

        public CreateStreamingInputJobRequestBody.SerializationProperties getProperties() {
            return properties;
        }

        public void setProperties(CreateStreamingInputJobRequestBody.SerializationProperties properties) {
            this.properties = properties;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class SerializationProperties {
        public String fieldDelimiter;
        public String encoding;

        public String getFieldDelimiter() {
            return fieldDelimiter;
        }

        public void setFieldDelimiter(String fieldDelimiter) {
            this.fieldDelimiter = fieldDelimiter;
        }

        public String getEncoding() {
            return encoding;
        }

        public void setEncoding(String encoding) {
            this.encoding = encoding;
        }

    }


    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class Datasource {
        public String type;
        //@JsonProperty("properties")
        CreateStreamingInputJobRequestBody.Datasource.SubProperties properties;

        public CreateStreamingInputJobRequestBody.Datasource.SubProperties getProperties() {
            return properties;
        }

        public void setProperties(CreateStreamingInputJobRequestBody.Datasource.SubProperties properties) {
            this.properties = properties;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        public static class SubProperties {
            public String container;
            public String pathPattern;
            public String dateFormat;
            public String timeFormat;
            @JsonProperty("storageAccounts")
            JSONArray strogeaccounts;

            public String getDateFormat() {
                return dateFormat;
            }

            public void setDateFormat(String dateFormat) {
                this.dateFormat = dateFormat;
            }

            public String getTimeFormat() {
                return timeFormat;
            }

            public void setTimeFormat(String timeFormat) {
                this.timeFormat = timeFormat;
            }

            public String getContainer() {
                return container;
            }

            public void setContainer(String container) {
                this.container = container;
            }

            public String getPathPattern() {
                return pathPattern;
            }

            public void setPathPattern(String pathPattern) {
                this.pathPattern = pathPattern;
            }

            public JSONArray getStrogeaccounts() {
                return strogeaccounts;
            }

            public void setStrogeaccounts(JSONArray strogeaccounts) {
                this.strogeaccounts = strogeaccounts;
            }


            @JsonInclude(JsonInclude.Include.NON_EMPTY)
            public static class StorageAccounts {
                public String accountName;
                public String accountKey;

                public String getAccountName() {
                    return accountName;
                }

                public void setAccountName(String accountName) {
                    this.accountName = accountName;
                }

                public String getAccountKey() {
                    return accountKey;
                }

                public void setAccountKey(String accountKey) {
                    this.accountKey = accountKey;
                }


            }


        }


    }
}
