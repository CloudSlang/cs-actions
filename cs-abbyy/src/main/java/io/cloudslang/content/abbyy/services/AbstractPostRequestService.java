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

package io.cloudslang.content.abbyy.services;

import io.cloudslang.content.abbyy.constants.ExceptionMsgs;
import io.cloudslang.content.abbyy.constants.OutputNames;
import io.cloudslang.content.abbyy.constants.SuccessMsgs;
import io.cloudslang.content.abbyy.exceptions.*;
import io.cloudslang.content.abbyy.http.AbbyyApi;
import io.cloudslang.content.abbyy.http.AbbyyRequest;
import io.cloudslang.content.abbyy.http.AbbyyResponse;
import io.cloudslang.content.abbyy.utils.ResultUtils;
import io.cloudslang.content.abbyy.validators.AbbyyRequestValidator;
import io.cloudslang.content.constants.ReturnCodes;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public abstract class AbstractPostRequestService<R extends AbbyyRequest> {

    final AbbyyApi abbyyApi;
    private final AbbyyRequestValidator<R> requestValidator;
    boolean timedOut;


    AbstractPostRequestService(@NotNull AbbyyRequestValidator<R> requestValidator, @NotNull AbbyyApi abbyyApi) {
        this.requestValidator = requestValidator;
        this.abbyyApi = abbyyApi;
    }


    public Map<String, String> execute(@NotNull R request) throws Exception {
        Map<String, String> results = ResultUtils.createNewEmptyMap();

        try {
            ValidationException validationEx = this.requestValidator.validate(request);
            if (validationEx != null) {
                throw validationEx;
            }

            AbbyyResponse response = this.abbyyApi.postRequest(request, buildUrl(request));

            if (response.getTaskStatus() == AbbyyResponse.TaskStatus.NOT_ENOUGH_CREDITS) {
                throw new AbbyySdkException(String.format(ExceptionMsgs.NOT_ENOUGH_CREDITS, response.getCredits()));
            }
            results.put(OutputNames.TASK_ID, response.getTaskId());
            results.put(OutputNames.CREDITS, String.valueOf(response.getCredits()));

            response = waitTaskToFinish(request, response.getTaskId(), response.getEstimatedProcessingTime());

            switch (response.getTaskStatus()) {
                case COMPLETED:
                    handleTaskCompleted(request, response, results);
                    break;
                case PROCESSING_FAILED:
                    throw new ServerSideException(String.format(ExceptionMsgs.TASK_PROCESSING_FAILED, response.getErrorMessage()));
                case DELETED:
                    throw new AbbyySdkException(ExceptionMsgs.TASK_DELETED);
                case NOT_ENOUGH_CREDITS:
                    throw new AbbyySdkException(ExceptionMsgs.NOT_ENOUGH_CREDITS);
                default:
                    throw new ClientSideException(ExceptionMsgs.UNEXPECTED_STATUS);
            }

            return results;

        } catch (AbbyySdkException ex) {
            if (ex.getResultsMap() == null) {
                ex.setResultsMap(results);
            }
            throw ex;
        } catch (Exception ex) {
            throw new ClientSideException(ex, results);
        } finally {
            results.put(OutputNames.STATUS_CODE, StringUtils.defaultString(this.abbyyApi.getLastStatusCode()));
            results.put(OutputNames.TIMED_OUT, String.valueOf(this.timedOut));
        }
    }


    private AbbyyResponse waitTaskToFinish(@NotNull R request, @NotNull String taskId, long timeToWait) throws Exception {
        final int numberOfAttempts = 6;
        int crtAttemptNr = 0;
        final long minTimeToWait = 1000;
        AbbyyResponse response;

        do {
            crtAttemptNr++;
            if (crtAttemptNr == 1 || timeToWait < minTimeToWait) {
                Thread.sleep(minTimeToWait);
            } else {
                Thread.sleep(timeToWait);
            }

            response = this.abbyyApi.getTaskStatus(request, taskId);

        } while (!isTaskFinished(response.getTaskStatus()) && crtAttemptNr < numberOfAttempts);

        if (!isTaskFinished(response.getTaskStatus())) {
            this.timedOut = true;
            throw new TimeoutException(ExceptionMsgs.OPERATION_TIMEOUT);
        }

        return response;
    }


    private boolean isTaskFinished(@NotNull AbbyyResponse.TaskStatus status) {
        return status == AbbyyResponse.TaskStatus.COMPLETED ||
                status == AbbyyResponse.TaskStatus.PROCESSING_FAILED ||
                status == AbbyyResponse.TaskStatus.DELETED;
    }


    void handleTaskCompleted(@NotNull R request, @NotNull AbbyyResponse response, @NotNull Map<String, String> results) throws Exception {
        results.put(io.cloudslang.content.constants.OutputNames.RETURN_RESULT, SuccessMsgs.DOCUMENT_PROCESSED_SUCCESSFULLY);
        results.put(io.cloudslang.content.constants.OutputNames.RETURN_CODE, ReturnCodes.SUCCESS);
    }


    abstract String buildUrl(@NotNull R request) throws Exception;
}