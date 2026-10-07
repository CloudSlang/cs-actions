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


package io.cloudslang.content.abbyy.constants;

import io.cloudslang.content.httpclient.utils.Inputs.HTTPInputs;

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
    public static final String DESTINATION_FILE = HTTPInputs.DESTINATION_FILE;
    public static final String SOURCE_FILE = HTTPInputs.SOURCE_FILE;
    public static final String PROXY_HOST = HTTPInputs.PROXY_HOST;
    public static final String PROXY_PORT = HTTPInputs.PROXY_PORT;
    public static final String PROXY_USERNAME = HTTPInputs.PROXY_USERNAME;
    public static final String PROXY_PASSWORD = HTTPInputs.PROXY_PASSWORD;
    public static final String TRUST_ALL_ROOTS = HTTPInputs.TRUST_ALL_ROOTS;
    public static final String X509_HOSTNAME_VERIFIER = HTTPInputs.X509_HOSTNAME_VERIFIER;
    public static final String TRUST_KEYSTORE = HTTPInputs.TRUST_KEYSTORE;
    public static final String TRUST_PASSWORD = HTTPInputs.TRUST_PASSWORD;
    public static final String CONNECT_TIMEOUT = HTTPInputs.CONNECT_TIMEOUT;
    public static final String SOCKET_TIMEOUT = HTTPInputs.RESPONSE_TIMEOUT;
    public static final String KEEP_ALIVE = HTTPInputs.KEEP_ALIVE;
    public static final String CONNECTIONS_MAX_PER_ROUTE = HTTPInputs.CONNECTIONS_MAX_PER_ROUTE;
    public static final String CONNECTIONS_MAX_TOTAL = HTTPInputs.CONNECTIONS_MAX_TOTAL;
    public static final String RESPONSE_CHARACTER_SET = HTTPInputs.RESPONSE_CHARACTER_SET;
    public static final String DISABLE_SIZE_LIMIT = "disableSizeLimit";


    private InputNames() {

    }
}
