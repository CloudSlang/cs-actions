/*
 * (c) Copyright 2019 Micro Focus, L.P.
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
package io.cloudslang.content.tesseract.utils;

public final class Constants {
    public static final String NEW_LINE = "\n";
    public static final String ENG = "ENG";
    public static final String FALSE = "false";
    public static final String TESSERACT_INITIALIZE_ERROR = "Error initializing Tesseract OCR.";
    public static final String TESSERACT_PARSE_ERROR = "Error encountered by the OCR engine while parsing the file.";
    public static final String TESSERACT_DATA_ERROR = "Cannot get resources from Jar file.";
    public static final String FILE_NOT_EXISTS = "The file does not exist.";
    public static final String TESSDATA = "tessdata";
    public static final String TESSDATA_ZIP = "tessdata.zip";
    public static final String DPI_SET = "300";
    public static final String UTF_8 = "UTF-8";
    public static final String TEXT_BLOCK = "text_block_";
    public static final double MINIMUM_DESKEW_THRESHOLD = 0.05d;
    public static final String PNG = "PNG";
    public static final String PNG_EXTENSION = ".png";
    public static final String PDF_EXTENSION = ".pdf";
    public static final String UNDERSCORE = "_";
    public static final String COMMA = ",";
    public static final String EXCEPTION_EXCEEDS_PAGES = "One or more values provided for the inputs exceeds the total" +
            " pages of the document";
    public static final String EXCEPTION_INVALID_NUMBER = "The %s for %s input is not valid";
    static final String EXCEPTION_NULL_EMPTY = "The %s can't be null or empty.";
    static final String EXCEPTION_INVALID_BOOLEAN = "The %s for %s input is not a valid boolean value.";
    static final String EXCEPTION_EMPTY_FILE = "The filePath input is required.";
    static final String EXCEPTION_INVALID_FILE = "The value '%s' for %s input is not a valid file path.";
    static final String EXCEPTION_INVALID_DATA_PATH = "The value '%s' for %s input is not a valid path.";
    static final String EXCEPTION_INVALID_INPUT = "The input is not valid.";
    static final String EXCEPTION_INVALID_FROM_PAGE = "The value for the input from_page cannot be higher than the one " +
            "for the input to_page";
}
