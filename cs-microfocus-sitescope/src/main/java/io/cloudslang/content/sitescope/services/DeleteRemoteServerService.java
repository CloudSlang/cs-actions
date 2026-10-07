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
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.net.URISyntaxException;
import java.util.Map;

import static io.cloudslang.content.httpclient.utils.Constants.BASIC;
import static io.cloudslang.content.sitescope.constants.Constants.*;
import static io.cloudslang.content.sitescope.services.HttpCommons.setCommonHttpInputs;

public class DeleteRemoteServerService {

    public @NotNull
    Map<String, String> execute(@NotNull DeleteRemoteServerInputs deleteRemoteServerInputs) throws Exception {
        final HttpClientInputs.HttpClientInputsBuilder httpClientInputs = HttpClientInputs.builder();
        final SiteScopeCommonInputs commonInputs = deleteRemoteServerInputs.getCommonInputs();

        setCommonHttpInputs(httpClientInputs, commonInputs);
        httpClientInputs.url(getUrl(deleteRemoteServerInputs));
        httpClientInputs.queryParamsAreURLEncoded(String.valueOf(true));
        httpClientInputs.authType(BASIC);
        httpClientInputs.username(commonInputs.getUsername());
        httpClientInputs.password(commonInputs.getPassword());
        httpClientInputs.method(DELETE);

        Map<String, String> httpClientOutputs = HttpClientService.execute(httpClientInputs.build());

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
