package org.score.content.httpclient.build;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.entity.ContentType;

/**
 * Created with IntelliJ IDEA.
 * User: tusaa
 * Date: 8/12/14
 */
public class ContentTypeBuilder {

    private String contentType = "text/plain";
    private String requestCharacterSet = Consts.ISO_8859_1.name();

    public ContentTypeBuilder setContentType(String contentType) {
        if (!StringUtils.isEmpty(contentType)) {
            this.contentType = contentType;
        }
        return this;
    }

    public ContentTypeBuilder setRequestCharacterSet(String requestCharacterSet) {
        if (!StringUtils.isEmpty(requestCharacterSet)) {
            this.requestCharacterSet = requestCharacterSet;
        }
        return this;
    }

    public ContentType buildContentType() {
        ContentType parsedContentType = ContentType.parse(contentType);
        return parsedContentType.withCharset(requestCharacterSet);
    }
}
