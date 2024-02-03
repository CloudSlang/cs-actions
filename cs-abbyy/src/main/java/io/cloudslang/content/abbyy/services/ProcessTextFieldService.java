/*
 * Copyright 2020-2024 Open Text
 * This program and the accompanying materials
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
import io.cloudslang.content.abbyy.constants.XsdSchemas;
import io.cloudslang.content.abbyy.entities.inputs.ProcessTextFieldInput;
import io.cloudslang.content.abbyy.entities.others.ExportFormat;
import io.cloudslang.content.abbyy.entities.responses.AbbyyResponse;
import io.cloudslang.content.abbyy.exceptions.AbbyySdkException;
import io.cloudslang.content.abbyy.exceptions.ValidationException;
import io.cloudslang.content.abbyy.http.AbbyyApi;
import io.cloudslang.content.abbyy.utils.EncodingUtils;
import io.cloudslang.content.abbyy.validators.AbbyyInputValidator;
import io.cloudslang.content.abbyy.validators.AbbyyResultValidator;
import io.cloudslang.content.abbyy.validators.ProcessTextFieldInputValidator;
import io.cloudslang.content.abbyy.validators.XmlResultValidator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

public class ProcessTextFieldService extends AbbyyService<ProcessTextFieldInput> {

    private final AbbyyResultValidator xmlResultValidator;


    public ProcessTextFieldService() throws ParserConfigurationException {
        this(new ProcessTextFieldInputValidator(), null, new AbbyyApi());
    }


    ProcessTextFieldService(@NotNull AbbyyInputValidator<ProcessTextFieldInput> requestValidator,
                            @Nullable AbbyyResultValidator xmlResultValidator,
                            @NotNull AbbyyApi abbyyApi) {
        super(requestValidator, abbyyApi);
        this.xmlResultValidator = xmlResultValidator != null ? xmlResultValidator :
                new XmlResultValidator(this.abbyyApi, XsdSchemas.PROCESS_TEXT_FIELD);
    }


    @Override
    protected void handleTaskCompleted(@NotNull ProcessTextFieldInput request, @NotNull AbbyyResponse response,
                                       @NotNull Map<String, String> results) throws Exception {
        super.handleTaskCompleted(request, response, results);

        String xml = getResult(request, response);

        results.put(OutputNames.XML_RESULT, EncodingUtils.escapePotentialMaliciousCharsInXml(xml));

        if (request.getDestinationFile() != null) {
            saveClearTextResultOnDisk(request, xml);
        }
    }


    private String getResult(ProcessTextFieldInput abbyyInput, AbbyyResponse abbyyPreviousResponse) throws Exception {
        ValidationException validationEx;

        if (abbyyPreviousResponse.getResultUrls().isEmpty()) {
            throw new AbbyySdkException(ExceptionMsgs.EXPORT_FORMAT_AND_RESULT_URLS_DO_NOT_MATCH);
        }
        String resultUrl = abbyyPreviousResponse.getResultUrls().get(0);

        validationEx = this.xmlResultValidator.validateBeforeDownload(abbyyInput, resultUrl);
        if (validationEx != null) {
            throw validationEx;
        }

        String result = this.abbyyApi.getResult(abbyyInput, resultUrl, ExportFormat.XML, null, true);

        validationEx = this.xmlResultValidator.validateAfterDownload(abbyyInput, result);
        if (validationEx != null) {
            throw validationEx;
        }

        return result;
    }


    private void saveClearTextResultOnDisk(ProcessTextFieldInput request, String clearText) throws IOException {
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(request.getDestinationFile().toFile()),
                request.getResponseCharacterSet())) {
            writer.write(clearText);
        }
    }

}
