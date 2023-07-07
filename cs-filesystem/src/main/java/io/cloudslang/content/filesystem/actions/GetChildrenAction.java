/*
 * Copyright 2020-2023 Open Text
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

package io.cloudslang.content.filesystem.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.filesystem.constants.InputNames;
import io.cloudslang.content.filesystem.entities.GetChildrenInputs;
import io.cloudslang.content.filesystem.services.GetChildrenService;
import io.cloudslang.content.utils.OutputUtilities;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.filesystem.constants.Constants.GET_CHILDREN;
import static io.cloudslang.content.filesystem.constants.Descriptions.GetChildren.*;
import static io.cloudslang.content.filesystem.constants.ResultsName.COUNT;

public class GetChildrenAction {

    @Action(name = GET_CHILDREN,
            outputs = {
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESCRIPTION),
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESCRIPTION),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESCRIPTION),
                    @Output(value = COUNT, description = COUNT_DESCRIPTION)
            },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = OutputNames.RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL),
                    @Response(text = ResponseNames.FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, isOnFail = true, isDefault = true)
            })
    public Map<String,String> execute(@Param(value = InputNames.SOURCE, description = SOURCE_DESCRIPTION, required = true) String source,
                                      @Param(value = InputNames.DELIMITER, description = DELIMITER_DESCRIPTION, required = true) String delimiter){


        try{
            return GetChildrenService.execute(GetChildrenInputs.builder().source(source).delimiter(delimiter).build());
        }catch(Exception ex){
            return OutputUtilities.getFailureResultsMap(ex);
        }
    }
}
