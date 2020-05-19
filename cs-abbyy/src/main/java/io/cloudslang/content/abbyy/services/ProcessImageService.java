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

package io.cloudslang.content.abbyy.services;

import io.cloudslang.content.abbyy.constants.*;
import io.cloudslang.content.abbyy.entities.ExportFormat;
import io.cloudslang.content.abbyy.entities.ProcessImageInput;
import io.cloudslang.content.abbyy.exceptions.AbbyySdkException;
import io.cloudslang.content.abbyy.exceptions.HttpException;
import io.cloudslang.content.abbyy.exceptions.ValidationException;
import io.cloudslang.content.abbyy.http.AbbyyApi;
import io.cloudslang.content.abbyy.http.AbbyyApiImpl;
import io.cloudslang.content.abbyy.http.AbbyyResponse;
import io.cloudslang.content.abbyy.validators.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.util.HashMap;
import java.util.Map;

public class ProcessImageService extends AbstractPostRequestService<ProcessImageInput> {

    private final AbbyyResultValidator xmlResultValidator, txtResultValidator, pdfResultValidator;


    public ProcessImageService() throws ParserConfigurationException {
        this(new ProcessImageValidator(), null, null, null, new AbbyyApiImpl());
    }


    ProcessImageService(@NotNull AbbyyRequestValidator<ProcessImageInput> requestValidator,
                        @Nullable AbbyyResultValidator xmlResultValidator,
                        @Nullable AbbyyResultValidator txtResultValidator,
                        @Nullable AbbyyResultValidator pdfResultValidator,
                        @NotNull AbbyyApi abbyyApi) {
        super(requestValidator, abbyyApi);
        this.xmlResultValidator = xmlResultValidator != null ? xmlResultValidator : new XmlResultValidator(abbyyApi, XmlSchemas.PROCESS_IMAGE);
        this.txtResultValidator = txtResultValidator != null ? txtResultValidator : new TxtResultValidator(abbyyApi);
        this.pdfResultValidator = pdfResultValidator != null ? pdfResultValidator : new PdfResultValidator(abbyyApi);
    }


    @Override
    String buildUrl(@NotNull ProcessImageInput input) throws Exception {
        URIBuilder urlBuilder = new URIBuilder()
                .setScheme(input.getLocationId().getProtocol())
                .setHost(String.format(Urls.HOST_TEMPLATE, input.getLocationId().toString(),
                        Endpoints.PROCESS_IMAGE));

        if (input.getLanguages() != null && !input.getLanguages().isEmpty()) {
            urlBuilder.addParameter(QueryParams.LANGUAGE, StringUtils.join(input.getLanguages(), ','));
        }

        if (input.getProfile() != null) {
            urlBuilder.addParameter(QueryParams.PROFILE, input.getProfile().toString());
        }

        if (input.getTextTypes() != null && !input.getTextTypes().isEmpty()) {
            urlBuilder.addParameter(QueryParams.TEXT_TYPE, StringUtils.join(input.getTextTypes(), ','));
        }

        if (input.getImageSource() != null) {
            urlBuilder.addParameter(QueryParams.IMAGE_SOURCE, input.getImageSource().toString());
        }

        urlBuilder.addParameter(QueryParams.CORRECT_ORIENTATION, String.valueOf(input.isCorrectOrientation()));

        urlBuilder.addParameter(QueryParams.CORRECT_SKEW, String.valueOf(input.isCorrectSkew()));

        if (input.isReadBarcodes() != null) {
            urlBuilder.addParameter(QueryParams.READ_BARCODES, String.valueOf(input.isReadBarcodes()));
        }

        if (input.getExportFormats() != null && !input.getExportFormats().isEmpty()) {
            urlBuilder.addParameter(QueryParams.EXPORT_FORMAT, StringUtils.join(input.getExportFormats(), ','));
        }

        if (StringUtils.isNotEmpty(input.getDescription())) {
            urlBuilder.addParameter(QueryParams.DESCRIPTION, input.getDescription());
        }

        if (StringUtils.isNotEmpty(input.getPdfPassword())) {
            urlBuilder.addParameter(QueryParams.PDF_PASSWORD, input.getPdfPassword());
        }

        if (input.getExportFormats() != null && input.getExportFormats().contains(ExportFormat.XML)) {
            urlBuilder.addParameter(QueryParams.WRITE_FORMATTING, String.valueOf(input.isWriteFormatting()))
                    .addParameter(QueryParams.WRITE_RECOGNITION_VARIANTS, String.valueOf(input.isWriteRecognitionVariants()));
        }

        if (input.getExportFormats() != null && input.getExportFormats().contains(ExportFormat.PDF_SEARCHABLE)
                && input.getWriteTags() != null) {
            urlBuilder.addParameter(QueryParams.WRITE_TAGS, input.getWriteTags().toString());
        }

        return urlBuilder.build().toString();
    }


