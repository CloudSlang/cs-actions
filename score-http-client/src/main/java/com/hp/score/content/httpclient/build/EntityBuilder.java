package com.hp.score.content.httpclient.build;

import com.hp.score.content.httpclient.HttpClientInputs;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.File;
import java.util.List;

public class EntityBuilder {
    private String body;
    private String filePath;
    private ContentType contentType;
    private String formParams;
    private String encodeFormParams = "true";

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

    public EntityBuilder setEncodeFormParams(String encodeFormParams) {
        if (!StringUtils.isEmpty(encodeFormParams)) {
            this.encodeFormParams = encodeFormParams;
        }
        return this;
    }

    public HttpEntity buildEntity() {
        if (!StringUtils.isEmpty(formParams)) {
            List<? extends NameValuePair> list;
            boolean encodeFormParams = Boolean.parseBoolean(this.encodeFormParams);
            try {
                list = Utils.urlEncodeMultipleParams(formParams, encodeFormParams);
            } catch (UrlEncodeException e) {
                throw new UrlEncodeException(HttpClientInputs.ENCODE_FORM_PARAMS +
                        " is 'false' but " + HttpClientInputs.FORM_PARAMS + " are not properly encoded. "
                        + e.getMessage(), e);
            }

            return new UrlEncodedFormEntity(list, contentType.getCharset());
        }

        if (!StringUtils.isEmpty(body)) {
            return new StringEntity(body, contentType);
        }

        if (!StringUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new IllegalArgumentException("file set by input '"+ HttpClientInputs.SOURCE_FILE
                        +"' does not exist:" + filePath);
            }
            FileEntity fileEntity = new FileEntity(file, contentType);
            //todo make this optional
            fileEntity.setChunked(true);
            return fileEntity;
        }
        //todo
//        MultipartEntityBuilder.create()
//                .addBinaryBody("name1", new File(filePath1), contentType1, "your1.filename")
//                .addBinaryBody("name2", new File(filePath2), contentType2, "your2.filename")
//                .addTextBody("name3","text3",contentType3)
//                .addTextBody("name4","text4",contentType4).build();
        return null;
    }
}