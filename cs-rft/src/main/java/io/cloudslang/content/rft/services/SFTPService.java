/*
 * (c) Copyright 2021 Micro Focus
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
package io.cloudslang.content.rft.services;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import io.cloudslang.content.rft.entities.sftp.IHasFTPOperation;
import io.cloudslang.content.rft.entities.sftp.SFTPConnection;
import io.cloudslang.content.rft.utils.CacheUtils;
import io.cloudslang.content.rft.utils.Constants;
import io.cloudslang.content.rft.utils.SFTPOperation;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Provider;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.rft.utils.Constants.*;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;

public class SFTPService {

    public Map<String, String> execute(IHasFTPOperation sftpInputs, SFTPOperation sftpOperation) {
        SFTPCopier sftpCopier = null;
        String sessionId = "";
        boolean providerAdded = addSecurityProvider();

        try {
            sessionId = "sshSession:" + sftpInputs.getSftpCommonInputs().getHost() + "-" + sftpInputs.getSftpCommonInputs().getPort() + "-" + sftpInputs.getSftpCommonInputs().getUsername();
            sftpCopier = getSftpCopierFromCache(sftpInputs, sessionId);

            if (sftpCopier == null || !sftpCopier.isConnected()) {
                sftpCopier = new SFTPCopier(sftpInputs);
            }

            performSFTPOperation(sftpInputs, sftpOperation, sftpCopier, sessionId);
            if (sftpOperation == SFTPOperation.GET_CHILDREN)
                return sftpCopier.getResult();

            return getSuccessResultsMap(SUCCESS_RESULT);


        } catch (Exception e) {
            if (sftpCopier != null) {
                cleanupCopier(sftpInputs, sftpCopier, sessionId);
            }
            Map<String, String> results = new HashMap<>();
            results.put(EXCEPTION, String.valueOf(e));
            results.put(RETURN_RESULT, e.getMessage());
            results.put(RETURN_CODE, FAILURE_RETURN_CODE);
            return results;
        } finally {
            if (providerAdded)
                removeSecurityProvider();
        }
    }


    private void performSFTPOperation(IHasFTPOperation sftpInputs, SFTPOperation sftpOperation, SFTPCopier sftpCopier, String sessionId) throws Exception {
        if (sftpOperation == SFTPOperation.GET) {
            sftpCopier.setSftpInputs(sftpInputs);
            try {
                CompletableFuture.runAsync(() -> {
                    try {
                        sftpCopier.getFromRemote();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).get(Integer.parseInt(sftpInputs.getSftpCommonInputs().getExecutionTimeout()) * 1000L, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                throw new Exception(EXCEPTION_EXECUTION_TIMED_OUT);
            }
        } else if (sftpOperation == SFTPOperation.PUT) {
            sftpCopier.setSftpInputs(sftpInputs);

            try {
                CompletableFuture.runAsync(() -> {
                    try {
                        sftpCopier.putToRemote();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).get(Integer.parseInt(sftpInputs.getSftpCommonInputs().getExecutionTimeout()) * 1000L, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                throw new Exception(EXCEPTION_EXECUTION_TIMED_OUT);
            }
        } else if (sftpOperation == SFTPOperation.GET_CHILDREN) {
            sftpCopier.setSftpInputs(sftpInputs);

            try {
                CompletableFuture.runAsync(() -> {
                    try {
                        sftpCopier.getChildren();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).get(Integer.parseInt(sftpInputs.getSftpCommonInputs().getExecutionTimeout()) * 1000L, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                throw new Exception(EXCEPTION_EXECUTION_TIMED_OUT);
            }
        }
        handleSessionClosure(sftpInputs, sftpCopier, sessionId);
    }

    private void handleSessionClosure(IHasFTPOperation sftpInputs, SFTPCopier sftpCopier, String sessionId) {
        boolean closeSession = Boolean.parseBoolean(sftpInputs.getSftpCommonInputs().getCloseSession());
        if (closeSession) {
            cleanupCopier(sftpInputs, sftpCopier, sessionId);
        } else {
            final boolean saved = saveToCache(sftpInputs.getSftpCommonInputs().getGlobalSessionObject(), sftpCopier, sessionId);
            if (!saved)
                throw new RuntimeException(EXCEPTION_UNABLE_SAVE_SESSION);
        }
    }

    public boolean saveToCache(GlobalSessionObject<Map<String, SFTPConnection>> sessionParam, SFTPCopier sftpCopier, String sessionId) {
        if (sessionParam != null && sessionParam.getName() == null) {
            sessionParam.setName(Constants.SSH_SESSIONS_DEFAULT_ID);
        }
        return sftpCopier.saveToCache(sessionParam, sessionId);
    }

    private void cleanupCopier(IHasFTPOperation sftpInputs, SFTPCopier sftpCopier, String sessionId) {
        sftpCopier.close();
        sftpCopier.removeFromCache(sftpInputs.getSftpCommonInputs().getGlobalSessionObject(), sessionId);
    }

    public SFTPCopier getSftpCopierFromCache(IHasFTPOperation sftpInputs, String sessionId) {
        if (sessionId != null) {
            synchronized (sessionId) {
                return CacheUtils.getFromCache(sftpInputs.getSftpCommonInputs().getGlobalSessionObject().getResource(), sessionId);
            }
        } else {
            return null;
        }
    }

    private boolean addSecurityProvider() {
        boolean providerAdded = false;
        Provider provider = Security.getProvider("BC");
        if (provider == null) {
            providerAdded = true;
            Security.insertProviderAt(new BouncyCastleProvider(), 2);
        }
        return providerAdded;
    }

    private void removeSecurityProvider() {
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME);
    }
}
