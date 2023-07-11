/*
 * Copyright 2020-2023 Open Text
 * This program and the accompanying materials
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
import io.cloudslang.content.sitescope.entities.RunMonitorInputs;
import io.cloudslang.content.sitescope.entities.SiteScopeCommonInputs;
import io.cloudslang.content.sitescope.utils.HttpUtils;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.httpclient.build.auth.AuthTypes.BASIC;
import static io.cloudslang.content.sitescope.constants.Constants.*;
import static io.cloudslang.content.sitescope.constants.Inputs.CommonInputs.FULL_PATH_TO_MONITOR;
import static io.cloudslang.content.sitescope.constants.Inputs.CommonInputs.IDENTIFIER;
import static io.cloudslang.content.sitescope.constants.Inputs.RunMonitor.EXECUTION_TIMEOUT;
import static io.cloudslang.content.sitescope.constants.Inputs.RunMonitor.MONITOR_ID;
import static io.cloudslang.content.sitescope.services.HttpCommons.setCommonHttpInputs;

public class RunMonitorService {

    public static String populateRunMonitorFormParams(RunMonitorInputs runMonitorInputs) throws Exception {

        Map<String, String> inputsMap = new HashMap<>();
        inputsMap.put(FULL_PATH_TO_MONITOR, runMonitorInputs.getFullPathToMonitor().replace(runMonitorInputs.getDelimiter(), SITE_SCOPE_DELIMITER));
        inputsMap.put(MONITOR_ID, runMonitorInputs.getMonitorId());
        inputsMap.put(EXECUTION_TIMEOUT, runMonitorInputs.getExecutionTimeout());
        inputsMap.put(IDENTIFIER, runMonitorInputs.getIdentifier());

        URIBuilder ub = new URIBuilder();

        for (Map.Entry<String, String> entry : inputsMap.entrySet()) {
            ub.addParameter(entry.getKey(), entry.getValue());
        }

        return ub.toString().substring(1);
    }

    public @NotNull
    Map<String, String> execute(@NotNull RunMonitorInputs runMonitorInputs) throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final SiteScopeCommonInputs commonInputs = runMonitorInputs.getCommonInputs();

        httpClientInputs.setUrl(commonInputs.getProtocol() + "://" + commonInputs.getHost() + COLON + commonInputs.getPort() +
                SITESCOPE_RUN_MONITOR_API);

        setCommonHttpInputs(httpClientInputs, commonInputs);

        httpClientInputs.setAuthType(BASIC);
        httpClientInputs.setUsername(commonInputs.getUsername());
        httpClientInputs.setPassword(commonInputs.getPassword());
        httpClientInputs.setMethod(POST);
        httpClientInputs.setContentType(X_WWW_FORM);
        httpClientInputs.setFormParams(populateRunMonitorFormParams(runMonitorInputs));
        httpClientInputs.setFormParamsAreURLEncoded(String.valueOf(true));
        httpClientInputs.setResponseCharacterSet(commonInputs.getResponseCharacterSet());

        Map<String, String> httpClientOutputs = new HttpClientService().execute(httpClientInputs);

        return HttpUtils.convertToSitescopeResultsMap(httpClientOutputs, SuccessMsgs.DEPLOY_TEMPLATE);
    }


}
