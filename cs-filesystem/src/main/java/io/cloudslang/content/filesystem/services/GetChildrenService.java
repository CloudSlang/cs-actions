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

package io.cloudslang.content.filesystem.services;

import io.cloudslang.content.filesystem.entities.GetChildrenInputs;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.filesystem.constants.Constants.RETURN_CODE_SUCCESS;
import static io.cloudslang.content.filesystem.constants.ExceptionMsgs.NO_CHILDREN;
import static io.cloudslang.content.filesystem.constants.ResultsName.COUNT;
import static io.cloudslang.content.filesystem.utils.Utils.validateIsDirectory;

public class GetChildrenService {

    public static Map<String, String> execute(GetChildrenInputs getChildrenInputs) throws Exception {
        Map<String, String> result = new HashMap<>();

        String path = getChildrenInputs.getSource().trim();
        File f = new File(path);
        validateIsDirectory(f,path);

        File[] children;
        children = f.listFiles();

        StringBuilder paths = new StringBuilder();
        for (File child : children) {
            if (paths.length() > 0)
                paths.append(getChildrenInputs.getDelimiter());
            paths.append(child.getCanonicalPath());
        }
        result.put(RETURN_CODE, RETURN_CODE_SUCCESS);
        result.put(RETURN_RESULT, paths.toString());
        result.put(COUNT, String.valueOf(children.length));

        return result;
    }
}
