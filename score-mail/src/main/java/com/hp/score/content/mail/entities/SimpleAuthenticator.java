package com.hp.score.content.mail.entities;

import javax.mail.PasswordAuthentication;
import javax.mail.Authenticator;

/**
 * Created by giloan on 11/3/2014.
 */
public class SimpleAuthenticator extends Authenticator {

    private String username;
    private String password;

    public SimpleAuthenticator(String username, String password){
        this.username = username;
        this.password = password;
    }

    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }
}
