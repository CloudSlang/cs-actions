/*
 * (c) Copyright 2018 Micro Focus, L.P.
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
package io.cloudslang.content.hcm.utils;

import io.cloudslang.content.xml.entities.inputs.CommonInputs;
import io.cloudslang.content.xml.entities.inputs.CustomInputs;

public class CustomInput {

    public CommonInputs getCommonInputs(String xmlDocument, String xmlDocumentSource, String xPathQuery, String secureProcessing) {

        return new CommonInputs.CommonInputsBuilder()
                .withXmlDocument(xmlDocument)
                .withXmlDocumentSource(xmlDocumentSource)
                .withXpathQuery(xPathQuery)
                .withSecureProcessing(secureProcessing)
                .build();
    }

    public CustomInputs getCustomInputs(String queryType, String delimiter) {

        return new CustomInputs.CustomInputsBuilder()
                .withQueryType(queryType)
                .withDelimiter(delimiter)
                .build();
    }
}
