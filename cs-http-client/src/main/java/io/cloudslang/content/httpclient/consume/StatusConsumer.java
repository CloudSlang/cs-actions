/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.httpclient.consume;

import org.apache.http.StatusLine;

import java.util.Map;

import static io.cloudslang.content.httpclient.entities.HttpClientOutputs.PROTOCOL_VERSION;
import static io.cloudslang.content.httpclient.entities.HttpClientOutputs.REASON_PHRASE;
import static io.cloudslang.content.httpclient.entities.HttpClientOutputs.STATUS_CODE;

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
        returnResult.put(STATUS_CODE, String.valueOf(statusLine));

        String protocolVersion = (this.statusLine != null && this.statusLine.getProtocolVersion() != null) ?
                this.statusLine.getProtocolVersion().toString() : "";
        returnResult.put(PROTOCOL_VERSION, protocolVersion);

        String reasonPhrase = (this.statusLine != null && this.statusLine.getReasonPhrase() != null) ?
                this.statusLine.getReasonPhrase() : "";
        returnResult.put(REASON_PHRASE, reasonPhrase);
    }
}
