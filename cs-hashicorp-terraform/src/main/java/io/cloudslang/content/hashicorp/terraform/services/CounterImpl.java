/*
 * (c) Copyright 2020 Micro Focus, L.P.
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

package io.cloudslang.content.hashicorp.terraform.services;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import io.cloudslang.content.hashicorp.terraform.exceptions.CounterImplException;
import io.cloudslang.content.hashicorp.terraform.utils.CounterProcessor;

import java.util.HashMap;
import java.util.Map;

public class CounterImpl {
    public static Map<String, String> counter(String to, String from, String by, boolean reset, GlobalSessionObject<Map<String, Object>> globalSessionObject) {

        CounterProcessor counter = new CounterProcessor();
        Map<String, String> returnResult = new HashMap<String, String>();
        returnResult.put("result", "failed");

        try {
            counter.init(to,from,by,reset, globalSessionObject);
            if (counter.hasNext()) {
                returnResult.put("index", Integer.toString(counter.getIndex()));
                returnResult.put("resultString", counter.getNext(globalSessionObject));
                if (counter.hasNext()) {
                    returnResult.put("result", "has more");
                    returnResult.put("returnCode", "0");
                } else {
                    returnResult.put("resultString", "");
                    returnResult.put("returnCode", "1");
                    counter.setStepSessionEnd(globalSessionObject);
                    returnResult.put("result", "no more");
                }
            }
        } catch (CounterImplException e) {
            returnResult.put("result", "failed");
            returnResult.put("resultString", e.getMessage());
            returnResult.put("returnCode", "-1");
        }
        return returnResult;
    }







}
