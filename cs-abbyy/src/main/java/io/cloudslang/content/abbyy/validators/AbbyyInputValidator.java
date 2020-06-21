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
import io.cloudslang.content.abbyy.constants.InputNames;
import io.cloudslang.content.abbyy.entities.inputs.AbbyyInput;
import io.cloudslang.content.abbyy.exceptions.ValidationException;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public abstract class AbbyyInputValidator<R extends AbbyyInput> {
    public ValidationException validate(@NotNull R request) {
        try {
            validateLocationId(request);
            validateApplicationId(request);
            validatePassword(request);
            validateProxyPort(request);
            validateDestinationFile(request);
            validateSourceFile(request);
            validateConnectTimeout(request);
            validateSocketTimeout(request);
            validateConnectionsMaxPerRoute(request);
            validateConnectionsMaxTotal(request);

            validateFurther(request);

            return null;
        } catch (ValidationException ex) {
            return ex;
        }
    }


    void validateLocationId(@NotNull R request) throws ValidationException {
        if (request.getLocationId() == null) {
            String msg = String.format(ExceptionMsgs.INVALID_VALUE_FOR_INPUT, null, InputNames.LOCATION_ID);
            throw new ValidationException(msg);
        }
    }


    void validateApplicationId(@NotNull R request) throws ValidationException {
        if (StringUtils.isBlank(request.getApplicationId())) {
            String msg = String.format(ExceptionMsgs.INVALID_VALUE_FOR_INPUT, request.getApplicationId(), InputNames.APPLICATION_ID);
            throw new ValidationException(msg);
        }
    }


    void validatePassword(@NotNull R request) throws ValidationException {
        if (StringUtils.isBlank(request.getPassword())) {
            String msg = String.format(ExceptionMsgs.INVALID_VALUE_FOR_INPUT, request.getApplicationId(), InputNames.PASSWORD);
            throw new ValidationException(msg);
        }
    }


    void validateProxyPort(@NotNull R request) throws ValidationException {
        if (request.getProxyPort() < 0 && request.getProxyPort() != -1) {
            String msg = String.format(ExceptionMsgs.INVALID_VALUE_FOR_INPUT, request.getProxyPort(), InputNames.PROXY_PORT);
            throw new ValidationException(msg);
        }
    }


    void validateConnectTimeout(@NotNull R request) throws ValidationException {
        if (request.getConnectTimeout() < 0) {
            String msg = String.format(ExceptionMsgs.INVALID_VALUE_FOR_INPUT, request.getConnectTimeout(), InputNames.CONNECT_TIMEOUT);
            throw new ValidationException(msg);
        }
    }


    void validateSocketTimeout(@NotNull R request) throws ValidationException {
        if (request.getSocketTimeout() < 0) {
            String msg = String.format(ExceptionMsgs.INVALID_VALUE_FOR_INPUT, request.getSocketTimeout(), InputNames.SOCKET_TIMEOUT);
            throw new ValidationException(msg);
        }
    }


    void validateConnectionsMaxPerRoute(@NotNull R request) throws ValidationException {
        if (request.getConnectionsMaxPerRoute() < 0) {
            String msg = String.format(ExceptionMsgs.INVALID_VALUE_FOR_INPUT, request.getConnectionsMaxPerRoute(),
                    InputNames.CONNECTIONS_MAX_PER_ROUTE);
            throw new ValidationException(msg);
        }
    }


    void validateConnectionsMaxTotal(@NotNull R request) throws ValidationException {
        if (request.getConnectionsMaxTotal() < 0) {
            String msg = String.format(ExceptionMsgs.INVALID_VALUE_FOR_INPUT, request.getConnectionsMaxTotal(),
                    InputNames.CONNECTIONS_MAX_TOTAL);
            throw new ValidationException(msg);
        }
    }


    void validateDestinationFile(@NotNull R request) throws ValidationException {
        if (request.getDestinationFile() == null) {
            return;
        }

        if (request.getDestinationFile().exists()) {
            throw new ValidationException(ExceptionMsgs.DESTINATION_FILE_ALREADY_EXISTS);
        }
        if (!request.getDestinationFile().getParentFile().exists()) {
            throw new ValidationException(ExceptionMsgs.DESTINATION_FOLDER_DOES_NOT_EXIST);
        }
    }


    void validateSourceFile(@NotNull R request) throws ValidationException {
        if (request.getSourceFile() == null) {
            throw new ValidationException(String.format(ExceptionMsgs.INVALID_VALUE_FOR_INPUT, null, InputNames.SOURCE_FILE));
        }

        if (!request.getSourceFile().exists()) {
            throw new ValidationException(ExceptionMsgs.SOURCE_FILE_DOES_NOT_EXIST);
        }
        if (!request.getSourceFile().isFile()) {
            throw new ValidationException(ExceptionMsgs.SOURCE_FILE_IS_NOT_FILE);
        }
    }


    abstract void validateFurther(@NotNull R request) throws ValidationException;

}
