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
package io.cloudslang.content.abbyy.entities;

import io.cloudslang.content.abbyy.constants.ExceptionMsgs;
import org.apache.commons.lang3.StringUtils;

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


    public String getTaskId() {
        return taskId;
    }


    public int getCredits() {
        return credits;
    }


    public TaskStatus getTaskStatus() {
        return taskStatus;
    }


    public String getErrorMessage() {
        return errorMessage;
    }


    public List<String> getResultUrls() {
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


        public static TaskStatus fromString(String str) throws Exception {
            for (TaskStatus status : TaskStatus.values()) {
                if (status.str.equals(str)) {
                    return status;
                }
            }
            throw new IllegalArgumentException(String.format(ExceptionMsgs.UNKNOWN_TASK_STATUS, str));
        }


        @Override
        public String toString() {
            return this.str;
        }
    }

    public static class Builder {
        private String taskId;
        private int credits;
        private TaskStatus taskStatus;
        private String errorMessage;
        private String resultUrl;
        private String resultUrl2;
        private String resultUrl3;
        private long estimatedProcessingTime;


        public Builder taskId(String id) {
            this.taskId = id;
            return this;
        }


        public Builder credits(int credits) {
            this.credits = credits;
            return this;
        }


        public Builder taskStatus(TaskStatus status) {
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


        public Builder estimatedProcessingTime(long millis) {
            this.estimatedProcessingTime = millis;
            return this;
        }


        public AbbyyResponse build() {
            AbbyyResponse response = new AbbyyResponse();

            response.taskId = this.taskId;

            response.credits = this.credits;

            response.taskStatus = this.taskStatus;

            response.errorMessage = this.errorMessage;

            response.resultUrls = new ArrayList<>();
            response.resultUrls.add(this.resultUrl);
            if (StringUtils.isNotEmpty(this.resultUrl2)) {
                response.resultUrls.add(this.resultUrl2);
            }
            if (StringUtils.isNotEmpty(this.resultUrl3)) {
                response.resultUrls.add(this.resultUrl3);
            }
            response.resultUrls = Collections.unmodifiableList(response.resultUrls);

            response.estimatedProcessingTime = this.estimatedProcessingTime;

            return response;
        }
    }
}
