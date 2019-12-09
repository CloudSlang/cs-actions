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


package io.cloudslang.content.hcm.utils;

public class Constants {

    public static final String GET = "GET";
    public static final String XML_DOCUMENT_SOURCE = "";
    public static final String X_PATH_QUERY_PROPERTY_NAME = "//options/property/*[name()=\"name\"]/text()";
    public static final String QUERY_TYPE = "nodelist";
    public static final String DELIMITER = ",";
    public static final String SECURE_PROCESSING = "true";
    public static final String QUERY = "//options/property[name=\"";
    public static final String QUERY_PART = "\"]/values/*[name()=\"value\" ]/text()";
    public static final String CONTENT_TYPE = "text/plain";
    public static final String SELECTED_VALUE = "selectedValue";
    public static final String PARAM = "param_";
    public static final String EXCEPTION = "exception";
    public static final String RETURN_RESULT_MESSAGE = "The operation was executed with success";
    public static final String PARAM_LIST = "paramList";
    public static final String NEW_LINE = System.lineSeparator();
    public static final String COMMA = ",";
    public static final String AND = "&";
    public static final String EQUALS = "=";
    public static final String WHITESPACE = " ";

}
