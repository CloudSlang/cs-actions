package org.score.content.httpclient;

import org.apache.http.Header;

import java.util.Map;

public class HeadersConsumer {
    private Header[] headers;

    public HeadersConsumer setHeaders(Header[] headers) {
        this.headers = headers;
        return this;
    }

    public void consume(Map<String, String> returnResult) {
        StringBuffer result = new StringBuffer();
        for (Header header : headers) {
            result.append(header.toString()).append("\r\n");
        }
        if (result.length() != 0) {
            result.delete(result.length() - 2, result.length());
        }
        returnResult.put(HttpClient.RESPONSE_HEADERS, result.toString());
    }
}