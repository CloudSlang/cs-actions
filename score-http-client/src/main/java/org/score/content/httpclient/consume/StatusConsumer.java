package org.score.content.httpclient.consume;

import org.apache.http.StatusLine;
import org.score.content.httpclient.HttpClientAction;

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
        returnResult.put(HttpClientAction.STATUS_CODE, String.valueOf(statusLine.getStatusCode()));
        returnResult.put(HttpClientAction.PROTOCOL_VERSION, statusLine.getProtocolVersion().toString());
        returnResult.put(HttpClientAction.REASON_PHRASE, String.valueOf(String.valueOf(statusLine.getReasonPhrase())));
    }
}
