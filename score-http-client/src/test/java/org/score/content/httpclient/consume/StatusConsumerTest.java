package org.score.content.httpclient.consume;

import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Adina Tusa
 * Date: 8/20/14
 */
public class StatusConsumerTest {

    private StatusConsumer statusConsumer;

    @Before
    public void setUp() {
        statusConsumer = new StatusConsumer();
    }

    @Test
    public void consume() {
        Map<String, String> returnResult = new HashMap<>();
        StatusLine statusLine = new StatusLine() {
            @Override
            public ProtocolVersion getProtocolVersion() {
                return new ProtocolVersion("HTTP", 1, 1);
            }

            @Override
            public int getStatusCode() {
                return 200;
            }

            @Override
            public String getReasonPhrase() {
                return "OK";
            }
        };
        statusConsumer.setStatusLine(statusLine).consume(returnResult);
    }

    @Test
    public void consumeWithNulls() {
        Map<String, String> returnResult = new HashMap<>();
        statusConsumer.setStatusLine(null).consume(returnResult);
    }
}
