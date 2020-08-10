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
package io.cloudslang.content.maps.constants;

public final class ExceptionsMsgs {

    public static final String KEY_CANNOT_CONTAIN_DELIMITER = String.format("Key cannot contain delimiter '%s', '%s' or '%s'.",
            Delimiters.KEY_VALUE_PAIR_DELIM, Delimiters.MAP_ENTRY_DELIM, Delimiters.QUOTE_DELIM);
    public static final String VALUE_CANNOT_CONTAIN_DELIMITER = String.format("Value cannot contain delimiter '%s', '%s' or '%s'.",
            Delimiters.KEY_VALUE_PAIR_DELIM, Delimiters.MAP_ENTRY_DELIM, Delimiters.QUOTE_DELIM);
    public static final String MAP_HAS_INVALID_FORMAT = "Given map has invalid format";
    public static final String NO_MAP_PROVIDED = "No map provided";

    private ExceptionsMsgs(){

    }
}
