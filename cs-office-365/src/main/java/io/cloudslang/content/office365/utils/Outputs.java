/*
 * (c) Copyright 2023 Micro Focus, L.P.
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


package io.cloudslang.content.office365.utils;

import io.cloudslang.content.constants.OutputNames;

public final class Outputs extends OutputNames {
    public static class CommonOutputs {
        public static final String DOCUMENT = "document";
        public static final String ATTACHMENT_ID = "attachment_id";
    }

    public static class AuthorizationOutputs {
        public static final String AUTH_TOKEN = "authToken";
        public static final String AUTH_TOKEN_TYPE = "authTokenType";
    }

    public static class GetAttachmentsOutputs {
        public static final String CONTENT_NAME = "contentName";
        public static final String CONTENT_TYPE = "contentType";
        public static final String CONTENT_BYTES = "contentBytes";
        public static final String CONTENT_SIZE = "contentSize";
    }
}

