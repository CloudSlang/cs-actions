/*
 * (c) Copyright 2020 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.cloudslang.content.sitescope.services;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.sitescope.constants.SuccessMsgs;
import io.cloudslang.content.sitescope.entities.RedeployTemplateInputs;
import io.cloudslang.content.sitescope.entities.SiteScopeCommonInputs;
import io.cloudslang.content.sitescope.utils.HttpUtils;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.httpclient.build.auth.AuthTypes.BASIC;
import static io.cloudslang.content.sitescope.constants.Constants.*;
import static io.cloudslang.content.sitescope.constants.ExceptionMsgs.EXCEPTION_INVALID_PARAM;
import static io.cloudslang.content.sitescope.constants.Inputs.RedeployTemplate.FULL_PATH_TO_TEMPLATE;
import static io.cloudslang.content.sitescope.services.HttpCommons.setCommonHttpInputs;

public class RedeployTemplateService {

    public @NotNull
    Map<String, String> execute(@NotNull RedeployTemplateInputs redeployTemplateInputs) throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final SiteScopeCommonInputs commonInputs = redeployTemplateInputs.getCommonInputs();

        httpClientInputs.setUrl(commonInputs.getProtocol() + "://" + commonInputs.getHost() + COLON + commonInputs.getPort() +
                SITESCOPE_TEMPLATES_API + REDEPLOY_TEMPLATE_ENDPOINT);

        setCommonHttpInputs(httpClientInputs, commonInputs);

        httpClientInputs.setAuthType(BASIC);
        httpClientInputs.setUsername(commonInputs.getUsername());
        httpClientInputs.setPassword(commonInputs.getPassword());
        httpClientInputs.setMethod(POST);
        httpClientInputs.setContentType(X_WWW_FORM);
        httpClientInputs.setFormParams(populateRedeployTemplateFormParams(redeployTemplateInputs));
        httpClientInputs.setFormParamsAreURLEncoded(String.valueOf(true));
        httpClientInputs.setResponseCharacterSet(commonInputs.getResponseCharacterSet());

        Map<String, String> httpClientOutputs = new HttpClientService().execute(httpClientInputs);

        return HttpUtils.convertToSitescopeResultsMap(httpClientOutputs, SuccessMsgs.REDEPLOY_TEMPLATE);
    }

    private static String populateRedeployTemplateFormParams(RedeployTemplateInputs redeployTemplateInputs) throws Exception {
        String delimiter = redeployTemplateInputs.getDelimiter();
        String fullPathToTemplate = redeployTemplateInputs.getFullPathToTemplate().replace(delimiter, SITE_SCOPE_DELIMITER);
        String property = redeployTemplateInputs.getProperties();

        Map<String, String> inputsMap = new HashMap<>();
        inputsMap.put(FULL_PATH_TO_TEMPLATE, fullPathToTemplate);
        if (!property.trim().isEmpty())
            addPropertyToForm(inputsMap, property);

        URIBuilder ub = new URIBuilder();

        for (Map.Entry<String, String> entry : inputsMap.entrySet()) {
            ub.addParameter(entry.getKey(), entry.getValue());
        }

        return ub.toString().substring(1);  // this is used to eliminate "?" from the beginning of the string
    }

    private static void addPropertyToForm(Map<String, String> inputsMap, String parameters) throws Exception {
        String[] tokenArray = parameters.split("&");
        int size = inputsMap.size();
        for (String token : tokenArray) {
            String[] pairs = token.split("=", 2);
            if (pairs.length == 2)
                if (!pairs[0].isEmpty())
                    inputsMap.put(pairs[0], pairs[1]);
        }
        if (size + tokenArray.length != inputsMap.size())  // if one of the pairs failed to be added then throw exception
            throw new Exception(EXCEPTION_INVALID_PARAM);
    }

}