    @Override
    void handleTaskCompleted(@NotNull ProcessImageInput request, @NotNull AbbyyResponse response,
                             @NotNull Map<String, String> results) throws Exception {
        super.handleTaskCompleted(request, response, results);

        Map<ExportFormat, String> failure = new HashMap<>();
        String result;

        for (ExportFormat exportFormat : request.getExportFormats()) {
            try {
                switch (exportFormat) {
                    case XML:
                        result = getClearTextResult(request, response, ExportFormat.XML, xmlResultValidator);
                        if (result.startsWith(Misc.BOM_CHAR)) {
                            result = result.substring(1);
                        }
                        results.put(OutputNames.XML_RESULT, result);
                        break;
                    case TXT:
                        result = getClearTextResult(request, response, ExportFormat.TXT, txtResultValidator);
                        results.put(OutputNames.TXT_RESULT, result);
                        break;
                    case PDF_SEARCHABLE:
                        result = getBinaryResult(request, response, ExportFormat.PDF_SEARCHABLE, pdfResultValidator);
                        results.put(OutputNames.PDF_URL, result);
                        break;
                }
            } catch (Exception ex) {
                failure.put(exportFormat, ex.toString());
            }
        }

        if (!failure.isEmpty()) {
            StringBuilder errBuilder = new StringBuilder(ExceptionMsgs.ERROR_RETRIEVING_EXPORT_FORMATS);
            for (ExportFormat exportFormat : failure.keySet()) {
                errBuilder.append('\n')
                        .append("For '" + exportFormat.toString() + "': " + failure.get(exportFormat));
            }
            throw new AbbyySdkException(errBuilder.toString());
        }
    }


    private String getClearTextResult(@NotNull ProcessImageInput abbyyInitialRequest, @NotNull AbbyyResponse abbyyPreviousResponse,
                                      @NotNull ExportFormat exportFormat, @NotNull AbbyyResultValidator resultValidator) throws Exception {
        ValidationException validationEx;

        if (abbyyInitialRequest.getExportFormats().size() != abbyyPreviousResponse.getResultUrls().size()) {
            throw new AbbyySdkException(ExceptionMsgs.EXPORT_FORMAT_AND_RESULT_URLS_DO_NOT_MATCH);
        }
        int indexOfResultUrl = abbyyInitialRequest.getExportFormats().indexOf(exportFormat);
        String resultUrl = abbyyPreviousResponse.getResultUrls().get(indexOfResultUrl);

        validationEx = resultValidator.validateBeforeDownload(abbyyInitialRequest, resultUrl);
        if (validationEx != null) {
            throw validationEx;
        }

        String result = this.abbyyApi.getResult(abbyyInitialRequest, resultUrl, exportFormat, null, true);

        validationEx = resultValidator.validateAfterDownload(result);
        if (validationEx != null) {
            throw validationEx;
        }

        if (abbyyInitialRequest.getDestinationFile() != null) {
            saveClearTextOnDisk(abbyyInitialRequest, result, exportFormat);
        }

        return result;
    }


    private String getBinaryResult(@NotNull ProcessImageInput abbyyInitialRequest, @NotNull AbbyyResponse abbyyPreviousResponse,
                                   @NotNull ExportFormat exportFormat, @NotNull AbbyyResultValidator resultValidator) throws Exception {
        ValidationException validationEx;

        if (abbyyInitialRequest.getExportFormats().size() != abbyyPreviousResponse.getResultUrls().size()) {
            throw new AbbyySdkException(ExceptionMsgs.EXPORT_FORMAT_AND_RESULT_URLS_DO_NOT_MATCH);
        }
        int indexOfResultUrl = abbyyInitialRequest.getExportFormats().indexOf(exportFormat);
        String resultUrl = abbyyPreviousResponse.getResultUrls().get(indexOfResultUrl);

        validationEx = resultValidator.validateBeforeDownload(abbyyInitialRequest, resultUrl);
        if (validationEx != null) {
            throw validationEx;
        }

        if (abbyyInitialRequest.getDestinationFile() != null) {
            String targetPath = downloadOnDisk(abbyyInitialRequest, resultUrl, exportFormat);
            validationEx = resultValidator.validateAfterDownload(targetPath);
            if (validationEx != null) {
                throw validationEx;
            }
        }

        return resultUrl;
    }


    private void saveClearTextOnDisk(@NotNull ProcessImageInput request, @NotNull String clearText,
                                     @NotNull ExportFormat exportFormat) throws IOException {
        String targetPath = getTargetPath(request, exportFormat);
        if (new File(targetPath).exists()) {
            throw new FileAlreadyExistsException(targetPath);
        }
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(targetPath), request.getResponseCharacterSet())) {
            writer.write(clearText);
        }
    }


    private String downloadOnDisk(ProcessImageInput abbyyInitialRequest, String downloadUrl, ExportFormat exportFormat)
            throws IOException, AbbyySdkException, HttpException {
        String targetPath = getTargetPath(abbyyInitialRequest, exportFormat);
        if (new File(targetPath).exists()) {
            throw new FileAlreadyExistsException(targetPath);
        }
        this.abbyyApi.getResult(abbyyInitialRequest, downloadUrl, exportFormat, getTargetPath(abbyyInitialRequest, exportFormat), false);
        return targetPath;
    }


    private String getTargetPath(@NotNull ProcessImageInput request, @NotNull ExportFormat exportFormat) {
        return request.getDestinationFile().getAbsolutePath() + "/" +
                FilenameUtils.removeExtension(request.getSourceFile().getName()) + "." +
                exportFormat.getFileExtension();
    }
}