/*
 * (c) Copyright 2021 Micro Focus, L.P.
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
package io.cloudslang.content.microsoftAD.services;

import io.cloudslang.content.microsoftAD.entities.AzureActiveDirectoryCommonInputs;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static io.cloudslang.content.microsoftAD.services.HttpCommons.httpGet;
import static io.cloudslang.content.microsoftAD.utils.Constants.*;


public class ListSubscribedSkusService {

    public static Map<String, String> getSubscribedSkus(@NotNull final AzureActiveDirectoryCommonInputs commonInputs) throws Exception {

        return httpGet(commonInputs, LIST_SUBSCRIBED_SKUS_URL);

    }


}
