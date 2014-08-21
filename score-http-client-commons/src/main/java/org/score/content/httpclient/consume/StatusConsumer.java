package org.score.content.httpclient.consume;

import org.apache.http.StatusLine;
import org.score.content.httpclient.ScoreHttpClient;

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
        int statusLine = (this.statusLine != null) ? this.statusLine.getStatusCode() : 0;
        returnResult.put(ScoreHttpClient.STATUS_CODE, String.valueOf(statusLine));

        String protocolVersion = (this.statusLine != null && this.statusLine.getProtocolVersion() != null) ?
                this.statusLine.getProtocolVersion().toString() : "";
        returnResult.put(ScoreHttpClient.PROTOCOL_VERSION, protocolVersion);

        String reasonPhrase = (this.statusLine != null && this.statusLine.getReasonPhrase() != null) ?
                this.statusLine.getReasonPhrase() : "";
        returnResult.put(ScoreHttpClient.REASON_PHRASE, reasonPhrase);
    }
}
