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
package io.cloudslang.content.rft.utils;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.oo.sdk.content.plugin.SessionResource;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.Session;
import io.cloudslang.content.rft.entities.sftp.SFTPConnection;
import io.cloudslang.content.rft.entities.sftp.SFTPSessionResource;
import io.cloudslang.content.rft.services.SFTPCopier;

import java.util.HashMap;
import java.util.Map;

public class CacheUtils {
    /**
     * @param resource the cache.
     * @return the SSH session from cache
     */
    private static Session getSftpSession(SessionResource<Map<String, SFTPConnection>> resource, String sessionId) {
        if (resource != null) {
            Object obj = resource.get();
            if (obj != null) {
                SFTPConnection connection = (SFTPConnection) ((Map) obj).get(sessionId);
                if (connection != null) {
                    return connection.getSession();
                }
            }
        }
        return null;
    }

    /**
     * @param resource the cache.
     * @return the SSH channel from cache
     */
    private static Channel getSftpChannel(SessionResource<Map<String, SFTPConnection>> resource, String sessionId) {
        if (resource != null) {
            Object obj = resource.get();
            if (obj != null) {
                SFTPConnection connection = (SFTPConnection) ((Map) obj).get(sessionId);
                if (connection != null) {
                    return connection.getChannel();
                }
            }
        }
        return null;
    }

    /**
     * Save the SSH session and the channel in the cache.
     * @param channel      The SSH channel.
     * @param session      The SSH session.
     * @param sessionParam The cache: GlobalSessionObject or SessionObject.
     */

    public static boolean saveSession(Session session, Channel channel, GlobalSessionObject<Map<String, SFTPConnection>> sessionParam, String sessionId) {
        final SFTPConnection sftpConnection;
        if (channel != null) {
            sftpConnection = new SFTPConnection(session, channel);
        } else {
        sftpConnection = new SFTPConnection(session);
       }
        if (sessionParam != null) {
            Map<String, SFTPConnection> tempMap = sessionParam.get();
            if (tempMap == null) {
                tempMap = new HashMap<>();
            }
            tempMap.put(sessionId, sftpConnection);
            sessionParam.setResource(new SFTPSessionResource(tempMap));
            return true;
        }
        return false;
    }


    /**
     * Remove the SSH session (and associated channel if any) from the cache.
     * @param sessionParam The cache.
     * @param sessionId    The key to the session in the cache map.
     */
    public static void removeSftpSession(GlobalSessionObject<Map<String, SFTPConnection>> sessionParam, String sessionId) {
        if (sessionParam != null) {
            SessionResource<Map<String, SFTPConnection>> resource = sessionParam.getResource();
            if (resource != null) {
                Map<String, SFTPConnection> tempMap = resource.get();
                if (tempMap != null) {
                    tempMap.remove(sessionId);
                }
            }
        }
    }

    /**
     * Get an opened SSH session from cache (Operation Orchestration session).
     *
     * @param sessionResource The session resource.
     * @return the SFTP Copier
     */
    public static SFTPCopier getFromCache(SessionResource<Map<String, SFTPConnection>> sessionResource, String sessionId) {
        Session savedSession = CacheUtils.getSftpSession(sessionResource, sessionId);
        if (savedSession != null && savedSession.isConnected()) {
            Channel savedChannel = CacheUtils.getSftpChannel(sessionResource, sessionId);
            return new SFTPCopier(savedSession,savedChannel);
        }
        return null;
    }
}
