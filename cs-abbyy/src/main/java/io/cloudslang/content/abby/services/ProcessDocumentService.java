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
import io.cloudslang.content.abby.entities.AbbyyResponse;
import io.cloudslang.content.abby.entities.ExportFormat;
import io.cloudslang.content.abby.entities.ProcessDocumentInput;
import io.cloudslang.content.constants.OutputNames;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;

import javax.xml.parsers.ParserConfigurationException;
import java.util.Map;

public class ProcessDocumentService extends AbstractPostRequestService<ProcessDocumentInput> {

    public ProcessDocumentService() throws ParserConfigurationException {
    }


    @Override
    protected String buildUrl(ProcessDocumentInput input) throws Exception {
        URIBuilder urlBuilder = new URIBuilder()
                .setScheme(ConnectionConstants.PROTOCOL)
                .setHost(String.format(ConnectionConstants.HOST_TEMPLATE, input.getLocationId().toString(),
                        ConnectionConstants.Endpoints.PROCESS_IMAGE))
                .addParameter(ConnectionConstants.QueryParams.LANGUAGE, StringUtils.join(input.getLanguages(), ','))
                .addParameter(ConnectionConstants.QueryParams.PROFILE, input.getProfile().toString())
                .addParameter(ConnectionConstants.QueryParams.TEXT_TYPE, StringUtils.join(input.getTextTypes(), ','))
                .addParameter(ConnectionConstants.QueryParams.IMAGE_SOURCE, input.getImageSource().toString())
                .addParameter(ConnectionConstants.QueryParams.CORRECT_ORIENTATION, String.valueOf(input.isCorrectOrientation()))
                .addParameter(ConnectionConstants.QueryParams.CORRECT_SKEW, String.valueOf(input.isCorrectSkew()))
                .addParameter(ConnectionConstants.QueryParams.READ_BARCODES, String.valueOf(input.isReadBarcodes()))
                .addParameter(ConnectionConstants.QueryParams.EXPORT_FORMAT, StringUtils.join(input.getExportFormats(), ','))
                .addParameter(ConnectionConstants.QueryParams.DESCRIPTION, input.getDescription())
                .addParameter(ConnectionConstants.QueryParams.PDF_PASSWORD, input.getPdfPassword());

        if (input.getExportFormats().contains(ExportFormat.XML) ||
                input.getExportFormats().contains(ExportFormat.XML_FOR_CORRECTED_IMAGE)) {
            urlBuilder.addParameter(ConnectionConstants.QueryParams.WRITE_FORMATTING, String.valueOf(input.isWriteFormatting()))
                    .addParameter(ConnectionConstants.QueryParams.WRITE_RECOGNITION_VARIANTS, String.valueOf(input.isWriteRecognitionVariants()));
        }
        if (input.getExportFormats().contains(ExportFormat.PDF_A) ||
                input.getExportFormats().contains(ExportFormat.PDF_SEARCHABLE) ||
                input.getExportFormats().contains(ExportFormat.PDF_TEXT_AND_IMAGES)) {
            urlBuilder.addParameter(ConnectionConstants.QueryParams.WRITE_TAGS, input.getWriteTags().toString());

        }

        return urlBuilder.build().toString();
    }


    @Override
    protected void handleTaskCompleted(ProcessDocumentInput request, AbbyyResponse response, Map<String, String> results) throws Exception {
        super.handleTaskCompleted(request, response, results);

        Map<String, String> processingResult = null;
        if (request.getExportFormats().contains(ExportFormat.TXT)) {
            if (request.getExportFormats().get(0).equals(ExportFormat.TXT)) {
                processingResult = getProcessingResult(request, response.getTaskResultUrl());
            }
            if (request.getExportFormats().size() >= 2 && request.getExportFormats().get(1).equals(ExportFormat.TXT)) {
                processingResult = getProcessingResult(request, response.getTaskResultUrl2());
            }
            if (request.getExportFormats().size() >= 3 && request.getExportFormats().get(2).equals(ExportFormat.TXT)) {
                processingResult = getProcessingResult(request, response.getTaskResultUrl3());
            }

            this.lastStatusCode = processingResult.get(MiscConstants.HTTP_STATUS_CODE_OUTPUT);
            if (!processingResult.get(MiscConstants.HTTP_STATUS_CODE_OUTPUT).equals(String.valueOf(200))) {
                throw new Exception(ExceptionMsgs.PROCESSING_RESULT_COULD_NOT_BE_RETRIEVED);
            }

            results.put(OutputNames.RETURN_RESULT, processingResult.get(MiscConstants.HTTP_RETURN_RESULT_OUTPUT));
        }
    }


    private Map<String, String> getProcessingResult(ProcessDocumentInput request, String resultUrl) {
        return this.httpClientAction.execute(
                resultUrl,
                null,
                null,
                StringUtils.EMPTY,
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
    }
}