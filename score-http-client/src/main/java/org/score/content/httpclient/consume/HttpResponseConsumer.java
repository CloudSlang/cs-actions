package org.score.content.httpclient.consume;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeaderValueParser;
import org.score.content.httpclient.HttpClientAction;

import java.io.*;
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
            String document = IOUtils.toString(httpResponse.getEntity().getContent(), responseCharacterSet);
            result.put(HttpClientAction.RETURN_RESULT, document);
        } else {
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), responseCharacterSet));
            BufferedWriter fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destinationFile), responseCharacterSet));

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
