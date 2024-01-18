/*
 * Copyright 2020-2024 Open Text
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
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.net.URISyntaxException;
import java.util.Map;

import static io.cloudslang.content.httpclient.build.auth.AuthTypes.BASIC;
import static io.cloudslang.content.sitescope.constants.Constants.*;
import static io.cloudslang.content.sitescope.constants.SuccessMsgs.GET_GROUP_PROPERTIES;
import static io.cloudslang.content.sitescope.services.HttpCommons.setCommonHttpInputs;
import static jdk.nashorn.internal.runtime.PropertyDescriptor.GET;


public class GetGroupPropertiesService {

    public @NotNull
    Map<String, String> execute(@NotNull GetGroupPropertiesInputs getGroupPropertiesInputs) throws Exception {

        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final SiteScopeCommonInputs commonInputs = getGroupPropertiesInputs.getCommonInputs();

        httpClientInputs.setUrl(getUrl(getGroupPropertiesInputs));
        httpClientInputs.setQueryParamsAreURLEncoded(String.valueOf(true));
        setCommonHttpInputs(httpClientInputs, commonInputs);
        httpClientInputs.setAuthType(BASIC);
        httpClientInputs.setUsername(commonInputs.getUsername());
        httpClientInputs.setPassword(commonInputs.getPassword());
        httpClientInputs.setMethod(GET);
        httpClientInputs.setResponseCharacterSet(commonInputs.getResponseCharacterSet());

        Map<String, String> httpClientOutputs = new HttpClientService().execute(httpClientInputs);

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


