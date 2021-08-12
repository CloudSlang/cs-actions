package io.cloudslang.content.rft.spike_rft.sftp;

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
