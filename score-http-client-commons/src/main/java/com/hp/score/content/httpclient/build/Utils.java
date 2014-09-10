package com.hp.score.content.httpclient.build;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: davidmih
 * Date: 9/9/14
 */
public class Utils {
    public static String urlEncodeMultipleParamsX(String params, boolean urlEncode) throws UrlEncodeException {
        org.apache.http.client.utils.URIBuilder uriBuilder = null;
        try {
            uriBuilder = new org.apache.http.client.utils.URIBuilder("/");
        } catch (URISyntaxException e) {
            //never happens
            throw new RuntimeException(e.getMessage(), e);
        }
        urlEncodeMultipleParams(params, urlEncode, uriBuilder);

        try {
            return uriBuilder.build().getQuery();
        } catch (URISyntaxException e) {
            //never happens
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static List<? extends NameValuePair> urlEncodeMultipleParams(String params, boolean urlEncode) throws UrlEncodeException {
        List<BasicNameValuePair> list = new ArrayList<>();

        String[] pairs = params.split("&");
        for (String pair : pairs) {
            String[] nameValue = pair.split("=", 2);
            String name = nameValue[0];
            String value = nameValue.length == 2 ? nameValue[1] : null;

            if (!urlEncode) {
                try {
                    name = URLDecoder.decode(name, "UTF-8");
                    if (value!=null) {
                        value = URLDecoder.decode(value, "UTF-8");
                    }
                } catch (UnsupportedEncodingException e) {
                    //never happens
                    throw new RuntimeException(e);
                } catch (IllegalArgumentException ie) {
                    throw new UrlEncodeException(ie.getMessage(), ie);
                }
            }

            list.add(new BasicNameValuePair(name, value));
        }
        return list;
    }

    public static void urlEncodeMultipleParams(String params, boolean urlEncode
            , org.apache.http.client.utils.URIBuilder uriBuilder) throws UrlEncodeException {
        String[] pairs = params.split("&");
        for (String pair : pairs) {
            String[] nameValue = pair.split("=", 2);
            String name = nameValue[0];
            String value = nameValue.length == 2 ? nameValue[1] : null;

            if (!urlEncode) {
                try {
                    name = URLDecoder.decode(name, "UTF-8");
                    if (value!=null) {
                        value = URLDecoder.decode(value, "UTF-8");
                    }
                } catch (UnsupportedEncodingException e) {
                    //never happens
                    throw new RuntimeException(e);
                } catch (IllegalArgumentException ie) {
                    throw new UrlEncodeException(ie.getMessage(), ie);
                }
            }


            uriBuilder.addParameter(name, value);
        }
    }
}
