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
import com.jcraft.jsch.*;
import io.cloudslang.content.rft.entities.sftp.*;
import io.cloudslang.content.rft.utils.CacheUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.rft.utils.Constants.*;

public class SFTPCopier {

    Session session;
    ChannelSftp channel;
    Map<String, String> result = new HashMap<>();
    private IHasFTPOperation sftpInputs;


    public SFTPCopier(IHasFTPOperation sftpInputs) throws Exception {

        JSch jsch = new JSch();

        String privateKey = sftpInputs.getSftpCommonInputs().getPrivateKey();
        String password = sftpInputs.getSftpCommonInputs().getPassword();

        String proxyHost = sftpInputs.getSftpCommonInputs().getProxyHost();
        int proxyPort = Integer.parseInt(sftpInputs.getSftpCommonInputs().getProxyPort());
        String proxyUsername = sftpInputs.getSftpCommonInputs().getProxyUsername();
        String proxyPassword = sftpInputs.getSftpCommonInputs().getProxyPassword();

        String connectionTimeout = sftpInputs.getSftpCommonInputs().getConnectionTimeout();
        String executionTimeout = sftpInputs.getSftpCommonInputs().getExecutionTimeout();

        MyUserInfo uInfo = new MyUserInfo();
        uInfo.setPasswd(password);
        uInfo.setPromptYesNo(true);
        uInfo.setPassphrase(null);
        uInfo.setPromptPassword(true);
        if (privateKey != null && !privateKey.isEmpty())
            if (new File(privateKey).isFile()) {
                uInfo.setPrivateKey(privateKey);
                uInfo.setPassphrase(password);
                jsch.addIdentity(uInfo.getPrivateKey(), password);
            } else {
                throw new Exception(String.format(EXCEPTION_INVALID_LOCAL_FILE, privateKey));
            }

        UserInfo ui = uInfo;
        session = jsch.getSession(sftpInputs.getSftpCommonInputs().getUsername(), sftpInputs.getSftpCommonInputs().getHost(), Integer.parseInt(sftpInputs.getSftpCommonInputs().getPort()));
        session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
        session.setUserInfo(ui);

        if (!proxyHost.isEmpty()) {
            ProxyHTTP proxy = new ProxyHTTP(proxyHost, proxyPort);
            if ((!proxyUsername.isEmpty()) && (!proxyPassword.isEmpty())) {
                proxy.setUserPasswd(proxyUsername, proxyPassword);
            }
            session.setProxy(proxy);
        }

        if (!connectionTimeout.isEmpty()) {
            int connTimeout = Integer.parseInt(sftpInputs.getSftpCommonInputs().getConnectionTimeout());
            session.setTimeout(connTimeout * 1000);
        }

        session.connect();
        Channel ochannel = session.openChannel(SFTP);
        ochannel.connect();
        channel = (ChannelSftp) ochannel;

    }

    public SFTPCopier(Session session, Channel savedChannel) {
        this.session = session;
        this.channel = (ChannelSftp) savedChannel;
    }

    public void setSftpInputs(IHasFTPOperation sftpInputs) {
        this.sftpInputs = sftpInputs;
    }

    public void getFromRemote() throws Exception {
        SFTPDownloadFileInputs sftpDownloadFileInputs = (SFTPDownloadFileInputs) sftpInputs;
        getFile(sftpDownloadFileInputs);
    }

    private void getFile(SFTPDownloadFileInputs sftpDownloadFileInputs) throws Exception {
        if (new File(sftpDownloadFileInputs.getLocalPath() + File.separator + sftpDownloadFileInputs.getRemoteFile()).exists()) {
            throw new Exception(String.format(EXCEPTION_LOCAL_FILE_EXISTS, sftpDownloadFileInputs.getRemoteFile()));

        } else {
            try {
                channel.setFilenameEncoding(sftpDownloadFileInputs.getSftpCommonInputs().getCharacterSet());
                int iMode = ChannelSftp.OVERWRITE;

                if (!(sftpDownloadFileInputs.getRemotePath().isEmpty())) {

                    channel.cd(BACKSLASH + sftpDownloadFileInputs.getRemotePath());
                    channel.lcd(BACKSLASH + sftpDownloadFileInputs.getLocalPath());
                    channel.get(sftpDownloadFileInputs.getRemoteFile(), sftpDownloadFileInputs.getRemoteFile(), null, iMode);

                } else {
                    channel.get(sftpDownloadFileInputs.getRemoteFile(),
                            sftpDownloadFileInputs.getLocalPath() + BACKSLASH + sftpDownloadFileInputs.getRemoteFile(),
                            null,
                            iMode);
                }
            } catch (Throwable e) {
                throw new Exception(EXCEPTION_UNABLE_TO_RETRIEVE, e);
            }
        }
    }

    void putToRemote() throws Exception {
        SFTPUploadFileInputs sftpUploadFileInputs = (SFTPUploadFileInputs) sftpInputs;
        putFile(sftpUploadFileInputs);
    }

    void getChildren() throws Exception {
        SFTPGetChildrenInputs sftpGetChildrenInputs = (SFTPGetChildrenInputs) sftpInputs;
        getFilesAndFolders(sftpGetChildrenInputs);
    }

