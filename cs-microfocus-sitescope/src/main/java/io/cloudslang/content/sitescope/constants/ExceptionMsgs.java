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
package io.cloudslang.content.sitescope.constants;

public final class ExceptionMsgs {

    public static final String EXCEPTION_AT_LEAST_ONE_OF_INPUTS = "At least one of the following inputs must have non-empty values: %s.";
    public static final String EXCEPTION_NULL_EMPTY = "The %s can't be null or empty.";
    public static final String EXCEPTION_INVALID_PORT = "The %s is not a valid port.";
    public static final String EXCEPTION_INVALID_BOOLEAN = "The %s for %s input is not a valid boolean value.";
    public static final String EXCEPTION_INVALID_STATUS = "The %s for %s input is not a valid value.";
    public static final String EXCEPTION_INVALID_NUMBER = "The %s for %s input is not a valid number value.";
    public static final String EXCEPTION_WHILE_PARSING_RESPONSE = "Error while parsing the server response";
    public static final String EXCEPTION_INVALID_CUSTOM_PARAM = "The provided customParameters are invalid.";
    public static final String EXCEPTION_INVALID_PLATFORM = "The %s is not a valid platform. The valid platforms are: Windows, Unix.";
    public static final String EXCEPTION_INVALID_PARAM = "The provided parameters are invalid.";

}
