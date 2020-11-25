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

public class IsDirectoryAction {

    private final IsDirectoryService service = new IsDirectoryService();

    @Action(name = "Is Directory",
            outputs = {
                @Output(RETURN_RESULT),
                @Output(RETURN_CODE),
                @Output(EXCEPTION)
            },
            responses = {
                @Response(text = ResponseNames.SUCCESS,
                          field = OutputNames.RETURN_CODE,
                          value = ReturnCodes.SUCCESS,
                          matchType = MatchType.COMPARE_EQUAL),
                @Response(text = ResponseNames.FAILURE,
                          field = RETURN_CODE,
                          value = ReturnCodes.FAILURE,
                          matchType = MatchType.COMPARE_EQUAL,
                          isOnFail = true, isDefault = true)
            })
    public Map<String,String> execute(@Param(value = InputNames.SOURCE, required = true) String source){
        try{
            IsDirectoryInputs inputs = new IsDirectoryInputs(source);
            return service.execute(inputs);
        }catch(Exception ex){
            return OutputUtilities.getFailureResultsMap(ex);
        }
    }
}
