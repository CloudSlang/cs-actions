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
import io.cloudslang.content.abbyy.entities.others.MarkingType;
import io.cloudslang.content.abbyy.entities.others.Region;
import io.cloudslang.content.abbyy.entities.others.TextType;
import io.cloudslang.content.abbyy.entities.others.WritingStyle;
import io.cloudslang.content.abbyy.utils.InputParser;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ProcessTextFieldInput extends AbbyyInput {
    private final Region region;
    private final List<String> languages;
    private final String letterSet;
    private final String regExp;
    private final TextType textType;
    private final boolean oneTextLine;
    private final boolean oneWordPerTextLine;
    private final MarkingType markingType;
    private final int placeholdersCount;
    private final WritingStyle writingStyle;
    private final String description;
    private final String pdfPassword;


    private ProcessTextFieldInput(Builder builder) {
        super(builder);
        this.region = builder.region;
        this.languages = builder.language;
        this.letterSet = builder.letterSet;
        this.regExp = builder.regExp;
        this.textType = builder.textType;
        this.oneTextLine = builder.oneTextLine;
        this.oneWordPerTextLine = builder.oneWordPerTextLine;
        this.markingType = builder.markingType;
        this.placeholdersCount = builder.placeholdersCount;
        this.writingStyle = builder.writingStyle;
        this.description = builder.description;
        this.pdfPassword = builder.pdfPassword;
    }


    public Region getRegion() {
        return region;
    }


    public List<String> getLanguages() {
        return languages;
    }


    public String getLetterSet() {
        return letterSet;
    }


    public String getRegExp() {
        return regExp;
    }


    public TextType getTextType() {
        return textType;
    }


    public Boolean isOneTextLine() {
        return oneTextLine;
    }


    public Boolean isOneWordPerTextLine() {
        return oneWordPerTextLine;
    }


    public MarkingType getMarkingType() {
        return markingType;
    }


    public Integer getPlaceholdersCount() {
        return placeholdersCount;
    }


    public WritingStyle getWritingStyle() {
        return writingStyle;
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
                        Endpoints.PROCESS_TEXT_FIELD));

        if (this.getRegion() != null) {
            urlBuilder.addParameter(QueryParams.REGION, this.getRegion().toString());
        }

        if (this.getLanguages() != null && !this.getLanguages().isEmpty()) {
            urlBuilder.addParameter(QueryParams.LANGUAGE, StringUtils.join(this.getLanguages(), ','));
        }

        if (StringUtils.isNotEmpty(this.getLetterSet())) {
            urlBuilder.addParameter(QueryParams.LETTER_SET, this.getLetterSet());
        }

        if (StringUtils.isNotEmpty(this.getRegExp())) {
            urlBuilder.addParameter(QueryParams.REG_EXP, this.getRegExp());
        }

        if (this.getTextType() != null) {
            urlBuilder.addParameter(QueryParams.TEXT_TYPE, this.getTextType().toString());
        }

        urlBuilder.addParameter(QueryParams.ONE_TEXT_LINE, String.valueOf(this.isOneTextLine()));

        urlBuilder.addParameter(QueryParams.ONE_WORD_PER_TEXT_LINE, String.valueOf(this.isOneWordPerTextLine()));

        if (this.getMarkingType() != null) {
            urlBuilder.addParameter(QueryParams.MARKING_TYPE, this.getMarkingType().toString());
        }

        urlBuilder.addParameter(QueryParams.PLACEHOLDERS_COUNT, String.valueOf(this.getPlaceholdersCount()));

        if (this.getWritingStyle() != null) {
            urlBuilder.addParameter(QueryParams.WRITING_STYLE, this.getWritingStyle().toString());
        }

        if (StringUtils.isNotEmpty(this.getDescription())) {
            urlBuilder.addParameter(QueryParams.DESCRIPTION, this.getDescription());
        }

        if (StringUtils.isNotEmpty(this.getPdfPassword())) {
            urlBuilder.addParameter(QueryParams.PDF_PASSWORD, this.getPdfPassword());
        }

        return urlBuilder.build().toString();
    }


    public static class Builder extends AbbyyInput.Builder {
        private Region region;
        private List<String> language;
        private String letterSet;
        private String regExp;
        private TextType textType;
        private Boolean oneTextLine;
        private Boolean oneWordPerTextLine;
        private MarkingType markingType;
        private Integer placeholdersCount;
        private WritingStyle writingStyle;
        private String description;
        private String pdfPassword;


        public Builder region(String region) {
            this.region = InputParser.parseRegion(StringUtils.defaultString(region, DefaultInputValues.REGION));
            return this;
        }


        public Builder language(String language) {
            this.language = StringUtils.isBlank(language) ?
                    Collections.singletonList(DefaultInputValues.LANGUAGE) :
                    Arrays.asList(language.split("[,]"));
            return this;
        }


        public Builder letterSet(String letterSet) {
            this.letterSet = letterSet;
            return this;
        }


        public Builder regExp(String regExp) {
            this.regExp = regExp;
            return this;
        }


        public Builder textType(String textType) {
            this.textType = InputParser.parseEnum(
                    StringUtils.defaultString(textType, DefaultInputValues.TEXT_TYPE),
                    TextType.class, InputNames.TEXT_TYPE);
            return this;
        }


        public Builder oneTextLine(String oneTextLine) {
            this.oneTextLine = InputParser.parseBoolean(
                    StringUtils.defaultString(oneTextLine, DefaultInputValues.ONE_TEXT_LINE),
                    InputNames.ONE_TEXT_LINE);
            return this;
        }


        public Builder oneWordPerTextLine(String oneWordPerTextLine) {
            this.oneWordPerTextLine = InputParser.parseBoolean(
                    StringUtils.defaultString(oneWordPerTextLine, DefaultInputValues.ONE_WORD_PER_TEXT_LINE),
                    InputNames.ONE_WORD_PER_TEXT_LINE);
            return this;
        }


        public Builder markingType(String markingType) {
            this.markingType = InputParser.parseEnum(
                    StringUtils.defaultString(markingType, DefaultInputValues.MARKING_TYPE),
                    MarkingType.class, InputNames.MARKING_TYPE);
            return this;
        }


        public Builder placeholdersCount(String placeholdersCount) {
            this.placeholdersCount = InputParser.parseInt(
                    StringUtils.defaultString(placeholdersCount, DefaultInputValues.PLACEHOLDERS_COUNT),
                    InputNames.PLACEHOLDERS_COUNT);
            return this;
        }


        public Builder writingStyle(String writingStyle) {
            this.writingStyle = InputParser.parseEnum(writingStyle, WritingStyle.class, InputNames.WRITING_STYLE);
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


        public ProcessTextFieldInput build() {
            return new ProcessTextFieldInput(this);
        }
    }
}