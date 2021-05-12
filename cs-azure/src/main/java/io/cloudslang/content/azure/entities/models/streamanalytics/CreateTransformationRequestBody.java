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

public class CreateTransformationRequestBody {

    Properties properties;

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class Properties {
        public String query;
        public int streamingUnits;

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        public int getStreamingUnits() {
            return streamingUnits;
        }

        public void setStreamingUnits(int streamingUnits) {
            this.streamingUnits = streamingUnits;
        }
    }


}
