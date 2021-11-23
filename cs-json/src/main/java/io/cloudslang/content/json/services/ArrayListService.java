/*
 * (c) Copyright 2021 EntIT Software LLC, a Micro Focus company, L.P.
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
package io.cloudslang.content.json.services;

import com.hp.oo.sdk.content.plugin.*;
import io.cloudslang.content.json.utils.*;

import java.util.*;

import static io.cloudslang.content.json.utils.Constants.ArrayIteratorAction.INDEX;
import static io.cloudslang.content.json.utils.Constants.ArrayIteratorAction.RESULT_TEXT;

public class ArrayListService {
    public static Map<String, String> iterate(String array, GlobalSessionObject<Map<String, Object>> globalSessionObject){
        IteratorProcessor iterate = new IteratorProcessor();
        Map<String, String> returnResult = new HashMap<>();
        returnResult.put(RESULT_TEXT, "failed");

        try {
            iterate.init(array,  globalSessionObject);
            if (iterate.hasNext()) {
                returnResult.put(INDEX, Integer.toString(iterate.getIndex()));
                returnResult.put("resultString", iterate.getNext(globalSessionObject));
                if (iterate.hasNext()) {
                    returnResult.put(RESULT_TEXT, "has more");
                    returnResult.put("returnCode", "0");
                } else {
                    returnResult.put("resultString", "");
                    returnResult.put("returnCode", "1");
                    iterate.setStepSessionEnd(globalSessionObject);
                    returnResult.put(RESULT_TEXT, "no more");
                }
            }
        } catch (Exception e) {
            returnResult.put(RESULT_TEXT, "failed");
            returnResult.put("resultString", e.getMessage());
            returnResult.put("returnCode", "-1");
        }
        return returnResult;
    }
}
