/*
 * Copyright 2026-2027 Open Text
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
package io.cloudslang.content.amazon.services;

import software.amazon.awssdk.services.costexplorer.CostExplorerClient;
import software.amazon.awssdk.services.costexplorer.model.GetRightsizingRecommendationRequest;
import software.amazon.awssdk.services.costexplorer.model.GetRightsizingRecommendationResponse;
import software.amazon.awssdk.services.support.SupportClient;
import software.amazon.awssdk.services.support.model.DescribeTrustedAdvisorCheckResultRequest;
import software.amazon.awssdk.services.support.model.DescribeTrustedAdvisorCheckResultResponse;

public class AWSAdvisorService {


    public static DescribeTrustedAdvisorCheckResultResponse getRecommendationDetails(
            final String checkID,
            final SupportClient awsSupport) {
        DescribeTrustedAdvisorCheckResultRequest request = DescribeTrustedAdvisorCheckResultRequest.builder()
                .language("en")
                .checkId(checkID)
                .build();
        DescribeTrustedAdvisorCheckResultResponse requestResult = awsSupport.describeTrustedAdvisorCheckResult(request);
        return requestResult;
    }

    public static GetRightsizingRecommendationResponse getCostRecommendations(CostExplorerClient awsCostExplorer, String serviceNameVal)
    {

        GetRightsizingRecommendationRequest request = GetRightsizingRecommendationRequest.builder()
                .service(serviceNameVal)
                .build();
        return awsCostExplorer.getRightsizingRecommendation(request);
    }

}
