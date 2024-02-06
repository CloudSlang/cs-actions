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

package io.cloudslang.content.json.services;
import com.hp.oo.sdk.content.plugin.StepSerializableSessionObject;
import io.cloudslang.content.json.utils.*;

import java.util.*;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.json.utils.Constants.ArrayIteratorAction.INDEX;
import static io.cloudslang.content.json.utils.Constants.ArrayIteratorAction.RETURN_RESULT;
import static io.cloudslang.content.json.utils.Constants.InputNames.*;

public class ArrayListService {
    public static Map<String, String> iterate(String array, StepSerializableSessionObject sessionObject){
        IteratorProcessor iterate = new IteratorProcessor();
        Map<String, String> returnResult = new HashMap<>();
        returnResult.put(RETURN_RESULT, FAILED);

        try {
            iterate.init(array,  sessionObject);
            if (iterate.getIndex() == 0 && iterate.getLength() == 1 || iterate.getIndex() == iterate.getLength()-1) {
                returnResult.put(RETURN_RESULT, HAS_MORE);
                returnResult.put(RESULT_STRING, iterate.getNext(sessionObject));
                returnResult.put(RETURN_CODE, ZERO);
            } else if (iterate.getIndex() == 1 && iterate.getLength() == 1 || iterate.getIndex() == iterate.getLength()) {
                returnResult.put(RETURN_RESULT, NO_MORE);
                returnResult.put(RESULT_STRING, EMPTY_STRING);
                returnResult.put(RETURN_CODE, ONE);
            }
            else if (iterate.hasNext()) {
                returnResult.put(INDEX, Integer.toString(iterate.getIndex()));
                returnResult.put(RESULT_STRING, iterate.getNext(sessionObject));
                if (iterate.hasNext()) {
                    returnResult.put(RETURN_RESULT, HAS_MORE);
                    returnResult.put(RETURN_CODE, ZERO);
                } else {
                        returnResult.put(RESULT_STRING, EMPTY_STRING);
                        returnResult.put(RETURN_CODE, ONE);
                        iterate.setStepSessionEnd(sessionObject);
                        returnResult.put(RETURN_RESULT, NO_MORE);
                    }
                }
        } catch (Exception e) {
            returnResult.put(RETURN_RESULT, e.getMessage());
            returnResult.put(RESULT_STRING, EMPTY_STRING);
            returnResult.put(RETURN_CODE, MINUS_ONE);
        }
        return returnResult;
    }
}
