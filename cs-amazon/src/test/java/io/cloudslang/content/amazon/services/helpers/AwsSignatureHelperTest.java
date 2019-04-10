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


package io.cloudslang.content.amazon.services.helpers;

import org.junit.Test;
import static junit.framework.Assert.assertEquals;

public class AwsSignatureHelperTest {

    public static final String[][] ENDPOINT_LIST = {

            { "https://ec2.amazonaws.com", "us-east-1" },
            { "https://ec2.eu-west-3.amazonaws.com", "eu-west-3" },
            { "https://apigateway.us-east-2.amazonaws.com", "us-east-2" },
            { "https://api.pricing.ap-south-1.amazonaws.com", "ap-south-1" },
            { "https://cognito-idp.eu-west-2.amazonaws.com", "eu-west-2" },
            { "https://cognito-idp.amazonaws.com", "us-east-1" },
            { "https://Comprehend.us-west-2.amazonaws.com", "us-west-2" },
            { "https://rekognition.us-east-1.amazonaws.com", "us-east-1" },
            { "https://rekognition-fips.us-east-1.amazonaws.com", "us-east-1" },
            { "https://s3.dualstack.us-east-1.amazonaws.com", "us-east-1" },
            { "https://s3.amazonaws.com", "us-east-1" },

            { "ec2.amazonaws.com", "us-east-1" },
            { "ec2.eu-west-3.amazonaws.com", "eu-west-3" },
            { "apigateway.us-east-2.amazonaws.com", "us-east-2" },
            { "api.pricing.ap-south-1.amazonaws.com", "ap-south-1" },
            { "cognito-idp.eu-west-2.amazonaws.com", "eu-west-2" },
            { "cognito-idp.amazonaws.com", "us-east-1" },
            { "Comprehend.us-west-2.amazonaws.com", "us-west-2" },
            { "rekognition.us-east-1.amazonaws.com", "us-east-1" },
            { "rekognition-fips.us-east-1.amazonaws.com", "us-east-1" },
            { "s3.dualstack.us-east-1.amazonaws.com", "us-east-1" },
            { "s3.amazonaws.com", "us-east-1" },

            { "https://ec2.cn-north-1.amazonaws.com.cn", "cn-north-1" },
            { "https://s3.cn-northwest-1.amazonaws.com.cn", "cn-northwest-1" },
            { "https://cognito-identity.cn-north-1.amazonaws.com.cn", "cn-north-1" },
            { "https://streams.dynamodb.cn-northwest-1.amazonaws.com.cn", "cn-northwest-1" },

    };

    @Test
    public void testGetAmazonRegion() {
        AwsSignatureHelper helper = new AwsSignatureHelper();
        for (String[] item : ENDPOINT_LIST) {
            String endpoint = item[0];
            String region = item[1];
            assertEquals(region, helper.getAmazonRegion(endpoint));
        }
    }

}
