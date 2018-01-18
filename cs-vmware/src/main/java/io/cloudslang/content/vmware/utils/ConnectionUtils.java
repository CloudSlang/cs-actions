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

package io.cloudslang.content.vmware.utils;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.oo.sdk.content.plugin.SessionResource;
import io.cloudslang.content.vmware.connection.Connection;
import io.cloudslang.content.vmware.connection.impl.BasicConnection;
import io.cloudslang.content.vmware.services.utils.VmWareSessionResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

/**
 * Created by victor on 06.04.2017.
 */
public class ConnectionUtils {

    private static final String SHA_256 = "SHA-256";
    private static final String FORMAT_URL = "%s%s%d%s";

    @NotNull
    public static String sha256(final @NotNull String base) {
        try {
            final MessageDigest digest = MessageDigest.getInstance(SHA_256);
            final byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
            return new String(hash);
        } catch (NoSuchAlgorithmException nsae) {
            throw new RuntimeException(nsae.getMessage(), nsae.getCause());
        }
    }

    @NotNull
    public static String computeConnectionContextKey(final String protocol, final String host, final Integer port, final String username) {
        return sha256(format(FORMAT_URL, protocol, host, port, username));
    }

    @NotNull
    public static BasicConnection getBasicConnectionFromContext(@Nullable GlobalSessionObject<Map<String, Connection>> globalSessionObject, @NotNull final String connectionContextKey) {
        if (globalSessionObject == null) {
            globalSessionObject = new GlobalSessionObject<>();
        }
        Map<String, Connection> connectionMap = globalSessionObject.get();
        if (connectionMap != null) {
            if (connectionMap.get(connectionContextKey) instanceof BasicConnection) {
                return (BasicConnection) connectionMap.get(connectionContextKey);
            }
            final BasicConnection basicConnection = new BasicConnection();
            connectionMap.put(connectionContextKey, basicConnection);
            return basicConnection;
        }
        connectionMap = new HashMap<>();
        final BasicConnection basicConnection = new BasicConnection();
        connectionMap.put(connectionContextKey, basicConnection);
        globalSessionObject.setResource(new VmWareSessionResource(connectionMap));
        return basicConnection;
    }

    public static void clearConnectionFromContext(@Nullable GlobalSessionObject<Map<String, Connection>> globalSessionObject) {
        if (globalSessionObject == null) {
            return;
        }
        final SessionResource<Map<String, Connection>> sessionResource = globalSessionObject.getResource();
        if (sessionResource != null) {
            sessionResource.release();
            globalSessionObject.setResource(null);
        }
    }

}
