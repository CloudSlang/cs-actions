package io.cloudslang.content.hashicorp.terraform.services;

import com.hp.oo.sdk.content.plugin.StepSerializableSessionObject;

public class CounterImpl {
    private static final Long endIndex = -1L;
    private long end;
    private long start;
    private long index;
    private int increment;

    public void init(String to, String from, String by, boolean reset, StepSerializableSessionObject session) throws CounterImplException {
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
