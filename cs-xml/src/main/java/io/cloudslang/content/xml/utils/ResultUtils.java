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

package io.cloudslang.content.xml.utils;

import io.cloudslang.content.constants.ResponseNames;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.xml.utils.Constants.Outputs.ERROR_MESSAGE;
import static io.cloudslang.content.xml.utils.Constants.Outputs.RESULT_TEXT;
import static io.cloudslang.content.xml.utils.Constants.Outputs.RESULT_XML;
import static io.cloudslang.content.xml.utils.Constants.Outputs.SELECTED_VALUE;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Created by markowis on 03/03/2016.
 */
public class ResultUtils {
    private ResultUtils() {
    }

    public static void populateFailureResult(Map<String, String> result, String errorMessage) {
        populateResult(result, ResponseNames.FAILURE, Constants.EMPTY_STRING, EMPTY, RESULT_XML, FAILURE, errorMessage);
    }

    public static void populateSuccessResult(Map<String, String> result, String returnResult, String resultXml) {
        populateResult(result, ResponseNames.SUCCESS, returnResult, resultXml, RESULT_XML, SUCCESS, EMPTY);
    }

    public static void populateValueResult(Map<String, String> result, String resultText, String returnResult, String selectedValue, String returnCode) {
        populateResult(result, resultText, returnResult, selectedValue, SELECTED_VALUE, returnCode, EMPTY);
    }

    private static void populateResult(Map<String, String> result, String resultText,
                                       String returnResult, String resultXml, String resultKey, String returnCode, String errorMessage) {
        result.put(RESULT_TEXT, resultText);
        result.put(RETURN_RESULT, returnResult);
        result.put(RETURN_CODE, returnCode);
        result.put(ERROR_MESSAGE, errorMessage);
        result.put(resultKey, resultXml);
    }
}
