package com.score.content.httpclient.build;

import com.score.content.httpclient.HttpClientInputs;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;

import java.io.File;

public class EntityBuilder {
    private String body;
    private String filePath;
    private ContentType contentType;

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

    public HttpEntity buildEntity() {
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
            fileEntity.setChunked(true);
            return fileEntity;
        }
        return null;
    }
}