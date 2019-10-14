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
package io.cloudslang.content.rft.entities.sftp;

import com.hp.oo.sdk.content.plugin.SessionResource;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.Session;

import java.util.Collection;
import java.util.Map;

/**
 * Wrapper class on SSH session.
 */
public class SFTPSessionResource extends SessionResource<Map<String, SFTPConnection>> {
    private Map<String, SFTPConnection> connectionMap;

    public SFTPSessionResource(Map<String, SFTPConnection> connectionMap) {
        this.connectionMap = connectionMap;
    }

    @Override
    public Map<String, SFTPConnection> get() {
        return connectionMap;
    }

    @Override
    public void release() {
        final Collection<SFTPConnection> sftpConnections = connectionMap.values();
        for (SFTPConnection sftpConnection : sftpConnections) {
            synchronized (sftpConnection) {
                Session session = sftpConnection.getSession();
                session.disconnect();
                Channel channel = sftpConnection.getChannel();
                if (channel != null) {
                    channel.disconnect();
                }
            }
        }
        connectionMap = null;
    }
}