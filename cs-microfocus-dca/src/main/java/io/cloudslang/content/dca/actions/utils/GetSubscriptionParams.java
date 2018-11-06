/*
 * (c) Copyright 2018 Micro Focus, L.P.
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
package io.cloudslang.content.dca.actions.utils;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.dca.utils.CustomInput;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.xml.services.XpathQueryService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static io.cloudslang.content.constants.BooleanValues.TRUE;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.dca.utils.Constants.*;
import static io.cloudslang.content.dca.utils.DefaultValues.*;
import static io.cloudslang.content.dca.utils.Descriptions.Common.*;
import static io.cloudslang.content.dca.utils.Descriptions.GetSubscriptionParams.*;
import static io.cloudslang.content.dca.utils.Utilities.setProxy;
import static io.cloudslang.content.dca.utils.Utilities.setSecurityInputs;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.*;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class GetSubscriptionParams {

    @Action(name = "Get Subscription Params",
            description = "",
            outputs = {
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = RETURN_RESULT),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC)},
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = RETURN_CODE, value = SUCCESS, matchType = COMPARE_EQUAL),
                    @Response(text = ResponseNames.FAILURE, field = RETURN_CODE, value = FAILURE, matchType = COMPARE_EQUAL, isDefault = true, isOnFail = true)})
    public Map<String, String> execute(@Param(value = URL, required = true, description = URL_DESC) String url,
                                       @Param(value = AUTH_TYPE, description = AUTH_DESC) String authType,
                                       @Param(value = USERNAME, description = USERNAME_DESC) String username,
                                       @Param(value = PASSWORD, description = PASSWORD_DESC) String password,
                                       @Param(value = PROXY_HOST, description = PROXY_HOST_DESC) final String proxyHost,
                                       @Param(value = PROXY_PORT, description = PROXY_PORT_DESC) final String proxyPortInp,
                                       @Param(value = PROXY_USERNAME, description = PROXY_USER_DESC) final String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, encrypted = true, description = PROXY_PASS_DESC) final String proxyPassword,
                                       @Param(value = TRUST_ALL_ROOTS, description = TRUST_ALL_ROOTS_DESC) final String trustAllRootsInp,
                                       @Param(value = X509_HOSTNAME_VERIFIER, description = X509_DESC) final String x509HostnameVerifierInp,
                                       @Param(value = TRUST_KEYSTORE, description = TRUST_KEYSTORE_DESC) final String trustKeystoreInp,
                                       @Param(value = TRUST_PASSWORD, encrypted = true, description = TRUST_PASSWORD_DESC) final String trustPasswordInp,
                                       @Param(value = KEYSTORE, description = KEYSTORE_DESC) final String keystoreInp,
                                       @Param(value = KEYSTORE_PASSWORD, encrypted = true, description = KEYSTORE_PASS_DESC) final String keystorePasswordInp,
                                       @Param(value = CONNECT_TIMEOUT, description = CONNECT_TIMEOUT_DESC) final String connectTimeout,
                                       @Param(value = SOCKET_TIMEOUT, description = SOCKET_TIMEOUT_DESC) final String socketTimeout,
                                       @Param(value = USE_COOKIES, description = USE_COOKIES_DESC) final String useCookies,
                                       @Param(value = KEEP_ALIVE, description = KEEP_ALIVE_DESC) final String keepAlive,
                                       @Param(value = QUERY_PARAMS, description = QUERY_PARAMS_DESC) String queryParams) {


        final String trustKeystore = defaultIfEmpty(trustKeystoreInp, DEFAULT_JAVA_KEYSTORE);
        final String trustPassword = defaultIfEmpty(trustPasswordInp, DEFAULT_JAVA_KEYSTORE_PASSWORD);
        final String keystore = defaultIfEmpty(keystoreInp, DEFAULT_JAVA_KEYSTORE);
        final String keystorePassword = defaultIfEmpty(keystorePasswordInp, DEFAULT_JAVA_KEYSTORE_PASSWORD);
        final String proxyPort = defaultIfEmpty(proxyPortInp, DEFAULT_PROXY_PORT);
        final String trustAllRoots = defaultIfEmpty(trustAllRootsInp, DEFAULT_TRUST_ALL_ROOTS);
        final String x509HostnameVerifier = defaultIfEmpty(x509HostnameVerifierInp, DEFAULT_X_509_HOSTNAME_VERIFIER);

        final HttpClientInputs httpClientInputs = new HttpClientInputs();

        setProxy(httpClientInputs, proxyHost, proxyPort, proxyUsername, proxyPassword);
        setSecurityInputs(httpClientInputs, trustAllRoots, x509HostnameVerifier,
                trustKeystore, trustPassword, keystore, keystorePassword);

        httpClientInputs.setUrl(url);
        httpClientInputs.setAuthType(authType);
        httpClientInputs.setUsername(username);
        httpClientInputs.setPassword(password);
        httpClientInputs.setConnectTimeout(connectTimeout);
        httpClientInputs.setSocketTimeout(socketTimeout);
        httpClientInputs.setUseCookies(useCookies);
        httpClientInputs.setKeepAlive(keepAlive);
        httpClientInputs.setQueryParams(queryParams);
        httpClientInputs.setContentType("text/plain");
        httpClientInputs.setFollowRedirects(TRUE);
        httpClientInputs.setMethod(GET);

        try {
            final Map<String, String> httpResponse = new HttpClientService().execute(httpClientInputs);

            CustomInput customInput = new CustomInput();

            Map<String, String> query_Property_Name = new XpathQueryService().execute(customInput.getCommonInputs(httpResponse.get("returnResult"), XML_DOCUMENT_SOURCE, X_PATH_QUERY_PROPERTY_NAME, SECURE_PROCESSING),
                    customInput.getCustomInputs(QUERY_TYPE, DELIMITER));

            List<String> finalList = new ArrayList<>();

            String[] arrayProp = query_Property_Name.get("selectedValue").split(",");

            int nr = 0;
            for (int j = 0; j < arrayProp.length; j++)
                if (arrayProp[j].contains("param_"))
                    nr++;

            for (int i = 0; i < arrayProp.length; i++) {

                if (arrayProp[i].contains("param_")) {

                    String xPathQueryChild = QUERY + arrayProp[i] + QUERY_PART;
                    Map<String, String> query_Child = new XpathQueryService().execute(customInput.getCommonInputs(httpResponse.get("returnResult"), XML_DOCUMENT_SOURCE, xPathQueryChild, SECURE_PROCESSING),
                            customInput.getCustomInputs(QUERY_TYPE, DELIMITER));

                    query_Child.put("selectedValue", query_Child.get("selectedValue").toString().replaceAll(" ", ""));

                    arrayProp[i] = arrayProp[i].replaceAll("param_", "");

                    if (i < nr && nr > 1) {
                        finalList.add(arrayProp[i] + "=" + query_Child.get("selectedValue") + "&");
                    } else
                        finalList.add(arrayProp[i] + "=" + query_Child.get("selectedValue"));

                }

            }

            String finalResponse = "";
            for (int j = 0; j < finalList.size(); j++)
                finalResponse = finalResponse + finalList.get(j);

            Map<String, String> result = getSuccessResultsMap("The operation was executed with success");
            result.put("paramList", finalResponse);
            return result;
        } catch (Exception e) {
            return getFailureResultsMap(e.getMessage());
        }
    }

}
