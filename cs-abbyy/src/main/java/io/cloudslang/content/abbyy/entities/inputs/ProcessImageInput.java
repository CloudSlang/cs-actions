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

package io.cloudslang.content.abbyy.entities.inputs;

import io.cloudslang.content.abbyy.constants.*;
import io.cloudslang.content.abbyy.entities.others.*;
import io.cloudslang.content.abbyy.utils.InputParser;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ProcessImageInput extends AbbyyInput {
    private final List<String> languages;
    private final Profile profile;
    private final List<TextType> textTypes;
    private final ImageSource imageSource;
    private final Boolean correctOrientation;
    private final Boolean correctSkew;
    private final Boolean readBarcodes;
    private final List<ExportFormat> exportFormats;
    private final Boolean writeFormatting;
    private final Boolean writeRecognitionVariants;
    private final WriteTags writeTags;
    private final String description;
    private final String pdfPassword;


    private ProcessImageInput(Builder builder) {
        super(builder);
        this.languages = builder.language;
        this.profile = builder.profile;
        this.textTypes = builder.textType;
        this.imageSource = builder.imageSource;
        this.correctOrientation = builder.correctOrientation;
        this.correctSkew = builder.correctSkew;
        this.readBarcodes = builder.readBarcodes;
        this.exportFormats = builder.exportFormat;
        this.writeFormatting = builder.writeFormatting;
        this.writeRecognitionVariants = builder.writeRecognitionVariants;
        this.writeTags = builder.writeTags;
        this.description = builder.description;
        this.pdfPassword = builder.pdfPassword;
    }


    public List<String> getLanguages() {
        return languages;
    }


    public Profile getProfile() {
        return profile;
    }


    public List<TextType> getTextTypes() {
        return textTypes;
    }


    public ImageSource getImageSource() {
        return imageSource;
    }


    public Boolean isCorrectOrientation() {
        return correctOrientation;
    }


    public Boolean isCorrectSkew() {
        return correctSkew;
    }


    public Boolean isReadBarcodes() {
        return readBarcodes;
    }


    public List<ExportFormat> getExportFormats() {
        return exportFormats;
    }


    public Boolean isWriteFormatting() {
        return writeFormatting;
    }


    public Boolean isWriteRecognitionVariants() {
        return writeRecognitionVariants;
    }


    public WriteTags getWriteTags() {
        return writeTags;
    }


    public String getDescription() {
        return description;
    }


    public String getPdfPassword() {
        return pdfPassword;
    }


    @Override
    public @NotNull String getUrl() throws URISyntaxException {
        URIBuilder urlBuilder = new URIBuilder()
                .setScheme(this.getLocationId().getProtocol())
                .setHost(String.format(Urls.HOST_TEMPLATE, this.getLocationId().toString(),
                        Endpoints.PROCESS_IMAGE));

        if (this.getLanguages() != null && !this.getLanguages().isEmpty()) {
            urlBuilder.addParameter(QueryParams.LANGUAGE, StringUtils.join(this.getLanguages(), ','));
        }

        if (this.getProfile() != null) {
            urlBuilder.addParameter(QueryParams.PROFILE, this.getProfile().toString());
        }

        if (this.getTextTypes() != null && !this.getTextTypes().isEmpty()) {
            urlBuilder.addParameter(QueryParams.TEXT_TYPE, StringUtils.join(this.getTextTypes(), ','));
        }

        if (this.getImageSource() != null) {
            urlBuilder.addParameter(QueryParams.IMAGE_SOURCE, this.getImageSource().toString());
        }

        urlBuilder.addParameter(QueryParams.CORRECT_ORIENTATION, String.valueOf(this.isCorrectOrientation()));

        urlBuilder.addParameter(QueryParams.CORRECT_SKEW, String.valueOf(this.isCorrectSkew()));

        if (this.isReadBarcodes() != null) {
            urlBuilder.addParameter(QueryParams.READ_BARCODES, String.valueOf(this.isReadBarcodes()));
        }

        if (this.getExportFormats() != null && !this.getExportFormats().isEmpty()) {
            urlBuilder.addParameter(QueryParams.EXPORT_FORMAT, StringUtils.join(this.getExportFormats(), ','));
        }

        if (StringUtils.isNotEmpty(this.getDescription())) {
            urlBuilder.addParameter(QueryParams.DESCRIPTION, this.getDescription());
        }

        if (StringUtils.isNotEmpty(this.getPdfPassword())) {
            urlBuilder.addParameter(QueryParams.PDF_PASSWORD, this.getPdfPassword());
        }

        if (this.getExportFormats() != null && this.getExportFormats().contains(ExportFormat.XML)) {
            urlBuilder.addParameter(QueryParams.WRITE_FORMATTING, String.valueOf(this.isWriteFormatting()))
                    .addParameter(QueryParams.WRITE_RECOGNITION_VARIANTS, String.valueOf(this.isWriteRecognitionVariants()));
        }

        if (this.getExportFormats() != null && this.getExportFormats().contains(ExportFormat.PDF_SEARCHABLE)
                && this.getWriteTags() != null) {
            urlBuilder.addParameter(QueryParams.WRITE_TAGS, this.getWriteTags().toString());
        }

        return urlBuilder.build().toString();
    }


    public static class Builder extends AbbyyInput.Builder {
        private List<String> language;
        private Profile profile;
        private List<TextType> textType;
        private ImageSource imageSource;
        private Boolean correctOrientation;
        private Boolean correctSkew;
        private Boolean readBarcodes;
        private List<ExportFormat> exportFormat;
        private Boolean writeFormatting;
        private Boolean writeRecognitionVariants;
        private WriteTags writeTags;
        private String description;
        private String pdfPassword;


        public Builder language(String language) {
            this.language = StringUtils.isBlank(language) ?
                    Collections.singletonList(DefaultInputValues.LANGUAGE) :
                    Arrays.asList(language.split("[,]"));
            return this;
        }


        public Builder profile(String profile) {
            this.profile = InputParser.parseEnum(profile, Profile.class, InputNames.PROFILE);
            return this;
        }


        public Builder textType(String textType) {
            String[] textTypeArray = StringUtils.defaultString(textType, DefaultInputValues.TEXT_TYPE).split("[,]");
            this.textType = new ArrayList<>();
            for (String elem : textTypeArray) {
                this.textType.add(InputParser.parseEnum(elem, TextType.class, InputNames.TEXT_TYPE));
            }
            return this;
        }


        public Builder imageSource(String imageSource) {
            this.imageSource = InputParser.parseEnum(
                    StringUtils.defaultString(imageSource, DefaultInputValues.IMAGE_SOURCE),
                    ImageSource.class, InputNames.IMAGE_SOURCE);
            return this;
        }


        public Builder correctOrientation(String correctOrientation) {
            this.correctOrientation = InputParser.parseBoolean(
                    StringUtils.defaultString(correctOrientation, DefaultInputValues.CORRECT_ORIENTATION),
                    InputNames.CORRECT_ORIENTATION);
            return this;
        }


        public Builder correctSkew(String correctSkew) {
            this.correctSkew = InputParser.parseBoolean(
                    StringUtils.defaultString(correctSkew, DefaultInputValues.CORRECT_SKEW),
                    InputNames.CORRECT_SKEW);
            return this;
        }


        public Builder readBarcodes(String readBarcodes) {
            if (StringUtils.isNotEmpty(readBarcodes)) {
                this.readBarcodes = InputParser.parseBoolean(readBarcodes, InputNames.READ_BARCODES);
            }
            return this;
        }


        public Builder exportFormat(String exportFormat) {
            this.exportFormat = new ArrayList<>();
            String[] exportFormatArray = StringUtils.defaultString(exportFormat, DefaultInputValues.EXPORT_FORMAT).split("[,]");
            for (String elem : exportFormatArray) {
                this.exportFormat.add(InputParser.parseEnum(elem, ExportFormat.class, InputNames.EXPORT_FORMAT));
            }
            return this;
        }


        public Builder writeFormatting(String writeFormatting) {
            this.writeFormatting = InputParser.parseBoolean(writeFormatting, InputNames.WRITE_FORMATTING);
            return this;
        }


        public Builder writeRecognitionVariants(String writeRecognitionVariants) {
            this.writeRecognitionVariants = InputParser.parseBoolean(writeRecognitionVariants, InputNames.WRITE_RECOGNITION_VARIANTS);
            return this;
        }


        public Builder writeTags(String writeTags) {
            this.writeTags = InputParser.parseEnum(writeTags, WriteTags.class, InputNames.WRITE_TAGS);
            return this;
        }


        public Builder description(String description) {
            this.description = InputParser.parseDescription(description);
            return this;
        }


        public Builder pdfPassword(String pdfPassword) {
            this.pdfPassword = pdfPassword;
            return this;
        }


        public ProcessImageInput build() {
            return new ProcessImageInput(this);
        }
    }
}
