package org.score.content.httpclient;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;

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

    public URI buildURI() throws URISyntaxException {

        if (StringUtils.isEmpty(encodeQueryParams)) {
            encodeQueryParams = "true";
        }

        org.apache.http.client.utils.URIBuilder uriBuilder;
        try {
            uriBuilder = new org.apache.http.client.utils.URIBuilder(url);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("could not parse " + url + " as a URI", e);
        }

        if (!StringUtils.isEmpty(queryParams)) {
            String[] pairs = queryParams.split("&");
            Map<String, String> queryParamsMap = new HashMap<>();
            for (String pair : pairs) {
                String[] nameValue = pair.split("=", 2);
                queryParamsMap.put(nameValue[0], nameValue[1]);
            }
            NameValuePairList queryPairList = new NameValuePairList(queryParamsMap);

            if (Boolean.parseBoolean(encodeQueryParams)) {
                uriBuilder.addParameters(queryPairList);
            } else {
                for (NameValuePair nameValuePair : queryPairList) {
                    try {
                        uriBuilder.addParameter(
                                URLDecoder.decode(nameValuePair.getName(), "UTF-8"),
                                URLDecoder.decode(nameValuePair.getValue(), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        throw new IllegalArgumentException(e);
                    }
                }
            }
        }

        try {
            return uriBuilder.build();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("could not build URL for " + url + " and queries " + encodeQueryParams, e);
        }

    }
}