package org.score.content.httpclient;

import org.score.content.httpclient.URIBuilder;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: tusaa
 * Date: 7/8/14
 */
public class HttpUriRequestBuilderTest {

    @Test
    public void buildWithGetMethod() throws URISyntaxException {
        URI uri = new URIBuilder().setUrl("http://testurl.com").setQueryParams("param 1=value1&param 2=value2").setEncodeQueryParams("true").buildURI();
        assertEquals("http://testurl.com?param+1=value1&param+2=value2", uri.toString());
    }


}
