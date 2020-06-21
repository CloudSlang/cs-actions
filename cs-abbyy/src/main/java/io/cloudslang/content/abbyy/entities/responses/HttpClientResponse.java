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
package io.cloudslang.content.abbyy.entities.responses;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

public class HttpClientResponse {
    private String returnResult;
    private String exception;
    private Properties responseHeaders;
    private Short statusCode;
    private String returnCode;


    private HttpClientResponse() {

    }


    public String getReturnResult() {
        return returnResult;
    }


    public String getException() {
        return exception;
    }


    public Properties getResponseHeaders() {
        return responseHeaders;
    }


    public Short getStatusCode() {
        return statusCode;
    }


    public String getReturnCode() {
        return returnCode;
    }


    public static class Builder {
        private String returnResult;
        private String exception;
        private String responseHeaders;
        private String statusCode;
        private String returnCode;


        public Builder returnResult(String returnResult) {
            this.returnResult = returnResult;
            return this;
        }


        public Builder exception(String exception) {
            this.exception = exception;
            return this;
        }


        public Builder responseHeaders(String responseHeaders) {
            this.responseHeaders = responseHeaders;
            return this;
        }


        public Builder statusCode(String statusCode) {
            this.statusCode = statusCode;
            return this;
        }


        public Builder returnCode(String returnCode) {
            this.returnCode = returnCode;
            return this;
        }


        public HttpClientResponse build() throws IOException {
            HttpClientResponse response = new HttpClientResponse();

            response.returnResult = StringUtils.defaultString(this.returnResult);

            response.exception = StringUtils.defaultString(this.exception);

            response.responseHeaders = new Properties();
            response.responseHeaders.load(new StringReader(StringUtils.defaultString(this.responseHeaders)));

            response.statusCode = StringUtils.isEmpty(statusCode) ? null : Short.parseShort(statusCode);

            response.returnCode = StringUtils.defaultString(this.returnCode);

            return response;
        }
    }
}
