/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.amazon.utils;

import io.cloudslang.content.amazon.entities.aws.AuthorizationHeader;
import io.cloudslang.content.amazon.entities.constants.Outputs;
import io.cloudslang.content.xml.actions.XpathQuery;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.AUTHORIZATION_HEADER_RESULT;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.SIGNATURE_RESULT;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.httpclient.CSHttpClient.STATUS_CODE;
import static io.cloudslang.content.xml.utils.Constants.Defaults.DELIMITER;
import static io.cloudslang.content.xml.utils.Constants.Defaults.XML_DOCUMENT_SOURCE;
import static io.cloudslang.content.xml.utils.Constants.Outputs.ERROR_MESSAGE;
import static io.cloudslang.content.xml.utils.Constants.Outputs.SELECTED_VALUE;
import static io.cloudslang.content.xml.utils.Constants.QueryTypes.VALUE;
import static java.lang.String.valueOf;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.http.HttpStatus.SC_OK;

/**
 * Created by Mihai Tusa.
 * 2/18/2016.
 */
public class OutputsUtil {

    private static final String XMLNS = "xmlns";
    private static final String WORKAROUND = "workaround";

    private OutputsUtil() {
    }

    public static Map<String, String> populateSignatureResultsMap(AuthorizationHeader authorizationHeader) {
        Map<String, String> signatureReturnResultMap = getResultsMap(authorizationHeader.getSignature());

        signatureReturnResultMap.put(SIGNATURE_RESULT, authorizationHeader.getSignature());
        signatureReturnResultMap.put(AUTHORIZATION_HEADER_RESULT, authorizationHeader.getAuthorizationHeader());

        return signatureReturnResultMap;
    }

    public static Map<String, String> getValidResponse(Map<String, String> queryMapResult) {
        if (queryMapResult != null) {
            if (queryMapResult.containsKey(STATUS_CODE) && (valueOf(SC_OK).equals(queryMapResult.get(STATUS_CODE))) && queryMapResult.containsKey(RETURN_RESULT) && !isEmpty(queryMapResult.get(RETURN_RESULT))) {
                queryMapResult.put(RETURN_CODE, SUCCESS);
            } else {
                queryMapResult.put(RETURN_CODE, FAILURE);
            }
            return queryMapResult;
        } else {
            Map<String, String> resultMap = new HashMap<>();
            resultMap.put(EXCEPTION, "Null response!");
            resultMap.put(RETURN_CODE, FAILURE);
            resultMap.put(RETURN_RESULT, "The query returned null response!");
            return resultMap;
        }
    }

    public static void putResponseIn(Map<String, String> queryMapResult, String outputName, String xPathQuery) {
        XpathQuery xpathQueryAction = new XpathQuery();
        String xmlString = queryMapResult.get(RETURN_RESULT);
        //We make this workaround because the xml has an xmlns property in the tag and our operation can not parse the xml
        //this should be removed when the xml operation will be enhanced
        if (!isBlank(xmlString)) {
            xmlString = xmlString.replace(XMLNS, WORKAROUND);
            Map<String, String> result = xpathQueryAction.execute(xmlString, XML_DOCUMENT_SOURCE, xPathQuery, VALUE, DELIMITER, valueOf(true));
            if (result.containsKey(RETURN_CODE) && SUCCESS.equals(result.get(RETURN_CODE))) {
                queryMapResult.put(outputName, result.get(SELECTED_VALUE));
            } else {
                queryMapResult.put(RETURN_CODE, FAILURE);
                queryMapResult.put(EXCEPTION, result.get(ERROR_MESSAGE));
            }
        } else {
            queryMapResult.put(RETURN_RESULT, "Empty response.");
            queryMapResult.put(RETURN_CODE, FAILURE);
        }
    }

    private static Map<String, String> getResultsMap(String returnResult) {
        Map<String, String> results = new HashMap<>();
        results.put(Outputs.RETURN_CODE, Outputs.SUCCESS_RETURN_CODE);
        results.put(Outputs.RETURN_RESULT, returnResult);

        return results;
    }
}
