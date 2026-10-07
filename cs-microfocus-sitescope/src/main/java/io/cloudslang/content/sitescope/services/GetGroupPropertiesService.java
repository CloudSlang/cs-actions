/*
 * Copyright 2020-2025 Open Text
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
import io.cloudslang.content.sitescope.constants.Inputs;
import io.cloudslang.content.sitescope.entities.GetGroupPropertiesInputs;
import io.cloudslang.content.sitescope.entities.SiteScopeCommonInputs;
import io.cloudslang.content.sitescope.utils.HttpUtils;
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.net.URISyntaxException;
import java.util.Map;

import static io.cloudslang.content.httpclient.utils.Constants.BASIC;
import static io.cloudslang.content.sitescope.constants.Constants.*;
import static io.cloudslang.content.sitescope.constants.SuccessMsgs.GET_GROUP_PROPERTIES;
import static io.cloudslang.content.sitescope.services.HttpCommons.setCommonHttpInputs;


public class GetGroupPropertiesService {

    public @NotNull
    Map<String, String> execute(@NotNull GetGroupPropertiesInputs getGroupPropertiesInputs) throws Exception {

        final HttpClientInputs.HttpClientInputsBuilder httpClientInputs = HttpClientInputs.builder();
        final SiteScopeCommonInputs commonInputs = getGroupPropertiesInputs.getCommonInputs();

        httpClientInputs.url(getUrl(getGroupPropertiesInputs));
        httpClientInputs.queryParamsAreURLEncoded(String.valueOf(true));
        setCommonHttpInputs(httpClientInputs, commonInputs);
        httpClientInputs.authType(BASIC);
        httpClientInputs.username(commonInputs.getUsername());
        httpClientInputs.password(commonInputs.getPassword());
        httpClientInputs.method(GET);
        httpClientInputs.responseCharacterSet(commonInputs.getResponseCharacterSet());

        Map<String, String> httpClientOutputs = HttpClientService.execute(httpClientInputs.build());

        return HttpUtils.convertToSitescopeResultsMap(httpClientOutputs, GET_GROUP_PROPERTIES);
    }

    private String getUrl(GetGroupPropertiesInputs inputs) throws URISyntaxException {
        URIBuilder urlBuilder = new URIBuilder();
        urlBuilder.setScheme(inputs.getCommonInputs().getProtocol());
        urlBuilder.setHost(inputs.getCommonInputs().getHost());
        urlBuilder.setPort(Integer.parseInt(inputs.getCommonInputs().getPort()));
        urlBuilder.setPath(SITESCOPE_MONITORS_API + GET_GROUP_PROPERTIES_ENDPOINT);
        String fullPathToGroup = inputs.getFullPathToGroup().replace(inputs.getDelimiter(), SITE_SCOPE_DELIMITER);
        urlBuilder.addParameter(Inputs.CommonInputs.FULL_PATH_TO_GROUP, fullPathToGroup);

        return urlBuilder.build().toString();
    }
}

