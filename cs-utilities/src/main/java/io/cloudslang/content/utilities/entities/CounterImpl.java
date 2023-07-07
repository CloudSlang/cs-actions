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


package io.cloudslang.content.utilities.entities;

import com.hp.oo.sdk.content.plugin.StepSerializableSessionObject;
import io.cloudslang.content.utilities.exceptions.CounterImplException;

public class CounterImpl {
    private static final Long endIndex = -1L;
    private long end;
    private long start;
    private long index;
    private int increment;

    public void init(String from, String to, String by, boolean reset, StepSerializableSessionObject session) throws CounterImplException {
        /*
         * If the session resource is not ini
         */
        if (session.getValue() == null || endIndex.equals(session.getValue()) || reset) {
            session.setValue(Long.valueOf(0));
        }

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

        // pull data from context
        this.index = (Long) session.getValue();
    }

    public String getNext(StepSerializableSessionObject session) {
        String ret = null;
        if (start < end) {
            if (start + index <= end) {
                ret = "" + (index + start);
                index += increment;
                session.setValue(index);
            } else
                index += increment;
        } else if (start - index >= end) {
            ret = "" + (start - index);
            index += increment;
            session.setValue(index);
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

    public void setStepSessionEnd(StepSerializableSessionObject session) {
        session.setValue(endIndex);
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

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public int getIncrement() {
        return increment;
    }

    public void setIncrement(int increment) {
        this.increment = increment;
    }
}