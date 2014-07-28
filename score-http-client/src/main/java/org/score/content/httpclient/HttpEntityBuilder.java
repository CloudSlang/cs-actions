package org.score.content.httpclient;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;

import java.io.File;

public class HttpEntityBuilder {
    private String body;
    private String filePath;
    private String contentType = "text/plain";
    private String requestCharacterSet = Consts.ISO_8859_1.name();

    public HttpEntityBuilder setBody(String body) {
        this.body = body;
        return this;
    }

    public HttpEntityBuilder setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public HttpEntityBuilder setContentType(String contentType) {
        if (!StringUtils.isEmpty(contentType)) {
            this.contentType = contentType;
        }
        return this;
    }

    public HttpEntityBuilder setRequestCharacterSet(String requestCharacterSet) {
        if (!StringUtils.isEmpty(requestCharacterSet)) {
            this.requestCharacterSet = requestCharacterSet;
        }
        return this;
    }

    public HttpEntity buildEntity() {
        if (!StringUtils.isEmpty(body)) {
            StringEntity stringEntity = new StringEntity(body,
                    ContentType.create(contentType, requestCharacterSet));
            return stringEntity;
        }

        if (!StringUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            FileEntity fileEntity = new FileEntity(file,
                    ContentType.create(contentType, requestCharacterSet));
            fileEntity.setChunked(true);
            return fileEntity;
        }
        return null;
    }
}