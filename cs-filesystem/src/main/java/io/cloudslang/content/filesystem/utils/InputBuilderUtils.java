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
package io.cloudslang.content.filesystem.utils;

import io.cloudslang.content.filesystem.constants.ExceptionMsgs;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

public final class InputBuilderUtils {

    public static String buildSource(String source) throws Exception {
        File f = new File(source);

        if (StringUtils.isEmpty(source)) {
            throw new Exception(ExceptionMsgs.EMPTY_SOURCE);
        }
        if (!(f.isAbsolute())) {
            throw new Exception(ExceptionMsgs.INVALID_PATH);
            }
        else {
            if (!(f.exists())) {
                throw new Exception(ExceptionMsgs.INVALID_SOURCE);
            }
        }
        return source;
    }


    public static String buildThreshold(String threshold) throws Exception {
        if (StringUtils.isEmpty(threshold)) {
            throw new Exception(ExceptionMsgs.EMPTY_THRESHOLD);
        } else {
            try {
                Integer.parseInt(threshold);
            } catch (NumberFormatException ex) {
                throw new Exception(ExceptionMsgs.INVALID_THRESHOLD);
            }
            return threshold;
        }
    }
}
