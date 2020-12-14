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
package io.cloudslang.content.filesystem.constants;

public final class ExceptionMsgs {
    public static final String EMPTY_SOURCE = "The input source can't be null or empty.";
    public static final String INVALID_ABSOLUTE_PATH = "File/Folder path must be absolute.";
    public static final String EMPTY_THRESHOLD = "The input threshold can't be null or empty.";
    public static final String INVALID_THRESHOLD = "The value of the threshold input must be a positive integer.";
    public static final String DIRECTORY_TRAVERSAL = "Directory traversal path is not allowed.";
    public static final String DOES_NOT_EXIST = "'%s' does not exist.";
    public static final String NOT_A_DIRECTORY = "'%s' is not a directory.";
    public static final String NO_CHILDREN = "The specified path does not have any children, or it could not be read.";
    public static final String NULL_OR_EMPTY_INPUT = "Value of input '%s' can't be null or empty.";
    public static final String ILLEGAL_CHARACTERS = "Illegal characters found in value of input '%s'.";
    public static final String INVALID_VALUE_FOR_INPUT = "Invalid value '%s' for input '%s'.";

    private ExceptionMsgs() {
    }
}
