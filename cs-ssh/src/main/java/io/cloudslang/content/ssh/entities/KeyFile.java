package io.cloudslang.content.ssh.entities;

/**
 * @author ioanvranauhp
 *         Date: 10/29/14
 */
public class KeyFile extends IdentityKey{
    private String keyFilePath;

    public KeyFile(String keyFilePath) {
        this.keyFilePath = keyFilePath;
        this.passPhrase = null;
    }

    public KeyFile(String keyFilePath, String passPhrase) {
        this.keyFilePath = keyFilePath;
        this.setPassPhrase(passPhrase);
    }

    public String getKeyFilePath() {
        return keyFilePath;
    }

    public void setKeyFilePath(String keyFilePath) {
        this.keyFilePath = keyFilePath;
    }
}
