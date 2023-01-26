/*
 * (c) Copyright 2022 Micro Focus, L.P.
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

package io.cloudslang.content.nutanix.prism.entities;

import org.jetbrains.annotations.NotNull;

import java.beans.ConstructorProperties;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class NutanixGetTaskDetailsInputs {

    private final String taskUUID;
    private final String includeSubtasksInfo;
    private final NutanixCommonInputs commonInputs;

    @ConstructorProperties({"taskUUID", "includeSubtasksInfo", "commonInputs"})
    public NutanixGetTaskDetailsInputs(String taskUUID, String includeSubtasksInfo, NutanixCommonInputs commonInputs) {
        this.taskUUID = taskUUID;
        this.includeSubtasksInfo = includeSubtasksInfo;
        this.commonInputs = commonInputs;
    }

    public static NutanixGetTaskDetailsInputsBuilder builder() {
        return new NutanixGetTaskDetailsInputsBuilder();
    }

    @NotNull
    public String getTaskUUID() {
        return taskUUID;
    }

    @NotNull
    public String getIncludeSubtasksInfo() {
        return includeSubtasksInfo;
    }

    @NotNull
    public NutanixCommonInputs getCommonInputs() {
        return commonInputs;
    }

    public static class NutanixGetTaskDetailsInputsBuilder {
        private String taskUUID = EMPTY;
        private String includeSubtasksInfo = EMPTY;
        private NutanixCommonInputs commonInputs;

        NutanixGetTaskDetailsInputsBuilder() {
        }

        @NotNull
        public NutanixGetTaskDetailsInputsBuilder taskUUID(@NotNull final String taskUUID) {
            this.taskUUID = taskUUID;
            return this;
        }

        @NotNull
        public NutanixGetTaskDetailsInputsBuilder includeSubtasksInfo
                (@NotNull final String includeSubtasksInfo) {
            this.includeSubtasksInfo = includeSubtasksInfo;
            return this;
        }

        @NotNull
        public NutanixGetTaskDetailsInputsBuilder commonInputs
                (@NotNull final NutanixCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public NutanixGetTaskDetailsInputs build() {
            return new NutanixGetTaskDetailsInputs(taskUUID, includeSubtasksInfo, commonInputs);
        }
    }
}
