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

import java.util.Map;

import static org.junit.Assert.*;

public class GetStackDetailsActionTest {
    public static final String RETURN_CODE = "returnCode";
    public static final String EXCEPTION = "exception";
    public static final String RETURN_RESULT = "returnResult";
    public static final String STACK_NAME_RESULT = "stackName";
    public static final String STACK_ID_RESULT = "stackId";
    public static final String STACK_STATUS_RESULT = "stackStatus";
    public static final String STACK_STATUS_RESULT_REASON = "stackStatusReason";
    public static final String STACK_CREATION_TIME_RESULT = "stackCreationTime";
    public static final String STACK_DESCRIPTION_RESULT = "stackDescription";
    public static final String STACK_OUTPUTS_RESULT = "stackOutputs";
    public static final String STACK_RESOURCES_RESULT = "stackResources";
    public static final String OUTPUTS_EXPECTED_INSIDE_JSON = "[5]";


    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void execute() {
        String proxy = System.getenv().get("HTTP_PROXY");
        String proxyHost = proxy.substring(proxy.lastIndexOf("/") + 1, proxy.lastIndexOf(":"));
        String proxyPort = proxy.substring(proxy.lastIndexOf(":") + 1);

        AWSCredentialsProvider awsCreds = new DefaultAWSCredentialsProviderChain();

        GetStackDetailsAction getStackDetailsAction = new GetStackDetailsAction();
        Map<String, String> getStackDetailsResult = getStackDetailsAction.execute(
                awsCreds.getCredentials().getAWSAccessKeyId(),
                awsCreds.getCredentials().getAWSSecretKey(),
                "us-east-1",
                proxyHost,
                proxyPort,
                "",
                "",
                "10000",
                "60000",
                "AWS-Resource-Scheduler");

        System.out.println(getStackDetailsResult);

        assertNotNull(getStackDetailsResult);
        assertNotNull(getStackDetailsResult.get(RETURN_RESULT));
        assertNotNull(getStackDetailsResult.get(STACK_NAME_RESULT));
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

    @Test
    public void getStackOutputs() {
    }
}
