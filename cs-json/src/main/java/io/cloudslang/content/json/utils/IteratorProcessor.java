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
package io.cloudslang.content.json.utils;

import com.google.gson.*;
import com.hp.oo.sdk.content.plugin.*;
import io.cloudslang.content.utils.*;

import java.util.*;

import static io.cloudslang.content.json.utils.Constants.ArrayIteratorAction.*;
import static io.cloudslang.content.json.utils.ExceptionMsgs.EMPTY_JSON_ARRAY;
import static io.cloudslang.content.json.utils.ExceptionMsgs.INVALID_JSON_ARRAY;

public class IteratorProcessor {

    private int index;
    private JsonArray jsonElements;

    public void init(String array, GlobalSessionObject<Map<String, Object>> session) throws Exception {

        if (session.get() == null) {
            Map<String, Object> initialIndex = new HashMap<String, Object>();
            initialIndex.put(INDEX, 0);
            session.setResource(new IteratorSessionResource(initialIndex));
        }

        Map<String, Object> sessionMap = session.get();
        if (StringUtilities.isBlank(array)) {
            throw new Exception(String.format(ExceptionMsgs.NULL_OR_EMPTY_INPUT, Constants.InputNames.ARRAY));
        }



        index = Integer.parseInt(String.valueOf(sessionMap.get(INDEX)));

        try {
        jsonElements = new Gson().fromJson(array, JsonArray.class);
        }catch (Exception ex) {
            throw new Exception(INVALID_JSON_ARRAY);
        }
        if(jsonElements.size() == 0)
            throw new Exception(EMPTY_JSON_ARRAY);
    }

    public boolean hasNext() {
        return index < jsonElements.size();
    }

    public int getLength() {
        return jsonElements.size();
    }

    public int getIndex() {
        return index;
    }

    public String getNext(GlobalSessionObject<Map<String, Object>> session) {
        if (index < jsonElements.size()) {
            String ret = String.valueOf(jsonElements.get(index));
            index += 1;
            session.get().put(INDEX, index);

            return ret;
        } else {
            this.index += 1;
            session.get().put(INDEX, index);

            return null;
        }
    }

    public void setStepSessionEnd(GlobalSessionObject<Map<String, Object>> session) {
        session.get().put(INDEX, Integer.toString(0));
    }

}
