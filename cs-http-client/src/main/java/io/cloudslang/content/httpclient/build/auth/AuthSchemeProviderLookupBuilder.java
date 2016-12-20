/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.httpclient.build.auth;

import io.cloudslang.content.httpclient.HttpClientInputs;
import io.cloudslang.content.httpclient.build.Utils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.config.Lookup;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.impl.auth.*;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HttpContext;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class AuthSchemeProviderLookupBuilder {
    private AuthTypes authTypes;
    private String skipPortAtKerberosDatabaseLookup = "true";
    private String kerberosConfigFile;
    private String kerberosLoginConfigFile;
    private String host;
    private String username;
    private String password;
    private List<Header> headers;

    public AuthSchemeProviderLookupBuilder setAuthTypes(AuthTypes authTypes) {
        this.authTypes = authTypes;
        return this;
    }

    public AuthSchemeProviderLookupBuilder setSkipPortAtKerberosDatabaseLookup(String skipPortAtKerberosDatabaseLookup) {
        if (!StringUtils.isEmpty(skipPortAtKerberosDatabaseLookup)) {
            this.skipPortAtKerberosDatabaseLookup = skipPortAtKerberosDatabaseLookup;
        }
        return this;
    }

    public AuthSchemeProviderLookupBuilder setKerberosConfigFile(String kerberosConfigFile) {
        this.kerberosConfigFile = kerberosConfigFile;
        return this;
    }

    public AuthSchemeProviderLookupBuilder setHost(String host) {
        this.host = host;
        return this;
    }

    public AuthSchemeProviderLookupBuilder setKerberosLoginConfigFile(String kerberosLoginConfigFile) {
        this.kerberosLoginConfigFile = kerberosLoginConfigFile;
        return this;
    }

    public AuthSchemeProviderLookupBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public AuthSchemeProviderLookupBuilder setPassword(String password) {
        this.password = password;
        return this;

    }

    public AuthSchemeProviderLookupBuilder setHeaders(List<Header> headers) {
        this.headers = headers;
        return this;
    }

    public Lookup<AuthSchemeProvider> buildAuthSchemeProviderLookup() {
        RegistryBuilder<AuthSchemeProvider> registryBuilder = RegistryBuilder.create();

        for (String type : authTypes) {
            switch (type.trim()) {
                case "NTLM":
                    registryBuilder.register(AuthSchemes.NTLM, new AuthSchemeProvider() {
                        @Override
                        public AuthScheme create(HttpContext httpContext) {
                            return new NTLMScheme(new JCIFSEngine());
                        }
                    });
                    break;
                case "BASIC":
                    registryBuilder.register(AuthSchemes.BASIC, new BasicSchemeFactory(Charset.forName(Utils.DEFAULT_CHARACTER_SET)));
                    String value = username + ":" + password;
                    byte[] encodedValue = Base64.encodeBase64(value.getBytes(StandardCharsets.UTF_8));
                    headers.add(new BasicHeader("Authorization", "Basic " + new String(encodedValue)));
                    break;
                case "DIGEST":
                    registryBuilder.register(AuthSchemes.DIGEST, new DigestSchemeFactory());
                    break;
                case "KERBEROS":
                    if (kerberosConfigFile != null) {
                        System.setProperty("java.security.krb5.conf", kerberosConfigFile);
                    } else {
                        File krb5Config;
                        String domain = host.replaceAll(".*\\.(?=.*\\.)", "");
                        try {
                            krb5Config = createKrb5Configuration(domain);
                        } catch (IOException e) {
                            throw new RuntimeException("could not create the krb5 config file" + e.getMessage(), e);
                        }
                        System.setProperty("java.security.krb5.conf", krb5Config.toURI().toString());
                    }

                    if (kerberosLoginConfigFile != null) {
                        System.setProperty("java.security.auth.login.config", kerberosLoginConfigFile);
                    } else {
                        File loginConfig;
                        try {
                            loginConfig = createLoginConfig();
                        } catch (IOException e) {
                            throw new RuntimeException("could not create the kerberos login config file" + e.getMessage(), e);
                        }
                        System.setProperty("java.security.auth.login.config", loginConfig.toURI().toString());
                    }

                    if (password != null) {
                        System.setProperty(KrbHttpLoginModule.PAS, password);
                    }
                    if (username != null) {
                        System.setProperty(KrbHttpLoginModule.USR, username);
                    }

                    System.setProperty("javax.security.auth.useSubjectCredsOnly", "false");

                    boolean skipPort = Boolean.parseBoolean(skipPortAtKerberosDatabaseLookup);
                    registryBuilder.register(AuthSchemes.KERBEROS, new KerberosSchemeFactory(skipPort));
                    registryBuilder.register(AuthSchemes.SPNEGO, new SPNegoSchemeFactory(skipPort));
                    break;
                case AuthTypes.ANONYMOUS:
                    break;
                default:
                    throw new IllegalStateException("Unsupported '" + HttpClientInputs.AUTH_TYPE
                            + "'authentication scheme: " + type);
            }
        }
        return registryBuilder.build();
    }


    private static File createKrb5Configuration(String domain) throws IOException {
        File tempFile = File.createTempFile("krb", "kdc");
        tempFile.deleteOnExit();
        ArrayList<String> lines = new ArrayList<>();
        lines.add("[libdefaults]");
        lines.add("\tdefault_realm = " + domain.toUpperCase());
        lines.add("[realms]");
        lines.add("\t" + domain.toUpperCase() + " = {");
        lines.add("\t\tkdc = " + domain);
        lines.add("\t\tadmin_server = " + domain);
        lines.add("\t}");
        FileWriter writer = null;
        try {
            writer = new FileWriter(tempFile);
            IOUtils.writeLines(lines, System.lineSeparator(), writer);
        } finally {
            if (writer != null) {
//                IOUtils.closeQuietly(writer);
                safeClose(writer);
            }
        }

        return tempFile;
    }

    private static File createLoginConfig() throws IOException {
        File tempFile = File.createTempFile("krb", "loginConf");
        tempFile.deleteOnExit();
        ArrayList<String> lines = new ArrayList<>();
        lines.add("com.sun.security.jgss.initiate {\n" +
                "  " + KrbHttpLoginModule.class.getCanonicalName() + " required\n" +
                "  doNotPrompt=true\n" +
                "  useFirstPass=true\n" +
                "  debug=true ;\n" +
                "};");
        FileWriter writer = null;
        try {
            writer = new FileWriter(tempFile);
            IOUtils.writeLines(lines, System.lineSeparator(), writer);
        } finally {
            if (writer != null) {
//                IOUtils.closeQuietly(writer);
                safeClose(writer);
            }
        }
        return tempFile;
    }

    public static void safeClose(FileWriter fis) {
        if (fis != null) {
            try {
                fis.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

}