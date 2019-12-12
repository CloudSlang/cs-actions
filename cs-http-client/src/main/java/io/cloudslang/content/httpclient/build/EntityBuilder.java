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



package io.cloudslang.content.httpclient.build;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

public class EntityBuilder {
    private String body;
    private String filePath;
    private ContentType contentType;
    private String formParams;
    private String formParamsAreURLEncoded = "false";
    private String multipartBodies;
    private String multipartFiles;
    private String multipartValuesAreURLEncoded = "false";
    private String multipartBodiesContentType = "text/plain; charset=ISO-8859-1";
    private String multipartFilesContentType = "application/octet-stream";
    private String chunkedRequestEntity;

    public EntityBuilder setBody(String body) {
        this.body = body;
        return this;
    }

    public EntityBuilder setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public EntityBuilder setContentType(ContentType contentType) {
        this.contentType = contentType;
        return this;
    }

    public EntityBuilder setFormParams(String formParams) {
        this.formParams = formParams;
        return this;
    }

    public EntityBuilder setFormParamsAreURLEncoded(String formParamsAreURLEncoded) {
        if (!StringUtils.isEmpty(formParamsAreURLEncoded)) {
            this.formParamsAreURLEncoded = formParamsAreURLEncoded;
        }
        return this;
    }

    public EntityBuilder setMultipartBodies(String multipartBodies) {
        this.multipartBodies = multipartBodies;
        return this;
    }

    public EntityBuilder setMultipartFiles(String multipartFiles) {
        this.multipartFiles = multipartFiles;
        return this;
    }

    public EntityBuilder setMultipartValuesAreURLEncoded(String multipartValuesAreURLEncoded) {
        if (!StringUtils.isEmpty(multipartValuesAreURLEncoded)) {
            this.multipartValuesAreURLEncoded = multipartValuesAreURLEncoded;
        }
        return this;
    }

    public EntityBuilder setMultipartBodiesContentType(String multipartBodiesContentType) {
        if (!StringUtils.isEmpty(multipartBodiesContentType)) {
            this.multipartBodiesContentType = multipartBodiesContentType;
        }
        return this;
    }

    public EntityBuilder setMultipartFilesContentType(String multipartFilesContentType) {
        if (!StringUtils.isEmpty(multipartFilesContentType)) {
            this.multipartFilesContentType = multipartFilesContentType;
        }
        return this;
    }

    public EntityBuilder setChunkedRequestEntity(String chunkedRequestEntity) {
        this.chunkedRequestEntity = chunkedRequestEntity;
        return this;
    }

    public HttpEntity buildEntity() {
        AbstractHttpEntity httpEntity = null;
        if (!StringUtils.isEmpty(formParams)) {
            List<? extends NameValuePair> list;
            list = getNameValuePairs(formParams, !Boolean.parseBoolean(this.formParamsAreURLEncoded),
                    HttpClientInputs.FORM_PARAMS, HttpClientInputs.FORM_PARAMS_ARE_URLENCODED);
            Charset charset = contentType != null ? contentType.getCharset() : null;
            httpEntity = new UrlEncodedFormEntity(list, charset);
        } else if (!StringUtils.isEmpty(body)) {
            httpEntity = new StringEntity(body, contentType);
        } else if (!StringUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new IllegalArgumentException("file set by input '" + HttpClientInputs.SOURCE_FILE
                        + "' does not exist:" + filePath);
            }
            httpEntity = new FileEntity(file, contentType);
        }
        if (httpEntity != null) {
            if (!StringUtils.isEmpty(chunkedRequestEntity)) {
                httpEntity.setChunked(Boolean.parseBoolean(chunkedRequestEntity));
            }
            return httpEntity;
        }

        if (!StringUtils.isEmpty(multipartBodies) || !StringUtils.isEmpty(multipartFiles)) {
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            if (!StringUtils.isEmpty(multipartBodies)) {
                List<? extends NameValuePair> list;
                list = getNameValuePairs(multipartBodies, !Boolean.parseBoolean(this.multipartValuesAreURLEncoded),
                        HttpClientInputs.MULTIPART_BODIES, HttpClientInputs.MULTIPART_VALUES_ARE_URLENCODED);
                ContentType bodiesCT = ContentType.parse(multipartBodiesContentType);
                for (NameValuePair nameValuePair : list) {
                    multipartEntityBuilder.addTextBody(nameValuePair.getName(), nameValuePair.getValue(), bodiesCT);
                }
            }

            if (!StringUtils.isEmpty(multipartFiles)) {
                List<? extends NameValuePair> list;
                list = getNameValuePairs(multipartFiles, !Boolean.parseBoolean(this.multipartValuesAreURLEncoded),
                        HttpClientInputs.MULTIPART_FILES, HttpClientInputs.MULTIPART_VALUES_ARE_URLENCODED);
                ContentType filesCT = ContentType.parse(multipartFilesContentType);
                for (NameValuePair nameValuePair : list) {
                    File file = new File(nameValuePair.getValue());
                    multipartEntityBuilder.addBinaryBody(nameValuePair.getName(), file, filesCT, file.getName());
                }
            }
            return multipartEntityBuilder.build();
        }

        return null;
    }

    private List<? extends NameValuePair> getNameValuePairs(String theInput, boolean encode, String constInput, String constEncode) {
        List<? extends NameValuePair> list;
        try {
            list = Utils.urlEncodeMultipleParams(theInput, encode);
        } catch (UrlEncodeException e) {
            throw new UrlEncodeException(constEncode +
                    " is 'false' but " + constInput + " are not properly encoded. "
                    + e.getMessage(), e);
        }
        return list;
    }
}