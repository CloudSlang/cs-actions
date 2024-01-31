/*
 * Copyright 2021-2024 Open Text
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


package io.cloudslang.content.json.actions;

import com.hp.oo.sdk.content.annotations.*;
import com.hp.oo.sdk.content.plugin.ActionMetadata.*;
import com.hp.oo.sdk.content.plugin.*;
import io.cloudslang.content.constants.*;
import io.cloudslang.content.json.services.*;
import io.cloudslang.content.json.utils.*;

import java.util.*;

import static io.cloudslang.content.constants.ResponseNames.*;
import static io.cloudslang.content.json.utils.Constants.ArrayIteratorAction.*;
import static io.cloudslang.content.json.utils.Descriptions.ArrayIteratorDescription.*;
import com.hp.oo.sdk.content.plugin.StepSerializableSessionObject;

public class ArrayIteratorAction {

    @Action(name = JSON_ARRAY_OP, description = ARRAY_ITERATOR_DESCRIPTION,
            outputs = {
                    @Output(OutputNames.RETURN_CODE),
                    @Output(OutputNames.RETURN_RESULT),
                    @Output(OutputNames.EXCEPTION)
            },
            responses = {
                    @Response(text = HAS_MORE, field = RETURN_RESULT, value = HAS_MORE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED, description = HAS_MORE_DESC),
                    @Response(text = NO_MORE, field = RETURN_RESULT, value = NO_MORE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED, description = NO_MORE_DESC),
                    @Response(text = FAILURE, field = RETURN_RESULT, value = FAILURE, matchType = MatchType.COMPARE_EQUAL, isDefault = true, isOnFail = true, description = FAILURE_DESC)})

    public Map<String, String> execute(@Param(value = Constants.InputNames.ARRAY, required = true) String array,
                                       @Param(value = "sessionIterator") StepSerializableSessionObject sessionIterator) {

        return ArrayListService.iterate(array, sessionIterator);
    }
}
