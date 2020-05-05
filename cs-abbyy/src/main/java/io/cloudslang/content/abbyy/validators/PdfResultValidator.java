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
import io.cloudslang.content.abbyy.constants.MiscConstants;
import io.cloudslang.content.abbyy.entities.ExportFormat;
import io.cloudslang.content.abbyy.exceptions.AbbyySdkException;
import io.cloudslang.content.abbyy.exceptions.ValidationException;
import io.cloudslang.content.abbyy.http.AbbyyAPI;
import io.cloudslang.content.abbyy.http.AbbyyRequest;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Arrays;

public class PdfResultValidator implements AbbyyResultValidator {

    private final AbbyyAPI abbyyApi;

    public PdfResultValidator(AbbyyAPI abbyyApi) {
        this.abbyyApi = abbyyApi;
    }

    @Override
    public ValidationException validateBeforeDownload(AbbyyRequest abbyyInitialRequest, String url) throws IOException, AbbyySdkException {
        if (abbyyInitialRequest == null) {
            throw new IllegalArgumentException(String.format(ExceptionMsgs.NULL_ARGUMENT, "abbyyInitialRequest"));
        }
        if (url == null) {
            throw new IllegalArgumentException(String.format(ExceptionMsgs.NULL_ARGUMENT, "url"));
        }

        if (this.abbyyApi.getResultSize(abbyyInitialRequest, url, ExportFormat.PDF_SEARCHABLE) > MiscConstants.MAX_SIZE) {
            return new ValidationException(String.format(ExceptionMsgs.MAX_SIZE_EXCEEDED, ExportFormat.PDF_SEARCHABLE));
        }

        String first4Chars = this.abbyyApi.getResultChunk(abbyyInitialRequest, url, ExportFormat.PDF_SEARCHABLE, 0, 3);
        if (!first4Chars.equals("%PDF")) {
            return new ValidationException(ExceptionMsgs.INVALID_TARGET_PDF);
        }

        return null;
    }


    @Override
    public ValidationException validateAfterDownload(String result) throws IOException, SAXException {
        return null;
    }
}
