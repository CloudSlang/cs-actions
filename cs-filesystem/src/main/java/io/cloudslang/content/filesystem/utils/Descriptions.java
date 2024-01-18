/*
 * Copyright 2020-2024 Open Text
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

package io.cloudslang.content.filesystem.utils;

public class Descriptions {

    public static class Common {
        public static final String RETURN_CODE_DESCRIPTION = "0 if the operation succeeded, -1 otherwise.";
        public static final String EXCEPTION_DESCRIPTION = "The exception's stack trace if the operation failed. Empty otherwise.";
    }

    public static class GetSize {
        public static final String SOURCE_DESCRIPTION = "The file to read. It must be an absolute path.";
        public static final String THRESHOLD_DESCRIPTION = "The threshold to compare the file size to (in bytes).";

        public static final String GET_SIZE_RETURN_RESULT_DESCRIPTION = "The result of the comparison between the file's size and " +
                "the threshold. Otherwise it will contain the exception message.";
        public static final String SIZE_DESCRIPTION = "The file's size in bytes.";
        public static final String LESS_THAN_DESCRIPTION = "The file's size is smaller than the threshold.";
        public static final String EQUAL_TO_DESCRIPTION = "The file's size is the same as the threshold.";
        public static final String GREATER_THAN_DESCRIPTION = "The file's size is the greater than the threshold.";
        public static final String FAILURE_DESCRIPTION = "The operation failed.";

    }

    public static class IsDirectory {
        public static final String SOURCE_DESCRIPTION = "The file/folder to be checked";
        public static final String RETURN_RESULT_DESCRIPTION = "A success message if source is a directory. " +
                "Otherwise, it will contain the exception message.";
        public static final String SUCCESS_DESCRIPTION = "The source input is a directory";
        public static final String FAILURE_DESCRIPTION = "Source is not a directory or operation failed";
    }

    public static class Rename {
        public static final String SOURCE_DESCRIPTION = "Absolute path to the file or directory that will be renamed.";
        public static final String NEW_NAME_DESCRIPTION = "The new name for the file or directory." +
                " The value of this input should contain only the name and extension of the file" +
                " and not the full path or parent folders names.";
        public static final String OVERWRITE_DESCRIPTION = "If set to 'false' the operation will fail if 'newName' exists.";
        public static final String RENAMED_PATH_DESCRIPTION = "The absolute path of the renamed file if operation succeeded. Empty otherwise.";
        public static final String RETURN_RESULT_DESCRIPTION = "A message describing the success or failure of the operation.";
        public static final String SUCCESS_DESCRIPTION = "Rename operation succeeded.";
        public static final String FAILURE_DESCRIPTION = "Rename operation failed.";
    }
}
