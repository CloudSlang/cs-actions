/*
 * Copyright 2019-2024 Open Text
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




package io.cloudslang.content.dropbox.entities.inputs;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;

/**
 * Created by TusaM
 * 5/30/2017.
 */
public class InputsWrapper {
    private final HttpClientInputs.HttpClientInputsBuilder httpClientInputsBuilder;
    private final String httpMethod;
    private final CommonInputs commonInputs;
    private FolderInputs folderInputs;

    private InputsWrapper(Builder builder) {
        this.httpClientInputsBuilder = builder.httpClientInputsBuilder;
        this.httpMethod = httpClientInputsBuilder.build().getMethod();
        this.commonInputs = builder.commonInputs;
    }

    public HttpClientInputs.HttpClientInputsBuilder getHttpClientInputsBuilder() {
        return httpClientInputsBuilder;
    }

    public String getHttpMethod() {
        return httpMethod;
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
        private HttpClientInputs.HttpClientInputsBuilder httpClientInputsBuilder;
        private CommonInputs commonInputs;

        public InputsWrapper build() {
            return new InputsWrapper(this);
        }

        public Builder withHttpClientInputsBuilder(HttpClientInputs.HttpClientInputsBuilder httpClientInputsBuilder) {
            this.httpClientInputsBuilder = httpClientInputsBuilder;
            return this;
        }

        public Builder withCommonInputs(CommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }
    }
}