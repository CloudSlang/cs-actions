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

package io.cloudslang.content.couchbase.factory.views;

import io.cloudslang.content.couchbase.entities.inputs.InputsWrapper;

import static org.apache.http.entity.ContentType.APPLICATION_JSON;
/**
 * Created by TusaM
 * 4/28/2017.
 */
public class ViewHeadersBuilder {
    private ViewHeadersBuilder() {
        // prevent instantiation
    }

    public static void setViewHeaders(InputsWrapper wrapper) {
        switch (wrapper.getCommonInputs().getAction()) {
            default:
                wrapper.getHttpClientInputs().setContentType(APPLICATION_JSON.getMimeType());
        }
    }
}