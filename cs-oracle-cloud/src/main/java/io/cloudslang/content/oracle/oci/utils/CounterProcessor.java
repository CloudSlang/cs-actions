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


package io.cloudslang.content.oracle.oci.utils;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import io.cloudslang.content.oracle.oci.exceptions.CounterImplException;

import java.util.HashMap;
import java.util.Map;

public class CounterProcessor {

    private static final String END_INDEX="endIndex";
    private static final String INDEX="index";
    private static final Long endIndex = -1L;
    private long end;
    private long start;
    private int index;
    private int increment;

    public CounterProcessor() {
    }

    private boolean initialized(GlobalSessionObject<Map<String, Object>> session) {
        return (session.get().get(INDEX) != null);

    }

    public void init(String to, String from, String by, boolean reset, GlobalSessionObject<Map<String, Object>> session) throws CounterImplException {
        /*
         * If the session resource is not ini
         */
        if (session.get() == null) {
            session.setResource(new CounterSessionResource(new HashMap<String, Object>()));
        }
        Map<String, Object> sessionMap = session.get();
        try {
            start = Long.parseLong(from.trim());
            end = Long.parseLong(to.trim());
            if (by == null || by.length() == 0)
                increment = 1;
            else
                try {
                    increment = Integer.parseInt(by);
                } catch (Exception e) {
                    increment = 1;
                }
        } catch (Exception e) {
            throw new CounterImplException("Start or end is not a long integer, or by is not an integer.\nfrom: " + from + "\nto: " + to + "\nby:" + by);
        }
        try{
            index = (Integer) sessionMap.get(INDEX);
        } catch (Exception e) {
            index = 0;
        }

        if(index == 0 && initialized(session)) {
            sessionMap.put(INDEX, 0);

        }
        // ok, now push data into context
        if (!initialized(session)) {
            sessionMap.put(INDEX, 0);
        }

        // pull data from context
        this.index = (Integer) sessionMap.get(INDEX);
    }

    public String getNext(GlobalSessionObject<Map<String, Object>> session) {
        String ret = null;
        if (start < end) {
            if (start + index <= end) {
                ret = "" + (index + start);
                index += increment;
                session.get().put(INDEX, index);
            } else
                index += increment;
        } else if (start - index >= end) {
            ret = "" + (start - index);
            index += increment;
            session.get().put(INDEX, index);
        } else
            index += increment;
        return ret;
    }

    public boolean hasNext() {
        long diff = (start < end) ? end - start : start - end;
        if (index <= diff) {
            return true;
        } else {
            return false;
        }
    }

    public void setStepSessionEnd(GlobalSessionObject<Map<String, Object>> session) {
        session.get().put(END_INDEX,endIndex);
    }


    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIncrement() {
        return increment;
    }

    public void setIncrement(int increment) {
        this.increment = increment;
    }
}
