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

import java.io.File;
import java.io.FileNotFoundException;

import static io.cloudslang.content.filesystem.constants.Constants.*;
import static io.cloudslang.content.filesystem.constants.ExceptionMsgs.*;

public class Utils {

    public static void validateIsDirectory(File source, String path) throws Exception {
        if (!source.isAbsolute())
            throw new IllegalArgumentException(INVALID_ABSOLUTE_PATH);
        if (!source.getCanonicalPath().equals(source.getAbsolutePath()))
            throw new Exception(ExceptionMsgs.DIRECTORY_TRAVERSAL);
        if (!source.exists())
            throw new FileNotFoundException(String.format(DOES_NOT_EXIST, path));
        if (!source.isDirectory())
            throw new Exception(String.format(NOT_A_DIRECTORY, path));
    }

    public static String createReturnResultGetSize(long size, long threshold){
        String returnResult;
        if(size == threshold)
            returnResult = String.format(EQUALS,threshold);
        else if(size > threshold)
            returnResult = String.format(GREATER_THAN, threshold);
        else
            returnResult = String.format(LESS_THAN,threshold);
        return returnResult;

    }
}
