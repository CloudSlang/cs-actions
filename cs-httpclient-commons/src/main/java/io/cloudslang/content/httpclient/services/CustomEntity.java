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
package io.cloudslang.content.httpclient.services;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.utils.HttpUtils;
import io.cloudslang.content.httpclient.utils.UrlEncodeException;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.AbstractHttpEntity;
import org.apache.hc.core5.http.io.entity.FileEntity;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.List;

import static io.cloudslang.content.httpclient.utils.Constants.*;
import static io.cloudslang.content.httpclient.utils.Constants.UTF_8;
import static io.cloudslang.content.httpclient.utils.Inputs.HTTPInputs.*;
import static io.cloudslang.content.httpclient.utils.Inputs.HTTPInputs.CONTENT_TYPE;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class CustomEntity {

    public static HttpEntity getHttpEntity(HttpClientInputs httpClientInputs) {
        ContentType parsedContentType = null;
        HttpEntity httpEntity = null;

        if (StringUtils.isEmpty(httpClientInputs.getContentType())) {
            try {
                parsedContentType = ContentType.parse(httpClientInputs.getContentType());
            } catch (UnsupportedCharsetException e) {
                throw new IllegalArgumentException("Could not parse input '"
                        + CONTENT_TYPE + "'. " + e.getMessage(), e);
            }

            if (!StringUtils.isEmpty(httpClientInputs.getRequestCharacterSet())) {
                try {
                    parsedContentType = parsedContentType.withCharset(httpClientInputs.getRequestCharacterSet());
                } catch (UnsupportedCharsetException e) {
                    throw new IllegalArgumentException("Could not parse input '" + REQUEST_CHARACTER_SET
                            + "'. " + e.getMessage(), e);
                }
            }
        }

        if (!StringUtils.isEmpty(httpClientInputs.getFormParams())) {

            List<? extends NameValuePair> list;
            list = getNameValuePairs(httpClientInputs.getFormParams(),
                    !Boolean.parseBoolean(httpClientInputs.getFormParamsAreURLEncoded()),
                    FORM_PARAMS,
                    FORM_PARAMS_ARE_URLENCODED);

            Charset charset = parsedContentType != null ? parsedContentType.getCharset() : null;

            httpEntity = new UrlEncodedFormEntity(list, charset);

        } else if (!StringUtils.isEmpty(httpClientInputs.getBody())) {

            httpEntity = new StringEntity(httpClientInputs.getBody(), parsedContentType);

        } else if (!StringUtils.isEmpty(httpClientInputs.getSourceFile())) {
            File file = new File(httpClientInputs.getSourceFile());
            httpEntity = new FileEntity(file, parsedContentType);
        }

        if (httpEntity != null) {
            return httpEntity;
        }

        if (!StringUtils.isEmpty(httpClientInputs.getMultipartBodies()) || !StringUtils.isEmpty(httpClientInputs.getMultipartFiles())) {
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            if (!StringUtils.isEmpty(httpClientInputs.getMultipartBodies())) {

                List<? extends NameValuePair> list;
                list = getNameValuePairs(httpClientInputs.getMultipartBodies(),
                        !Boolean.parseBoolean(httpClientInputs.getMultipartValuesAreURLEncoded()),
                        MULTIPART_BODIES,
                        MULTIPART_VALUES_ARE_URLENCODED);

                ContentType bodiesCT = ContentType.parse(httpClientInputs.getMultipartBodiesContentType());
                for (NameValuePair nameValuePair : list) {
                    multipartEntityBuilder.addTextBody(nameValuePair.getName(), nameValuePair.getValue(), bodiesCT);
                }
            }

            if (!StringUtils.isEmpty(httpClientInputs.getMultipartFiles())) {
                List<? extends NameValuePair> list;
                list = getNameValuePairs(httpClientInputs.getMultipartFiles(),
                        !Boolean.parseBoolean(httpClientInputs.getMultipartValuesAreURLEncoded()),
                        MULTIPART_FILES,
                        MULTIPART_VALUES_ARE_URLENCODED);

                ContentType filesCT = ContentType.parse(httpClientInputs.getMultipartFilesContentType());
                for (NameValuePair nameValuePair : list) {
                    File file = new File(nameValuePair.getValue());
                    multipartEntityBuilder.addBinaryBody(nameValuePair.getName(), file, filesCT, file.getName());
                }
            }
            return multipartEntityBuilder.build();
        }
        return new StringEntity(EMPTY);
    }

    private static List<? extends NameValuePair> getNameValuePairs(String theInput, boolean encode, String constInput, String constEncode) {
        List<? extends NameValuePair> list;
        try {
            list = HttpUtils.urlEncodeMultipleParams(theInput, encode);
        } catch (UrlEncodeException e) {
            throw new UrlEncodeException(constEncode +
                    " is 'false' but " + constInput + " are not properly encoded. "
                    + e.getMessage(), e);
        }
        return list;
    }

    public static List<? extends NameValuePair> urlEncodeMultipleParams(String params, boolean urlEncode) throws UrlEncodeException {
        List<BasicNameValuePair> list = new ArrayList<>();

        String[] pairs = params.split(AND);
        for (String pair : pairs) {
            String[] nameValue = pair.split(EQUAL, 2);
            String name = nameValue[0];
            String value = nameValue.length == 2 ? nameValue[1] : null;

            if (!urlEncode) {
                try {
                    name = URLDecoder.decode(name, UTF_8);
                    if (value != null) {
                        value = URLDecoder.decode(value, UTF_8);
                    }
                } catch (UnsupportedEncodingException e) {
                    //never happens
                    throw new RuntimeException(e);
                } catch (IllegalArgumentException ie) {
                    throw new UrlEncodeException(ie.getMessage(), ie);
                }
            }
            list.add(new BasicNameValuePair(name, value));
        }

        return list;
    }
}
