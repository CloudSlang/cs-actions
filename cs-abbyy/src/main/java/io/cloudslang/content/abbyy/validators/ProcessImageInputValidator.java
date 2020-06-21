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
import io.cloudslang.content.abbyy.constants.Limits;
import io.cloudslang.content.abbyy.entities.inputs.ProcessImageInput;
import io.cloudslang.content.abbyy.entities.others.ExportFormat;
import io.cloudslang.content.abbyy.entities.others.TextType;
import io.cloudslang.content.abbyy.exceptions.ValidationException;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class ProcessImageInputValidator extends AbbyyInputValidator<ProcessImageInput> {


    @Override
    public void validateFurther(@NotNull ProcessImageInput request) throws ValidationException {
        validateExportFormats(request);
        validateLanguages(request);
        validateTextTypes(request);
        validateDescription(request);
    }


    @Override
    void validateDestinationFile(@NotNull ProcessImageInput request) throws ValidationException {
        if (request.getDestinationFile() == null) {
            return;
        }

        if (!request.getDestinationFile().exists()) {
            throw new ValidationException(ExceptionMsgs.DESTINATION_FOLDER_DOES_NOT_EXIST);
        }
        if (!request.getDestinationFile().isDirectory()) {
            throw new ValidationException(ExceptionMsgs.DESTINATION_FOLDER_IS_NOT_FOLDER);
        }
    }


    private void validateLanguages(@NotNull ProcessImageInput request) throws ValidationException {
        if (request.getLanguages() == null) {
            return;
        }

        for (String language : request.getLanguages()) {
            if (StringUtils.isBlank(language)) {
                String msg = String.format(ExceptionMsgs.INVALID_VALUE_DETECTED, language, InputNames.LANGUAGE);
                throw new ValidationException(msg);
            }
        }
    }


    private void validateTextTypes(@NotNull ProcessImageInput request) throws ValidationException {
        if (request.getTextTypes() == null) {
            return;
        }

        for (TextType textType : request.getTextTypes()) {
            if (textType == null) {
                String msg = String.format(ExceptionMsgs.INVALID_VALUE_DETECTED, null, InputNames.LANGUAGE);
                throw new ValidationException(msg);
            }
        }
    }


    private void validateExportFormats(@NotNull ProcessImageInput request) throws ValidationException {
        if (request.getExportFormats() == null || request.getExportFormats().isEmpty()) {
            throw new ValidationException(ExceptionMsgs.MISSING_EXPORT_FORMATS);
        }

        if (request.getExportFormats().size() > Limits.MAX_NR_OF_EXPORT_FORMATS) {
            throw new ValidationException(ExceptionMsgs.TOO_MANY_EXPORT_FORMATS);
        }

        Set<ExportFormat> distinctExportFormats = new HashSet<>(request.getExportFormats());
        if (distinctExportFormats.size() < request.getExportFormats().size()) {
            throw new ValidationException(ExceptionMsgs.DUPLICATED_EXPORT_FORMATS);
        }
    }


    private void validateDescription(@NotNull ProcessImageInput request) throws ValidationException {
        String a = request.getDescription();
        if (StringUtils.isEmpty(request.getDescription())) {
            return;
        }

        if (request.getDescription().length() > Limits.MAX_SIZE_OF_DESCR) {
            throw new ValidationException(ExceptionMsgs.MAX_SIZE_OF_DESCR_EXCEEDED);
        }
    }
}
