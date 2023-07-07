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




package io.cloudslang.content.azure.entities;

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class CreateStreamingJobInputs {


    private final String skuName;
    private final String eventsOutOfOrderPolicy;
    private final String outputErrorPolicy;
    private final String eventsOutOfOrderMaxDelayInSeconds;
    private final String eventsLateArrivalMaxDelayInSeconds;
    private final String dataLocale;
    private final String compatibilityLevel;
    private final String location;
    private final String tags;
    private final AzureCommonInputs azureCommonInputs;

    @java.beans.ConstructorProperties({"skuName", "eventsOutOfOrderPolicy", "outputErrorPolicy", "eventsOutOfOrderMaxDelayInSeconds", "eventsLateArrivalMaxDelayInSeconds", "dataLocale", "compatibilityLevel", "location", "tags", "azureCommonInputs"})
    public CreateStreamingJobInputs(String skuName, String eventsOutOfOrderPolicy, String outputErrorPolicy, String eventsOutOfOrderMaxDelayInSeconds, String eventsLateArrivalMaxDelayInSeconds, String dataLocale, String compatibilityLevel, String location, String tags, AzureCommonInputs azureCommonInputs) {


        this.skuName = skuName;
        this.eventsOutOfOrderPolicy = eventsOutOfOrderPolicy;
        this.outputErrorPolicy = outputErrorPolicy;
        this.eventsOutOfOrderMaxDelayInSeconds = eventsOutOfOrderMaxDelayInSeconds;
        this.eventsLateArrivalMaxDelayInSeconds = eventsLateArrivalMaxDelayInSeconds;
        this.dataLocale = dataLocale;
        this.compatibilityLevel = compatibilityLevel;
        this.location = location;
        this.tags = tags;
        this.azureCommonInputs = azureCommonInputs;
    }

    @NotNull
    public static CreateStreamingJobInputsBuilder builder() {
        return new CreateStreamingJobInputsBuilder();
    }





    @NotNull
    public String getSkuName() {
        return skuName;
    }

    @NotNull
    public String getEventsOutOfOrderPolicy() {
        return eventsOutOfOrderPolicy;
    }

    @NotNull
    public String getOutputErrorPolicy() {
        return outputErrorPolicy;
    }

    @NotNull
    public String getEventsOutOfOrderMaxDelayInSeconds() {
        return eventsOutOfOrderMaxDelayInSeconds;
    }

    @NotNull
    public String getEventsLateArrivalMaxDelayInSeconds() {
        return eventsLateArrivalMaxDelayInSeconds;
    }

    @NotNull
    public String getDataLocale() {
        return dataLocale;
    }

    @NotNull
    public String getCompatibilityLevel() {
        return compatibilityLevel;
    }

    @NotNull
    public String getLocation() {
        return location;
    }

    @NotNull
    public String getTags() {
        return tags;
    }

    @NotNull
    public AzureCommonInputs getAzureCommonInputs(){
        return azureCommonInputs;
    }

    public static final class CreateStreamingJobInputsBuilder {

        private String skuName = EMPTY;
        private String eventsOutOfOrderPolicy = EMPTY;
        private String outputErrorPolicy = EMPTY;
        private String eventsOutOfOrderMaxDelayInSeconds = EMPTY;
        private String eventsLateArrivalMaxDelayInSeconds = EMPTY;
        private String dataLocale = EMPTY;
        private String compatibilityLevel = EMPTY;
        private String location = EMPTY;
        private String tags = EMPTY;
        private AzureCommonInputs azureCommonInputs;

        CreateStreamingJobInputsBuilder() {
        }




        @NotNull
        public CreateStreamingJobInputsBuilder skuName(@NotNull final String skuName) {
            this.skuName = skuName;
            return this;
        }

        @NotNull
        public CreateStreamingJobInputsBuilder eventsOutOfOrderPolicy(@NotNull final String eventsOutOfOrderPolicy) {
            this.eventsOutOfOrderPolicy = eventsOutOfOrderPolicy;
            return this;
        }

        @NotNull
        public CreateStreamingJobInputsBuilder outputErrorPolicy(@NotNull final String outputErrorPolicy) {
            this.outputErrorPolicy = outputErrorPolicy;
            return this;
        }

        @NotNull
        public CreateStreamingJobInputsBuilder eventsOutOfOrderMaxDelayInSeconds(@NotNull final String eventsOutOfOrderMaxDelayInSeconds) {
            this.eventsOutOfOrderMaxDelayInSeconds = eventsOutOfOrderMaxDelayInSeconds;
            return this;
        }

        @NotNull
        public CreateStreamingJobInputsBuilder eventsLateArrivalMaxDelayInSeconds(@NotNull final String eventsLateArrivalMaxDelayInSeconds) {
            this.eventsLateArrivalMaxDelayInSeconds = eventsLateArrivalMaxDelayInSeconds;
            return this;
        }

        @NotNull
        public CreateStreamingJobInputsBuilder dataLocale(@NotNull final String dataLocale) {
            this.dataLocale = dataLocale;
            return this;
        }

        @NotNull
        public CreateStreamingJobInputsBuilder compatibilityLevel(@NotNull final String compatibilityLevel) {
            this.compatibilityLevel = compatibilityLevel;
            return this;
        }

        @NotNull
        public CreateStreamingJobInputsBuilder location(@NotNull final String location) {
            this.location = location;
            return this;
        }

        @NotNull
        public CreateStreamingJobInputsBuilder tags(@NotNull final String tags) {
            this.tags = tags;
            return this;
        }

        @NotNull
        public CreateStreamingJobInputsBuilder azureCommonInputs(@NotNull final AzureCommonInputs azureCommonInputs) {
            this.azureCommonInputs = azureCommonInputs;
            return this;
        }

        public CreateStreamingJobInputs build() {
            return new CreateStreamingJobInputs(skuName, eventsOutOfOrderPolicy, outputErrorPolicy, eventsOutOfOrderMaxDelayInSeconds, eventsLateArrivalMaxDelayInSeconds, dataLocale, compatibilityLevel, location, tags, azureCommonInputs);
        }
    }
}
