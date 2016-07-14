package io.cloudslang.content.ssh.entities;

/**
 * @author ioanvranauhp
 *         Date: 10/29/14
 */
public class KeyFile {
    private String keyFilePath;
    private String passPhrase;

    public KeyFile(String keyFilePath) {
        this.keyFilePath = keyFilePath;
        this.passPhrase = null;
    }

    public KeyFile(String keyFilePath, String passPhrase) {
        this.keyFilePath = keyFilePath;
        this.passPhrase = passPhrase;
    }

    public String getKeyFilePath() {
        return keyFilePath;
    }

    public void setKeyFilePath(String keyFilePath) {
        this.keyFilePath = keyFilePath;
    }

    public String getPassPhrase() {
        return passPhrase;
    }

    public void setPassPhrase(String passPhrase) {
        this.passPhrase = passPhrase;
    }
}
