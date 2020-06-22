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

import io.cloudslang.content.httpclient.entities.HttpClientInputs;

public final class InputNames {
    public static final String LOCATION_ID = "locationId";
    public static final String APPLICATION_ID = "applicationId";
    public static final String PASSWORD = "password";
    public static final String LANGUAGE = "language";
    public static final String PROFILE = "profile";
    public static final String TEXT_TYPE = "textType";
    public static final String IMAGE_SOURCE = "imageSource";
    public static final String CORRECT_ORIENTATION = "correctOrientation";
    public static final String CORRECT_SKEW = "correctSkew";
    public static final String READ_BARCODES = "readBarcodes";
    public static final String EXPORT_FORMAT = "exportFormat";
    public static final String WRITE_FORMATTING = "writeFormatting";
    public static final String WRITE_RECOGNITION_VARIANTS = "writeRecognitionVariants";
    public static final String WRITE_TAGS = "writeTags";
    public static final String DESCRIPTION = "description";
    public static final String PDF_PASSWORD = "pdfPassword";
    public static final String REGION = "region";
    public static final String LETTER_SET = "letterSet";
    public static final String REG_EXP = "regExp";
    public static final String ONE_TEXT_LINE = "oneTextLine";
    public static final String ONE_WORD_PER_TEXT_LINE = "oneWordPerTextLine";
    public static final String MARKING_TYPE = "markingType";
    public static final String PLACEHOLDERS_COUNT = "placeholdersCount";
    public static final String WRITING_STYLE = "writingStyle";
    public static final String DESTINATION_FOLDER = "destinationFolder";
    public static final String DESTINATION_FILE = HttpClientInputs.DESTINATION_FILE;
    public static final String SOURCE_FILE = HttpClientInputs.SOURCE_FILE;
    public static final String PROXY_HOST = HttpClientInputs.PROXY_HOST;
    public static final String PROXY_PORT = HttpClientInputs.PROXY_PORT;
    public static final String PROXY_USERNAME = HttpClientInputs.PROXY_USERNAME;
    public static final String PROXY_PASSWORD = HttpClientInputs.PROXY_PASSWORD;
    public static final String TRUST_ALL_ROOTS = HttpClientInputs.TRUST_ALL_ROOTS;
    public static final String X509_HOSTNAME_VERIFIER = HttpClientInputs.X509_HOSTNAME_VERIFIER;
    public static final String TRUST_KEYSTORE = HttpClientInputs.TRUST_KEYSTORE;
    public static final String TRUST_PASSWORD = HttpClientInputs.TRUST_PASSWORD;
    public static final String CONNECT_TIMEOUT = HttpClientInputs.CONNECT_TIMEOUT;
    public static final String SOCKET_TIMEOUT = HttpClientInputs.SOCKET_TIMEOUT;
    public static final String KEEP_ALIVE = HttpClientInputs.KEEP_ALIVE;
    public static final String CONNECTIONS_MAX_PER_ROUTE = HttpClientInputs.CONNECTIONS_MAX_PER_ROUTE;
    public static final String CONNECTIONS_MAX_TOTAL = HttpClientInputs.CONNECTIONS_MAX_TOTAL;
    public static final String RESPONSE_CHARACTER_SET = HttpClientInputs.RESPONSE_CHARACTER_SET;
    public static final String DISABLE_SIZE_LIMIT = "disableSizeLimit";


    private InputNames() {

    }
}
