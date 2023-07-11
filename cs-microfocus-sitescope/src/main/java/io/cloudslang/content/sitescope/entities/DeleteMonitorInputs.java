/*
 * Copyright 2020-2023 Open Text
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

public class DeleteMonitorInputs {
    private String fullPathToMonitor;
    private String delimiter;
    private SiteScopeCommonInputs commonInputs;

    private DeleteMonitorInputs(String fullPathToMonitor, String delimiter, SiteScopeCommonInputs commonInputs) {
        this.fullPathToMonitor = fullPathToMonitor;
        this.delimiter = delimiter;
        this.commonInputs = commonInputs;
    }

    @NotNull
    public static DeleteMonitorInputsBuilder builder() {
        return new DeleteMonitorInputsBuilder();
    }

    @NotNull
    public String getFullPathToMonitor() {
        return fullPathToMonitor;
    }

    @NotNull
    public String getDelimiter() {
        return delimiter;
    }

    @NotNull
    public SiteScopeCommonInputs getCommonInputs() {
        return this.commonInputs;
    }

    public static class DeleteMonitorInputsBuilder {
        private String fullPathToMonitor = EMPTY;
        private String delimiter = EMPTY;
        private SiteScopeCommonInputs commonInputs;

        public DeleteMonitorInputsBuilder() {

        }

        @NotNull
        public DeleteMonitorInputs.DeleteMonitorInputsBuilder fullPathToMonitor(@NotNull final String fullPathToMonitor) {
            this.fullPathToMonitor = fullPathToMonitor;
            return this;
        }

        @NotNull
        public DeleteMonitorInputs.DeleteMonitorInputsBuilder delimiter(@NotNull final String delimiter) {
            this.delimiter = delimiter;
            return this;
        }

        @NotNull
        public DeleteMonitorInputs.DeleteMonitorInputsBuilder commonInputs(@NotNull final SiteScopeCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public DeleteMonitorInputs build() {
            return new DeleteMonitorInputs(fullPathToMonitor, delimiter, commonInputs);
        }
    }
}
