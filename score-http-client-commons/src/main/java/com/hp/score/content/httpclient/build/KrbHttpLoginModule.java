package com.hp.score.content.httpclient.build;

import com.sun.security.auth.module.Krb5LoginModule;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: davidmih
 * Date: 9/23/14
 */
public class KrbHttpLoginModule extends Krb5LoginModule {
    static String USR = "javax.security.auth.login.name";
    static String PAS = "javax.security.auth.login.password";
    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
        super.initialize(subject, callbackHandler, sharedState, options);    //To change body of overridden methods use File | Settings | File Templates.
        Map<String, Object> myss = (Map<String, Object>) sharedState;
        myss.put(USR, System.getProperty(USR));
        myss.put(PAS, System.getProperty(PAS).toCharArray());
    }
}
