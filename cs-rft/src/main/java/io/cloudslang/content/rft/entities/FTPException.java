/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
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
package io.cloudslang.content.rft.entities;

public class FTPException extends Exception {

    public static final int DEFAULT_CODE = 550;
    public static final String DEFAULT_MESSAGE = "Requested action not taken";
    private static final long serialVersionUID = -9144039210324117444L;
    protected int _code = DEFAULT_CODE;

    public FTPException() {
        super(DEFAULT_MESSAGE);
    }

    public FTPException(String message) {
        super(message);
    }

    public FTPException(Throwable cause) {
        super(cause);
    }

    public FTPException(String message, Throwable cause) {
        super(message, cause);
    }

    public FTPException(int code, String message) {
        super(message);
        _code = code;
    }

    public FTPException(int code, String message, Throwable cause) {
        super(message, cause);
        _code = code;
    }

    public int getCode() {
        return _code;
    }

}
