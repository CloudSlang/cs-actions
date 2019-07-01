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



package io.cloudslang.content.entities;

import org.apache.commons.codec.binary.Base64;

import java.nio.charset.Charset;

/**
 * Created by giloan on 4/3/2016.
 */
public class EncoderDecoder {

    /**
     * Encodes a string to base64.
     *
     * @param str     The string value to encode.
     * @param charset The encoding charset used to obtain the byte sequence of the string.
     * @return The encoded byte sequence using the base64 encoding.
     */
    public static String encodeStringInBase64(String str, Charset charset) {
        return new String(Base64.encodeBase64(str.getBytes(charset)));
    }

    /**
     * Decodes a string from base64.
     *
     * @param str The string value to decode.
     * @return The decoded string value.
     */
    public static String decodeBase64String(String str) {
        return new String(Base64.decodeBase64(str));
    }
}
