/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.httpclient.build.auth;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.AuthSchemes;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: davidmih
 * Date: 10/8/14
 */
public class AuthTypes implements Iterable<String> {
    public static final String BASIC = AuthSchemes.BASIC.toUpperCase();
    public static final String DIGEST = AuthSchemes.DIGEST.toUpperCase();
    public static final String NTLM = AuthSchemes.NTLM.toUpperCase();
    public static final String KERBEROS = AuthSchemes.KERBEROS.toUpperCase();

    public static final String DEFAULT = BASIC;
    public static final String ANY = "ANY";
    public static final String ANONYMOUS = "ANONYMOUS";

    private static Set<String> supportedAuthTypes = new HashSet<>();

    static {
        supportedAuthTypes.add(BASIC);
        supportedAuthTypes.add(DIGEST);
        supportedAuthTypes.add(NTLM);
        supportedAuthTypes.add(KERBEROS);
        supportedAuthTypes.add(ANONYMOUS);
    }

    private Set<String> authTypes = new HashSet();

    public AuthTypes(String authType) {
        parseAuthTypes(authType);
    }

    public Iterator<String> iterator() {
        return authTypes.iterator();
    }

    public void add(String authType) {
        authTypes.add(authType.toUpperCase());
    }

    public boolean contains(String authType) {
        return authTypes.contains(authType.toUpperCase());
    }

    public int size() {
        return authTypes.size();
    }

    public void parseAuthTypes(String authType) {
        if (StringUtils.isEmpty(authType)) {
            add(DEFAULT);
            return;
        }

        if (authType.equalsIgnoreCase(ANY)) {
            authTypes.addAll(supportedAuthTypes);
        } else {
            String[] toks = authType.split(",");
            for (String tok : toks) {
                if (supportedAuthTypes.contains(tok.toUpperCase())) {
                    add(tok);
                } else {
                    throw new IllegalArgumentException("unsupported authType in \"" + authType + "\"");
                }
            }
        }
    }
}
