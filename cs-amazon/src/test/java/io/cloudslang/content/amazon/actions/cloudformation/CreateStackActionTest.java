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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.*;

public class CreateStackActionTest {
    public static final String RETURN_RESULT = "returnResult";
    public static final String US_EAST_1 = "us-east-1";
    public static final String PROXY_USERNAME = "";
    public static final String PROXY_PASSWORD = "";
    public static final String CONNECT_TIMEOUT_MS = "10000";
    public static final String EXEC_TIMEOUT_MS = "60000";
    public static final String TEMPLATE_BODY = "";
    public static final String TEMPLATE_URL = "https://s3-external-1.amazonaws.com/cf-templates-1if2yhb1djnlp-us-east-1/2021316v6u-ec2_instance_testtbg18g5efi";
    public static final String TEMPLATE_PARAMETERS = "";
    public static final String TEMPLATE_CAPABILITIES = "";
    public static String proxyHost = null;
    public static String proxyPort = null;
    public static String TEST_STACK_NAME = "CloudSlang-Test-Stack";

    //@Before
    public void setUp() {
        try {
            String proxy = System.getenv().get("HTTP_PROXY");
            proxyHost = new URL(proxy).getHost();
            proxyPort = String.valueOf(new URL(proxy).getPort());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        TEST_STACK_NAME = TEST_STACK_NAME + "-" + UUID.randomUUID();
    }

   // @After
    public void tearDown() {
        proxyHost = proxyPort = null;
    }

   // @Test
    public void execute() {
        AWSCredentialsProvider awsCreds = new DefaultAWSCredentialsProviderChain();
        CreateStackAction createStackAction = new CreateStackAction();
        Map<String, String> createStackResults = createStackAction.execute(
                awsCreds.getCredentials().getAWSAccessKeyId(),
                awsCreds.getCredentials().getAWSSecretKey(),
                US_EAST_1,
                proxyHost,
                proxyPort,
                PROXY_USERNAME,
                PROXY_PASSWORD,
                CONNECT_TIMEOUT_MS,
                EXEC_TIMEOUT_MS,
                TEST_STACK_NAME,
                TEMPLATE_BODY,
                TEMPLATE_URL,
                TEMPLATE_PARAMETERS,
                TEMPLATE_CAPABILITIES
                );
        assertNotNull(createStackResults);
        assertNotNull(createStackResults.get(RETURN_RESULT));
    }
}
