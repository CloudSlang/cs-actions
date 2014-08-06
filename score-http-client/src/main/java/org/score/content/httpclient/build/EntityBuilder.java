package org.score.content.httpclient.build;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;

import java.io.File;

public class EntityBuilder {
    private String body;
    private String filePath;
    private String contentType = "text/plain";
    private String requestCharacterSet = Consts.ISO_8859_1.name();

    public EntityBuilder setBody(String body) {
        this.body = body;
        return this;
    }

    public EntityBuilder setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public EntityBuilder setContentType(String contentType) {
        if (!StringUtils.isEmpty(contentType)) {
            this.contentType = contentType;
        }
        return this;
    }

    public EntityBuilder setRequestCharacterSet(String requestCharacterSet) {
        if (!StringUtils.isEmpty(requestCharacterSet)) {
            this.requestCharacterSet = requestCharacterSet;
        }
        return this;
    }

    public HttpEntity buildEntity() {
        ContentType parsedContentType = ContentType.parse(contentType);
        if (!StringUtils.isEmpty(requestCharacterSet)) {
            parsedContentType.withCharset(requestCharacterSet);
        }

        if (!StringUtils.isEmpty(body)) {
            return new StringEntity(body, parsedContentType);
        }

        if (!StringUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            FileEntity fileEntity = new FileEntity(file, parsedContentType);
            fileEntity.setChunked(true);
            return fileEntity;
        }
        return null;
    }
}