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
package io.cloudslang.content.abby.entities;

import io.cloudslang.content.abby.constants.ExceptionMsgs;

public class AbbyyResponse {
    private String taskId;
    private int taskCredits;
    private TaskStatus taskStatus;
    private String taskError;
    private String taskResultUrl;
    private String taskResultUrl2;
    private String taskResultUrl3;
    private long taskEstimatedProcessingTime;

    private AbbyyResponse() {
    }


    public String getTaskId() {
        return taskId;
    }


    public int getTaskCredits() {
        return taskCredits;
    }


    public TaskStatus getTaskStatus() {
        return taskStatus;
    }


    public String getTaskError() {
        return taskError;
    }


    public String getTaskResultUrl() {
        return taskResultUrl;
    }


    public String getTaskResultUrl2() {
        return taskResultUrl2;
    }


    public String getTaskResultUrl3() {
        return taskResultUrl3;
    }


    public long getTaskEstimatedProcessingTime() {
        return taskEstimatedProcessingTime;
    }


    public static class Builder {
        private String taskId;
        private int taskCredits;
        private TaskStatus taskStatus;
        private String taskError;
        private String taskResultUrl;
        private String taskResultUrl2;
        private String taskResultUrl3;
        private long taskEstimatedProcessingTime;


        public Builder taskId(String id) {
            this.taskId = id;
            return this;
        }


        public Builder taskCredits(int credits) {
            this.taskCredits = credits;
            return this;
        }


        public Builder taskStatus(TaskStatus status) {
            this.taskStatus = status;
            return this;
        }


        public Builder taskError(String error) {
            this.taskError = error;
            return this;
        }


        public Builder taskResultUrl(String resultUrl) {
            this.taskResultUrl = resultUrl;
            return this;
        }


        public Builder taskResultUrl2(String resultUrl2) {
            this.taskResultUrl2 = resultUrl2;
            return this;
        }


        public Builder taskResultUrl3(String resultUrl3) {
            this.taskResultUrl3 = resultUrl3;
            return this;
        }


        public Builder taskEstimatedProcessingTime(long millis) {
            this.taskEstimatedProcessingTime = millis;
            return this;
        }


        public AbbyyResponse build() {
            AbbyyResponse response = new AbbyyResponse();

            response.taskId = this.taskId;
            response.taskCredits = this.taskCredits;
            response.taskStatus = this.taskStatus;
            response.taskError = this.taskError;
            response.taskResultUrl = this.taskResultUrl;
            response.taskResultUrl2 = this.taskResultUrl2;
            response.taskResultUrl3 = this.taskResultUrl3;
            response.taskEstimatedProcessingTime = this.taskEstimatedProcessingTime;

            return response;
        }
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


        public static TaskStatus fromString(String str) throws Exception {
            for (TaskStatus status : TaskStatus.values()) {
                if (status.str.equals(str)) {
                    return status;
                }
            }
            throw new IllegalArgumentException(String.format(ExceptionMsgs.UNKNOWN_TASK_STATUS, str));
        }
    }
}
