package org.score.content.httpclient;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: davidmih
 * Date: 6/27/14
 */
public class NameValuePairList extends ArrayList<NameValuePair> {

    public NameValuePairList(Map<String, String> keyValueMap) {
        for (Map.Entry<String, String> entry : keyValueMap.entrySet()) {
            this.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
    }


}
