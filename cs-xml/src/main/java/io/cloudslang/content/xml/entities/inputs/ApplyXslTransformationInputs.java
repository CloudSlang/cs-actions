/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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

package io.cloudslang.content.xml.entities.inputs;

import io.cloudslang.content.utils.StringUtilities;
import io.cloudslang.content.xml.utils.Constants;

/**
 * Created by moldovas on 08/07/2016.
 */
public class ApplyXslTransformationInputs {
    private String xmlDocument;
    private String xslTemplate;
    private String outputFile;
    private String parsingFeatures;

    private ApplyXslTransformationInputs(ApplyXslTransformationInputsBuilder builder) {
        this.xmlDocument = builder.xmlDocument;
        this.xslTemplate = builder.xslTemplate;
        this.outputFile = builder.outputFile;
        this.parsingFeatures = builder.parsingFeatures;
    }

    public String getXmlDocument() {
        return xmlDocument;
    }

    public String getXslTemplate() {
        return xslTemplate;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public String getParsingFeatures() {
        return parsingFeatures;
    }

    public static class ApplyXslTransformationInputsBuilder {
        private String xmlDocument;
        private String xslTemplate;
        private String outputFile;
        private String parsingFeatures;

        public ApplyXslTransformationInputs build() {
            return new ApplyXslTransformationInputs(this);
        }


        public ApplyXslTransformationInputsBuilder withXmlDocument(String inputValue) {
            xmlDocument = StringUtilities.defaultIfBlank(inputValue, Constants.EMPTY_STRING);
            return this;
        }

        public ApplyXslTransformationInputsBuilder withXslTemplate(String inputValue) {
            xslTemplate = inputValue;
            return this;
        }

        public ApplyXslTransformationInputsBuilder withOutputFile(String inputValue) {
            outputFile = StringUtilities.defaultIfBlank(inputValue, Constants.EMPTY_STRING);
            return this;
        }

        public ApplyXslTransformationInputsBuilder withParsingFeatures(String inputValue) {
            parsingFeatures = inputValue;
            return this;
        }
    }
}

