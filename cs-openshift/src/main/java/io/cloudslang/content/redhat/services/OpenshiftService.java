package io.cloudslang.content.redhat.services;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.oo.sdk.content.plugin.SerializableSessionObject;
import io.cloudslang.content.httpclient.actions.HttpClientPostAction;
import io.cloudslang.content.redhat.entities.HttpInput;
import com.google.gson.JsonPrimitive;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.redhat.utils.Constants.CommonConstants.*;
import static io.cloudslang.content.redhat.utils.Descriptions.GetTokenAction.SUCCESS_DESC;
import static io.cloudslang.content.redhat.utils.Outputs.OutputNames.EXCEPTION;
import static io.cloudslang.content.redhat.utils.Outputs.OutputNames.STATUS_CODE;
import static io.cloudslang.content.redhat.utils.Outputs.OutputNames.*;
import static io.cloudslang.content.redhat.utils.Descriptions.GetDeploymentStatus.RETURN_RESULT_MESSAGE_DESC;

public class OpenshiftService {

    public static void processAuthTokenResult(Map<String, String> httpResults, HttpInput input, SerializableSessionObject sessionCookies, GlobalSessionObject sessionConnectionPool) {

        try {
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
                    input.getHost() + DISPLAY_TOKEN_ENDPOINT,
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
            if (!StringUtils.isEmpty(postHtmlResponse)){
                httpResults.put(RETURN_RESULT, SUCCESS_DESC);
                httpResults.put(AUTH_TOKEN, second_response.select(CODE).get(0).text());
            }

            if (StringUtils.isEmpty(statusCode) || Integer.parseInt(statusCode) < 200 || Integer.parseInt(statusCode) >= 300) {
                if (StringUtils.isEmpty(httpResults.get(EXCEPTION)))
                    httpResults.put(EXCEPTION, httpResults.get(RETURN_RESULT));
                httpResults.put(RETURN_CODE, "-1");
            }


        } catch (IndexOutOfBoundsException e) {
            httpResults.put(EXCEPTION, httpResults.get(RETURN_RESULT));
            httpResults.put(RETURN_CODE, "-1");
        }
    }
    public static void processHttpResult(Map<String, String> httpResults) {

        String statusCode = httpResults.get(STATUS_CODE);

        if (StringUtils.isEmpty(statusCode) || Integer.parseInt(statusCode) < 200 || Integer.parseInt(statusCode) >= 300) {
            if (StringUtils.isEmpty(httpResults.get(EXCEPTION)))
                httpResults.put(EXCEPTION, httpResults.get(RETURN_RESULT));
            httpResults.put(RETURN_CODE, "-1");
        }
    }

    public static void processHttpGetDeploymentStatusResult(Map<String, String> httpResults) {

        //Process the return result output
        String returnResult = httpResults.get(RETURN_RESULT);

        if (!(returnResult.isEmpty())) {
            httpResults.put(DOCUMENT_OUTPUT, returnResult);

            JsonObject jsonResponse = ((new JsonParser()).parse(httpResults.get(RETURN_RESULT))).getAsJsonObject();

            //Kind output
            JsonPrimitive tmpResponse = (JsonPrimitive) jsonResponse.get(KIND_OUTPUT);
            httpResults.put(KIND_OUTPUT, tmpResponse.toString());

            //Name outputs
            String namePath = "$.metadata.name";
            String namePathResponse = JsonPath.read(jsonResponse.toString(), namePath);
            httpResults.put(NAME_OUTPUT, namePathResponse);

            //Namespace outputs
            String namespacePath = "$.metadata.namespace";
            String namespacePathResponse = JsonPath.read(jsonResponse.toString(), namespacePath);
            httpResults.put(NAMESPACE_OUTPUT, namespacePathResponse);

            //Uid outputs
            String uidPath = "$.metadata.uid";
            String uidPathResponse = JsonPath.read(jsonResponse.toString(), uidPath);
            httpResults.put(UID_OUTPUT, uidPathResponse);

            //ObservedGeneration output
            String observedGenerationPath = "$.status.observedGeneration";
            Integer observedGenerationPathResponse = JsonPath.read(jsonResponse.toString(), observedGenerationPath);
            httpResults.put(OBSERVED_GENERATION_OUTPUT, observedGenerationPathResponse.toString());

            //Replicas output
            String replicasPath = "$.status.replicas";
            Integer replicasPathResponse = JsonPath.read(jsonResponse.toString(), replicasPath);
            httpResults.put(REPLICAS_OUTPUT, replicasPathResponse.toString());

            //UpdatedReplicas output
            String updatedReplicasPath = "$.status.updatedReplicas";
            Integer updatedReplicasPathResponse = JsonPath.read(jsonResponse.toString(), updatedReplicasPath);
            httpResults.put(UPDATED_REPLICAS_OUTPUT, updatedReplicasPathResponse.toString());

            //UnavailableReplicas output
            String unavailableReplicasPath = "$.status.unavailableReplicas";
            Integer unavailableReplicasPathResponse = JsonPath.read(jsonResponse.toString(), unavailableReplicasPath);
            httpResults.put(UNAVAILABLE_REPLICAS_OUTPUT, unavailableReplicasPathResponse.toString());

            //Conditions output
            String conditionsPath = "$.status.conditions";
            List<String> conditionsPathResponse = JsonPath.read(jsonResponse.toString(), conditionsPath);
            httpResults.put(CONDITIONS_OUTPUT, conditionsPathResponse.toString());

            httpResults.put(RETURN_RESULT, RETURN_RESULT_MESSAGE_DESC);
        }


        //Process HTTP status code
        String statusCode = httpResults.get(STATUS_CODE);

        if (StringUtils.isEmpty(statusCode) || Integer.parseInt(statusCode) < 200 || Integer.parseInt(statusCode) >= 300) {
            if (StringUtils.isEmpty(httpResults.get(EXCEPTION)))
                httpResults.put(EXCEPTION, httpResults.get(RETURN_RESULT));
            httpResults.put(RETURN_CODE, "-1");
        }
    }

}
