/*******************************************************************************
 * (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/

package org.openscore.content.httpclient.build;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.openscore.content.httpclient.HttpClientInputs;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: davidmih, ioanvranauhp
 * Date: 9/9/14
 */
public class Utils {

    public static final String DEFAULT_CHARACTER_SET = "UTF-8";

    public static List<? extends NameValuePair> urlEncodeMultipleParams(String params, boolean urlEncode) throws UrlEncodeException {
        List<BasicNameValuePair> list = new ArrayList<>();

        String[] pairs = params.split("&");
        for (String pair : pairs) {
            String[] nameValue = pair.split("=", 2);
            String name = nameValue[0];
            String value = nameValue.length == 2 ? nameValue[1] : null;

            if (!urlEncode) {
                try {
                    name = URLDecoder.decode(name, DEFAULT_CHARACTER_SET);
                    if (value != null) {
                        value = URLDecoder.decode(value, DEFAULT_CHARACTER_SET);
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

    public static String urlEncodeQueryParams(String params, boolean urlEncode) throws UrlEncodeException {
        String encodedParams = params;
        if (!urlEncode) {
            try {
                encodedParams = URLDecoder.decode(params, DEFAULT_CHARACTER_SET);
            } catch (UnsupportedEncodingException e) {
//                never happens
                throw new RuntimeException(e);
            } catch (IllegalArgumentException ie) {
                throw new UrlEncodeException(ie.getMessage(), ie);
            }
        }
        return encodedParams;
    }

    /**
     * Checks if a given value represents a valid port number and returns an int value representing that port number otherwise throws an exception when an invalid port value is provided.
     * Valid port values: -1 and integer numbers greater than 0.
     * Although network specifications state that port values need to be 16-bit unsigned integers, the value '-1' is considered valid by some party components.
     * Example: For the Apache HttpHost class, which is used in {@link org.openscore.content.httpclient.build.RequestConfigBuilder#buildRequestConfig()} , the value '-1' indicates the scheme default port.
     * @param portStringValue String value representing the port number;
     * @return int value representing a valid port number
     */
    public static int validatePortNumber(String portStringValue){
        final int portNumber;
        final StringBuilder exceptionMessageBuilder = new StringBuilder();
        exceptionMessageBuilder.append("Invalid value '").append(portStringValue)
                .append("' for input '").append( HttpClientInputs.PROXY_PORT)
                .append("'. Valid Values: -1, integer values greater than 0. ");

        try{
            portNumber = Integer.parseInt(portStringValue);
             if((portNumber < 0) && (portNumber != -1)){
                    throw new IllegalArgumentException(exceptionMessageBuilder.toString());
                }
        } catch (NumberFormatException e) {
             throw new IllegalArgumentException(exceptionMessageBuilder.toString() , e);
        }
        return portNumber;
    }
}
