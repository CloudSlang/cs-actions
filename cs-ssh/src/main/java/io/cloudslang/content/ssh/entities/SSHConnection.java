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



package io.cloudslang.content.ssh.entities;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.Session;

/**
 * @author ioanvranauhp
 *         Date: 10/29/14
 */
public class SSHConnection {
    private Session session;
    private Channel channel;
    private int sessionsCounter;

    public SSHConnection(Session session) {
        this.session = session;
    }

    public SSHConnection(Session session, Channel channel) {
        this.session = session;
        this.channel = channel;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public int getSessionsCounter() {
        return sessionsCounter;
    }

    public void setSessionsCounter(int sessionsCounter) {
        this.sessionsCounter = sessionsCounter;
    }
}
