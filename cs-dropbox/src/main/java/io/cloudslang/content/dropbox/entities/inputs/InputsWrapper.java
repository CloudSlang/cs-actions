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



package io.cloudslang.content.dropbox.entities.inputs;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;

/**
 * Created by TusaM
 * 5/30/2017.
 */
public class InputsWrapper {
    private final HttpClientInputs httpClientInputs;
    private final CommonInputs commonInputs;
    private FolderInputs folderInputs;

    private InputsWrapper(Builder builder) {
        this.httpClientInputs = builder.httpClientInputs;
        this.commonInputs = builder.commonInputs;
    }

    public HttpClientInputs getHttpClientInputs() {
        return httpClientInputs;
    }

    public CommonInputs getCommonInputs() {
        return commonInputs;
    }

    public FolderInputs getFolderInputs() {
        return folderInputs;
    }

    public void setFolderInputs(FolderInputs folderInputs) {
        this.folderInputs = folderInputs;
    }

    public static class Builder {
        private HttpClientInputs httpClientInputs;
        private CommonInputs commonInputs;

        public InputsWrapper build() {
            return new InputsWrapper(this);
        }

        public Builder withHttpClientInputs(HttpClientInputs httpClientInputs) {
            this.httpClientInputs = httpClientInputs;
            return this;
        }

        public Builder withCommonInputs(CommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }
    }
}