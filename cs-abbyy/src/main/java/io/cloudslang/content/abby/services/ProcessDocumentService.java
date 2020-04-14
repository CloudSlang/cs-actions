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
import io.cloudslang.content.abby.exceptions.AbbyySdkException;
import io.cloudslang.content.abby.utils.AbbyyResponseParser;
import io.cloudslang.content.abby.utils.AbbyyResultValidator;
import io.cloudslang.content.abby.utils.JavaxXmlValidatorAdapter;
import io.cloudslang.content.httpclient.actions.HttpClientAction;
import org.apache.commons.io.FilenameUtils;
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

public class ProcessDocumentService extends AbstractPostRequestService<ProcessDocumentInput> {

    private AbbyyResultValidator resultValidator;


    public ProcessDocumentService() throws ParserConfigurationException, SAXException {
        this.resultValidator = new JavaxXmlValidatorAdapter(MiscConstants.ABBYY_XML_RESULT_XSD_SCHEMA_PATH);
    }


    public ProcessDocumentService(AbbyyResponseParser responseParser, HttpClientAction httpClientAction,
                                  AbbyyResultValidator resultValidator) throws ParserConfigurationException, SAXException {
        super(responseParser, httpClientAction);
        this.resultValidator = (resultValidator != null) ?
                resultValidator :
                new JavaxXmlValidatorAdapter(MiscConstants.ABBYY_XML_RESULT_XSD_SCHEMA_PATH);
    }


    @Override
    protected String buildUrl(ProcessDocumentInput input) throws Exception {
        URIBuilder urlBuilder = new URIBuilder()
                .setScheme(ConnectionConstants.PROTOCOL)
                .setHost(String.format(ConnectionConstants.HOST_TEMPLATE, input.getLocationId().toString(),
                        ConnectionConstants.Endpoints.PROCESS_IMAGE));

        if (!input.getLanguages().isEmpty()) {
            urlBuilder.addParameter(ConnectionConstants.QueryParams.LANGUAGE, StringUtils.join(input.getLanguages(), ','));
        }

        if (input.getProfile() != null) {
            urlBuilder.addParameter(ConnectionConstants.QueryParams.PROFILE, input.getProfile().toString());
        }

        if (!input.getTextTypes().isEmpty()) {
            urlBuilder.addParameter(ConnectionConstants.QueryParams.TEXT_TYPE, StringUtils.join(input.getTextTypes(), ','));
        }

        if (input.getImageSource() != null) {
            urlBuilder.addParameter(ConnectionConstants.QueryParams.IMAGE_SOURCE, input.getImageSource().toString());
        }

        urlBuilder.addParameter(ConnectionConstants.QueryParams.CORRECT_ORIENTATION, String.valueOf(input.isCorrectOrientation()));

        urlBuilder.addParameter(ConnectionConstants.QueryParams.CORRECT_SKEW, String.valueOf(input.isCorrectSkew()));

        urlBuilder.addParameter(ConnectionConstants.QueryParams.READ_BARCODES, String.valueOf(input.isReadBarcodes()));

        if (!input.getExportFormats().isEmpty()) {
            urlBuilder.addParameter(ConnectionConstants.QueryParams.EXPORT_FORMAT, StringUtils.join(input.getExportFormats(), ','));
        }

        if (StringUtils.isNotEmpty(input.getDescription())) {
            urlBuilder.addParameter(ConnectionConstants.QueryParams.DESCRIPTION, input.getDescription());
        }

        if (StringUtils.isNotEmpty(input.getPdfPassword())) {
            urlBuilder.addParameter(ConnectionConstants.QueryParams.PDF_PASSWORD, input.getPdfPassword());
        }

        if (input.getExportFormats().contains(ExportFormat.XML)) {
            urlBuilder.addParameter(ConnectionConstants.QueryParams.WRITE_FORMATTING, String.valueOf(input.isWriteFormatting()))
                    .addParameter(ConnectionConstants.QueryParams.WRITE_RECOGNITION_VARIANTS, String.valueOf(input.isWriteRecognitionVariants()));
        }

        if (input.getExportFormats().contains(ExportFormat.PDF_SEARCHABLE)) {
            urlBuilder.addParameter(ConnectionConstants.QueryParams.WRITE_TAGS, input.getWriteTags().toString());
        }

        return urlBuilder.build().toString();
    }


    @Override
    protected void handleTaskCompleted(ProcessDocumentInput request, AbbyyResponse response, Map<String, String> results) throws Exception {
        super.handleTaskCompleted(request, response, results);

        for (ExportFormat exportFormat : request.getExportFormats()) {
            switch (exportFormat) {
                case TXT:
                    String txt = getProcessingResult(request, response, exportFormat);
                    results.put(OutputNames.TXT_RESULT, txt);
                    if (request.getDestinationFile() != null) {
                        saveClearTextResultOnDisk(request, txt, exportFormat);
                    }
                    break;
                case XML:
                    String xml = getProcessingResult(request, response, exportFormat);
                    AbbyySdkException validationEx = resultValidator.validate(xml);
                    if (validationEx != null) {
                        throw validationEx;
                    }
                    results.put(OutputNames.XML_RESULT, xml);
                    if (request.getDestinationFile() != null) {
                        saveClearTextResultOnDisk(request, xml, exportFormat);
                    }
                    break;
                case PDF_SEARCHABLE:
                    int indexOfPdfUrl = request.getExportFormats().indexOf(exportFormat);
                    String pdfUrl = response.getResultUrls().get(indexOfPdfUrl);
                    results.put(OutputNames.PDF_URL, pdfUrl);
                    if (request.getDestinationFile() != null) {
                        downloadFromUrl(request, pdfUrl, exportFormat);
                    }
                    break;
            }
        }
    }


    private String getProcessingResult(ProcessDocumentInput request, AbbyyResponse response, ExportFormat exportFormat) throws AbbyySdkException {
        int indexOfResultUrl = request.getExportFormats().indexOf(exportFormat);
        Map<String, String> processingResult = this.httpClientAction.execute(
                response.getResultUrls().get(indexOfResultUrl),
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
            throw new AbbyySdkException(String.format(ExceptionMsgs.PROCESSING_RESULT_COULD_NOT_BE_RETRIEVED, exportFormat));
        }

        return processingResult.get(MiscConstants.HTTP_RETURN_RESULT_OUTPUT);
    }


    private void saveClearTextResultOnDisk(ProcessDocumentInput request, String clearText, ExportFormat exportFormat) throws IOException {
        String targetPath = getTargetPath(request, exportFormat);
        try (FileWriter writer = new FileWriter(targetPath)) {
            writer.write(clearText);
        }
    }


    private String getTargetPath(ProcessDocumentInput request, ExportFormat exportFormat) {
        return request.getDestinationFile().getAbsolutePath() + "/" +
                FilenameUtils.removeExtension(request.getSourceFile().getName()) + "." +
                exportFormat.getFileExtension();
    }


    private void downloadFromUrl(ProcessDocumentInput request, String url, ExportFormat exportFormat) throws IOException {
        String targetPath = getTargetPath(request, exportFormat);
        try (InputStream in = new URL(url).openStream()) {
            Files.copy(in, Paths.get(targetPath), StandardCopyOption.REPLACE_EXISTING);
        }
    }
}