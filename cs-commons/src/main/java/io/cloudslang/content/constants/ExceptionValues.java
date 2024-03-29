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




package io.cloudslang.content.constants;

/**
 * Created by victor on 31.08.2016.
 */
public class ExceptionValues {
    public static final String EXCEPTION_DELIMITER = ": ";
    public static final String INVALID_BOOLEAN_VALUE = "The provided string cannot be converted to a boolean value. It must be 'true' or 'false'";
    public static final String INVALID_INTEGER_VALUE = "The provided string cannot be converted to an integer value.";
    public static final String INVALID_DOUBLE_VALUE = "The provided string cannot be converted to a double value.";
    public static final String INVALID_LONG_VALUE = "The provided string cannot be converted to a long value.";
    public static final String INVALID_BOUNDS = "The lower bound is required to be less than the upper bound";
    public static final String INVALID_KEY_VALUE_PAIR = "The Key, Value pair has to be formatted as: key<keyValueDelimiter>value";
}
