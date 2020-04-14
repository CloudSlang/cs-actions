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
package io.cloudslang.content.abby.services;

import io.cloudslang.content.abby.constants.ConnectionConstants;
import io.cloudslang.content.abby.constants.ExceptionMsgs;
import io.cloudslang.content.abby.constants.MiscConstants;
import io.cloudslang.content.abby.constants.OutputNames;
import io.cloudslang.content.abby.entities.AbbyyResponse;
import io.cloudslang.content.abby.entities.ExportFormat;
import io.cloudslang.content.abby.entities.ProcessDocumentInput;
import io.cloudslang.content.abby.entities.ProcessTextFieldInput;
import io.cloudslang.content.abby.entities.Region;
import io.cloudslang.content.abby.exceptions.AbbyySdkException;
import io.cloudslang.content.abby.utils.AbbyyResponseParser;
import io.cloudslang.content.abby.utils.AbbyyResultValidator;
import io.cloudslang.content.abby.utils.JavaxXmlValidatorAdapter;
import io.cloudslang.content.abby.utils.ResultUtils;
import io.cloudslang.content.httpclient.actions.HttpClientAction;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;

public class ProcessTextFieldService extends AbstractPostRequestService<ProcessTextFieldInput>{

    private AbbyyResultValidator resultValidator;


    public ProcessTextFieldService() throws ParserConfigurationException, SAXException {
        this.resultValidator = new JavaxXmlValidatorAdapter(MiscConstants.ABBYY_XML_RESULT_XSD_SCHEMA_PATH);
    }


    ProcessTextFieldService(AbbyyResponseParser responseParser, HttpClientAction httpClientAction) throws ParserConfigurationException {
        super(responseParser, httpClientAction);
    }


    public ProcessTextFieldService(AbbyyResponseParser responseParser, HttpClientAction httpClientAction,
                                  AbbyyResultValidator resultValidator) throws ParserConfigurationException, SAXException {
        super(responseParser, httpClientAction);
        this.resultValidator = (resultValidator != null) ?
                resultValidator :
                new JavaxXmlValidatorAdapter(MiscConstants.ABBYY_XML_RESULT_XSD_SCHEMA_PATH);
    }


    @Override
    protected String buildUrl(ProcessTextFieldInput input) throws Exception {
        URIBuilder urlBuilder = new URIBuilder()
                .setScheme(ConnectionConstants.PROTOCOL)
                .setHost(String.format(ConnectionConstants.HOST_TEMPLATE, input.getLocationId().toString(),
                        ConnectionConstants.Endpoints.PROCESS_TEXT_FIELD))
                .addParameter(ConnectionConstants.QueryParams.REGION, input.getRegion().toString())
                .addParameter(ConnectionConstants.QueryParams.LANGUAGE, StringUtils.join(input.getLanguages(), ','))
                .addParameter(ConnectionConstants.QueryParams.LETTER_SET, input.getLetterSet())
                .addParameter(ConnectionConstants.QueryParams.REG_EXP, input.getRegExp())
                .addParameter(ConnectionConstants.QueryParams.TEXT_TYPE, input.getTextType().toString())
                .addParameter(ConnectionConstants.QueryParams.ONE_TEXT_LINE, String.valueOf(input.isOneTextLine()))
                .addParameter(ConnectionConstants.QueryParams.ONE_WORD_PER_TEXT_LINE, String.valueOf(input.isOneWordPerTextLine()))
                .addParameter(ConnectionConstants.QueryParams.MARKING_TYPE, input.getMarkingType().toString())
                .addParameter(ConnectionConstants.QueryParams.PLACEHOLDERS_COUNT, String.valueOf(input.getPlaceholdersCount()))
                .addParameter(ConnectionConstants.QueryParams.WRITING_STYLE, input.getWritingStyle().toString())
                .addParameter(ConnectionConstants.QueryParams.DESCRIPTION, input.getDescription())
                .addParameter(ConnectionConstants.QueryParams.PDF_PASSWORD, input.getPdfPassword());

        return urlBuilder.build().toString();
    }


    @Override
    protected void handleTaskCompleted(ProcessTextFieldInput request, AbbyyResponse response, Map<String, String> results) throws Exception {
        super.handleTaskCompleted(request, response, results);

        String xml = getProcessingResult(request, response);
        results.put(OutputNames.XML_RESULT, xml);
        if (request.getDestinationFile() != null) {
            saveClearTextResultOnDisk(request, xml);
        }
    }


    private String getProcessingResult(ProcessTextFieldInput request, AbbyyResponse response) throws AbbyySdkException {
        Map<String, String> processingResult = this.httpClientAction.execute(
                response.getResultUrls().get(0),
                null,
                null,
                "anonymous",
                String.valueOf(false),
                null,
                null,
                null,
                null,
                null,
                request.getProxyHost(),
                String.valueOf(request.getProxyPort()),
                request.getProxyUsername(),
                request.getProxyPassword(),
                String.valueOf(request.isTrustAllRoots()),
                request.getX509HostnameVerifier(),
                request.getTrustKeystore(),
                request.getTrustPassword(),
                null,
                null,
                String.valueOf(request.getConnectTimeout()),
                String.valueOf(request.getSocketTimeout()),
                String.valueOf(false),
                String.valueOf(request.isKeepAlive()),
                String.valueOf(request.getConnectionsMaxPerRoute()),
                String.valueOf(request.getConnectionsMaxTotal()),
                null,
                request.getResponseCharacterSet(),
                null,
                StringUtils.EMPTY,
                null,
                String.valueOf(true),
                String.valueOf(false),
                null,
                String.valueOf(true),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                String.valueOf(true),
                null,
                ConnectionConstants.HttpMethods.GET,
                null,
                null);

        this.lastStatusCode = processingResult.get(MiscConstants.HTTP_STATUS_CODE_OUTPUT);
        if (!processingResult.get(MiscConstants.HTTP_STATUS_CODE_OUTPUT).equals(String.valueOf(200))) {
            throw new AbbyySdkException(String.format(ExceptionMsgs.PROCESSING_RESULT_COULD_NOT_BE_RETRIEVED, "xml"));
        }

        return processingResult.get(MiscConstants.HTTP_RETURN_RESULT_OUTPUT);
    }


    private void saveClearTextResultOnDisk(ProcessTextFieldInput request, String clearText) throws IOException {
        try (FileWriter writer = new FileWriter(request.getDestinationFile())) {
            writer.write(clearText);
        }
    }

}
