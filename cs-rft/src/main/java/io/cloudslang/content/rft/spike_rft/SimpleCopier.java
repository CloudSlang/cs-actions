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
package io.cloudslang.content.rft.spike_rft;

import java.io.File;

public abstract class SimpleCopier implements ICopier {

    public int timeout;
    public String protocol;
    public String version = "";

    /**
     * @param host
     * @param defaultPort
     * @return
     */
    public static int resolvePort(String host, int defaultPort) {
        if (host.contains(":")) {
            int portNo = Integer.parseInt(host.split(":")[1]);
            if (portNo < 0 || portNo > 65535) {
                throw new IllegalArgumentException("invalid port number: " + portNo);
            }
            return portNo;
        } else {
            return defaultPort;
        }
    }

    /**
     * @param host
     * @return
     */
    public static String resolveHost(String host) {
        if (host.contains(":")) {
            return host.split(":")[0];
        } else {
            return host;
        }
    }

    public void copyTo(ICopier destination, String sourcePath, String destPath) throws Exception {
        //if either the source or the destination protocols are local, useTemp is false, i.e. we do not use a temp file
        //boolean useTemp = CopierFactory.copiers.local.name().equals(destination.getImplementation().getProtocolName())?false:CopierFactory.copiers.local.name().equals(this.getProtocolName())?false:true;

        if (!CopierFactory.copiers.local.name().equals(destination.getImplementation().getProtocolName())) {
            IReader reader = null;
            try {
                reader = getFile(sourcePath);
                destination.getImplementation().putFile(reader, destPath);
            } finally {
                if (!CopierFactory.copiers.local.name().equals(this.getImplementation().getProtocolName())
                        && reader != null && reader.getFile() != null) {
                    reader.getFile().delete();
                }
            }
        } else {
            if (new File(destPath).exists() && !Boolean.parseBoolean(destination.getCustomArgument(simpleArgument.overwrite))) {
                throw new Exception("The specified file exists, and overwrite is disabled.");
            }
            getFile(sourcePath, new File(destPath));
        }
    }

    @Override
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    protected abstract IReader getFile(String source) throws Exception;//, boolean useTemp) throws Exception;

    protected abstract void getFile(String source, File destination) throws Exception;

    protected abstract void putFile(IReader sourceFile, String destination) throws Exception;

    protected String getSimpleFileName(String path) {
        //String name = (path.contains("/"))?path.substring(path.lastIndexOf("/")+1):path;
        //modified to handle both paths with / and paths with \
        String name = (path.contains("/")) ? path.substring(path.lastIndexOf("/") + 1) : (path.contains("\\")) ? path.substring(path.lastIndexOf("\\") + 1) : path;
        return name;
    }

    public SimpleCopier getImplementation() throws Exception {
        return this;
    }

    @Override
    public void setCredentials(String host, int port, String username, String password) throws UnsupportedOperationException {
        if (host != null && host.length() > 0) {
            throw new UnsupportedOperationException(getProtocolName() + " does not allow the host to be set.");
        } else if (username != null && username.length() > 0) {
            throw new UnsupportedOperationException(getProtocolName() + " does not allow the username to be set.");
        } else if (password != null && password.length() > 0) {
            throw new UnsupportedOperationException(getProtocolName() + " does not allow the password to be set.");
        } else if (port >= 0) {
            throw new UnsupportedOperationException(getProtocolName() + " does not allow the port to be set.");
        }
    }

    @Override
    public void setCredentials(String host, int port, String username, String password, String privateKeyFile) throws UnsupportedOperationException {
        if (privateKeyFile != null && privateKeyFile.length() > 0) {
            throw new UnsupportedOperationException(getProtocolName() + " does not allow the privateKeyFile to be set.");
        }
        setCredentials(host, port, username, password);
    }

    @Override
    public void setCustomArgument(simpleArgument name, String value) {
        throw new UnsupportedOperationException(getProtocolName() + " does not allow " + name.name() + " to be set");
    }

    @Override
    public void setCustomArgument(complexArgument name, Object value) {
        throw new UnsupportedOperationException(getProtocolName() + " does not allow " + name.name() + " to be set");
    }

    @Override
    public String getCustomArgument(simpleArgument name) {
        throw new UnsupportedOperationException(getProtocolName() + " does not support " + name.name());
    }

    @Override
    public void setProtocol(String protocol){
        this.protocol = protocol;
    }

    @Override
    public void setVersion(String version){
        this.version = version;
    };

}
