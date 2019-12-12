/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
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



package io.cloudslang.content.couchbase.execute;

import io.cloudslang.content.couchbase.entities.inputs.CommonInputs;
import io.cloudslang.content.couchbase.entities.inputs.InputsWrapper;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;

import java.net.MalformedURLException;
import java.util.Map;

import static io.cloudslang.content.couchbase.factory.HeadersBuilder.buildHeaders;
import static io.cloudslang.content.couchbase.factory.InputsWrapperBuilder.buildWrapper;
import static io.cloudslang.content.couchbase.factory.PayloadBuilder.buildPayload;
import static io.cloudslang.content.couchbase.utils.InputsUtil.buildUrl;

/**
 * Created by Mihai Tusa
 * 3/26/2017.
 */
public class CouchbaseService {
    @SafeVarargs
    public final <T> Map<String, String> execute(HttpClientInputs httpClientInputs, CommonInputs commonInputs, T... builders)
            throws MalformedURLException {
        InputsWrapper wrapper = buildWrapper(httpClientInputs, commonInputs, builders);

        httpClientInputs.setUrl(buildUrl(wrapper));

        buildHeaders(wrapper);
        buildPayload(wrapper);

        return new HttpClientService().execute(httpClientInputs);
    }
}