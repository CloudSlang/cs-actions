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

import com.jcraft.jsch.UserInfo;
import javax.security.auth.Subject;

public class MyUserInfo implements UserInfo {

    private String passwd;
    private boolean promptYesNo;
    private boolean promptPassphrase;
    private boolean promptPassword;
    private String Passphrase;
    private String privateKey;

    /**
     * This callback gets invoked when using GSSAPI-with-MIC authentication with the Kerberos mechanism; when that
     * happens, we must somehow provide a subject on whose behalf the connection is being made.
     *
     * @see com.jcraft.jsch.UserInfo#getSubject()
     */
    public Subject getSubject() {
        return null;
    }

    public String getPassword() {
        return passwd;
    }

    public boolean promptPassword(String arg0) {
        // TODO Auto-generated method stub
        return promptPassword;
    }

    public boolean promptPassphrase(String arg0) {
        // TODO Auto-generated method stub
        return promptPassphrase;
    }

    public boolean promptYesNo(String _prompt) {
        return promptYesNo;
    }

    public void showMessage(String arg0) {
        // TODO Auto-generated method stub

    }

    public String getPrivateKey() {
        return privateKey;
    }


    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }


    public boolean isPromptPassphrase() {
        return promptPassphrase;
    }

    public void setPromptPassphrase(boolean promptPassphrase) {
        this.promptPassphrase = promptPassphrase;
    }

    public boolean isPromptPassword() {
        return promptPassword;
    }

    public void setPromptPassword(boolean promptPassword) {
        this.promptPassword = promptPassword;
    }

    public boolean isPromptYesNo() {
        return promptYesNo;
    }

    public void setPromptYesNo(boolean promptYesNo) {
        this.promptYesNo = promptYesNo;
    }

    public String getPassphrase() {
        return Passphrase;
    }

    public void setPassphrase(String passphrase) {
        Passphrase = passphrase;
    }

    public String getPasswd() {
        return passwd;
    }


    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

}
