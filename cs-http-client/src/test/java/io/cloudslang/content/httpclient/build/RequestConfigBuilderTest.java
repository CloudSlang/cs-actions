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

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.core5.util.Timeout;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.net.URISyntaxException;

import static junit.framework.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: tusaa
 * Date: 7/28/14
 */
public class RequestConfigBuilderTest {

    private RequestConfigBuilder requestConfigBuilder;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        requestConfigBuilder = new RequestConfigBuilder();
    }

    @After
    public void tearDown() {
        requestConfigBuilder = null;
    }

    @Test
    public void buildProxyRoute() throws URISyntaxException {
        RequestConfig reqConfig = requestConfigBuilder
                .setProxyHost("myproxy.com")
                .setProxyPort("80")
                .setSocketTimeout("-2")
                .setConnectionTimeout("-2")
                .setFollowRedirects("false")
                .buildRequestConfig();
        assertNotNull(reqConfig);
        assertFalse(reqConfig.isRedirectsEnabled());
        // HC5: negative/zero timeouts map to Timeout.ofMilliseconds(0) (infinite wait)
        assertEquals(Timeout.ofMilliseconds(0), reqConfig.getConnectTimeout());
        assertEquals(Timeout.ofMilliseconds(0), reqConfig.getResponseTimeout());
        assertNotNull(reqConfig.getProxy());
        assertEquals("myproxy.com", reqConfig.getProxy().getHostName());
        assertEquals("80", String.valueOf(reqConfig.getProxy().getPort()));
    }

    @Test
    public void buildNoProxyRoute() throws URISyntaxException {
        RequestConfig reqConfig = requestConfigBuilder
                .buildRequestConfig();

        assertNotNull(reqConfig);
        assertTrue(reqConfig.isRedirectsEnabled());
        // HC5: 0 timeout → Timeout.ofMilliseconds(0) (infinite wait)
        assertEquals(Timeout.ofMilliseconds(0), reqConfig.getConnectTimeout());
        assertEquals(Timeout.ofMilliseconds(0), reqConfig.getResponseTimeout());
        assertNull(reqConfig.getProxy());
    }

    @Test
    public void testBuildWithInvalidProxyPort() {
        final String invalidProxyPort = "invalidProxyPortText";
        final String expectedExceptionMessage = "Invalid value '" + invalidProxyPort + "' for input 'proxyPort'. Valid Values: -1 and integer values greater than 0";

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(expectedExceptionMessage);
        requestConfigBuilder.setProxyHost("myproxy.com")
                .setProxyPort(invalidProxyPort)
                .buildRequestConfig();
    }

    /*
       According to network specifications: a port number should be a 16-bit unsigned integer.
       Therefor negative values are not considered valid and should not be allowed.
     */
    @Test
    public void testBuildWithNegativeProxyPort() {
        final String invalidProxyPort = "-2";
        final String expectedExceptionMessage = "Invalid value '" + invalidProxyPort + "' for input 'proxyPort'. Valid Values: -1 and integer values greater than 0";

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(expectedExceptionMessage);
        requestConfigBuilder.setProxyHost("myproxy.com")
                .setProxyPort(invalidProxyPort)
                .buildRequestConfig();
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

