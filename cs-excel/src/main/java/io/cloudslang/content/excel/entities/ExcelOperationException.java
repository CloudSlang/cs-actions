/*
 * Copyright 2019-2023 Open Text
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

package io.cloudslang.content.excel.entities;

/**
 * Exception wrapper class for Excel
 *
 * @author shexiaoy
 */
public class ExcelOperationException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * constructor
     *
     * @param message
     */
    public ExcelOperationException(String message) {
        super(message);
    }

    /**
     * Constructor
     *
     * @param aThrowable a Throwable
     */

    public ExcelOperationException(Throwable aThrowable) {
        super(aThrowable);
    }

    /**
     * constructor
     *
     * @param message    a message
     * @param aThrowable a Throwable
     */
    public ExcelOperationException(String message, Throwable aThrowable) {
        super(message, aThrowable);
    }

}
