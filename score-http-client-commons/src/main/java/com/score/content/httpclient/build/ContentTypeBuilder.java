package com.score.content.httpclient.build;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.ParseException;
import org.apache.http.entity.ContentType;
import com.score.content.httpclient.HttpClientInputs;

import java.nio.charset.UnsupportedCharsetException;

/**
 * Created with IntelliJ IDEA.
 * User: tusaa
 * Date: 8/12/14
 */
public class ContentTypeBuilder {
    private static String DEFAULT_CONTENT_TYPE = "text/plain";
    private static String DEFAULT_CHARACTER_SET = Consts.ISO_8859_1.name();

    private String contentType;
    private String requestCharacterSet;

    public ContentTypeBuilder setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public ContentTypeBuilder setRequestCharacterSet(String requestCharacterSet) {
        if (!StringUtils.isEmpty(requestCharacterSet)) {
            this.requestCharacterSet = requestCharacterSet;
        }
        return this;
    }

    public ContentType buildContentType() {
        String contentType = this.contentType;
        String requestCharacterSet = this.requestCharacterSet;
        ContentType parsedContentType;
        if (StringUtils.isEmpty(contentType)) {
            parsedContentType = ContentType.parse(DEFAULT_CONTENT_TYPE).withCharset(DEFAULT_CHARACTER_SET);
        } else {
            try {
                parsedContentType = ContentType.parse(contentType);
            } catch (ParseException | UnsupportedCharsetException e) {
                throw new IllegalArgumentException("Could not parse input '"
                        + HttpClientInputs.CONTENT_TYPE+"'. " + e.getMessage(), e);
            }
        }
        //do not override contentType provide by user
        if (!StringUtils.isEmpty(requestCharacterSet)) {
            try {
                parsedContentType = parsedContentType.withCharset(requestCharacterSet);
            } catch (UnsupportedCharsetException e) {
                throw new IllegalArgumentException("Could not parse input '"+HttpClientInputs.REQUEST_CHARACTER_SET
                        +"'. " + e.getMessage(), e);
            }
        }
        return parsedContentType;
    }
}
