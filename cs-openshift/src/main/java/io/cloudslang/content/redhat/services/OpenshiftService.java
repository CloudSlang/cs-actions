package io.cloudslang.content.redhat.services;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.oo.sdk.content.plugin.SerializableSessionObject;
import io.cloudslang.content.httpclient.actions.HttpClientPostAction;
import io.cloudslang.content.redhat.entities.HttpInput;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.redhat.utils.Constants.CommonConstants.*;
import static io.cloudslang.content.redhat.utils.Outputs.OutputNames.EXCEPTION;
import static io.cloudslang.content.redhat.utils.Outputs.OutputNames.STATUS_CODE;

public class OpenshiftService {

    public static void processAuthTokenResult(Map<String, String> httpResults, HttpInput input, SerializableSessionObject sessionCookies, GlobalSessionObject sessionConnectionPool) {


        String getHtmlResponse = httpResults.get(RETURN_RESULT);
        Document doc = Jsoup.parse(getHtmlResponse);
        String code = doc.select(FORM_INPUT).get(0).attr(VALUE);
        String csrf = doc.select(FORM_INPUT).get(1).attr(VALUE);
        String statusCode = httpResults.get(STATUS_CODE);

        if (StringUtils.isEmpty(statusCode) || Integer.parseInt(statusCode) < 200 || Integer.parseInt(statusCode) >= 300) {
            if (StringUtils.isEmpty(httpResults.get(EXCEPTION)))
                httpResults.put(EXCEPTION, httpResults.get(RETURN_RESULT));
            httpResults.put(RETURN_CODE, "-1");
        }
        Map<String, String> test = new HttpClientPostAction().execute(
                input.getHost()+DISPLAY_TOKEN_ENDPOINT,
                BASIC,
                input.getUsername(),
                input.getPassword(),
                EMPTY_STRING,
                input.getProxyHost(),
                input.getProxyPort(),
                input.getProxyUsername(),
                input.getPassword(),
                input.getTlsVersion(),
                input.getAllowedCyphers(),
                TRUE,
                input.getX509HostnameVerifier(),
                input.getTrustKeystore(),
                input.getTrustPassword(),
                input.getKeystore(),
                input.getKeystorePassword(),
                input.getKeepAlive(),
                input.getConnectionsMaxPerRoute(),
                input.getConnectionsMaxTotal(),
                EMPTY_STRING, EMPTY_STRING, EMPTY_STRING,
                EMPTY_STRING, EMPTY_STRING, EMPTY_STRING,
                EMPTY_STRING,
                EMPTY_STRING,
                CODE + EQUAL + code + CSRF + EQUAL + csrf,
                TRUE, EMPTY_STRING,
                EMPTY_STRING,
                CONTENT_TYPE_FORM,
                EMPTY_STRING, input.getConnectTimeout(),
                EMPTY_STRING, input.getExecutionTimeout(),
                sessionCookies, sessionConnectionPool);

        String postHtmlResponse = test.get(RETURN_RESULT);
        Document second_response = Jsoup.parse(postHtmlResponse);
        if (!StringUtils.isEmpty(postHtmlResponse))
            httpResults.put(RETURN_RESULT, second_response.select(CODE).get(0).text());

    }

    public static void processHttpResult(Map<String, String> httpResults) {

        String statusCode = httpResults.get(STATUS_CODE);

        if (StringUtils.isEmpty(statusCode) || Integer.parseInt(statusCode) < 200 || Integer.parseInt(statusCode) >= 300) {
            if (StringUtils.isEmpty(httpResults.get(EXCEPTION)))
                httpResults.put(EXCEPTION, httpResults.get(RETURN_RESULT));
            httpResults.put(RETURN_CODE, "-1");
        }
    }

}
