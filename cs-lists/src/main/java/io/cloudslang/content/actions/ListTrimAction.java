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

package io.cloudslang.content.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import io.cloudslang.content.utils.ListProcessor;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.utils.Constants.OutputNames.RESPONSE;
import static io.cloudslang.content.utils.Constants.OutputNames.RESULT_TEXT;
import static io.cloudslang.content.utils.Constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.utils.Constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.utils.Constants.ResponseNames.FAILURE;
import static io.cloudslang.content.utils.Constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.utils.Constants.ReturnCodes.RETURN_CODE_FAILURE;
import static io.cloudslang.content.utils.Constants.ReturnCodes.RETURN_CODE_SUCCESS;

/**
 * Created by giloan on 7/8/2016.
 */
public class ListTrimAction {

    private static final String LIST = "list";
    private static final String DELIMITER = "delimiter";
    private static final String PERCENTAGE = "pct";
    private static final String PARSING_PERCENT_EXCEPTION_MSG = "\n While parsing percent to trim.";

    /**
     * This method trims values from a list. The values trimmed are equally distributed from the high and low ends
     * of the list. The list is sorted before trimming. The number of elements trimmed is dictated by the percentage
     * that is passed in.  If the percentage would indicate an odd number of elements the number trimmed is lowered
     * by one so that the same number are taken from both ends.
     *
     * @param list       A list of numbers.
     * @param delimiter  The list delimiter.
     * @param percentage The percentage of elements to trim.
     * @return The trimmed list separated by the same delimiter.
     */
    @Action(name = "Trim List",
            outputs = {
                    @Output(RESULT_TEXT),
                    @Output(RESPONSE),
                    @Output(RETURN_RESULT),
                    @Output(RETURN_CODE)
            },
            responses = {@Response(text = SUCCESS, field = RETURN_CODE, value = RETURN_CODE_SUCCESS, matchType = MatchType.COMPARE_EQUAL),
                    @Response(text = FAILURE, field = RETURN_CODE, value = RETURN_CODE_FAILURE, matchType = MatchType.COMPARE_EQUAL, isOnFail = true, isDefault = true)})

    public Map<String, String> trimList(@Param(value = LIST, required = true) String list,
                                        @Param(value = DELIMITER, required = true) String delimiter,
                                        @Param(value = PERCENTAGE, required = true) String percentage) {
        Map<String, String> result = new HashMap<>();
        try {
            int intPct;
            try {
                intPct = Integer.parseInt(percentage);
            } catch (NumberFormatException e) {
                throw new NumberFormatException(e.getMessage() + PARSING_PERCENT_EXCEPTION_MSG);
            }

            String value;
            try {
                value = trimIntList(list, delimiter, intPct);
            } catch (Exception e) {
                try {
                    value = trimDoubleList(list, delimiter, intPct);
                } catch (Exception f) {
                    value = trimStringList(list, delimiter, intPct);
                }
            }
            result.put(RESULT_TEXT, value);
            result.put(RESPONSE, SUCCESS);
            result.put(RETURN_RESULT, value);
            result.put(RETURN_CODE, RETURN_CODE_SUCCESS);
        } catch (Exception e) {
            result.put(RESULT_TEXT, e.getMessage());
            result.put(RESPONSE, FAILURE);
            result.put(RETURN_RESULT, e.getMessage());
            result.put(RETURN_CODE, RETURN_CODE_FAILURE);
        }
        return result;
    }

    public String trimIntList(String str_list, String delimiter, int percent) throws Exception {
        int[] list = ListProcessor.toIntArray(str_list, delimiter);
        list = ListProcessor.trimPercent(ListProcessor.sort(list), percent);
        return ListProcessor.toString(list, delimiter);
    }

    public String trimDoubleList(String str_list, String delimiter, int percent) throws Exception {
        double[] list = ListProcessor.toDoubleArray(str_list, delimiter);
        list = ListProcessor.trimPercent(ListProcessor.sort(list), percent);
        return ListProcessor.toString(list, delimiter);
    }

    public String trimStringList(String str_list, String delimiter, int percent) throws Exception {
        String[] list = ListProcessor.toArray(str_list, delimiter);
        list = ListProcessor.trimPercent(ListProcessor.sort(list), percent);
        return ListProcessor.toString(list, delimiter);
    }
}
