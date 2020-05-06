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

public class TxtResultValidator extends AbbyyResultValidator {

    private final AbbyyApi abbyyApi;


    public TxtResultValidator(@NotNull AbbyyApi abbyyApi) {
        this.abbyyApi = abbyyApi;
    }


    @Override
    void validateBefore(@NotNull AbbyyRequest abbyyInitialRequest, @NotNull String url) throws Exception {
        if (this.abbyyApi.getResultSize(abbyyInitialRequest, url, ExportFormat.TXT) > Limits.MAX_SIZE_OF_RESULT) {
            throw new ValidationException(String.format(ExceptionMsgs.MAX_SIZE_OF_RESULT_EXCEEDED, ExportFormat.TXT));
        }
    }


    @Override
    void validateAfter(@NotNull String result) throws Exception {
    }
}
