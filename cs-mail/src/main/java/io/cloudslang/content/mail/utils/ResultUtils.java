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
package io.cloudslang.content.mail.utils;

import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.mail.constants.ExceptionMsgs;
import org.apache.commons.lang3.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public final class ResultUtils {

    public static Map<String, String> fromException(Exception e) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        String exStr = writer.toString().replace(StringUtils.EMPTY + (char) 0x00, StringUtils.EMPTY);

        Map<String, String> results = new HashMap<>();
        if(e.toString().contains(ExceptionMsgs.UNRECOGNIZED_SSL_MESSAGE)){
            results.put(OutputNames.RETURN_RESULT, ExceptionMsgs.UNRECOGNIZED_SSL_MESSAGE_PLAINTEXT_CONNECTION);
        } else {
            results.put(OutputNames.RETURN_RESULT, e.getMessage());
        }
        results.put(OutputNames.RETURN_CODE, ReturnCodes.FAILURE);
        results.put(OutputNames.EXCEPTION, exStr);
        return results;
    }
}
