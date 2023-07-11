/*
 * Copyright 2023 Open Text
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



package io.cloudslang.content.azure.entities.models.streamanalytics;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.json.simple.JSONObject;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CreateStreamingJobRequestBody {

    Properties properties;
    String location;
    JSONObject tags;


    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public JSONObject getTags() {
        return tags;
    }

    public void setTags(JSONObject tags) {
        this.tags = tags;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class Properties {
        public String eventsOutOfOrderPolicy;
        public String outputErrorPolicy;
        public Integer eventsOutOfOrderMaxDelayInSeconds;
        public Integer eventsLateArrivalMaxDelayInSeconds;
        public String dataLocale;
        public String compatibilityLevel;
        SKU sku;

        public SKU getSku() {
            return sku;
        }

        public void setSku(SKU sku) {
            this.sku = sku;
        }

        public String getEventsOutOfOrderPolicy() {
            return eventsOutOfOrderPolicy;
        }

        public void setEventsOutOfOrderPolicy(String eventsOutOfOrderPolicy) {
            this.eventsOutOfOrderPolicy = eventsOutOfOrderPolicy;
        }

        public String getOutputErrorPolicy() {
            return outputErrorPolicy;
        }

        public void setOutputErrorPolicy(String outputErrorPolicy) {
            this.outputErrorPolicy = outputErrorPolicy;
        }

        public Integer getEventsOutOfOrderMaxDelayInSeconds() {
            return eventsOutOfOrderMaxDelayInSeconds;
        }

        public void setEventsOutOfOrderMaxDelayInSeconds(Integer eventsOutOfOrderMaxDelayInSeconds) {
            this.eventsOutOfOrderMaxDelayInSeconds = eventsOutOfOrderMaxDelayInSeconds;
        }

        public Integer getEventsLateArrivalMaxDelayInSeconds() {
            return eventsLateArrivalMaxDelayInSeconds;
        }

        public void setEventsLateArrivalMaxDelayInSeconds(Integer eventsLateArrivalMaxDelayInSeconds) {
            this.eventsLateArrivalMaxDelayInSeconds = eventsLateArrivalMaxDelayInSeconds;
        }

        public String getDataLocale() {
            return dataLocale;
        }

        public void setDataLocale(String dataLocale) {
            this.dataLocale = dataLocale;
        }

        public String getCompatibilityLevel() {
            return compatibilityLevel;
        }

        public void setCompatibilityLevel(String compatibilityLevel) {
            this.compatibilityLevel = compatibilityLevel;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class SKU {
        public String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
