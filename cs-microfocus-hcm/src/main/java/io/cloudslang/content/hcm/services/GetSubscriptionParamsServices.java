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
package io.cloudslang.content.hcm.services;

import io.cloudslang.content.hcm.utils.CustomInput;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.xml.services.XpathQueryService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.cloudslang.content.hcm.utils.Constants.*;
import static io.cloudslang.content.httpclient.services.HttpClientService.RETURN_RESULT;
import static io.cloudslang.content.httpclient.services.HttpClientService.STATUS_CODE;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static java.lang.Integer.parseInt;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.join;

public class GetSubscriptionParamsServices {

    public static Map<String, String> createInstance(HttpClientInputs httpClientInputs) {
        final Map<String, String> httpResponse = new HttpClientService().execute(httpClientInputs);

        if (parseInt(httpResponse.get(STATUS_CODE)) == HTTP_OK) {
            final CustomInput customInput = new CustomInput();

            final Map<String, String> queryPropertyName = new XpathQueryService().execute(customInput.getCommonInputs(httpResponse.get(RETURN_RESULT), XML_DOCUMENT_SOURCE, X_PATH_QUERY_PROPERTY_NAME, SECURE_PROCESSING),
                    customInput.getCustomInputs(QUERY_TYPE, DELIMITER));

            final List<String> finalList = new ArrayList<>();

            final String[] arrayProp = queryPropertyName.get(SELECTED_VALUE).split(COMMA);

            int nr = 0;
            for (String anArrayProp : arrayProp)
                if (anArrayProp.contains(PARAM))
                    nr++;

            listValue(finalList, arrayProp, nr, customInput, httpResponse, PARAM);

            final String finalResponse = join(finalList, EMPTY);

            Map<String, String> result = getSuccessResultsMap(RETURN_RESULT_MESSAGE);
            result.put(PARAM_LIST, finalResponse);

            return result;
        } else {
            return getFailureResultsMap(join(httpResponse.get(EXCEPTION), NEW_LINE));
        }
    }

    public static void listValue(List<String> finalList, String[] arrayProp, int nr, CustomInput customInput, Map<String, String> httpResponse, String param) {
        for (int i = 0; i < arrayProp.length; i++)
            if (arrayProp[i].contains(param)) {

                String xPathQueryChild = QUERY + arrayProp[i] + QUERY_PART;
                Map<String, String> queryChild = new XpathQueryService().execute(customInput.getCommonInputs(httpResponse.get(RETURN_RESULT), XML_DOCUMENT_SOURCE, xPathQueryChild, SECURE_PROCESSING),
                        customInput.getCustomInputs(QUERY_TYPE, DELIMITER));

                queryChild.put(SELECTED_VALUE, queryChild.get(SELECTED_VALUE).replaceAll(WHITESPACE, EMPTY));

                arrayProp[i] = arrayProp[i].replaceAll(param, EMPTY);

                if (i < nr && nr > 1)
                    finalList.add(arrayProp[i] + EQUALS + queryChild.get(SELECTED_VALUE) + AND);
                else
                    finalList.add(arrayProp[i] + EQUALS + queryChild.get(SELECTED_VALUE));

            }

    }

}
