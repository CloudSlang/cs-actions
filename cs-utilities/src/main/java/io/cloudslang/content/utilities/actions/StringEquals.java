/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
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

package io.cloudslang.content.utilities.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.utilities.util.Descriptions;

import java.util.HashMap;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.utilities.entities.constants.Inputs.*;
import static io.cloudslang.content.utilities.util.Constants.DEFAULT_IGNORE_CASE;
import static io.cloudslang.content.utilities.util.Descriptions.ConvertBytesToFile.RETURN_CODE_DESC;
import static io.cloudslang.content.utilities.util.Descriptions.StringEqualsDescriptions.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class StringEquals {

    /**
     * Verifies if two strings are equal.
     *
     * @param firstString  First string to compare.
     *                     Default: ''
     *                     Optional
     * @param secondString Second string to compare.
     *                     Default: ''
     *                     Optional
     * @param ignoreCase   If set to 'true', then the comparison ignores case considerations. The two strings are
     *                     considered equal ignoring case if they are of the same length and corresponding characters in the
     *                     two strings are equal ignoring case. If set to any value other than 'true', then the strings must
     *                     match exactly to be considered equal.
     *                     Default: 'false'
     *                     Optional
     */

    @Action(name = "String Equals",
            outputs = {
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = Descriptions.RandomPasswordGeneratorDescriptions.SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, description = Descriptions.RandomPasswordGeneratorDescriptions.FAILURE_DESC)
            })
    public Map<String, String> execute(@Param(value = FIRST_STRING, description = FIRST_STRING_DESC) String firstString,
                                       @Param(value = SECOND_STRING, description = SECOND_STRING_DESC) String secondString,
                                       @Param(value = IGNORE_CASE, description = IGNORE_CASE_DESC) String ignoreCase) {

        firstString = defaultIfEmpty(firstString, EMPTY);
        secondString = defaultIfEmpty(secondString, EMPTY);
        ignoreCase = defaultIfEmpty(ignoreCase, DEFAULT_IGNORE_CASE);

        Boolean eq;
        Map<String, String> result = new HashMap();

        if (ignoreCase.toLowerCase().equals("true")) {
            eq = firstString.equalsIgnoreCase(secondString);
        } else eq = firstString.equals(secondString);

        if (String.valueOf(eq).equals("true")) {
            result.put("returnCode", "0");
            return result;
        } else {
            result.put("returnCode", "-1");
            return result;
        }
    }
}

