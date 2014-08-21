package org.score.content.httpclient.consume;

import org.apache.http.Header;
import org.score.content.httpclient.ScoreHttpClient;

import java.util.Map;

public class HeadersConsumer {
    private Header[] headers;

    public HeadersConsumer setHeaders(Header[] headers) {
        this.headers = headers;
        return this;
    }

    public void consume(Map<String, String> returnResult) {
        StringBuilder result = new StringBuilder();
        if (headers != null) {
            for (Header header : headers) {
                result.append(header.toString()).append("\r\n");
            }
            if (result.length() != 0) {
                result.delete(result.length() - 2, result.length());
            }
        }
        returnResult.put(ScoreHttpClient.RESPONSE_HEADERS, result.toString());
    }
}