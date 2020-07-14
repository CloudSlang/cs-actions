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
package io.cloudslang.content.abbyy.entities.responses;

import io.cloudslang.content.abbyy.constants.ExceptionMsgs;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AbbyyResponse {
    private String taskId;
    private int credits;
    private TaskStatus taskStatus;
    private String errorMessage;
    private List<String> resultUrls;
    private long estimatedProcessingTime;


    private AbbyyResponse() {
    }


    public @NotNull String getTaskId() {
        return taskId;
    }


    public int getCredits() {
        return credits;
    }


    public @NotNull TaskStatus getTaskStatus() {
        return taskStatus;
    }


    public @Nullable String getErrorMessage() {
        return errorMessage;
    }


    public @NotNull List<String> getResultUrls() {
        return resultUrls;
    }


    public long getEstimatedProcessingTime() {
        return estimatedProcessingTime;
    }


    public enum TaskStatus {
        UNKNOWN("Unknown"),
        SUBMITTED("Submitted"),
        QUEUED("Queued"),
        IN_PROGRESS("InProgress"),
        COMPLETED("Completed"),
        PROCESSING_FAILED("ProcessingFailed"),
        DELETED("Deleted"),
        NOT_ENOUGH_CREDITS("NotEnoughCredits");

        private final String str;


        TaskStatus(String str) {
            this.str = str;
        }


        @Override
        public String toString() {
            return this.str;
        }
    }


    public static class Builder {
        private String taskId;
        private String credits;
        private String taskStatus;
        private String errorMessage;
        private String resultUrl;
        private String resultUrl2;
        private String resultUrl3;
        private String estimatedProcessingTime;


        public Builder taskId(String id) {
            this.taskId = id;
            return this;
        }


        public Builder credits(String credits) {
            this.credits = credits;
            return this;
        }


        public Builder taskStatus(String status) {
            this.taskStatus = status;
            return this;
        }


        public Builder errorMessage(String error) {
            this.errorMessage = error;
            return this;
        }


        public Builder resultUrl(String resultUrl) {
            this.resultUrl = resultUrl;
            return this;
        }


        public Builder resultUrl2(String resultUrl2) {
            this.resultUrl2 = resultUrl2;
            return this;
        }


        public Builder resultUrl3(String resultUrl3) {
            this.resultUrl3 = resultUrl3;
            return this;
        }


        public Builder estimatedProcessingTime(String millis) {
            this.estimatedProcessingTime = millis;
            return this;
        }


        public AbbyyResponse build() {
            AbbyyResponse response = new AbbyyResponse();

            response.taskId = this.taskId;

            try {
                response.credits = Integer.parseInt(this.credits);
            } catch (Exception ex) {
                throw new IllegalArgumentException(ExceptionMsgs.INVALID_CREDITS);
            }

            boolean found = false;
            for (TaskStatus status : TaskStatus.values()) {
                if (status.str.equals(this.taskStatus)) {
                    response.taskStatus = status;
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new IllegalArgumentException(String.format(ExceptionMsgs.UNEXPECTED_TASK_STATUS, this.taskStatus));
            }

            response.errorMessage = this.errorMessage;

            response.resultUrls = new ArrayList<>();
            if (StringUtils.isNotEmpty(this.resultUrl)) {
                response.resultUrls.add(this.resultUrl);
            }
            if (StringUtils.isNotEmpty(this.resultUrl2)) {
                response.resultUrls.add(this.resultUrl2);
            }
            if (StringUtils.isNotEmpty(this.resultUrl3)) {
                response.resultUrls.add(this.resultUrl3);
            }
            response.resultUrls = Collections.unmodifiableList(response.resultUrls);

            if (StringUtils.isNotEmpty(this.estimatedProcessingTime)) {
                try {
                    response.estimatedProcessingTime = Long.parseLong(this.estimatedProcessingTime);
                } catch (Exception ex) {
                    throw new IllegalArgumentException(ExceptionMsgs.INVALID_ESTIMATED_PROCESSING_TIME);
                }
            }

            return response;
        }
    }
}
