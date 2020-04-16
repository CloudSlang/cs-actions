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

package io.cloudslang.content.abbyy.utils;

import io.cloudslang.content.abbyy.constants.ExceptionMsgs;
import io.cloudslang.content.abbyy.constants.MiscConstants;
import io.cloudslang.content.abbyy.entities.AbbyyResponse;
import io.cloudslang.content.abbyy.exceptions.AbbyySdkException;
import io.cloudslang.content.abbyy.exceptions.ClientSideException;
import io.cloudslang.content.abbyy.exceptions.ServerSideException;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.StringReader;
import java.util.Map;

public class XmlResponseParser implements AbbyyResponseParser {

    private static final String MESSAGE_TAG_NAME = "message";
    private static final String TASK_TAG_NAME = "task";
    private static final String ID_ATTR_NAME = "id";
    private static final String STATUS_ATTR_NAME = "status";
    private static final String CREDITS_ATTR_NAME = "credits";
    private static final String ERROR_ATTR_NAME = "error";
    private static final String RESULT_URL_ATTR_NAME = "resultUrl";
    private static final String RESULT_URL2_ATTR_NAME = "resultUrl2";
    private static final String RESULT_URL3_ATTR_NAME = "resultUrl3";
    private static final String ESTIMATED_PROCESSING_TIME_ATTR_NAME = "estimatedProcessingTime";

    private final DocumentBuilder xmlDocBuilder;


    public XmlResponseParser() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        this.xmlDocBuilder = factory.newDocumentBuilder();
    }


    private AbbyyResponse parseResponse(short statusCode, String responseBody) throws Exception {
        Document xml = this.xmlDocBuilder.parse(new InputSource(new StringReader(responseBody)));
        xml.normalize();
        return (statusCode == 200 || statusCode == 550 || statusCode == 501) ?
                parseSuccessResponse(xml) :
                parseFailureResponse(xml);
    }


    @Override
    public AbbyyResponse parseResponse(Map<String, String> response) throws Exception {
        if (response == null) {
            throw new IllegalArgumentException(String.format(ExceptionMsgs.NULL_ARGUMENT, "response"));
        }

        if (StringUtils.isNotEmpty(response.get(MiscConstants.HTTP_EXCEPTION_OUTPUT))) {
            throw new Exception(response.get(MiscConstants.HTTP_EXCEPTION_OUTPUT));
        }

        return parseResponse(
                Short.parseShort(response.get(MiscConstants.HTTP_STATUS_CODE_OUTPUT)),
                response.get(MiscConstants.HTTP_RETURN_RESULT_OUTPUT)
        );
    }


    private AbbyyResponse parseSuccessResponse(Document xml) throws Exception {
        NodeList tasks = xml.getElementsByTagName(TASK_TAG_NAME);
        if (tasks.getLength() == 0) {
            throw new AbbyySdkException(ExceptionMsgs.INVALID_RESPONSE);
        }
        Element task = (Element) tasks.item(0);

        AbbyyResponse.Builder responseBuilder = new AbbyyResponse.Builder();

        if (StringUtils.isEmpty(task.getAttribute(ID_ATTR_NAME))) {
            throw new AbbyySdkException(ExceptionMsgs.EMPTY_ID);
        }
        responseBuilder.taskId(task.getAttribute(ID_ATTR_NAME));

        if (StringUtils.isEmpty(task.getAttribute(CREDITS_ATTR_NAME))) {
            throw new AbbyySdkException(ExceptionMsgs.EMPTY_CREDITS);
        }
        try {
            int taskCredits = Integer.parseInt(task.getAttribute(CREDITS_ATTR_NAME));
            if (taskCredits < 0) {
                throw new AbbyySdkException(ExceptionMsgs.INVALID_CREDITS);
            }
            responseBuilder.credits(taskCredits);
        } catch (NumberFormatException ex) {
            throw new AbbyySdkException(ExceptionMsgs.INVALID_CREDITS);
        }

        if (StringUtils.isEmpty(task.getAttribute(STATUS_ATTR_NAME))) {
            throw new ClientSideException(ExceptionMsgs.EMPTY_STATUS);
        }
        responseBuilder.taskStatus(AbbyyResponse.TaskStatus.fromString(task.getAttribute(STATUS_ATTR_NAME)));

        responseBuilder.errorMessage(task.getAttribute(ERROR_ATTR_NAME));

        if (StringUtils.isNotEmpty(task.getAttribute(RESULT_URL_ATTR_NAME))) {
            responseBuilder.resultUrl(StringEscapeUtils.unescapeHtml4(task.getAttribute(RESULT_URL_ATTR_NAME)));
        }

        if (StringUtils.isNotEmpty(task.getAttribute(RESULT_URL2_ATTR_NAME))) {
            responseBuilder.resultUrl2(StringEscapeUtils.unescapeHtml4(task.getAttribute(RESULT_URL2_ATTR_NAME)));
        }

        if (StringUtils.isNotEmpty(task.getAttribute(RESULT_URL3_ATTR_NAME))) {
            responseBuilder.resultUrl3(StringEscapeUtils.unescapeHtml4(task.getAttribute(RESULT_URL3_ATTR_NAME)));
        }

        if (StringUtils.isNotEmpty(task.getAttribute(ESTIMATED_PROCESSING_TIME_ATTR_NAME))) {
            try {
                long millis = Long.parseLong(task.getAttribute(ESTIMATED_PROCESSING_TIME_ATTR_NAME));
                if (millis < 0) {
                    throw new AbbyySdkException(ExceptionMsgs.INVALID_ESTIMATED_PROCESSING_TIME);
                }
                responseBuilder.estimatedProcessingTime(millis);
            } catch (NumberFormatException ex) {
                throw new AbbyySdkException(ExceptionMsgs.INVALID_ESTIMATED_PROCESSING_TIME);
            }
        }

        return responseBuilder.build();
    }


    private AbbyyResponse parseFailureResponse(Document xml) throws Exception {
        NodeList messages = xml.getElementsByTagName(MESSAGE_TAG_NAME);
        if (messages.getLength() == 0) {
            throw new Exception(ExceptionMsgs.INVALID_RESPONSE);
        }
        String errMsg = messages.item(0).getTextContent();
        throw new ServerSideException(errMsg);
    }
}
