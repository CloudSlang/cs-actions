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
package io.cloudslang.content.abbyy.constants;

import io.cloudslang.content.abbyy.entities.others.ExportFormat;
import io.cloudslang.content.abbyy.entities.others.ImageSource;
import io.cloudslang.content.abbyy.entities.others.MarkingType;
import io.cloudslang.content.abbyy.entities.others.TextType;

public final class DefaultInputValues {
    public static final String LANGUAGE = "English";
    public static final String IMAGE_SOURCE = ImageSource.AUTO.toString();
    public static final String CORRECT_ORIENTATION = String.valueOf(true);
    public static final String CORRECT_SKEW = String.valueOf(true);
    public static final String EXPORT_FORMAT = ExportFormat.XML.toString();
    public static final String REGION = "-1,-1,-1,-1";
    public static final String TEXT_TYPE = TextType.NORMAL.toString();
    public static final String ONE_TEXT_LINE = String.valueOf(false);
    public static final String ONE_WORD_PER_TEXT_LINE = String.valueOf(false);
    public static final String MARKING_TYPE = MarkingType.SIMPLE_TEXT.toString();
    public static final String PLACEHOLDERS_COUNT = String.valueOf(1);
    public static final String PROXY_PORT = String.valueOf(8080);
    public static final String TRUST_ALL_ROOTS = String.valueOf(false);
    public static final String X_509_HOSTNAME_VERIFIER = "strict";
    public static final String CONNECT_TIMEOUT = String.valueOf(0);
    public static final String SOCKET_TIMEOUT = String.valueOf(0);
    public static final String KEEP_ALIVE = String.valueOf(true);
    public static final String CONNECTIONS_MAX_PER_ROUTE = String.valueOf(2);
    public static final String CONNECTIONS_MAX_TOTAL = String.valueOf(20);
    public static final String RESPONSE_CHARACTER_SET = "UTF-8";
    public static final String DISABLE_SIZE_LIMIT = String.valueOf(false);


    private DefaultInputValues(){

    }
}
