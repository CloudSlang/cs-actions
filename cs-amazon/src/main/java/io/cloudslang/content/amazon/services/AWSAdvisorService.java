/*
 * Copyright 2019-2023 Open Text
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

import com.amazonaws.services.costexplorer.AWSCostExplorer;
import com.amazonaws.services.costexplorer.model.GetRightsizingRecommendationRequest;
import com.amazonaws.services.costexplorer.model.GetRightsizingRecommendationResult;
import com.amazonaws.services.support.AWSSupport;
import com.amazonaws.services.support.model.DescribeTrustedAdvisorCheckResultRequest;
import com.amazonaws.services.support.model.DescribeTrustedAdvisorCheckResultResult;

public class AWSAdvisorService {


    public static DescribeTrustedAdvisorCheckResultResult getRecommendationDetails(
            final String checkID,
            final AWSSupport awsSupport) {
        DescribeTrustedAdvisorCheckResultRequest request = new DescribeTrustedAdvisorCheckResultRequest()
                .withLanguage("en")
                .withCheckId(checkID)
                ;
        DescribeTrustedAdvisorCheckResultResult requestResult = awsSupport.describeTrustedAdvisorCheckResult(request);
        return requestResult;
    }

    public static GetRightsizingRecommendationResult getCostRecommendations(AWSCostExplorer awsCostExplorer, String serviceNameVal)
    {

        GetRightsizingRecommendationRequest getRightsizingRecommendationRequest = new GetRightsizingRecommendationRequest();
        getRightsizingRecommendationRequest.setService(serviceNameVal);
        GetRightsizingRecommendationResult getRightsizingRecommendationResult = awsCostExplorer.getRightsizingRecommendation(getRightsizingRecommendationRequest);

        return getRightsizingRecommendationResult;
    }

}
