package io.cloudslang.content.ssh.entities;

import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * User: sacalosb
 * Date: 07.01.2016
 */
public abstract class IdentityKey {
    public static final Charset KEY_ENCODING = Charset.forName("UTF-8");
    protected byte[] passPhrase;

    public byte[] getPassPhrase() {
        return (passPhrase == null) ? null : Arrays.copyOf(passPhrase, passPhrase.length);
    }

    public void setPassPhrase(byte[] passPhrase) {
        this.passPhrase = (passPhrase == null) ? null : Arrays.copyOf(passPhrase, passPhrase.length);
    }

    public void setPassPhrase(String passPhrase) {
        this.passPhrase = (passPhrase == null) ? null : passPhrase.getBytes(KEY_ENCODING);
    }
}
