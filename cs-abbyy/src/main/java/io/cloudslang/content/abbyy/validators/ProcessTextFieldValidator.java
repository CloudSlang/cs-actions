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
package io.cloudslang.content.abbyy.validators;

import io.cloudslang.content.abbyy.constants.ExceptionMsgs;
import io.cloudslang.content.abbyy.entities.ProcessTextFieldInput;
import io.cloudslang.content.abbyy.exceptions.ValidationException;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;

public class ProcessTextFieldValidator implements AbbyyRequestValidator<ProcessTextFieldInput> {

    @Override
    public ValidationException validate(ProcessTextFieldInput request) {
        if (request == null) {
            throw new IllegalArgumentException(String.format(ExceptionMsgs.NULL_ARGUMENT, "request"));
        }

        try {
            validateDestinationFile(request);
            validateSourceFile(request);
            return null;
        } catch (ValidationException ex) {
            return ex;
        }
    }


    private void validateDestinationFile(ProcessTextFieldInput request) throws ValidationException {
        if(request.getDestinationFile() == null) {
            return;
        }

        if(request.getDestinationFile().exists()) {
            throw new ValidationException(ExceptionMsgs.DESTINATION_FILE_ALREADY_EXISTS);
        }
        if(!request.getDestinationFile().getParentFile().exists()) {
            throw new ValidationException(ExceptionMsgs.DESTINATION_FOLDER_DOES_NOT_EXIST);
        }
    }


    private void validateSourceFile(ProcessTextFieldInput request) throws ValidationException {
        if (request.getSourceFile() == null) {
            throw new ValidationException(String.format(ExceptionMsgs.INVALID_VALUE_FOR_INPUT, null, HttpClientInputs.SOURCE_FILE));
        }

        if (!request.getSourceFile().exists()) {
            throw new ValidationException(ExceptionMsgs.SOURCE_FILE_DOES_NOT_EXIST);
        }
        if (!request.getSourceFile().isFile()) {
            throw new ValidationException(ExceptionMsgs.SOURCE_FILE_IS_NOT_FILE);
        }
    }
}
