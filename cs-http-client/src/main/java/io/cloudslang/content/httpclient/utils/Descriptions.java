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


package io.cloudslang.content.httpclient.utils;

public class Descriptions {
    public static class UrlEncoder {

        public static final String URL_ENCODER_DESC = "This operation will percent encode (URL encode) the given text. This is used for encoding parts of a URI and for the preparation of data of the application/x-www-form-urlencoded media type. This is conforming to RFC 3986.";
        public static final String RETURN_RESULT_DESC = "The percent-encoded 'url'. In case of an error this output will contain the error message.";
    }

    public static class UrlDecoder {
        public static final String URL_DECODER_DESC = "This operation will decode percent (URL decode) the given text. This is used for decoding parts of a URI and for the preparation of data of the application/x-www-form-urlencoded media type. This is conforming to RFC 3986.";
        public static final String RETURN_RESULT_DESC = "The percent-decoded 'url'. In case of an error this output will contain the error message.";
    }

    public static class Commons {
        public static final String URL_DESC = "Any text like query or form values. Adding a whole URL will not work.";
        public static final String CHARACTER_SET_DESC = "The character encoding used for URL encoding. Leave this UTF-8, like the standard recommends and because the inputs are stored as UTF-8.";
        public static final String RETURN_CODE_DESC = "The return code of the operation. 0 if the operation goes to success, -1 if the operation goes to failure.";
        public static final String EXCEPTION_DESC = "In case of success response, this result is empty. In case of failure response, this result contains the java stack trace of the runtime exception.";

    }
}
