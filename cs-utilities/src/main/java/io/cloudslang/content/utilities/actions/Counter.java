/*
 * Copyright 2022-2023 Open Text
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


package io.cloudslang.content.utilities.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.oo.sdk.content.plugin.StepSerializableSessionObject;
import io.cloudslang.content.utilities.entities.CounterImpl;
import io.cloudslang.content.utilities.exceptions.CounterImplException;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;

import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.utilities.util.Constants.CounterConstants.*;



import static io.cloudslang.content.utilities.util.Descriptions.Counter.*;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;


public class Counter {

    public static final String RESULT_DBG_INDEX = "index";

    private Map<String, String> count(String from, String to, String incrementBy, boolean reset, StepSerializableSessionObject session) {
        CounterImpl counter = new CounterImpl();
        Map<String, String> returnResult = new HashMap<String, String>();
        returnResult.put("result", "failed");

        try {
            counter.init(from, to, incrementBy, reset, session);
            if (counter.hasNext()) {
                returnResult.put(RESULT_DBG_INDEX, Long.toString(counter.getIndex()));
                returnResult.put(RETURN_RESULT, counter.getNext(session));
                returnResult.put(RESULT, "has more");
                returnResult.put(RETURN_CODE,"0");
            } else {
                counter.setStepSessionEnd(session);
                returnResult.put(RETURN_RESULT, "");
                returnResult.put(RESULT, "no more");
                returnResult.put(RETURN_CODE,"1");
            }
        } catch (CounterImplException e) {
            returnResult.put(RESULT, "failed");
            returnResult.put(RETURN_RESULT, e.getMessage());
            returnResult.put(RETURN_CODE,"-1");
        }
        return returnResult;
    }

    @Action(name = COUNTER_OPERATION_NAME, description = COUNTER_DESC,
            outputs = {
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = RESULT, description = RESULT_DESC)},
            responses = {
                    @Response(text = HASMORE, field = RESULT, value = HASMORE, matchType = MatchType.COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = NOMORE, field = RESULT, value = NOMORE, matchType = MatchType.COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RESULT, value = FAILURE, matchType = MatchType.COMPARE_EQUAL, isDefault = true, isOnFail = true, responseType = ERROR, description = FAILURE_DESC)})
    public Map<String, String> execute(@Param(value = FROM, required = true, description = FROM_DESC) String from,
                                       @Param(value = TO, required = true, description = TO_DESC) String to,
                                       @Param(value = INCREMENT_BY, description = INCREMENT_BY_DESC) String incrementBy,
                                       @Param(value = RESET, description = RESET_DESC) String reset,
                                       @Param("sessionCounter") StepSerializableSessionObject sessionCounter) {

        reset = defaultIfEmpty(reset, BOOLEAN_FALSE);
        incrementBy = defaultIfEmpty(incrementBy, INCREMENT_BY_DEFAULT_VALUE);

        return count(from, to, incrementBy, Boolean.parseBoolean(reset), sessionCounter);
    }
}
