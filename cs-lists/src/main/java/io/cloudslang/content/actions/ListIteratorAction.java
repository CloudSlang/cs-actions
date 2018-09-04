/*
 * (c) Copyright 2018 Micro Focus
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
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import io.cloudslang.content.services.ListService;
import io.cloudslang.content.utils.Constants;

import java.util.Map;

import static io.cloudslang.content.utils.Constants.Descriptions.*;
import static io.cloudslang.content.utils.Constants.OutputNames.*;
import static io.cloudslang.content.utils.Constants.ResponseNames.FAILURE;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class ListIteratorAction {

    @Action(name = "List Iterator", description = LIST_ITERATOR,
            outputs = {
                    @Output(value = RESULT_STRING, description = RESULT_STRING_DESC),
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC)},
            responses = {
                    @Response(text = HAS_MORE, field = RESULT_TEXT, value = HAS_MORE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED, description = HAS_MORE_DESC),
                    @Response(text = NO_MORE, field = RESULT_TEXT, value = NO_MORE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED, description = NO_MORE_DESC),
                    @Response(text = FAILURE, field = RESULT_TEXT, value = FAILURE, matchType = MatchType.COMPARE_EQUAL, isDefault = true, isOnFail = true, description = FAILURE_DESC)})
    public Map<String, String> execute(@Param(value = LIST, required = true, description = LIST_DESC) String list,
                                       @Param(value = SEPARATOR, required = true, description = Constants.Descriptions.SEPARATOR_DESC) String separator,
                                       @Param("globalSessionObject") GlobalSessionObject<Map<String, Object>> globalSessionObject) {

        //Get default values
        final String separatorImp = defaultIfEmpty(separator, ",");

        return ListService.iterate(list, separatorImp, globalSessionObject);
    }
}
