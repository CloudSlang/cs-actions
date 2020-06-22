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
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Paths;

public class PdfResultValidator implements AbbyyResultValidator {

    private final AbbyyApi abbyyApi;


    public PdfResultValidator(@NotNull AbbyyApi abbyyApi) {
        this.abbyyApi = abbyyApi;
    }


    public ValidationException validateBeforeDownload(@NotNull AbbyyInput abbyyInput, @NotNull String downloadUrl) throws Exception {
        try {
            validateSize(abbyyInput, downloadUrl);
            validateFileType(abbyyInput, downloadUrl);
            return null;
        } catch (ValidationException ex) {
            return ex;
        }
    }


    public ValidationException validateAfterDownload(@NotNull String targetPath) throws Exception {
        try {
            validateSize(targetPath);
            return null;
        } catch (ValidationException ex) {
            return ex;
        }
    }


    private void validateSize(AbbyyInput abbyyInput, String downloadUrl) throws Exception {
        long pdfSize = this.abbyyApi.getResultSize(abbyyInput, downloadUrl, ExportFormat.PDF_SEARCHABLE);
        if (!abbyyInput.getDisableSizeLimit() && pdfSize > Limits.MAX_SIZE_OF_PDF_FILE) {
            throw new ValidationException(String.format(ExceptionMsgs.MAX_SIZE_OF_PDF_RESULT_EXCEEDED, ExportFormat.PDF_SEARCHABLE));
        }
    }


    private void validateSize(@NotNull String targetPath) throws Exception {
        if (Files.size(Paths.get(targetPath)) > Limits.MAX_SIZE_OF_PDF_FILE) {
            throw new ValidationException(String.format(ExceptionMsgs.MAX_SIZE_OF_PDF_RESULT_EXCEEDED, ExportFormat.PDF_SEARCHABLE));
        }
    }


    private void validateFileType(@NotNull AbbyyInput abbyyInitialRequest, @NotNull String url) throws Exception {
        String first4Chars = this.abbyyApi.getResultChunk(abbyyInitialRequest, url, ExportFormat.PDF_SEARCHABLE, 0, 3);
        if (!StringUtils.equals(first4Chars, "%PDF")) {
            throw new ValidationException(ExceptionMsgs.INVALID_TARGET_PDF);
        }
    }
}
