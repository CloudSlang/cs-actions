/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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

package io.cloudslang.content.mail.entities;

/**
 * Created by giloan on 11/5/2014.
 */
public class StringOutputStream extends java.io.OutputStream {
    String written;

    public StringOutputStream() {
        written = "";
    }

    public void write(int val) {
        written += (char) val;
    }

    public void write(byte[] buff) {
        written += new String(buff);
    }

    public void write(byte[] buff, int offset, int len) {
        written += new String(buff, offset, len);
    }

    public String toString() {
        return written;
    }

}
