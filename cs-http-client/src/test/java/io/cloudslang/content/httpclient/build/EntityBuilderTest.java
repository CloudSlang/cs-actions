/*
 * Copyright 2022-2024 Open Text
 * This program and the accompanying materials
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

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * User: Adina Tusa
 * Date: 8/19/14
 */
public class EntityBuilderTest {
    private static final String CONTENT_TYPE = "text/plain";
    private EntityBuilder entityBuilder;

    @BeforeEach
    public void setUp() {
        entityBuilder = new EntityBuilder();
    }

    @Test
    public void buildEntity() {
        HttpEntity httpEntity = entityBuilder
                .setBody("testBody")
                .buildEntity();
        assertThat(httpEntity).isInstanceOf(StringEntity.class);
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
        assertThat(httpEntity).isInstanceOf(StringEntity.class);
        StringEntity stringEntity = (StringEntity) httpEntity;
        assertEquals(CONTENT_TYPE, stringEntity.getContentType().getValue());
    }

    @Test
    public void buildEntityWithFile() throws Exception {
        ContentType parsedContentType = ContentType.parse(CONTENT_TYPE);
        final String fileName = "pom.xml";

        HttpEntity httpEntity = entityBuilder
                .setFilePath(fileName)
                .setContentType(parsedContentType)
                .buildEntity();
        assertThat(httpEntity).isInstanceOf(FileEntity.class);
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
