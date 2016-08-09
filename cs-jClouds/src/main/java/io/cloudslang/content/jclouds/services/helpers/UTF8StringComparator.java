package io.cloudslang.content.jclouds.services.helpers;

import io.cloudslang.content.jclouds.entities.constants.Constants;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Comparator;
import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 8/8/2016.
 */
class UTF8StringComparator implements Comparator<Map.Entry<String, String>>, Serializable {
    public int compare(Map.Entry<String, String> e1, Map.Entry<String, String> e2) {
        try {
            byte[] b1 = e1.getKey().getBytes(Constants.Miscellaneous.ENCODING);
            byte[] b2 = e2.getKey().getBytes(Constants.Miscellaneous.ENCODING);

            int pos = 0;
            for (; pos < Math.min(b1.length, b2.length); pos++) {
                if (b1[pos] == b2[pos]) {
                    continue;
                }
                return b1[pos] < b2[pos] ? -1 : 1;
            }

            if (b1.length == b2.length) {
                return 0;
            }

            return b1.length < b2.length ? -1 : 1;
        } catch (UnsupportedEncodingException uee) {
            uee.printStackTrace();
        }
        return 0;
    }
}