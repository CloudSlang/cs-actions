package org.score.content.httpclient;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeaderValueParser;

import java.io.FileWriter;
import java.io.IOException;
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
        if (responseCharacterSet == null || responseCharacterSet.equals("")) {
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
                responseCharacterSet = "ISO-8859-1";
            }
        }

        if (StringUtils.isEmpty(destinationFile)) {
            String document = IOUtils.toString(httpResponse.getEntity().getContent(), responseCharacterSet);
            result.put(HttpClientAction.RETURN_RESULT, document);
        } else {
            FileWriter fileWriter = new FileWriter(destinationFile);
            IOUtils.copy(httpResponse.getEntity().getContent(), fileWriter, responseCharacterSet);
        }

    }
}
