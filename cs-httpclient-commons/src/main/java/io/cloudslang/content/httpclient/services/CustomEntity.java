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

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

import static io.cloudslang.content.httpclient.utils.Inputs.HTTPInputs.FORM_PARAMS;
import static io.cloudslang.content.httpclient.utils.Inputs.HTTPInputs.FORM_PARAMS_ARE_URLENCODED;
import static io.cloudslang.content.httpclient.utils.Inputs.HTTPInputs.MULTIPART_BODIES;
import static io.cloudslang.content.httpclient.utils.Inputs.HTTPInputs.MULTIPART_FILES;
import static io.cloudslang.content.httpclient.utils.Inputs.HTTPInputs.MULTIPART_VALUES_ARE_URLENCODED;

public class CustomEntity {

    public HttpEntity getHttpEntity(HttpClientInputs httpClientInputs) {

        AbstractHttpEntity httpEntity = null;
        if (!StringUtils.isEmpty(httpClientInputs.getFormParams())) {

            List<? extends NameValuePair> list;
            list = getNameValuePairs(httpClientInputs.getFormParams(),
                    !Boolean.parseBoolean(httpClientInputs.getFormParamsAreURLEncoded()),
                    FORM_PARAMS,
                    FORM_PARAMS_ARE_URLENCODED);

            Charset charset = ContentType.parse(httpClientInputs.getContentType()) != null ?
                    ContentType.parse(httpClientInputs.getContentType()).getCharset() : null;

            httpEntity = new UrlEncodedFormEntity(list, charset);

        } else if (!StringUtils.isEmpty(httpClientInputs.getBody())) {

            httpEntity = new StringEntity(httpClientInputs.getBody(), ContentType.parse(httpClientInputs.getContentType()));

        } else if (!StringUtils.isEmpty(httpClientInputs.getSourceFile())) {
            File file = new File(httpClientInputs.getSourceFile());
            httpEntity = new FileEntity(file, ContentType.parse(httpClientInputs.getContentType()));
        }

        if (httpEntity != null) {
            if (!StringUtils.isEmpty(httpClientInputs.getChunkedRequestEntity()) &&
                    Boolean.parseBoolean(httpClientInputs.getChunkedRequestEntity()) == true) {
                httpEntity.isChunked();
            }
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

        return null;
    }

    private List<? extends NameValuePair> getNameValuePairs(String theInput, boolean encode, String constInput, String constEncode) {
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
}
