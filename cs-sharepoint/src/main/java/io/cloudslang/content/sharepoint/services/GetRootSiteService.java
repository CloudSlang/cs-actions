/*
 * (c) Copyright 2023 Micro Focus, L.P.
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
package io.cloudslang.content.sharepoint.services;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.httpclient.utils.Outputs.HTTPClientOutputs.STATUS_CODE;
import static io.cloudslang.content.sharepoint.utils.Constants.NEGATIVE_RETURN_CODE;
import static io.cloudslang.content.sharepoint.utils.Outputs.EXCEPTION;

public class GetRootSiteService {
    public static void processHttpResult(Map<String, String> httpResults) {

        String statusCode = httpResults.get(STATUS_CODE);

        if (StringUtils.isEmpty(statusCode) || Integer.parseInt(statusCode) < 200 || Integer.parseInt(statusCode) >= 300) {
            if (StringUtils.isEmpty(httpResults.get(EXCEPTION)))
                httpResults.put(EXCEPTION, httpResults.get(RETURN_RESULT));
            httpResults.put(RETURN_CODE, NEGATIVE_RETURN_CODE);
        }
    }
}
