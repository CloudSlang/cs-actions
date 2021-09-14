/*
 * (c) Copyright 2021 Micro Focus
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

package io.cloudslang.content.active_directory.utils;

import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.constants.ReturnCodes;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.*;

public final class ResultUtils {

    public static Map<String, String> createNewResultsEmptyMap() {
        Map<String, String> results = new HashMap<>();

        results.put(OutputNames.RETURN_RESULT, StringUtils.EMPTY);
        results.put(OutputNames.EXCEPTION, StringUtils.EMPTY);
        results.put(OutputNames.RETURN_CODE, StringUtils.EMPTY);
        return results;
    }


    public static Map<String, String> fromException(Exception ex) {
        Map<String, String> results = createNewResultsEmptyMap();

        results.put(OutputNames.RETURN_RESULT, ex.getMessage());
        results.put(OutputNames.EXCEPTION, ExceptionUtils.getStackTrace(ex));
        results.put(OutputNames.RETURN_CODE, ReturnCodes.FAILURE);
        return results;
    }

    public static String replaceInvalidXMLCharacters(String s) {
        List<String> list = new ArrayList();
        list.add("\u0000");

        String c;
        for (Iterator var2 = list.iterator(); var2.hasNext(); s = s.replace(c, "")) {
            c = (String) var2.next();
        }

        return s;
    }

}
