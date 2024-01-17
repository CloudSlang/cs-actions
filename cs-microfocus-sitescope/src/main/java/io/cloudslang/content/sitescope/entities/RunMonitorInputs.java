/*
 * Copyright 2020-2024 Open Text
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

package io.cloudslang.content.sitescope.entities;

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class RunMonitorInputs {

    private String fullPathToMonitor;
    private String monitorId;
    private String delimiter;
    private String executionTimeout;
    private String identifier;
    private SiteScopeCommonInputs commonInputs;

    private RunMonitorInputs(String fullPathToMonitor, String monitorId, String delimiter, String identifier,
                             String executionTimeout, SiteScopeCommonInputs commonInputs) {
        this.fullPathToMonitor = fullPathToMonitor;
        this.monitorId = monitorId;
        this.delimiter = delimiter;
        this.identifier = identifier;
        this.executionTimeout = executionTimeout;
        this.commonInputs = commonInputs;
    }

    @NotNull
    public static RunMonitorInputsBuilder builder() {
        return new RunMonitorInputsBuilder();
    }

    @NotNull
    public String getFullPathToMonitor() {
        return fullPathToMonitor;
    }

    @NotNull
    public String getMonitorId() {
        return monitorId;
    }

    @NotNull
    public String getDelimiter() {
        return delimiter;
    }

    @NotNull
    public String getIdentifier() {
        return identifier;
    }

    @NotNull
    public String getExecutionTimeout() {
        return executionTimeout;
    }

    @NotNull
    public SiteScopeCommonInputs getCommonInputs() {
        return this.commonInputs;
    }

    public static class RunMonitorInputsBuilder {
        private String fullPathToMonitor = EMPTY;
        private String monitorId = EMPTY;
        private String delimiter = EMPTY;
        private String identifier = EMPTY;
        private String executionTimeout = EMPTY;
        private SiteScopeCommonInputs commonInputs;

        public RunMonitorInputsBuilder() {

        }

        @NotNull
        public RunMonitorInputsBuilder fullPathToMonitor(@NotNull final String fullPathToMonitor) {
            this.fullPathToMonitor = fullPathToMonitor;
            return this;
        }

        @NotNull
        public RunMonitorInputsBuilder monitorId(@NotNull final String monitorId) {
            this.monitorId = monitorId;
            return this;
        }

        @NotNull
        public RunMonitorInputsBuilder delimiter(@NotNull final String delimiter) {
            this.delimiter = delimiter;
            return this;
        }

        @NotNull
        public RunMonitorInputsBuilder identifier(@NotNull final String identifier) {
            this.identifier = identifier;
            return this;
        }

        @NotNull
        public RunMonitorInputsBuilder executionTimeout(@NotNull final String executionTimeout) {
            this.executionTimeout = executionTimeout;
            return this;
        }

        @NotNull
        public RunMonitorInputsBuilder commonInputs(@NotNull final SiteScopeCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public RunMonitorInputs build() {
            return new RunMonitorInputs(fullPathToMonitor, monitorId, delimiter, identifier, executionTimeout, commonInputs);
        }
    }
}

