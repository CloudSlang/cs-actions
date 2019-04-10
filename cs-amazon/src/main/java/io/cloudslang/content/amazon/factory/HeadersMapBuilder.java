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



package io.cloudslang.content.amazon.factory;

import io.cloudslang.content.amazon.entities.inputs.InputsWrapper;
import io.cloudslang.content.amazon.factory.helpers.StorageUtils;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;

import static io.cloudslang.content.amazon.utils.InputsUtil.getHeadersOrQueryParamsMap;

import static io.cloudslang.content.amazon.entities.constants.Constants.Apis.S3_API;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.HEADER_DELIMITER;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.COLON;

/**
 * Created by TusaM
 * 12/21/2016.
 */
public class HeadersMapBuilder {
    private HeadersMapBuilder() {
        // prevent instantiation
    }

    public static Map<String, String> getHeadersMap(InputsWrapper wrapper) {
        Map<String, String> headersMap = isBlank(wrapper.getCommonInputs().getHeaders()) ? new HashMap<String, String>() :
                getHeadersOrQueryParamsMap(new HashMap<String, String>(), wrapper.getCommonInputs().getHeaders(), HEADER_DELIMITER, COLON, true);

        switch (wrapper.getCommonInputs().getApiService()) {
            case S3_API:
                new StorageUtils().setS3ApiHeadersMap(headersMap, wrapper);
                return headersMap;
            default:
                return headersMap;
        }
    }
}