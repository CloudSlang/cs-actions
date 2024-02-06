/*
 * Copyright 2022-2024 Open Text
 * This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.cloudslang.content.httpclient.services;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.utils.URIUtils;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.message.StatusLine;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.List;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.httpclient.utils.Constants.UTF_8;
import static io.cloudslang.content.httpclient.utils.Outputs.HTTPClientOutputs.*;

public class ResponseHandler {

    public static void consume(Map<String, String> result, CloseableHttpResponse httpResponse, String responseCharacterSet, String destinationFile) throws IOException {

        if (httpResponse.getEntity() != null) {
            if (StringUtils.isEmpty(responseCharacterSet)) {
                String contentType = httpResponse.getEntity().getContentType();
                if (contentType != null) {
                    responseCharacterSet = String.valueOf(ContentType.parse(contentType).getCharset());
                }
                if (StringUtils.isEmpty(responseCharacterSet)) {
                    responseCharacterSet = UTF_8;
                }
            }
            consumeResponseContent(result, httpResponse, responseCharacterSet, destinationFile);
        }
    }

    protected static void consumeResponseContent(Map<String, String> result, CloseableHttpResponse httpResponse, String responseCharacterSet, String destinationFile) throws IOException {
        if (StringUtils.isEmpty(destinationFile)) {
            String document;
            try {
                document = IOUtils.toString(httpResponse.getEntity().getContent(), responseCharacterSet);
            } catch (UnsupportedCharsetException e) {
                throw new IllegalArgumentException("Could not parse responseCharacterSet. " + e.getMessage(), e);
            }
            result.put(RETURN_RESULT, document);
        } else {
            consumeToDestinationFile(httpResponse, responseCharacterSet, destinationFile);
        }
    }

    private static void consumeToDestinationFile(CloseableHttpResponse httpResponse, String responseCharacterSet, String destinationFile) throws IOException {
        BufferedReader reader;
        BufferedWriter fileWriter = null;
        FileOutputStream fos = null;
        try {
            try {
                reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), responseCharacterSet));
                fos = new FileOutputStream(destinationFile);
                fileWriter = new BufferedWriter(new OutputStreamWriter(fos, responseCharacterSet));
            } catch (UnsupportedEncodingException e) {
                throw new IllegalArgumentException("Could not parse '" + responseCharacterSet
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

    private static void safeClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }

    public static void getFinalLocationResponse(Map<String, String> returnResult, URI uri,List<URI> redirectLocations) {
        URI location;
        try {
            location = URIUtils.resolve(uri, URIUtils.extractHost(uri), redirectLocations);
        } catch (URISyntaxException e) {
            //this is not a fatal error
            throw new IllegalArgumentException("could not determine '" + FINAL_LOCATION
                    + "': " + e.getMessage(), e);
        }
        returnResult.put(FINAL_LOCATION, location.toASCIIString());
    }

    public static void getResponseHeaders(Map<String, String> returnResult, Header[] headers) {
        StringBuilder result = new StringBuilder();
        if (headers != null) {
            for (Header header : headers) {
                result.append(header.toString()).append("\r\n");
            }
            if (result.length() != 0) {
                result.delete(result.length() - 2, result.length());
            }
        }
        returnResult.put(RESPONSE_HEADERS, result.toString());
    }

    public static void getStatusResponse(Map<String, String> returnResult, HttpResponse response) {
        int statusCode = (response != null) ? response.getCode() : 0;
        returnResult.put(STATUS_CODE, String.valueOf(statusCode));

        String protocolVersion = (response != null && response.getVersion() != null) ?
                response.getVersion().toString() : "";
        returnResult.put(PROTOCOL_VERSION, protocolVersion);

        String reasonPhrase = (response != null && response.getReasonPhrase() != null) ?
                response.getReasonPhrase() : "";
        returnResult.put(REASON_PHRASE, reasonPhrase);
    }
}
