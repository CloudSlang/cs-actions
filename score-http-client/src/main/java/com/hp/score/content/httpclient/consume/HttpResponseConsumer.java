/*
 * Licensed to Hewlett-Packard Development Company, L.P. under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
*/
package com.hp.score.content.httpclient.consume;

import com.hp.score.content.httpclient.ScoreHttpClient;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeaderValueParser;
import com.hp.score.content.httpclient.HttpClientInputs;

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
            BufferedReader reader;
            BufferedWriter fileWriter;
            try {
                reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), responseCharacterSet));
                fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destinationFile), responseCharacterSet));
            } catch (UnsupportedEncodingException e) {
                throw new IllegalArgumentException("Could not parse '"+ HttpClientInputs.RESPONSE_CHARACTER_SET
                        +"'. " + e.getMessage(), e);
            }
            char[] buffer = new char[1024];
            int b;
            while ((b = reader.read(buffer, 0, buffer.length)) != -1) {
                fileWriter.write(buffer, 0, b);
            }

            fileWriter.flush();
            fileWriter.close();
        }
    }
}
