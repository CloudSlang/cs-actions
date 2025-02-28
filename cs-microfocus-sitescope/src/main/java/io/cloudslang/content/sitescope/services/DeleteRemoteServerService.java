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
import io.cloudslang.content.sitescope.constants.SuccessMsgs;
import io.cloudslang.content.sitescope.entities.DeleteRemoteServerInputs;
import io.cloudslang.content.sitescope.entities.SiteScopeCommonInputs;
import io.cloudslang.content.sitescope.utils.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.net.URISyntaxException;
import java.util.Map;

import static io.cloudslang.content.httpclient.build.auth.AuthTypes.BASIC;
import static io.cloudslang.content.sitescope.constants.Constants.*;
import static io.cloudslang.content.sitescope.services.HttpCommons.setCommonHttpInputs;

public class DeleteRemoteServerService {

    public @NotNull
    Map<String, String> execute(@NotNull DeleteRemoteServerInputs deleteRemoteServerInputs) throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final SiteScopeCommonInputs commonInputs = deleteRemoteServerInputs.getCommonInputs();

        setCommonHttpInputs(httpClientInputs, commonInputs);
        httpClientInputs.setUrl(getUrl(deleteRemoteServerInputs));
        httpClientInputs.setQueryParamsAreURLEncoded(String.valueOf(true));
        httpClientInputs.setAuthType(BASIC);
        httpClientInputs.setUsername(commonInputs.getUsername());
        httpClientInputs.setPassword(commonInputs.getPassword());
        httpClientInputs.setMethod(DELETE);

        Map<String, String> httpClientOutputs = new HttpClientService().execute(httpClientInputs);

        return HttpUtils.convertToSitescopeResultsMap(httpClientOutputs, SuccessMsgs.DELETE_REMOTE_SERVER);
    }


    private String getUrl(DeleteRemoteServerInputs inputs) throws URISyntaxException {
        URIBuilder urlBuilder = new URIBuilder();
        urlBuilder.setScheme(inputs.getCommonInputs().getProtocol());
        urlBuilder.setHost(inputs.getCommonInputs().getHost());
        urlBuilder.setPort(Integer.parseInt(inputs.getCommonInputs().getPort()));
        urlBuilder.setPath(SITESCOPE_ADMIN_API + DELETE_REMOTE_SERVER_ENDPOINT);
        urlBuilder.addParameter(Inputs.DeleteRemoteServerInputs.PLATFORM, inputs.getPlatform());
        if (StringUtils.isNotEmpty(inputs.getRemoteName())) {
            urlBuilder.addParameter(Inputs.DeleteRemoteServerInputs.REMOTE_NAME, inputs.getRemoteName());
        }
        return urlBuilder.build().toString();
    }
}
