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
import io.cloudslang.content.abbyy.entities.ExportFormat;
import io.cloudslang.content.abbyy.exceptions.ValidationException;
import io.cloudslang.content.abbyy.http.AbbyyApi;
import io.cloudslang.content.abbyy.http.AbbyyRequest;
import org.jetbrains.annotations.NotNull;

public class PdfResultValidator extends AbbyyResultValidator {

    private final AbbyyApi abbyyApi;


    public PdfResultValidator(@NotNull AbbyyApi abbyyApi) {
        this.abbyyApi = abbyyApi;
    }


    @Override
    void validateBefore(@NotNull AbbyyRequest abbyyInitialRequest, @NotNull String url) throws Exception {
        if (this.abbyyApi.getResultSize(abbyyInitialRequest, url, ExportFormat.PDF_SEARCHABLE) > Limits.MAX_SIZE_OF_RESULT) {
            throw new ValidationException(String.format(ExceptionMsgs.MAX_SIZE_OF_RESULT_EXCEEDED, ExportFormat.PDF_SEARCHABLE));
        }

        String first4Chars = this.abbyyApi.getResultChunk(abbyyInitialRequest, url, ExportFormat.PDF_SEARCHABLE, 0, 3);
        if (!first4Chars.equals("%PDF")) {
            throw new ValidationException(ExceptionMsgs.INVALID_TARGET_PDF);
        }
    }


    @Override
    void validateAfter(@NotNull String result) throws Exception {
    }
}
