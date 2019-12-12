/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
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

import org.apache.http.NameValuePair;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by ioanvranauhp
 * Date 1/30/2015.
 */
public class UtilsTest {
    @Test
    public void testUrlEncodeQueryParamsUrlEncodeTrue() throws Exception {
        String queryParams = "pa%20ra+m 1=The+string+%C3%BC%40foo-bar";
        String encodeQueryParams = Utils.urlEncodeQueryParams(queryParams, true);
        assertEquals(queryParams, encodeQueryParams);

        queryParams = "param1=The%20st+ring %C3%BC%40foo-bar";
        encodeQueryParams = Utils.urlEncodeQueryParams(queryParams, true);
        assertEquals(queryParams, encodeQueryParams);
    }

    @Test
    public void testUrlEncodeQueryParamsUrlEncodeFalse() throws Exception {
        String queryParams = "pa%20ra+m 1=The+string+%40foo-bar";
        String encodeQueryParams = Utils.urlEncodeQueryParams(queryParams, false);
        assertEquals("pa ra m 1=The string @foo-bar", encodeQueryParams);

        queryParams = "param1=The%20st+ring %40foo-bar";
        encodeQueryParams = Utils.urlEncodeQueryParams(queryParams, false);
        assertEquals("param1=The st ring @foo-bar", encodeQueryParams);
    }

    @Test
    public void testUrlEncodeMultipleParamsUrlEncodeTrue() throws Exception {
        String queryParams = "p ar%20am+1=The+string+%C3%BC%40foo-bar";
        List<? extends NameValuePair> encodeQueryParams = Utils.urlEncodeMultipleParams(queryParams, true);
        assertEquals("p ar%20am+1", encodeQueryParams.get(0).getName());
        assertEquals("The+string+%C3%BC%40foo-bar", encodeQueryParams.get(0).getValue());

        queryParams = "p ar%20am+1=The%20st+ring %C3%BC%40foo-bar";
        encodeQueryParams = Utils.urlEncodeMultipleParams(queryParams, true);
        assertEquals("p ar%20am+1", encodeQueryParams.get(0).getName());
        assertEquals("The%20st+ring %C3%BC%40foo-bar", encodeQueryParams.get(0).getValue());
    }

    @Test
    public void testNameValueUrlEncodeMultipleParamsUrlEncodeFalse() throws Exception {
        String queryParams = "p ar%20am+1=The+string+%40foo-bar";
        List<? extends NameValuePair> encodeQueryParams = Utils.urlEncodeMultipleParams(queryParams, false);
        assertEquals("p ar am 1", encodeQueryParams.get(0).getName());
        assertEquals("The string @foo-bar", encodeQueryParams.get(0).getValue());

        queryParams = "p ar%20am+1=The%20st+ring %40foo-bar";
        encodeQueryParams = Utils.urlEncodeMultipleParams(queryParams, false);
        assertEquals("p ar am 1", encodeQueryParams.get(0).getName());
        assertEquals("The st ring @foo-bar", encodeQueryParams.get(0).getValue());
    }

    @Test
    public void testValidatePortNumberValidValue() {
        String portStringValue = "821";
        final int portNumber;
        portNumber = Integer.parseInt(portStringValue);
        assertEquals(portNumber, Utils.validatePortNumber(portStringValue));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidatePortNumberInvalidValue() {
        String portStringValue = "0";
        Utils.validatePortNumber(portStringValue);
    }
}
