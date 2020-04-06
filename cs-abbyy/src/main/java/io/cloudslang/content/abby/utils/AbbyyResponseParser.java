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
package io.cloudslang.content.abby.utils;

import io.cloudslang.content.abby.entities.AbbyyResponse;

import java.util.Map;

public interface AbbyyResponseParser {
    AbbyyResponse parseResponse(short statusCode, String responseBody) throws Exception;

    AbbyyResponse parseResponse(Map<String, String> response) throws Exception;
}
