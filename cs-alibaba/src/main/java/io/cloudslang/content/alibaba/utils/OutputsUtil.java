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

package io.cloudslang.content.alibaba.utils;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.httpclient.services.HttpClientService.STATUS_CODE;
import static java.lang.String.valueOf;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.http.HttpStatus.SC_OK;

import io.cloudslang.content.xml.actions.XpathQuery;
import io.cloudslang.content.alibaba.entities.constants.Outputs;

import static io.cloudslang.content.xml.utils.Constants.Defaults.DELIMITER;
import static io.cloudslang.content.xml.utils.Constants.Defaults.XML_DOCUMENT_SOURCE;
import static io.cloudslang.content.xml.utils.Constants.Outputs.ERROR_MESSAGE;
import static io.cloudslang.content.xml.utils.Constants.Outputs.SELECTED_VALUE;
import static io.cloudslang.content.xml.utils.Constants.QueryTypes.VALUE;


public class OutputsUtil {

    private static final String XMLNS = "xmlns";
    private static final String WORKAROUND = "workaround";

    private OutputsUtil() {
    }


    public static Map<String, String> getValidResponse(Map<String, String> queryMapResult) {
        if (queryMapResult != null) {
            if (queryMapResult.containsKey(STATUS_CODE) && (valueOf(SC_OK).equals(queryMapResult.get(STATUS_CODE))) && queryMapResult.containsKey(RETURN_RESULT) && !isEmpty(queryMapResult.get(RETURN_RESULT))) {
                queryMapResult.put(RETURN_CODE, SUCCESS);
            } else {
                queryMapResult.put(RETURN_CODE, FAILURE);
            }
            return queryMapResult;
        } else {
            Map<String, String> resultMap = new HashMap<>();
            resultMap.put(EXCEPTION, "Null response!");
            resultMap.put(RETURN_CODE, FAILURE);
            resultMap.put(RETURN_RESULT, "The query returned null response!");
            return resultMap;
        }
    }

    public static void putResponseIn(Map<String, String> queryMapResult, String outputName, String xPathQuery) {
        XpathQuery xpathQueryAction = new XpathQuery();
        String xmlString = queryMapResult.get(RETURN_RESULT);
        //We make this workaround because the xml has an xmlns property in the tag and our operation can not parse the xml
        //this should be removed when the xml operation will be enhanced
        if (!isBlank(xmlString)) {
            xmlString = xmlString.replace(XMLNS, WORKAROUND);
            Map<String, String> result = xpathQueryAction.execute(xmlString, XML_DOCUMENT_SOURCE, xPathQuery, VALUE, DELIMITER, valueOf(true));
            if (result.containsKey(RETURN_CODE) && SUCCESS.equals(result.get(RETURN_CODE))) {
                queryMapResult.put(outputName, result.get(SELECTED_VALUE));
            } else {
                queryMapResult.put(RETURN_CODE, FAILURE);
                queryMapResult.put(EXCEPTION, result.get(ERROR_MESSAGE));
            }
        } else {
            queryMapResult.put(RETURN_RESULT, "Empty response.");
            queryMapResult.put(RETURN_CODE, FAILURE);
        }
    }

    private static Map<String, String> getResultsMap(String returnResult) {
        Map<String, String> results = new HashMap<>();
        results.put(Outputs.RETURN_CODE, Outputs.SUCCESS_RETURN_CODE);
        results.put(Outputs.RETURN_RESULT, returnResult);

        return results;
    }


}

