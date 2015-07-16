/*******************************************************************************
 * (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/

package io.cloudslang.content.httpclient.consume;

import io.cloudslang.content.httpclient.HttpClientInputs;
import io.cloudslang.content.httpclient.ScoreHttpClient;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeaderValueParser;

import java.io.*;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: davidmih
 * Date: 7/28/14
 */
public class HttpResponseConsumer {
    private HttpResponse httpResponse;
    private String responseCharacterSet;
    private String destinationFile;

    public HttpResponseConsumer setHttpResponse(HttpResponse httpResponse) {
        this.httpResponse = httpResponse;
        return this;
    }

    public HttpResponseConsumer setResponseCharacterSet(String responseCharacterSet) {
        this.responseCharacterSet = responseCharacterSet;
        return this;
    }

    public HttpResponseConsumer setDestinationFile(String destinationFile) {
        this.destinationFile = destinationFile;
        return this;
    }

    public void consume(Map<String, String> result) throws IOException {
        if (httpResponse.getEntity() != null) {
            if (responseCharacterSet == null || responseCharacterSet.isEmpty()) {
                Header contentType = httpResponse.getEntity().getContentType();
                if (contentType != null) {
                    String value = contentType.getValue();
                    NameValuePair[] nameValuePairs = BasicHeaderValueParser.parseParameters(value, BasicHeaderValueParser.INSTANCE);
                    for (NameValuePair nameValuePair : nameValuePairs) {
                        if (nameValuePair.getName().equalsIgnoreCase("charset")) {
                            responseCharacterSet = nameValuePair.getValue();
                            break;
                        }
                    }
                }
                if (responseCharacterSet == null || responseCharacterSet.isEmpty()) {
                    responseCharacterSet = Consts.ISO_8859_1.name();
                }
            }
            consumeResponseContent(result);
        }
    }

    protected void consumeResponseContent(Map<String, String> result) throws IOException {
        if (StringUtils.isEmpty(destinationFile)) {
            String document;
            try {
                document = IOUtils.toString(httpResponse.getEntity().getContent(), responseCharacterSet);
            } catch (UnsupportedCharsetException e) {
                throw new IllegalArgumentException("Could not parse responseCharacterSet. " + e.getMessage(), e);
            }
            result.put(ScoreHttpClient.RETURN_RESULT, document);
        } else {
            consumeToDestinationFile();
        }
    }

    private void consumeToDestinationFile() throws IOException {
        BufferedReader reader;
        BufferedWriter fileWriter = null;
        FileOutputStream fos = null;
        try {
            try {
                reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), responseCharacterSet));
                fos = new FileOutputStream(new File(destinationFile));
                fileWriter = new BufferedWriter(new OutputStreamWriter(fos, responseCharacterSet));
            } catch (UnsupportedEncodingException e) {
                throw new IllegalArgumentException("Could not parse '" + HttpClientInputs.RESPONSE_CHARACTER_SET
                        + "'. " + e.getMessage(), e);
            }
            char[] buffer = new char[1024];
            int b;
            while ((b = reader.read(buffer, 0, buffer.length)) != -1) {
                fileWriter.write(buffer, 0, b);
            }
            fileWriter.flush();
        } finally {
            if (fos != null) {
                safeClose(fos);
            }
            if (fileWriter != null) {
                safeClose(fileWriter);
            }
        }
    }

    private void safeClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }
}
