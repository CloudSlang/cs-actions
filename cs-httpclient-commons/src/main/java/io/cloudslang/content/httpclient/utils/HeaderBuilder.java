package io.cloudslang.content.httpclient.utils;

import io.cloudslang.content.httpclient.entities.*;
import org.apache.commons.lang3.*;
import org.apache.hc.client5.http.classic.methods.*;

import static io.cloudslang.content.httpclient.utils.Constants.*;

public class HeaderBuilder {

    public static void headerBuiler(HttpUriRequestBase httpRequest, HttpClientInputs httpClientInputs) {

        if (!StringUtils.isEmpty(httpClientInputs.getHeaders())) {
            if (!httpClientInputs.getContentType().isEmpty()) {
                String[] headerList = httpClientInputs.getHeaders().split(COMMA);
                boolean counter = false;
                for (String eachHeader : headerList) {
                    if (eachHeader.toLowerCase().contains(CONTENT_TYPE.toLowerCase())) {
                        httpRequest.setHeader(new HeaderObj(CONTENT_TYPE, httpClientInputs.getContentType()));
                        counter = true;
                    } else {
                        String[] headerListSingle = eachHeader.split(COLON);
                        httpRequest.setHeader(new HeaderObj(headerListSingle[0], headerListSingle[1]));
                    }
                }
                if(!counter)
                    httpRequest.setHeader(new HeaderObj(CONTENT_TYPE, httpClientInputs.getContentType()));
            } else {
                String[] listheader = httpClientInputs.getHeaders().split(COMMA);
                for (String list : listheader) {
                    String[] headerListSingle = list.split(COLON);
                    httpRequest.setHeader(new HeaderObj(headerListSingle[0], headerListSingle[1]));
                }
            }
        } else if (!httpClientInputs.getContentType().isEmpty())
            httpRequest.setHeader(new HeaderObj(CONTENT_TYPE, httpClientInputs.getContentType()));
    }
}

