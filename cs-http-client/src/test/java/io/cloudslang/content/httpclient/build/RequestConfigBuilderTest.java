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

/**
 * Created with IntelliJ IDEA.
 * User: tusaa
 * Date: 7/29/14
 */

import org.apache.http.HttpException;
import org.apache.http.client.config.RequestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created with IntelliJ IDEA.
 * User: tusaa
 * Date: 7/28/14
 */
public class RequestConfigBuilderTest {

    private RequestConfigBuilder requestConfigBuilder;

    @BeforeEach
    public void setUp() {
        requestConfigBuilder = new RequestConfigBuilder();
    }

    @Test
    public void buildProxyRoute() throws URISyntaxException, HttpException {
        RequestConfig reqConfig = requestConfigBuilder
                .setProxyHost("myproxy.com")
                .setProxyPort("80")
                .setSocketTimeout("-2")
                .setConnectionTimeout("-2")
                .setFollowRedirects("false")
                .buildRequestConfig();
        assertNotNull(reqConfig);
        assertFalse(reqConfig.isRedirectsEnabled());
        assertEquals("-2", String.valueOf(reqConfig.getConnectTimeout()));
        assertEquals("-2", String.valueOf(reqConfig.getSocketTimeout()));
        assertNotNull(reqConfig.getProxy());
        assertEquals("myproxy.com", reqConfig.getProxy().getHostName());
        assertEquals("80", String.valueOf(reqConfig.getProxy().getPort()));
    }

    @Test
    public void buildNoProxyRoute() throws URISyntaxException, HttpException {
        RequestConfig reqConfig = requestConfigBuilder
                .buildRequestConfig();

        assertNotNull(reqConfig);
        assertTrue(reqConfig.isRedirectsEnabled());
        assertEquals("0", String.valueOf(reqConfig.getConnectTimeout()));
        assertEquals("0", String.valueOf(reqConfig.getSocketTimeout()));
        assertNull(reqConfig.getProxy());
    }

    @Test
    public void testBuildWithInvalidProxyPort() {
        final String invalidProxyPort = "invalidProxyPortText";
        final String expectedExceptionMessage = "Invalid value '" + invalidProxyPort + "' for input 'proxyPort'. Valid Values: -1 and integer values greater than 0";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                requestConfigBuilder.setProxyHost("myproxy.com")
                        .setProxyPort(invalidProxyPort)
                        .buildRequestConfig());
        assertTrue(exception.getMessage().contains(expectedExceptionMessage));
    }

    /*
       According to network specifications: a port number should be a 16-bit unsigned integer.
       Therefor negative values are not considered valid and should not be allowed.
     */
    @Test
    public void testBuildWithNegativeProxyPort() {
        final String invalidProxyPort = "-2";
        final String expectedExceptionMessage = "Invalid value '" + invalidProxyPort + "' for input 'proxyPort'. Valid Values: -1 and integer values greater than 0";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                requestConfigBuilder.setProxyHost("myproxy.com")
                        .setProxyPort(invalidProxyPort)
                        .buildRequestConfig());
        assertTrue(exception.getMessage().contains(expectedExceptionMessage));
    }

    /*
       Tests if a request configuration is created when the value '-1' is provided as a proxy port.
       The the value '-1' is provided then the proxy port input will be ignored and the default port of the scheme will be used.
       For example the port 80 will be used if the scheme is http.
     */
    @Test
    public void testBuildWithAcceptedNegativeProxyPort() {
        final String validProxyPort = "-1";

        RequestConfig reqConfig = requestConfigBuilder
                .setProxyHost("myproxy.com")
                .setProxyPort(validProxyPort)
                .buildRequestConfig();
        assertNotNull(reqConfig);
        assertNotNull(reqConfig.getProxy());
        assertEquals("myproxy.com", reqConfig.getProxy().getHostName());
    }
}
