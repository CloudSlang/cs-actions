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
package io.cloudslang.content.services;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import io.cloudslang.content.exceptions.IteratorProcessorException;
import io.cloudslang.content.utils.IteratorProcessor;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;

public class ListService {
    public static Map<String, String> iterate(String list, String separator, GlobalSessionObject<Map<String, Object>> globalSessionObject) {

        IteratorProcessor iterator = new IteratorProcessor();
        Map<String, String> returnResult = new HashMap<String, String>();
        returnResult.put("result", "failed");

        try {
            iterator.init(list, separator, globalSessionObject);
            if (iterator.hasNext()) {
                returnResult.put("index", Integer.toString(iterator.getIndex()));
                returnResult.put("resultString", iterator.getNext(globalSessionObject));
                if (iterator.hasNext()) {
                    returnResult.put("result", "has more");
                    returnResult.put("returnCode", "0");
                } else {
                    returnResult.put("resultString", "");
                    returnResult.put("returnCode", "1");
                    iterator.setStepSessionEnd(globalSessionObject);
                    returnResult.put("result", "no more");
                }
            }
        } catch (IteratorProcessorException e) {
            returnResult.put("result", "failed");
            returnResult.put("resultString", e.getMessage());
            returnResult.put("returnCode", "-1");
        }
        return returnResult;
    }
}
