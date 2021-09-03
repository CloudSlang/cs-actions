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
package io.cloudslang.content.rft.remote_copy;

public interface ICopier {

    /**
     * @param host
     * @param username
     * @param password
     * @throws UnsupportedOperationException
     */
    public void setCredentials(String host, int port, String username, String password) throws UnsupportedOperationException;

    ;

    /**
     * @param host
     * @param username
     * @param password
     * @param privateKeyFile
     * @throws UnsupportedOperationException
     */
    public void setCredentials(String host, int port, String username, String password, String privateKeyFile) throws UnsupportedOperationException;

    ;

    /**
     * @param destination
     * @param sourcePath
     * @param destPath
     * @throws Exception
     */
    public void copyTo(ICopier destination, String sourcePath, String destPath) throws Exception;

    /**
     * @return
     * @throws Exception
     */
    SimpleCopier getImplementation() throws Exception;

    /**
     * @param name
     * @param value
     */
    public void setCustomArgument(simpleArgument name, String value);

    /**
     * @param name
     * @param value
     */
    public void setCustomArgument(complexArgument name, Object value);

    /**
     * @param name
     * @return
     */
    public String getCustomArgument(simpleArgument name);

    /**
     * @return
     */
    public String getProtocolName();

    /**
     * @param version
     */
    public void setVersion(String version);

    /**
     * @param connectionTimeout
     */
    public void setConnectionTimeout(int connectionTimeout);

    /**
     * @param executionTimeout
     */
    public void setExecutionTimeout(int executionTimeout);

    /**
     * @param protocol
     */
    public void setProtocol(String protocol);


    /**
     *
     *
     *
     */
    public static enum simpleArgument {
        overwrite, type, characterSet, passive
    }

    /**
     *
     *
     *
     */
    public static enum complexArgument {
        kerberosTickets, sshSession
    }
}
