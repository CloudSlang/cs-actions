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

package io.cloudslang.content.httpclient.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.httpclient.build.Utils;
import io.cloudslang.content.httpclient.services.HttpClientService;
import org.apache.commons.lang3.StringUtils;

import java.net.URLEncoder;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.httpclient.entities.Constants.CHARACTER_SET;
import static io.cloudslang.content.httpclient.entities.Constants.URL;
import static io.cloudslang.content.httpclient.utils.Descriptions.Commons.CHARACTER_SET_DESC;
import static io.cloudslang.content.httpclient.utils.Descriptions.Commons.EXCEPTION_DESC;
import static io.cloudslang.content.httpclient.utils.Descriptions.Commons.RETURN_CODE_DESC;
import static io.cloudslang.content.httpclient.utils.Descriptions.Commons.URL_DESC;
import static io.cloudslang.content.httpclient.utils.Descriptions.UrlEncoder.RETURN_RESULT_DESC;
import static io.cloudslang.content.httpclient.utils.Descriptions.UrlEncoder.URL_ENCODER_DESC;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;

public class URLEncoderAction {


    @Action(name = "URL Encoder", description = URL_ENCODER_DESC,
            outputs = {
                    @Output(value = HttpClientService.EXCEPTION, description = EXCEPTION_DESC),
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR)
            }
    )
    public Map<String, String> execute(
            @Param(value = URL, required = true, description = URL_DESC) String text,
            @Param(value = CHARACTER_SET, description = CHARACTER_SET_DESC) String characterSet) {

        try {
            if (StringUtils.isEmpty(characterSet)) {
                characterSet = Utils.DEFAULT_CHARACTER_SET;
            }
            String returnResult = URLEncoder.encode(text, characterSet);

            return getSuccessResultsMap(returnResult);
        } catch (Exception e) {
            return getFailureResultsMap(e.getMessage());
        }
    }

}
