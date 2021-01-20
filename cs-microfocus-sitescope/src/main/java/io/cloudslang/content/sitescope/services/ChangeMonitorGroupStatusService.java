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
import io.cloudslang.content.sitescope.entities.ChangeMonitorGroupStatusInputs;
import io.cloudslang.content.sitescope.entities.SiteScopeCommonInputs;
import io.cloudslang.content.sitescope.utils.HttpUtils;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.httpclient.build.auth.AuthTypes.BASIC;
import static io.cloudslang.content.sitescope.constants.Constants.*;
import static io.cloudslang.content.sitescope.constants.Inputs.CommonInputs.FULL_PATH_TO_GROUP;
import static io.cloudslang.content.sitescope.constants.Inputs.CommonInputs.IDENTIFIER;
import static io.cloudslang.content.sitescope.constants.Inputs.ChangeMonitorGroupStatusInputs.*;
import static io.cloudslang.content.sitescope.constants.SuccessMsgs.ENABLE_MONITOR_GROUP;
import static io.cloudslang.content.sitescope.services.HttpCommons.setCommonHttpInputs;


public class ChangeMonitorGroupStatusService {

    public @NotNull
    Map<String, String> execute(@NotNull ChangeMonitorGroupStatusInputs changeMonitorGroupStatusInputs) throws Exception {

        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final SiteScopeCommonInputs commonInputs = changeMonitorGroupStatusInputs.getCommonInputs();

        httpClientInputs.setUrl(commonInputs.getProtocol() + "://" + commonInputs.getHost() + COLON + commonInputs.getPort() +
                SITESCOPE_MONITORS_API + ENABLE_MONITOR_GROUP_ENDPOINT);

        setCommonHttpInputs(httpClientInputs, commonInputs);

        httpClientInputs.setAuthType(BASIC);
        httpClientInputs.setUsername(commonInputs.getUsername());
        httpClientInputs.setPassword(commonInputs.getPassword());
        httpClientInputs.setMethod(POST);
        httpClientInputs.setContentType(X_WWW_FORM);
        httpClientInputs.setFormParams(populateChangeMonitorGroupStatusFormParams(changeMonitorGroupStatusInputs));
        httpClientInputs.setFormParamsAreURLEncoded(String.valueOf(true));
        httpClientInputs.setResponseCharacterSet(commonInputs.getResponseCharacterSet());

        Map<String, String> httpClientOutputs = new HttpClientService().execute(httpClientInputs);

        return HttpUtils.convertToSitescopeResultsMap(httpClientOutputs, ENABLE_MONITOR_GROUP);
    }

    public static String populateChangeMonitorGroupStatusFormParams(ChangeMonitorGroupStatusInputs changeMonitorGroupStatusInputs) throws UnsupportedEncodingException {

        String delimiter = changeMonitorGroupStatusInputs.getDelimiter();
        String status = changeMonitorGroupStatusInputs.getStatus();
        String fullPath = changeMonitorGroupStatusInputs.getFullPathToGroup();
        String timePeriod = changeMonitorGroupStatusInputs.getTimePeriod();
        String fromTime = changeMonitorGroupStatusInputs.getFromTime();
        String toTime = changeMonitorGroupStatusInputs.getToTime();
        String description = changeMonitorGroupStatusInputs.getDescription();
        String identifier = changeMonitorGroupStatusInputs.getIdentifier();

        if (!delimiter.isEmpty())
            fullPath = fullPath.replace(delimiter, SITE_SCOPE_DELIMITER);

        Map<String,String> inputsMap = new HashMap<>();
        inputsMap.put(FULL_PATH_TO_GROUP, fullPath);
        inputsMap.put(STATUS, status);
        inputsMap.put(TIME_PERIOD, timePeriod);
        inputsMap.put(FROM_TIME, fromTime);
        inputsMap.put(TO_TIME, toTime);
        inputsMap.put(DESCRIPTION, description);
        inputsMap.put(IDENTIFIER, identifier);

        URIBuilder ub = new URIBuilder();

        for (Map.Entry<String, String> entry : inputsMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (!value.isEmpty())
              ub.addParameter(key, value);
        }
        String url = ub.toString();

        return url;
    }
}


