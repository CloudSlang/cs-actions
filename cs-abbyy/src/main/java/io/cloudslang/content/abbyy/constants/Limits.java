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

public final class Limits {

    public static final long MAX_SIZE_OF_TXT_FILE = 50 * (int) Math.pow(2, 20);
    public static final long MAX_SIZE_OF_XML_FILE = 100 * (int) Math.pow(2, 20);
    public static final long MAX_SIZE_OF_PDF_FILE = 500 * (int) Math.pow(2, 20);
    public static final int MAX_SIZE_OF_DESCR = 255;
    public static final int MAX_NR_OF_EXPORT_FORMATS = 3;


    private Limits() {

    }
}
