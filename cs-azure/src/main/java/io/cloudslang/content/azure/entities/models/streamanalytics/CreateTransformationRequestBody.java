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
        public String streamingUnits;

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        public String getStreamingUnits() {
            return streamingUnits;
        }

        public void setStreamingUnits(String streamingUnits) {
            this.streamingUnits = streamingUnits;
        }
    }


}
