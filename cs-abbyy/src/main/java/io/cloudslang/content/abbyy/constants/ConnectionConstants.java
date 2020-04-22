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

public final class ConnectionConstants {
    public static final String PROTOCOL = "https";
    public static final String HOST_TEMPLATE = "%s.ocrsdk.com/%s";


    private ConnectionConstants() {
    }


    public static final class Endpoints {
        public static final String PROCESS_IMAGE = "processImage";
        public static final String PROCESS_TEXT_FIELD = "processTextField";
        public static final String GET_TASK_STATUS = "getTaskStatus";


        private Endpoints() {
        }
    }

    public static final class Headers {
        public static final String AUTH_TYPE = "basic";
        public static final String CONTENT_TYPE = "application/octet-stream";


        private Headers() {

        }
    }

    public static final class QueryParams {
        public static final String LANGUAGE = "language";
        public static final String PROFILE = "profile";
        public static final String TEXT_TYPE = "textType";
        public static final String IMAGE_SOURCE = "imageSource";
        public static final String CORRECT_ORIENTATION = "correctOrientation";
        public static final String CORRECT_SKEW = "correctSkew";
        public static final String READ_BARCODES = "readBarcodes";
        public static final String EXPORT_FORMAT = "exportFormat";
        public static final String WRITE_FORMATTING = "xml:writeFormatting";
        public static final String WRITE_RECOGNITION_VARIANTS = "xml:writeRecognitionVariants";
        public static final String WRITE_TAGS = "pdf:writeTags";
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
        public static final String TASK_ID = "taskId";


        private QueryParams() {
        }
    }

    public static final class HttpMethods {
        public static final String GET = "GET";
        public static final String POST = "POST";


        private HttpMethods() {

        }
    }
}
