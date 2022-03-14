/*
 * (c) Copyright 2022 Micro Focus, L.P.
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
package io.cloudslang.content.utilities.entities;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import io.cloudslang.content.utilities.util.CounterProcessor;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.utilities.util.Constants.CounterConstants.INDEX;
import static io.cloudslang.content.utilities.util.Constants.CounterConstants.RESULT;
import static io.cloudslang.content.utils.Constants.OutputNames.RETURN_RESULT;

public class CounterImpl {
    public static Map<String, String> counter(String to, String from, String by, boolean reset, GlobalSessionObject<Map<String, Object>> globalSessionObject) {

        CounterProcessor counter = new CounterProcessor();
        Map<String, String> returnResult = new HashMap<>();
        returnResult.put("result", "failed");

        try {
            counter.init(to, from, by, reset, globalSessionObject);
            if (counter.hasNext()) {
                returnResult.put(INDEX, Integer.toString(counter.getIndex()));
                returnResult.put(RETURN_RESULT, counter.getNext(globalSessionObject));
                if (counter.hasNext()) {
                    returnResult.put(RESULT, "has more");
                    returnResult.put(RETURN_CODE, "0");
                } else {
                    returnResult.put(RETURN_RESULT, "");
                    returnResult.put(RETURN_CODE, "1");
                    counter.setStepSessionEnd(globalSessionObject);
                    returnResult.put(RESULT, "no more");
                }
            }
        } catch (Exception e) {
            returnResult.put(RESULT, "failed");
            returnResult.put(RETURN_RESULT, e.getMessage());
            returnResult.put(RETURN_CODE, "-1");
        }
        return returnResult;
    }


}
