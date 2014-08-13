package org.score.content.httpclient.build;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BufferedHeader;
import org.junit.Test;
import org.score.content.httpclient.build.HeadersBuilder;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: tusaa
 * Date: 7/8/14
 */
public class HeadersBuilderTest {

    @Test
    public void build() {
        Header[] headers = new HeadersBuilder().setHeaders("header1:value1\nheader2:value2").buildHeaders();
        assertEquals(2, headers.length);
        assertThat(headers[0], instanceOf(BufferedHeader.class));
        BufferedHeader basicHeader = (BufferedHeader)headers[1];
        assertEquals("header2", basicHeader.getName());
        assertEquals("value2", basicHeader.getValue());
    }
}
