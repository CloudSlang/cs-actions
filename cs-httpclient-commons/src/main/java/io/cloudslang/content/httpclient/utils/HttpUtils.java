/*
 * Copyright 2022-2024 Open Text
 * This program and the accompanying materials
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

package io.cloudslang.content.httpclient.utils;

import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.httpclient.utils.Constants.AND;
import static io.cloudslang.content.httpclient.utils.Constants.EQUAL;
import static io.cloudslang.content.httpclient.utils.Constants.UTF_8;
import static io.cloudslang.content.httpclient.utils.Outputs.HTTPClientOutputs.STATUS_CODE;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class HttpUtils {

    @NotNull
    public static Map<String, String> getOperationResults(@NotNull final Map<String, String> result,
                                                          final String successMessage,
                                                          final String failureMessage) {
        final Map<String, String> results;
        final String statusCode = result.get(STATUS_CODE);


        //Validation for empty status code
        if (statusCode.equals(EMPTY)) {

            results = getFailureResultsMap(failureMessage);

            if (result.get(EXCEPTION) != null)
                results.put(EXCEPTION, result.get(EXCEPTION));

            return results;
        }

        if (Integer.parseInt(statusCode) >= 200 && Integer.parseInt(statusCode) < 300)
            results = getSuccessResultsMap(successMessage);
        else
            results = getFailureResultsMap(failureMessage);

        results.put(STATUS_CODE, statusCode);

        if (result.get(EXCEPTION) != null)
            results.put(EXCEPTION, result.get(EXCEPTION));

        return results;
    }

    @NotNull
    public static void parseApiExceptionMessage(@NotNull final Map<String, String> result) {

        String exception = result.get(EXCEPTION);

        //to do
    }

    public static List<? extends NameValuePair> urlEncodeMultipleParams(String params, boolean urlEncode) throws UrlEncodeException {
        List<BasicNameValuePair> list = new ArrayList<>();

        String[] pairs = params.split(AND);
        for (String pair : pairs) {
            String[] nameValue = pair.split(EQUAL, 2);
            String name = nameValue[0];
            String value = nameValue.length == 2 ? nameValue[1] : null;

            if (!urlEncode) {
                try {
                    name = URLDecoder.decode(name, UTF_8);
                    if (value != null) {
                        value = URLDecoder.decode(value, UTF_8);
                    }
                } catch (UnsupportedEncodingException e) {
                    //never happens
                    throw new RuntimeException(e);
                } catch (IllegalArgumentException ie) {
                    throw new UrlEncodeException(ie.getMessage(), ie);
                }
            }
            list.add(new BasicNameValuePair(name, value));
        }

        return list;
    }
}
