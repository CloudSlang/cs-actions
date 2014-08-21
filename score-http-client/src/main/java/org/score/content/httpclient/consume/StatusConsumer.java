package org.score.content.httpclient.consume;

import org.apache.http.StatusLine;
import org.score.content.httpclient.HttpClient;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: davidmih
 * Date: 7/28/14
 */
public class StatusConsumer {
    private StatusLine statusLine;

    public StatusConsumer setStatusLine(StatusLine statusLine) {
        this.statusLine = statusLine;
        return this;
    }

    public void consume(Map<String, String> returnResult) {
        returnResult.put(HttpClient.STATUS_CODE, String.valueOf(statusLine.getStatusCode()));
        returnResult.put(HttpClient.PROTOCOL_VERSION, statusLine.getProtocolVersion().toString());
        returnResult.put(HttpClient.REASON_PHRASE, String.valueOf(String.valueOf(statusLine.getReasonPhrase())));
    }
}
