/*
 * Licensed to Hewlett-Packard Development Company, L.P. under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
*/
package com.hp.score.content.httpclient.build.auth;

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
    private static Set<String> supportedAuthTypes = new HashSet<>();
    static {
        supportedAuthTypes.add(BASIC);
        supportedAuthTypes.add(DIGEST);
        supportedAuthTypes.add(NTLM);
        supportedAuthTypes.add(KERBEROS);
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
                    throw new IllegalArgumentException("unsupported authType in \"" + authType +"\"");
                }
            }
        }
    }
}
