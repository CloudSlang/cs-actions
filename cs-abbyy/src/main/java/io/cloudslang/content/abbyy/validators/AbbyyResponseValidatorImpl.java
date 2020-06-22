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
package io.cloudslang.content.abbyy.validators;

import io.cloudslang.content.abbyy.constants.ExceptionMsgs;
import io.cloudslang.content.abbyy.entities.responses.AbbyyResponse;
import io.cloudslang.content.abbyy.exceptions.ValidationException;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public class AbbyyResponseValidatorImpl implements AbbyyResponseValidator {

    public ValidationException validate(@NotNull AbbyyResponse response) {
        try {
            validateTaskId(response);
            validateCredits(response);
            validateTaskStatus(response);
            validateResultUrls(response);
            validateEstimatedProcessingTime(response);
            return null;
        } catch (ValidationException ex) {
            return ex;
        }
    }


    private void validateTaskId(@NotNull AbbyyResponse response) throws ValidationException {
        if (StringUtils.isEmpty(response.getTaskId())) {
            throw new ValidationException(ExceptionMsgs.EMPTY_ID);
        }
    }


    private void validateCredits(@NotNull AbbyyResponse response) throws ValidationException {
        if (response.getCredits() < 0) {
            throw new ValidationException(ExceptionMsgs.INVALID_CREDITS);
        }
    }


    private void validateTaskStatus(@NotNull AbbyyResponse response) throws ValidationException {
        if (response.getTaskStatus() == null) {
            throw new ValidationException(ExceptionMsgs.UNEXPECTED_TASK_STATUS);
        }
    }


    private void validateResultUrls(@NotNull AbbyyResponse response) throws ValidationException {
        if (response.getTaskStatus() == AbbyyResponse.TaskStatus.COMPLETED && response.getResultUrls().isEmpty()) {
            throw new ValidationException(ExceptionMsgs.MISSING_RESULT_URLS);
        }

        for (String resultUrl : response.getResultUrls()) {
            if (StringUtils.isBlank(resultUrl)) {
                throw new ValidationException(ExceptionMsgs.ILLEGAL_RESULT_URL);
            }
        }
    }


    private void validateEstimatedProcessingTime(@NotNull AbbyyResponse response) throws ValidationException {
        if (response.getEstimatedProcessingTime() < 0) {
            throw new ValidationException(ExceptionMsgs.INVALID_ESTIMATED_PROCESSING_TIME);
        }
    }
}
