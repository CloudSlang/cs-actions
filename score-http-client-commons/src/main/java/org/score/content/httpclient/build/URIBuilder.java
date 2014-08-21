package org.score.content.httpclient.build;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class URIBuilder {
    private String url;
    private String queryParams;
    private String encodeQueryParams = "true";

    public URIBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    public URIBuilder setQueryParams(String queryParams) {
        this.queryParams = queryParams;
        return this;
    }

    public URIBuilder setEncodeQueryParams(String encodeQueryParams) {
        if (!StringUtils.isEmpty(encodeQueryParams)) {
            this.encodeQueryParams = encodeQueryParams;
        }
        return this;
    }

    public URI buildURI() {

        org.apache.http.client.utils.URIBuilder uriBuilder;
        try {
            uriBuilder = new org.apache.http.client.utils.URIBuilder(url);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("could not parse " + url + " as a URI", e);
        }

        boolean bEncodeQueryParams = Boolean.parseBoolean(encodeQueryParams);

        if (!StringUtils.isEmpty(queryParams)) {
            String[] pairs = queryParams.split("&");
            Map<String, String> queryParamsMap = new HashMap<>();
            for (String pair : pairs) {
                String[] nameValue = pair.split("=", 2);
                String name = nameValue[0];
                String value = nameValue.length == 2 ? nameValue[1] : null;
                if (!bEncodeQueryParams) {
                    try {
                        name = URLDecoder.decode(name, "UTF-8");
                        if (value!=null) {
                            value = URLDecoder.decode(value, "UTF-8");
                        }
                    } catch (UnsupportedEncodingException e) {
                        throw new IllegalArgumentException(e);
                    }
                }
                //queryParamsMap.put(nameValue[0], nameValue.length == 2 ? nameValue[1] : "" );
                uriBuilder.addParameter(name, value);

            }
        }

        try {
            return uriBuilder.build();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("could not build URL for " + url + " and queries " + encodeQueryParams, e);
        }

    }
}