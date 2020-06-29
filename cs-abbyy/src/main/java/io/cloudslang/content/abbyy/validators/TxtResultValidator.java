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
import io.cloudslang.content.abbyy.constants.Limits;
import io.cloudslang.content.abbyy.entities.inputs.AbbyyInput;
import io.cloudslang.content.abbyy.entities.others.ExportFormat;
import io.cloudslang.content.abbyy.exceptions.ValidationException;
import io.cloudslang.content.abbyy.http.AbbyyApi;
import org.jetbrains.annotations.NotNull;

public class TxtResultValidator implements AbbyyResultValidator {

    private final AbbyyApi abbyyApi;


    public TxtResultValidator(@NotNull AbbyyApi abbyyApi) {
        this.abbyyApi = abbyyApi;
    }


    public ValidationException validateBeforeDownload(@NotNull AbbyyInput abbyyInput, @NotNull String downloadUrl) throws Exception {
        try {
            validateSizeBeforeDownload(abbyyInput, downloadUrl);
            return null;
        } catch (ValidationException ex) {
            return ex;
        }
    }


    public ValidationException validateAfterDownload(@NotNull AbbyyInput abbyyInput, @NotNull String result) throws Exception {
        try {
            validateSizeAfterDownload(abbyyInput, result);
            return null;
        } catch (ValidationException ex) {
            return ex;
        }
    }


    private void validateSizeBeforeDownload(AbbyyInput abbyyInput, String downloadUrl) throws Exception {
        if (abbyyInput.isDisableSizeLimit()) {
            return;
        }

        long txtSize = this.abbyyApi.getResultSize(abbyyInput, downloadUrl, ExportFormat.TXT);
        if (txtSize > Limits.MAX_SIZE_OF_TXT_FILE) {
            throw new ValidationException(String.format(ExceptionMsgs.MAX_SIZE_OF_TXT_RESULT_EXCEEDED, ExportFormat.TXT));
        }
    }


    private void validateSizeAfterDownload(AbbyyInput abbyyInput, String result) throws Exception {
        if (abbyyInput.isDisableSizeLimit()) {
            return;
        }

        if (result.getBytes().length > Limits.MAX_SIZE_OF_TXT_FILE) {
            throw new ValidationException(String.format(ExceptionMsgs.MAX_SIZE_OF_TXT_RESULT_EXCEEDED, ExportFormat.TXT));
        }
    }
}
