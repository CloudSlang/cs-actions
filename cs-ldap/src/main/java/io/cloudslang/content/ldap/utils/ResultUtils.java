/*
 * (c) Copyright 2020 EntIT Software LLC, a Micro Focus company, L.P.
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

package main.java.io.cloudslang.content.ldap.utils;

import com.sun.istack.internal.NotNull;
import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.constants.ReturnCodes;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.*;

import static main.java.io.cloudslang.content.ldap.constants.OutputNames.*;

public final class ResultUtils {

    public static Map<String, String> createNewEmptyMap() {
        Map<String, String> results = new HashMap<>();

        results.put(OutputNames.RETURN_RESULT, StringUtils.EMPTY);
        results.put(OutputNames.EXCEPTION, StringUtils.EMPTY);
        results.put(OutputNames.RETURN_CODE, ReturnCodes.FAILURE);
        results.put(RESULT_COMPUTER_DN, StringUtils.EMPTY);
        return results;
    }


    public static Map<String, String> fromException(@NotNull Exception ex) {
        Map<String, String> results = createNewEmptyMap();

        results.put(OutputNames.RETURN_RESULT, ex.getMessage());
        results.put(OutputNames.EXCEPTION, ExceptionUtils.getStackTrace(ex));
        results.put(OutputNames.RETURN_CODE, ReturnCodes.FAILURE);
        results.put(RESULT_COMPUTER_DN, StringUtils.EMPTY);
        return results;
    }

    public static String replaceInvalidXMLCharacters(String s) {
        List<String> list = new ArrayList();
        list.add("\u0000");

        String c;
        for(Iterator var2 = list.iterator(); var2.hasNext(); s = s.replace(c, "")) {
            c = (String)var2.next();
        }

        return s;
    }

}
