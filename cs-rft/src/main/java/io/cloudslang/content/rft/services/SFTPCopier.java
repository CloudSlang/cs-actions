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
package io.cloudslang.content.rft.services;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.jcraft.jsch.*;
import io.cloudslang.content.rft.entities.sftp.*;
import io.cloudslang.content.rft.utils.CacheUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

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
        session.connect();

        Channel ochannel = session.openChannel("sftp");
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
        SFTPGetInputs sftpGetInputs = (SFTPGetInputs) sftpInputs;
        getFile(sftpGetInputs);
    }

    private void getFile(SFTPGetInputs sftpGetInputs) throws Exception {
        if (new File(sftpGetInputs.getLocalLocation()).exists()) {
            throw new Exception(String.format(EXCEPTION_LOCAL_FILE_EXISTS, sftpGetInputs.getLocalLocation()));

        } else {
            try {
                channel.setFilenameEncoding(sftpGetInputs.getSftpCommonInputs().getCharacterSet());
                int iMode = ChannelSftp.OVERWRITE;
                channel.get(sftpGetInputs.getRemoteFile(), sftpGetInputs.getLocalLocation(), null, iMode);
            } catch (Throwable e) {
                throw new Exception(EXCEPTION_UNABLE_TO_RETRIEVE, e);
            }
        }
    }

    void putToRemote() throws Exception {
        SFTPPutInputs sftpPutInputs = (SFTPPutInputs) sftpInputs;
        putFile(sftpPutInputs);
    }

    void getChildren() throws Exception {
        SFTPGetChildrenInputs sftpGetChildrenInputs = (SFTPGetChildrenInputs) sftpInputs;
        getFilesAndFolders(sftpGetChildrenInputs);
    }

    private void getFilesAndFolders(SFTPGetChildrenInputs sftpGetChildrenInputs) throws Exception {
        String remotePath = sftpGetChildrenInputs.getRemotePath();
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
                            if (!remotePath.endsWith("/")) {
                                folderBuffer.append("/");
                            }
                            folderBuffer.append(item);
                            folderBuffer.append(delimiter);
                        } else if (((ChannelSftp.LsEntry) lineObj).getAttrs().isLink()) {
                            String linkPath = remotePath + "/" + ((ChannelSftp.LsEntry) lineObj).getFilename();
                            Vector linkLs = channel.ls(linkPath);
                            if (linkLs.size() > 1) {
                                folderBuffer.append(remotePath);
                                if (!remotePath.endsWith("/"))
                                    folderBuffer.append("/");
                                folderBuffer.append(item);
                                folderBuffer.append(delimiter);
                            } else {
                                fileBuffer.append(remotePath);
                                if (!remotePath.endsWith("/"))
                                    fileBuffer.append("/");
                                fileBuffer.append(item);
                                fileBuffer.append(delimiter);
                            }
                        } else {
                            //a file
                            fileBuffer.append(remotePath);
                            if (!remotePath.endsWith("/"))
                                fileBuffer.append("/");

                            fileBuffer.append(item);
                            fileBuffer.append(delimiter);
                        }

                        resultBuffer.append(remotePath);
                        if (!remotePath.endsWith("/")) {
                            resultBuffer.append("/");
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
            throw new Exception("LS Output was null.");
        }
    }


    public Map<String, String> getResult() {
        return result;
    }

    private void putFile(SFTPPutInputs sftpPutInputs) throws Exception {
        if (!(new File(sftpPutInputs.getLocalFile()).exists())) {
            throw new Exception(String.format(EXCEPTION_INVALID_LOCAL_FILE, sftpPutInputs.getLocalFile()));

        } else {
            try {
                channel.setFilenameEncoding(sftpPutInputs.getSftpCommonInputs().getCharacterSet());
                int iMode = ChannelSftp.OVERWRITE;
                channel.put(sftpPutInputs.getLocalFile(), sftpPutInputs.getRemoteLocation(), null, iMode);
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
        result.put("returnCode", "0");
        result.put("files", files);
        result.put("folders", folders);
        result.put("returnResult",returnResult);
    }

}
