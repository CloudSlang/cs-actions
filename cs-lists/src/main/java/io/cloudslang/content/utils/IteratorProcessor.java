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
package io.cloudslang.content.utils;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import io.cloudslang.content.exceptions.IteratorProcessorException;

import java.util.HashMap;
import java.util.Map;

public class IteratorProcessor {

    private static final String TEXT_ARRAYLIST = "iterator_arraylist";
    private static final String TEXT_INDEX = "iterator_index";
    private static final String TEXT_STRINGLIST = "iterator_stringlist";
    private String[] splitList;
    private int index;

    public IteratorProcessor() {
    }

    private boolean initialized(GlobalSessionObject<Map<String, Object>> session) {
        return (session.get().get(TEXT_ARRAYLIST) != null &&
                session.get().get(TEXT_INDEX) != null);
    }

    public void init(String list, String delim, GlobalSessionObject<Map<String, Object>> session) throws IteratorProcessorException {

        if (session.get() == null) {
            session.setResource(new IteratorSessionResource(new HashMap<String, Object>()));
        }

        Map<String, Object> sessionMap = session.get();
        if (list != null && delim != null) {
            if (delim.length() == 0) {
                throw new IteratorProcessorException("delimiter has null or 0 length");
            }

            // Get array list value
            try {
                splitList = (String[]) sessionMap.get(TEXT_ARRAYLIST);
            } catch (Exception e) {
                splitList = new String[0];
            }

            // Get String list value
            String stringList;
            try {
                stringList = (String) sessionMap.get(TEXT_STRINGLIST);
            } catch (Exception e) {
                stringList = "";
            }

            // Get index value
            try {
                index = (Integer) sessionMap.get(TEXT_INDEX);
            } catch (Exception e) {
                index = 0;
            }

            if (splitList == null) {
                splitList = new String[0];
            }

            if (stringList == null) {
                stringList = "";
            }

            if (index == 0 && initialized(session)) {
                if (list.indexOf(stringList) != 0) {
                    stringList = "";
                    splitList = new String[0];
                }
                sessionMap.put(TEXT_ARRAYLIST, splitList);
                sessionMap.put(TEXT_INDEX, 0);
            }

            if (!initialized(session) || !stringList.equals(list)) {
                String lastIn = list;
                int index = list.indexOf(stringList);
                if (index == 0) {
                    if (list.length() == 0) {
                        throw new IteratorProcessorException("list has null or 0 length");
                    }
                    splitList = ListProcessor.toArray(list, delim);
                } else {
                    if (initialized(session) && list.startsWith(delim)) {
                        list = list.substring(1, list.length());
                    }
                    String listarray[] = ListProcessor.toArray(list, delim);
                    splitList = combine(this.splitList, listarray);
                }

                if (list == "" && splitList.length == 0) {
                    throw new IteratorProcessorException("list has null or zero length");
                }

                // ok, now push data into context
                if (!initialized(session)) {
                    sessionMap.put(TEXT_INDEX, 0);
                }

                sessionMap.put(TEXT_ARRAYLIST, splitList);
                sessionMap.put(TEXT_STRINGLIST, lastIn);
            }
        }

        // pull data from context
        splitList = (String[]) sessionMap.get(TEXT_ARRAYLIST);
        index = (Integer) sessionMap.get(TEXT_INDEX);
    }

    private String[] combine(String[] s1, String[] s2) {
        String[] temp = new String[s1.length + s2.length];
        System.arraycopy(s1, 0, temp, 0, s1.length);
        System.arraycopy(s2, 0, temp, s1.length, s2.length);
        return temp;
    }

    public String getNext(GlobalSessionObject<Map<String, Object>> session) {
        if (index < splitList.length) {
            String ret = splitList[index];
            index += 1;
            session.get().put(TEXT_INDEX, index);

            return ret;
        } else {
            this.index += 1;
            session.get().put(TEXT_INDEX, index);

            return null;
        }
    }

    public boolean hasNext() {
        return index <= splitList.length;
    }

    public void setStepSessionEnd(GlobalSessionObject<Map<String, Object>> session) {
        session.get().put(TEXT_INDEX, Integer.toString(0));
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
