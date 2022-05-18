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
package io.cloudslang.content.amazon.actions.cloudformation;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.ArraySizeComparator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import static org.junit.Assert.*;

public class GetStackDetailsActionTest {
    public static final String US_EAST_1 = "us-east-1";
    public static final String RETURN_RESULT = "returnResult";
    public static final String STACK_NAME_RESULT = "stackName";
    public static final String STACK_ID_RESULT = "stackId";
    public static final String STACK_OUTPUTS_RESULT = "stackOutputs";
    public static final String STACK_RESOURCES_RESULT = "stackResources";
    public static final String OUTPUTS_EXPECTED_INSIDE_JSON = "[5]";
    public static final String PROXY_USERNAME = "";
    public static final String PROXY_PASSWORD = "";
    public static final String CONNECT_TIMEOUT_MS = "10000";
    public static final String EXEC_TIMEOUT_MS = "60000";
    public static final String STACK_NAME = "AWS-Resource-Scheduler";
    public static String proxyHost = null;
    public static String proxyPort = null;


    //@Before
    public void setUp() throws Exception {
        try {
            String proxy = System.getenv().get("HTTP_PROXY");
            proxyHost = new URL(proxy).getHost();
            proxyPort = String.valueOf(new URL(proxy).getPort());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

   // @After
    public void tearDown() throws Exception {
    }

    //@Test
    public void execute() {
        AWSCredentialsProvider awsCreds = new DefaultAWSCredentialsProviderChain();

        GetStackDetailsAction getStackDetailsAction = new GetStackDetailsAction();
        Map<String, String> getStackDetailsResult = getStackDetailsAction.execute(
                awsCreds.getCredentials().getAWSAccessKeyId(),
                awsCreds.getCredentials().getAWSSecretKey(),
                US_EAST_1,
                proxyHost,
                proxyPort,
                PROXY_USERNAME,
                PROXY_PASSWORD,
                CONNECT_TIMEOUT_MS,
                EXEC_TIMEOUT_MS,
                STACK_NAME);

        assertNotNull(getStackDetailsResult);
        assertNotNull(getStackDetailsResult.get(RETURN_RESULT));
//        assertNotNull(getStackDetailsResult.get(STACK_NAME_RESULT));
        assertNotNull(getStackDetailsResult.get(STACK_ID_RESULT));
        assertNotNull(getStackDetailsResult.get(STACK_OUTPUTS_RESULT));
        assertNotNull(getStackDetailsResult.get(STACK_RESOURCES_RESULT));
        try {
            JSONAssert.assertEquals(OUTPUTS_EXPECTED_INSIDE_JSON,
                    getStackDetailsResult.get(STACK_OUTPUTS_RESULT),
                    new ArraySizeComparator(JSONCompareMode.LENIENT));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

   // @Test
    public void getStackOutputs() {
    }
}
