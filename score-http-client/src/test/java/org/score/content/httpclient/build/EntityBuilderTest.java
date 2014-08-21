package org.score.content.httpclient.build;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

/**
 * User: Adina Tusa
 * Date: 8/19/14
 */
public class EntityBuilderTest {
    private static final String CONTENT_TYPE = "text/plain";
    private EntityBuilder entityBuilder;

    @Before
    public void setUp() {
        entityBuilder = new EntityBuilder();
    }

    @Test
    public void buildEntity() {
        HttpEntity httpEntity = entityBuilder
                .setBody("testBody")
                .buildEntity();
        assertThat(httpEntity, instanceOf(StringEntity.class));
        StringEntity stringEntity = (StringEntity) httpEntity;
        assertNull(stringEntity.getContentType());
    }

    @Test
    public void buildEntityWithContentType() {
        ContentType parsedContentType = ContentType.parse(CONTENT_TYPE);
        HttpEntity httpEntity = entityBuilder
                .setBody("testBody")
                .setContentType(parsedContentType)
                .buildEntity();
        assertThat(httpEntity, instanceOf(StringEntity.class));
        StringEntity stringEntity = (StringEntity) httpEntity;
        assertEquals(CONTENT_TYPE, stringEntity.getContentType().getValue());
    }

    @Test
    public void buildEntityWithFile() {
        ContentType parsedContentType = ContentType.parse(CONTENT_TYPE);
        HttpEntity httpEntity = entityBuilder
                .setFilePath("C:/testfile.txt")
                .setContentType(parsedContentType)
                .buildEntity();
        assertThat(httpEntity, instanceOf(FileEntity.class));
        FileEntity fileEntity = (FileEntity) httpEntity;
        assertEquals(CONTENT_TYPE, fileEntity.getContentType().getValue());
    }

    @Test
    public void buildEmptyEntity() {
        ContentType parsedContentType = ContentType.parse(CONTENT_TYPE);
        HttpEntity httpEntity = entityBuilder
                .setContentType(parsedContentType)
                .buildEntity();
        assertNull(httpEntity);
    }
}
