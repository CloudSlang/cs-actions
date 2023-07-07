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
import io.cloudslang.content.filesystem.entities.IsDirectoryInputs;
import io.cloudslang.content.filesystem.services.IsDirectoryService;
import io.cloudslang.content.utils.OutputUtilities;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.filesystem.utils.Descriptions.Common.EXCEPTION_DESCRIPTION;
import static io.cloudslang.content.filesystem.utils.Descriptions.Common.RETURN_CODE_DESCRIPTION;
import static io.cloudslang.content.filesystem.utils.Descriptions.IsDirectory.*;

public class IsDirectoryAction {

    private final IsDirectoryService service = new IsDirectoryService();


    /**
     * Checks to see if the file/folder a path points to is a directory.
     *
     * @param source    The file to read. It must be an absolute path.
     * @return a map with following entries:
     * return_result: A success message if source is a directory. Otherwise, it will contain the exception message.
     * return_code: 0 if the operation succeeded, -1 otherwise.
     * exception: The exception's stack trace if the operation failed. Empty otherwise.
     * @result SUCCESS       Source is a directory.
     * @result FAILURE       Source is not a directory or operation failed.
     */
    @Action(name = "Is Directory",
            outputs = {
                @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESCRIPTION),
                @Output(value = RETURN_CODE,description = RETURN_CODE_DESCRIPTION),
                @Output(value = EXCEPTION, description = EXCEPTION_DESCRIPTION)
            },
            responses = {
                @Response(text = ResponseNames.SUCCESS,
                          field = OutputNames.RETURN_CODE,
                          value = ReturnCodes.SUCCESS,
                          matchType = MatchType.COMPARE_EQUAL,
                        description = SUCCESS_DESCRIPTION),
                @Response(text = ResponseNames.FAILURE,
                          field = RETURN_CODE,
                          value = ReturnCodes.FAILURE,
                          matchType = MatchType.COMPARE_EQUAL,
                          description = FAILURE_DESCRIPTION,
                          isOnFail = true, isDefault = true)
            })
    public Map<String,String> execute(@Param(value = InputNames.SOURCE, required = true,description = SOURCE_DESCRIPTION) String source){
        try{
            IsDirectoryInputs inputs = new IsDirectoryInputs(source);
            return service.execute(inputs);
        }catch(Exception ex){
            return OutputUtilities.getFailureResultsMap(ex);
        }
    }
}
