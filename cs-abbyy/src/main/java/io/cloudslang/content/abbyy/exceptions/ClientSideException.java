/*
 * Copyright 2020-2023 Open Text
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



package io.cloudslang.content.abbyy.exceptions;

import java.util.Map;

public class ClientSideException extends AbbyySdkException {
    public ClientSideException(String msg) {
        super(msg);
    }


    public ClientSideException(Throwable innerEx, Map<String, String> resultsMap) {
        super(innerEx, resultsMap);
    }
}
