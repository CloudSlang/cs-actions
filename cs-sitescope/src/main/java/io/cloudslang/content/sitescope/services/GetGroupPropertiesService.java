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

import org.jetbrains.annotations.NotNull;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.sitescope.entities.GetGroupPropertiesInputs;
import io.cloudslang.content.sitescope.entities.SiteScopeCommonInputs;

import java.util.Map;

import static io.cloudslang.content.httpclient.build.auth.AuthTypes.BASIC;
import static io.cloudslang.content.httpclient.entities.Constants.CHANGEIT;
import static io.cloudslang.content.httpclient.entities.Constants.DEFAULT_JAVA_KEYSTORE;
import static io.cloudslang.content.sitescope.constants.Constants.*;
import static io.cloudslang.content.sitescope.services.HttpCommons.setCommonHttpInputs;
import static jdk.nashorn.internal.runtime.PropertyDescriptor.GET;


public class GetGroupPropertiesService {

    public @NotNull
    Map<String, String> execute(@NotNull GetGroupPropertiesInputs getGroupPropertiesInputs) throws Exception {

        String delimiter = getGroupPropertiesInputs.getDelimiter();
        String fullPath = getGroupPropertiesInputs.getFullPathToGroup();
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final SiteScopeCommonInputs commonInputs = getGroupPropertiesInputs.getCommonInputs();

        if (!delimiter.isEmpty())
            fullPath = fullPath.replace(delimiter,SITE_SCOPE_DELIMITER);

        httpClientInputs.setUrl(commonInputs.getProtocol() + "://" + commonInputs.getHost() + COLON + commonInputs.getPort() +
                SITESCOPE_API + GET_GROUP_PROPERTIES_ENDPOINT + fullPath);

        setCommonHttpInputs(httpClientInputs, commonInputs);

        httpClientInputs.setAuthType(BASIC);
        httpClientInputs.setUsername(commonInputs.getUsername());
        httpClientInputs.setPassword(commonInputs.getPassword());
        httpClientInputs.setMethod(GET);
        httpClientInputs.setKeystore(DEFAULT_JAVA_KEYSTORE);
        httpClientInputs.setKeystorePassword(CHANGEIT);
        httpClientInputs.setResponseCharacterSet(commonInputs.getResponseCharacterSet());

        return new HttpClientService().execute(httpClientInputs);
    }
}


