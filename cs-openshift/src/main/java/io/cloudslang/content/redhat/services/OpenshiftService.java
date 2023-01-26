package io.cloudslang.content.redhat.services;

import com.google.gson.*;
import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.oo.sdk.content.plugin.SerializableSessionObject;
import io.cloudslang.content.httpclient.actions.HttpClientPostAction;
import io.cloudslang.content.redhat.entities.HttpInput;
import com.google.gson.JsonPrimitive;
import com.jayway.jsonpath.JsonPath;
import io.cloudslang.content.redhat.utils.Descriptions;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.redhat.utils.Constants.CommonConstants.*;
import static io.cloudslang.content.redhat.utils.Constants.CommonConstants.COMMA;
import static io.cloudslang.content.redhat.utils.Descriptions.GetTokenAction.SUCCESS_DESC;
import static io.cloudslang.content.redhat.utils.Outputs.OutputNames.AUTH_TOKEN;
import static io.cloudslang.content.redhat.utils.Outputs.OutputNames.EXCEPTION;
import static io.cloudslang.content.redhat.utils.Outputs.OutputNames.STATUS_CODE;
import static io.cloudslang.content.redhat.utils.Descriptions.GetPodList.*;
import static io.cloudslang.content.redhat.utils.Outputs.OutputNames.*;
import static io.cloudslang.content.redhat.utils.Descriptions.GetDeploymentStatus.RETURN_RESULT_MESSAGE_DESC;
import static org.apache.commons.lang3.StringUtils.EMPTY;

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
                httpResults.put(RETURN_CODE, NEGATIVE_RETURN_CODE);
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
                    FALSE,
                    CONNECTION_MAX_PER_ROUTE,
                    CONNECTIONS_MAX_TOTAL_VALUE,
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
            if (!StringUtils.isEmpty(postHtmlResponse)) {
                httpResults.put(RETURN_RESULT, SUCCESS_DESC);
                httpResults.put(AUTH_TOKEN, second_response.select(CODE).get(0).text());
            }

            if (StringUtils.isEmpty(statusCode) || Integer.parseInt(statusCode) < 200 || Integer.parseInt(statusCode) >= 300) {
                if (StringUtils.isEmpty(httpResults.get(EXCEPTION)))
                    httpResults.put(EXCEPTION, httpResults.get(RETURN_RESULT));
                httpResults.put(RETURN_CODE, NEGATIVE_RETURN_CODE);
            }


        } catch (IndexOutOfBoundsException e) {
            httpResults.put(EXCEPTION, httpResults.get(RETURN_RESULT));
            httpResults.put(RETURN_CODE, NEGATIVE_RETURN_CODE);
        }
    }

    public static void processHttpResult(Map<String, String> httpResults) {

        String statusCode = httpResults.get(STATUS_CODE);

        if (StringUtils.isEmpty(statusCode) || Integer.parseInt(statusCode) < 200 || Integer.parseInt(statusCode) >= 300) {
            if (StringUtils.isEmpty(httpResults.get(EXCEPTION)))
                httpResults.put(EXCEPTION, httpResults.get(RETURN_RESULT));
            httpResults.put(RETURN_CODE, NEGATIVE_RETURN_CODE);
        }
    }

    public static void addPodListResults(Map<String, String> httpResults) {
        try {
            if (!(httpResults.get(RETURN_RESULT).isEmpty())) {

                StringBuilder podList = new StringBuilder();
                List<JsonObject> podPairList = new ArrayList<>();

                extractValue(httpResults, podList, podPairList);

                //populate the podList and podArray outputs
                httpResults.put(POD_LIST, podList.toString());
                httpResults.put(POD_ARRAY, podPairList.toString());

                //overwrite the returnResult output with a success message
                httpResults.put(RETURN_RESULT, SUCCESSFUL_RETURN_RESULT);

            }

        } catch (Exception e) {
            //in case an error arises during the parsing, populate the custom outputs with empty values
            setFailureCustomResults(httpResults, POD_LIST, POD_ARRAY, DOCUMENT_OUTPUT);

            throw new RuntimeException(e);
        }

    }

    public static void addPodTemplateListResults(Map<String, String> httpResults) {
        try {
            if (!(httpResults.get(RETURN_RESULT).isEmpty())) {

                StringBuilder podTemplateList = new StringBuilder();
                List<JsonObject> podTemplatePairList = new ArrayList<>();

                extractValue(httpResults, podTemplateList, podTemplatePairList);

                //populate the podTemplateList and podTemplateArray outputs
                httpResults.put(POD_TEMPLATE_LIST, podTemplateList.toString());
                httpResults.put(POD_TEMPLATE_ARRAY, podTemplatePairList.toString());

                //overwrite the returnResult output with a success message
                httpResults.put(RETURN_RESULT, Descriptions.GetPodTemplateList.SUCCESSFUL_RETURN_RESULT);

            }

        } catch (Exception e) {
            //in case an error arises during the parsing, populate the custom outputs with empty values
            setFailureCustomResults(httpResults, POD_TEMPLATE_LIST, POD_TEMPLATE_ARRAY, DOCUMENT_OUTPUT);

            throw new RuntimeException(e);
        }

    }


    public static void addTemplateListResults(Map<String, String> httpResults) {
        try {
            if (!(httpResults.get(RETURN_RESULT).isEmpty())) {

                StringBuilder templateList = new StringBuilder();
                List<JsonObject> templatePairList = new ArrayList<>();

                extractValue(httpResults, templateList, templatePairList);

                //populate the templateList and templateArray outputs
                httpResults.put(TEMPLATE_LIST, templateList.toString());
                httpResults.put(TEMPLATE_ARRAY, templatePairList.toString());

                //overwrite the returnResult output with a success message
                httpResults.put(RETURN_RESULT, Descriptions.GetTemplateList.SUCCESSFUL_RETURN_RESULT);

            }

        } catch (Exception e) {
            //in case an error arises during the parsing, populate the custom outputs with empty values
            setFailureCustomResults(httpResults, TEMPLATE_LIST, TEMPLATE_ARRAY, DOCUMENT_OUTPUT);

            throw new RuntimeException(e);
        }

    }

    public static void addRouteListResults(Map<String, String> httpResults) {
        try {
            if (!(httpResults.get(RETURN_RESULT).isEmpty())) {

                StringBuilder routeList = new StringBuilder();
                List<JsonObject> routePairList = new ArrayList<>();

                extractValue(httpResults, routeList, routePairList);

                //populate the routeList and routeArray outputs
                httpResults.put(ROUTE_LIST, routeList.toString());
                httpResults.put(ROUTE_ARRAY, routePairList.toString());

                //overwrite the returnResult output with a success message
                httpResults.put(RETURN_RESULT, Descriptions.GetRouteList.SUCCESSFUL_RETURN_RESULT);

            }

        } catch (Exception e) {
            //in case an error arises during the parsing, populate the custom outputs with empty values
            setFailureCustomResults(httpResults, DOCUMENT_OUTPUT, ROUTE_LIST, ROUTE_ARRAY);

            throw new RuntimeException(e);
        }

    }

    private static void extractValue(Map<String, String> httpResults, StringBuilder elementList, List<JsonObject> elementPairList) {

        //populate the document output
        httpResults.put(DOCUMENT_OUTPUT, httpResults.get(RETURN_RESULT));

        //parse the API response, extract required information construct the elementList and elementArray outputs
        JsonObject jsonResponse = JsonParser.parseString(httpResults.get(RETURN_RESULT)).getAsJsonObject();
        JsonArray elementArray = jsonResponse.getAsJsonArray(PROPERTY_ITEMS);

        for (JsonElement element : elementArray) {
            JsonObject elementObject = new JsonObject();
            elementObject.add(PROPERTY_NAME, element.getAsJsonObject().get(PROPERTY_METADATA).getAsJsonObject().get(PROPERTY_NAME));
            elementObject.add(PROPERTY_UID, element.getAsJsonObject().get(PROPERTY_METADATA).getAsJsonObject().get(PROPERTY_UID));
            elementPairList.add(elementObject);
            elementList.append(element.getAsJsonObject().get(PROPERTY_METADATA).getAsJsonObject().get(PROPERTY_UID).getAsString());
            elementList.append(COMMA);
        }

        //remove the last comma from the podList value

        try {
            elementList.deleteCharAt(elementList.length() - 1);
        } catch (IndexOutOfBoundsException e) {
            //do nothing with the exception as this was added for the case where the http request is successful but no item is returned
            //in that case this would throw an IndexOutOfBoundsException
        }
    }

    public static void setFailureCustomResults(Map<String, String> httpResults, String... inputs) {

        for (String input : inputs)
            httpResults.put(input, EMPTY);
    }

    public static void setFailureCommonResults(Map<String, String> httpResults, Exception e) {
        httpResults.put(RETURN_CODE, NEGATIVE_RETURN_CODE);
        httpResults.put(RETURN_RESULT, e.getMessage());
        httpResults.put(EXCEPTION, ExceptionUtils.getStackTrace(e));
    }

    public static void processHttpGetDeploymentStatusResult(Map<String, String> httpResults) {

        //Process the return result output
        String returnResult = httpResults.get(RETURN_RESULT);
        try {

            if (!(returnResult.isEmpty())) {
                httpResults.put(DOCUMENT_OUTPUT, returnResult);

                JsonObject jsonResponse = JsonParser.parseString(httpResults.get(RETURN_RESULT)).getAsJsonObject();

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

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
