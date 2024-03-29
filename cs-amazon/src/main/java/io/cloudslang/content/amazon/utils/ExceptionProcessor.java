/*
 * Copyright 2019-2024 Open Text
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
/*
 * Copyright 2019-2024 Open Text
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
 



package io.cloudslang.content.amazon.utils;

import io.cloudslang.content.amazon.entities.constants.Outputs;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;

/**
 * Created by persdana
 * 7/13/2015.
 */
public final class ExceptionProcessor {
    public static Map<String, String> getExceptionResult(Exception exception) {
        StringWriter writer = new StringWriter();
        exception.printStackTrace(new PrintWriter(writer));
        String exceptionString = writer.toString().replace(EMPTY + (char) 0x00, EMPTY);

        return getResultsMap(exception, exceptionString);
    }

    private static Map<String, String> getResultsMap(Exception exception, String exceptionString) {
        Map<String, String> returnResult = new HashMap<>();
        returnResult.put(Outputs.RETURN_RESULT, exception.getMessage());
        returnResult.put(Outputs.RETURN_CODE, Outputs.FAILURE_RETURN_CODE);
        returnResult.put(Outputs.EXCEPTION, exceptionString);
        return returnResult;
    }
}
