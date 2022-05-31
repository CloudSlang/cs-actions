/*
  * (c) Copyright 2022 Micro Focus
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
package io.cloudslang.content.httpclient.utils;

import io.cloudslang.content.constants.OutputNames;

public class Outputs extends OutputNames {
    public static class HTTPClientOutputs {
        public static final String STATUS_CODE = "statusCode";
        public static final String FINAL_LOCATION = "finalLocation";
        public static final String RESPONSE_HEADERS = "responseHeaders";
        public static final String PROTOCOL_VERSION = "protocolVersion";
        public static final String REASON_PHRASE = "reasonPhrase";
    }
}
