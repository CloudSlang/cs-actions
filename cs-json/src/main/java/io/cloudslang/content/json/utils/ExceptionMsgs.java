/*
 * (c) Copyright 2021 EntIT Software LLC, a Micro Focus company, L.P.
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

package io.cloudslang.content.json.utils;

public final class ExceptionMsgs {
    public static final String EXCEPTION_WHILE_PARSING = "Exception occurred while parsing input '%s': %s";
    public static final String NULL_OR_EMPTY_INPUT = "Value of input '%s' was null or empty.";
    public static final String JSON_OBJECT_SHOULD_BE_OBJECT_OR_ARRAY = "Json object should be object or array.";
    public static final String NOT_A_VALID_JSON_ARRAY_MESSAGE = "The input value is not a valid JavaScript array!";
    public static final String DIFFERENT_ARRAY = "List under iteration was changed between successive iteration steps";
    public static final String INVALID_JSON_ARRAY = "Input value is not a valid JavaScript array";
    public static final String EMPTY_JSON_ARRAY = "JSON array is empty.";
    private ExceptionMsgs() {

    }
}
