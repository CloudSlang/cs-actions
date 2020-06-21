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

package io.cloudslang.content.abbyy.http;

import io.cloudslang.content.abbyy.constants.ExceptionMsgs;
import io.cloudslang.content.abbyy.entities.responses.AbbyyResponse;
import io.cloudslang.content.abbyy.entities.responses.HttpClientResponse;
import io.cloudslang.content.abbyy.exceptions.AbbyySdkException;
import io.cloudslang.content.abbyy.exceptions.ServerSideException;
import io.cloudslang.content.abbyy.exceptions.ValidationException;
import io.cloudslang.content.abbyy.validators.AbbyyResponseValidator;
import io.cloudslang.content.abbyy.validators.AbbyyResponseValidatorImpl;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

class AbbyyResponseParser {

    private final DocumentBuilder xmlDocBuilder;
    private final AbbyyResponseValidator validator;


    public AbbyyResponseParser() throws ParserConfigurationException {
        this(null);
    }


    public AbbyyResponseParser(@Nullable AbbyyResponseValidator validator) throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        this.xmlDocBuilder = factory.newDocumentBuilder();
        this.validator = validator != null ? validator : new AbbyyResponseValidatorImpl();
    }


    public @NotNull AbbyyResponse parseResponse(@NotNull HttpClientResponse response)
            throws SAXException, IOException, AbbyySdkException {
        if (response.getStatusCode() == null || response.getReturnResult() == null) {
            throw new IllegalArgumentException(ExceptionMsgs.INVALID_HTTP_RESPONSE);
        }

        return parseResponse(response.getStatusCode(), response.getReturnResult());
    }


    private synchronized @NotNull AbbyyResponse parseResponse(short statusCode, @NotNull String responseBody)
            throws IOException, SAXException, AbbyySdkException {
        responseBody = responseBody.trim().replaceFirst("^([\\W]+)<", "<");
        Document xml = this.xmlDocBuilder.parse(new InputSource(new StringReader(responseBody)));
        xml.normalize();
        return (statusCode == 200 || statusCode == 550 || statusCode == 501) ?
                parseSuccessResponse(xml) :
                parseFailureResponse(xml);
    }


    private @NotNull AbbyyResponse parseSuccessResponse(@NotNull Document xml) throws AbbyySdkException {
        NodeList tasks = xml.getElementsByTagName(XmlTags.TASK);
        if (tasks.getLength() == 0) {
            throw new AbbyySdkException(ExceptionMsgs.INVALID_ABBYY_RESPONSE);
        }
        Element task = (Element) tasks.item(0);

        AbbyyResponse abbyyResponse = new AbbyyResponse.Builder()
                .taskId(task.getAttribute(XmlAttrs.ID))
                .credits(task.getAttribute(XmlAttrs.CREDITS))
                .taskStatus(task.getAttribute(XmlAttrs.STATUS))
                .errorMessage(task.getAttribute(XmlAttrs.ERROR))
                .resultUrl(StringEscapeUtils.unescapeHtml4(task.getAttribute(XmlAttrs.RESULT_URL)))
                .resultUrl2(StringEscapeUtils.unescapeHtml4(task.getAttribute(XmlAttrs.RESULT_URL2)))
                .resultUrl3(StringEscapeUtils.unescapeHtml4(task.getAttribute(XmlAttrs.RESULT_URL3)))
                .estimatedProcessingTime(task.getAttribute(XmlAttrs.ESTIMATED_PROCESSING_TIME))
                .build();

        ValidationException validationEx = this.validator.validate(abbyyResponse);
        if (validationEx != null) {
            throw validationEx;
        }

        return abbyyResponse;
    }


    private AbbyyResponse parseFailureResponse(@NotNull Document xml) throws AbbyySdkException {
        NodeList messages = xml.getElementsByTagName(XmlTags.MESSAGE);
        if (messages.getLength() == 0) {
            throw new AbbyySdkException(ExceptionMsgs.INVALID_ABBYY_RESPONSE);
        }
        String errMsg = messages.item(0).getTextContent();
        throw new ServerSideException(errMsg);
    }


    private static final class XmlTags {
        private static final String MESSAGE = "message";
        private static final String TASK = "task";
    }


    private static final class XmlAttrs {
        private static final String ID = "id";
        private static final String STATUS = "status";
        private static final String CREDITS = "credits";
        private static final String ERROR = "error";
        private static final String RESULT_URL = "resultUrl";
        private static final String RESULT_URL2 = "resultUrl2";
        private static final String RESULT_URL3 = "resultUrl3";
        private static final String ESTIMATED_PROCESSING_TIME = "estimatedProcessingTime";
    }
}
