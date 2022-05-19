/*
  * (c) Copyright 2022 Micro Focus
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
package io.cloudslang.content.httpclient.utils;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.httpclient.utils.Constants.MINUS_1;

public class ErrorHandler {
    public static Map<String, String> handleErrors(Exception e, Map<String, String> results) {
        results.put(RETURN_CODE, MINUS_1);
        try {
            results.put(RETURN_RESULT, ExceptionUtils.getRootCause(e).toString());
        } catch (NullPointerException exception) {
            results.put(RETURN_RESULT, e.getMessage());
        }
        results.put(EXCEPTION, ExceptionUtils.getStackTrace(e));
        return results;
    }
}
