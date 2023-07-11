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
import io.cloudslang.content.sitescope.entities.ChangeMonitorStatusInputs;
import io.cloudslang.content.sitescope.entities.SiteScopeCommonInputs;
import io.cloudslang.content.sitescope.utils.HttpUtils;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.httpclient.build.auth.AuthTypes.BASIC;
import static io.cloudslang.content.sitescope.constants.Constants.*;
import static io.cloudslang.content.sitescope.constants.Inputs.ChangeMonitorStatusInputs.MONITOR_ID;
import static io.cloudslang.content.sitescope.constants.Inputs.CommonInputs.FULL_PATH_TO_MONITOR;
import static io.cloudslang.content.sitescope.constants.Inputs.CommonInputs.IDENTIFIER;
import static io.cloudslang.content.sitescope.constants.Inputs.ChangeMonitorGroupStatusInputs.*;
import static io.cloudslang.content.sitescope.constants.SuccessMsgs.CHANGE_MONITOR_STATUS;
import static io.cloudslang.content.sitescope.services.HttpCommons.setCommonHttpInputs;

public class ChangeMonitorStatusService {

    private static String populateChangeMonitorStatusFormParams(ChangeMonitorStatusInputs changeMonitorStatusInputs) throws UnsupportedEncodingException {

        String delimiter = changeMonitorStatusInputs.getDelimiter();
        String fullPath = changeMonitorStatusInputs.getFullPathToMonitor();

        if (!delimiter.isEmpty())
            fullPath = fullPath.replace(delimiter, SITE_SCOPE_DELIMITER);

        Map<String, String> inputsMap = new HashMap<>();
        inputsMap.put(FULL_PATH_TO_MONITOR, fullPath);
        inputsMap.put(MONITOR_ID, changeMonitorStatusInputs.getMonitorId());
        inputsMap.put(ENABLE, changeMonitorStatusInputs.getStatus());
        inputsMap.put(TIME_PERIOD, changeMonitorStatusInputs.getTimePeriod());
        inputsMap.put(FROM_TIME, changeMonitorStatusInputs.getFromTime());
        inputsMap.put(TO_TIME, changeMonitorStatusInputs.getToTime());
        inputsMap.put(DESCRIPTION, changeMonitorStatusInputs.getDescription());
        inputsMap.put(IDENTIFIER, changeMonitorStatusInputs.getIdentifier());

        URIBuilder ub = new URIBuilder();

        for (Map.Entry<String, String> entry : inputsMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if ((value != null) && !value.isEmpty())
                ub.addParameter(key, value);
        }

        return ub.toString();
    }

    public @NotNull
    Map<String, String> execute(@NotNull ChangeMonitorStatusInputs changeMonitorStatusInputs) throws Exception {

        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final SiteScopeCommonInputs commonInputs = changeMonitorStatusInputs.getCommonInputs();

        httpClientInputs.setUrl(commonInputs.getProtocol() + "://" + commonInputs.getHost() + COLON + commonInputs.getPort() +
                SITESCOPE_MONITORS_API + ENABLE_MONITOR_ENDPOINT);

        setCommonHttpInputs(httpClientInputs, commonInputs);

        httpClientInputs.setAuthType(BASIC);
        httpClientInputs.setUsername(commonInputs.getUsername());
        httpClientInputs.setPassword(commonInputs.getPassword());
        httpClientInputs.setMethod(POST);
        httpClientInputs.setContentType(X_WWW_FORM);
        httpClientInputs.setFormParams(populateChangeMonitorStatusFormParams(changeMonitorStatusInputs));
        httpClientInputs.setFormParamsAreURLEncoded(String.valueOf(true));
        httpClientInputs.setResponseCharacterSet(commonInputs.getResponseCharacterSet());

        Map<String, String> httpClientOutputs = new HttpClientService().execute(httpClientInputs);

        return HttpUtils.convertToSitescopeResultsMap(httpClientOutputs, CHANGE_MONITOR_STATUS);
    }
}
