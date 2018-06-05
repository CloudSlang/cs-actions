/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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

package io.cloudslang.content.dropbox.execute;

import io.cloudslang.content.dropbox.entities.inputs.CommonInputs;
import io.cloudslang.content.dropbox.entities.inputs.InputsWrapper;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;

import java.net.MalformedURLException;
import java.util.Map;

import static io.cloudslang.content.dropbox.factory.HeadersBuilder.buildHeaders;
import static io.cloudslang.content.dropbox.factory.InputsWrapperBuilder.buildWrapper;
import static io.cloudslang.content.dropbox.factory.PayloadBuilder.buildPayload;
import static io.cloudslang.content.dropbox.utils.InputsUtil.buildUrl;

/**
 * Created by TusaM
 * 5/29/2017.
 */
public class DropboxService {
    @SafeVarargs
    public final <T> Map<String, String> execute(HttpClientInputs httpClientInputs, CommonInputs commonInputs, T... builders) throws MalformedURLException {
        InputsWrapper wrapper = buildWrapper(httpClientInputs, commonInputs, builders);

        httpClientInputs.setUrl(buildUrl(wrapper));
        httpClientInputs.setBody(buildPayload(wrapper));
        buildHeaders(wrapper);

        return new HttpClientService().execute(httpClientInputs);
    }
}