    private void getFilesAndFolders(SFTPGetChildrenInputs sftpGetChildrenInputs) throws Exception {
        String remotePath = BACKSLASH + sftpGetChildrenInputs.getRemotePath();
        String delimiter = sftpGetChildrenInputs.getDelimiter();
        Vector lsOutput = channel.ls(remotePath);

        StringBuffer resultBuffer = new StringBuffer();  //list of all children
        StringBuffer fileBuffer = new StringBuffer();  //list of children that are files
        StringBuffer folderBuffer = new StringBuffer(); //list of children that are folders

        if (lsOutput != null) {
            for (int i = 0; i < lsOutput.size(); i++) {
                Object lineObj = lsOutput.elementAt(i);
                if (lineObj instanceof ChannelSftp.LsEntry) {
                    String item = ((ChannelSftp.LsEntry) lineObj).getFilename();
                    if (".".equals(item) || "..".equals(item)) {
                        //ignore it
                    } else {
                        if (((ChannelSftp.LsEntry) lineObj).getAttrs().isDir()) {
                            //a directory
                            folderBuffer.append(remotePath);
                            if (!remotePath.endsWith(BACKSLASH)) {
                                folderBuffer.append(BACKSLASH);
                            }
                            folderBuffer.append(item);
                            folderBuffer.append(delimiter);
                        } else if (((ChannelSftp.LsEntry) lineObj).getAttrs().isLink()) {
                            String linkPath = remotePath + BACKSLASH + ((ChannelSftp.LsEntry) lineObj).getFilename();
                            Vector linkLs = channel.ls(linkPath);
                            if (linkLs.size() > 1) {
                                folderBuffer.append(remotePath);
                                if (!remotePath.endsWith(BACKSLASH))
                                    folderBuffer.append(BACKSLASH);
                                folderBuffer.append(item);
                                folderBuffer.append(delimiter);
                            } else {
                                fileBuffer.append(remotePath);
                                if (!remotePath.endsWith(BACKSLASH))
                                    fileBuffer.append(BACKSLASH);
                                fileBuffer.append(item);
                                fileBuffer.append(delimiter);
                            }
                        } else {
                            //a file
                            fileBuffer.append(remotePath);
                            if (!remotePath.endsWith(BACKSLASH))
                                fileBuffer.append(BACKSLASH);

                            fileBuffer.append(item);
                            fileBuffer.append(delimiter);
                        }

                        resultBuffer.append(remotePath);
                        if (!remotePath.endsWith(BACKSLASH)) {
                            resultBuffer.append(BACKSLASH);
                        }
                        resultBuffer.append(item);
                        if (i != lsOutput.size() - 1) {
                            //apply a delimiter to all but the last item.
                            resultBuffer.append(delimiter);
                        }
                    }
                }
            }
            if (folderBuffer.length() > 0)
                //remove last delimiter
                folderBuffer.delete(folderBuffer.length() - delimiter.length(), folderBuffer.length());
            if (fileBuffer.length() > 0)
                //remove last delimiter
                fileBuffer.delete(fileBuffer.length() - delimiter.length(), fileBuffer.length());
            populateResult(fileBuffer.toString(), folderBuffer.toString(), resultBuffer.toString());

        } else {
            throw new Exception(NULL_OUTPUT);
        }
    }


    public Map<String, String> getResult() {
        return result;
    }

    private void putFile(SFTPUploadFileInputs sftpUploadFileInputs) throws Exception {
        if (!(new File(sftpUploadFileInputs.getLocalPath() + File.separator + sftpUploadFileInputs.getLocalFile()).exists())) {
            throw new Exception(String.format(EXCEPTION_INVALID_LOCAL_FILE, sftpUploadFileInputs.getLocalFile()));

        } else {
            try {
                channel.setFilenameEncoding(sftpUploadFileInputs.getSftpCommonInputs().getCharacterSet());
                int iMode = ChannelSftp.OVERWRITE;

                if (!(sftpUploadFileInputs.getRemotePath().isEmpty())) {

                    channel.cd(BACKSLASH + sftpUploadFileInputs.getRemotePath());
                    channel.lcd(BACKSLASH + sftpUploadFileInputs.getLocalPath());
                    channel.put(sftpUploadFileInputs.getLocalFile(), sftpUploadFileInputs.getLocalFile(), null, iMode);

                } else {
                    channel.put(sftpUploadFileInputs.getLocalPath() + BACKSLASH + sftpUploadFileInputs.getLocalFile(),
                            sftpUploadFileInputs.getLocalFile(),
                            null,
                            iMode);
                }
            } catch (Throwable e) {
                throw new Exception(EXCEPTION_UNABLE_TO_STORE, e);
            }
        }
    }

    public boolean isConnected() {
        return session.isConnected();
    }

    public void close() {
        if (channel != null) {
            channel.disconnect();
            channel = null;
        }
        session.disconnect();
        session = null;
    }

    public boolean saveToCache(GlobalSessionObject<Map<String, SFTPConnection>> sessionParam, String sessionId) {
        return CacheUtils.saveSession(session, channel, sessionParam, sessionId);
    }

    public void removeFromCache(GlobalSessionObject<Map<String, SFTPConnection>> sessionParam, String sessionId) {
        CacheUtils.removeSftpSession(sessionParam, sessionId);
    }

    private void populateResult(String files, String folders, String returnResult) {
        result.put(RETURN_CODE, SUCCESS_RETURN_CODE);
        result.put(FILES, files);
        result.put(FOLDERS, folders);
        result.put(RETURN_RESULT, returnResult);
    }

}
