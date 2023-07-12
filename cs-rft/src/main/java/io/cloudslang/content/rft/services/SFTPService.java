/*
 * Copyright 2021-2023 Open Text
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

package io.cloudslang.content.rft.services;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import io.cloudslang.content.rft.entities.sftp.*;
import io.cloudslang.content.rft.utils.CacheUtils;
import io.cloudslang.content.rft.utils.Constants;
import io.cloudslang.content.rft.utils.SFTPOperation;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Provider;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.rft.utils.Constants.*;
import static io.cloudslang.content.rft.utils.Descriptions.SFTPCreateDirectoryDescriptions.CREATE_DIR_SUCCESS_DESC;
import static io.cloudslang.content.rft.utils.Descriptions.SFTPDeleteDirectoryDescriptions.DELETE_DIR_SUCCESS_DESC;
import static io.cloudslang.content.rft.utils.Descriptions.SFTPDeleteFileDescriptions.SUCCESS_DESC;
import static io.cloudslang.content.rft.utils.Descriptions.SFTPGetChildrenDescriptions.NO_FILES_AND_FOLDERS_DESC;
import static io.cloudslang.content.rft.utils.Descriptions.SFTPRenameDescriptions.RENAME_SUCCESS_DESC;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;

public class SFTPService {

    public Map<String, String> execute(IHasFTPOperation sftpInputs, SFTPOperation sftpOperation) {
        SFTPCopier sftpCopier = null;
        String sessionId = EMPTY_STRING;
        boolean providerAdded = addSecurityProvider();

        try {
            sessionId = SSH_SESSION + sftpInputs.getSftpCommonInputs().getHost() + HYPHEN + sftpInputs.getSftpCommonInputs().getPort() + HYPHEN + sftpInputs.getSftpCommonInputs().getUsername();
            sftpCopier = getSftpCopierFromCache(sftpInputs, sessionId);

            if (sftpCopier == null || !sftpCopier.isConnected()) {
                sftpCopier = new SFTPCopier(sftpInputs);
            }

            performSFTPOperation(sftpInputs, sftpOperation, sftpCopier, sessionId);
            if (sftpOperation == SFTPOperation.GET_CHILDREN) {
                if (sftpCopier.getResult().get(RETURN_RESULT).length() == 0)
                    return getSuccessResultsMap(NO_FILES_AND_FOLDERS_DESC);
                else
                    return sftpCopier.getResult();
            }
            if (sftpOperation == SFTPOperation.DELETE_FILE)
                return getSuccessResultsMap(SUCCESS_DESC);
            else if (sftpOperation == SFTPOperation.CREATE_DIRECTORY)
                return getSuccessResultsMap(CREATE_DIR_SUCCESS_DESC);
            else if (sftpOperation == SFTPOperation.DELETE_DIRECTORY)
                return getSuccessResultsMap(DELETE_DIR_SUCCESS_DESC);
            else if (sftpOperation == SFTPOperation.RENAME)
                return getSuccessResultsMap(RENAME_SUCCESS_DESC);
            else
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
                        throw new RuntimeException(e);
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
                        throw new RuntimeException(e);
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
                        throw new RuntimeException(e);
                    }
                }).get(Integer.parseInt(sftpInputs.getSftpCommonInputs().getExecutionTimeout()) * 1000L, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                throw new Exception(EXCEPTION_EXECUTION_TIMED_OUT);
            }
        } else if (sftpOperation == SFTPOperation.RENAME) {
            SFTPRenameInputs sftpRenameInputs = (SFTPRenameInputs) sftpInputs;
            try {
                CompletableFuture.runAsync(() -> {
                    try {
                        if (sftpRenameInputs.getremotePath().isEmpty() && sftpRenameInputs.getNewRemotePath().isEmpty())
                            sftpCopier.channel.rename(sftpRenameInputs.getRemoteFile(), sftpRenameInputs.getNewRemoteFile());
                        else if (sftpRenameInputs.getremotePath().isEmpty() && !sftpRenameInputs.getNewRemotePath().isEmpty())
                            sftpCopier.channel.rename(sftpRenameInputs.getRemoteFile(), BACKSLASH + sftpRenameInputs.getNewRemotePath() + BACKSLASH + sftpRenameInputs.getNewRemoteFile());
                        else if (!sftpRenameInputs.getremotePath().isEmpty() && sftpRenameInputs.getNewRemotePath().isEmpty())
                            sftpCopier.channel.rename(BACKSLASH + sftpRenameInputs.getremotePath() + BACKSLASH + sftpRenameInputs.getRemoteFile(), sftpRenameInputs.getNewRemoteFile());
                        else if (!sftpRenameInputs.getremotePath().isEmpty() && !sftpRenameInputs.getNewRemotePath().isEmpty())
                            sftpCopier.channel.rename(BACKSLASH + sftpRenameInputs.getremotePath() + BACKSLASH + sftpRenameInputs.getRemoteFile(), BACKSLASH + sftpRenameInputs.getNewRemotePath() + BACKSLASH + sftpRenameInputs.getNewRemoteFile());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }).get(Integer.parseInt(sftpInputs.getSftpCommonInputs().getExecutionTimeout()) * 1000L, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                throw new Exception(EXCEPTION_EXECUTION_TIMED_OUT);
            }
        } else if (sftpOperation == SFTPOperation.COMMAND) {
            SFTPCommandInputs sftpCommandInputs = (SFTPCommandInputs) sftpInputs;
            switch (sftpCommandInputs.getCommandType()) {
                case CHMOD:
                    sftpCopier.channel.chmod(Integer.parseInt(sftpCommandInputs.getMode()), sftpCommandInputs.getremotePath());
                    break;
                case CHGRP:
                    sftpCopier.channel.chgrp(Integer.parseInt(sftpCommandInputs.getGid()), sftpCommandInputs.getremotePath());
                    break;
                case CHOWN:
                    sftpCopier.channel.chown(Integer.parseInt(sftpCommandInputs.getUid()), sftpCommandInputs.getremotePath());
                    break;
                case RENAME:
                    sftpCopier.channel.rename(sftpCommandInputs.getremotePath(), sftpCommandInputs.getnewRemotePath());
                    break;
            }
        } else if (sftpOperation == SFTPOperation.DELETE_FILE) {
            SFTPDeleteFileInputs sftpDeleteFileInputs = (SFTPDeleteFileInputs) sftpInputs;
            try {
                CompletableFuture.runAsync(() -> {
                    try {
                        if (sftpDeleteFileInputs.getRemotePath().isEmpty())
                            sftpCopier.channel.rm(sftpDeleteFileInputs.getRemoteFile());
                        else if (!(sftpDeleteFileInputs.getRemotePath().isEmpty()) && !(sftpDeleteFileInputs.getRemoteFile().isEmpty()))
                            sftpCopier.channel.rm(BACKSLASH + sftpDeleteFileInputs.getRemotePath() + BACKSLASH + sftpDeleteFileInputs.getRemoteFile());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }).get(Integer.parseInt(sftpInputs.getSftpCommonInputs().getExecutionTimeout()) * 1000L, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                throw new Exception(EXCEPTION_EXECUTION_TIMED_OUT);
            }
        } else if (sftpOperation == SFTPOperation.CREATE_DIRECTORY) {
            SFTPGeneralDirectoryInputs sftpGeneralDirectoryInputs = (SFTPGeneralDirectoryInputs) sftpInputs;
            try {
                CompletableFuture.runAsync(() -> {
                    try {
                        String path = sftpGeneralDirectoryInputs.getRemotePath();
                        if (path.endsWith(BACKSLASH))
                            path = path.substring(0, path.length() - 1);
                        if (!path.contains(BACKSLASH))
                            sftpCopier.channel.mkdir(path);
                        else
                            sftpCopier.channel.mkdir(BACKSLASH + path);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }).get(Integer.parseInt(sftpInputs.getSftpCommonInputs().getExecutionTimeout()) * 1000L, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                throw new Exception(EXCEPTION_EXECUTION_TIMED_OUT);
            } catch (ExecutionException e) {
                throw new Exception(EXCEPTION_EXISTING_DIRECTORY);
            }
        } else if (sftpOperation == SFTPOperation.DELETE_DIRECTORY) {
            SFTPGeneralDirectoryInputs sftpGeneralDirectoryInputs = (SFTPGeneralDirectoryInputs) sftpInputs;
            try {
                CompletableFuture.runAsync(() -> {
                    try {
                        String path = sftpGeneralDirectoryInputs.getRemotePath();
                        if (path.endsWith(BACKSLASH))
                            path = path.substring(0, sftpGeneralDirectoryInputs.getRemotePath().length() - 1);
                        if (!path.contains(BACKSLASH))
                            sftpCopier.channel.rmdir(path);
                        else
                            sftpCopier.channel.rmdir(BACKSLASH + path);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }).get(Integer.parseInt(sftpInputs.getSftpCommonInputs().getExecutionTimeout()) * 1000L, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                throw new Exception(EXCEPTION_EXECUTION_TIMED_OUT);
            } catch (ExecutionException e) {
                throw new Exception(EXCEPTION_DIRECTORY_DELETE);
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
