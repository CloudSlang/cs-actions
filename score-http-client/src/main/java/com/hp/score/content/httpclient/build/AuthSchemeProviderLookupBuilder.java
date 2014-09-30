package com.hp.score.content.httpclient.build;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.config.Lookup;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.impl.auth.*;
import org.apache.http.protocol.HttpContext;
import com.hp.score.content.httpclient.HttpClientInputs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class AuthSchemeProviderLookupBuilder {
    private String authType;
    private String skipPortAtKerberosDatabaseLookup = "true";
    private String kerberosConfigFile;
    private String kerberosLoginConfigFile;
    private String host;
    private String username;
    private String password;

    public AuthSchemeProviderLookupBuilder setAuthType(String authType) {
        this.authType = authType;
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

    public Lookup<AuthSchemeProvider> buildAuthSchemeProviderLookup() {
        if (StringUtils.isEmpty(authType)) {
            this.authType = AuthSchemes.BASIC;
        }
        authType = authType.toUpperCase();

        RegistryBuilder<AuthSchemeProvider> registryBuilder = RegistryBuilder.create();

        if (authType.equals("ANY")) {
            authType = "NTLM,BASIC,DIGEST,KERBEROS";
        }
        String[] authTypes = authType.split(",");
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
                    registryBuilder.register(AuthSchemes.BASIC, new BasicSchemeFactory(Charset.forName("UTF-8")));
                    break;
                case "DIGEST":
                    registryBuilder.register(AuthSchemes.DIGEST, new DigestSchemeFactory());
                    break;
                case "KERBEROS":

                    if (kerberosConfigFile != null) {
                        System.setProperty("java.security.krb5.conf", kerberosConfigFile);
                    } else if (StringUtils.isEmpty(System.getProperty("java.security.krb5.conf"))) {
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
                    } else if (StringUtils.isEmpty(System.getProperty("java.security.auth.login.config"))) {
                        File loginConfig;
                        try {
                            loginConfig = createLoginConfig();
                        } catch (IOException e) {
                            throw new RuntimeException("could not create the kerberos login config file" + e.getMessage(), e);
                        }
                        System.setProperty("java.security.auth.login.config", loginConfig.toURI().toString());
                    }

                    //todo fix security issue
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
                default:
                    throw new IllegalStateException("Unsupported '" + HttpClientInputs.AUTH_TYPE
                            + "'authentication scheme: " + authType);
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
        FileWriter writer = new FileWriter(tempFile);
        IOUtils.writeLines(lines, System.lineSeparator(), writer);
        IOUtils.closeQuietly(writer);
        return tempFile;
    }

    private static File createLoginConfig() throws IOException {
        File tempFile = File.createTempFile("krb", "loginConf");
        tempFile.deleteOnExit();
        ArrayList<String> lines = new ArrayList<>();
        lines.add("com.sun.security.jgss.initiate {\n" +
                "  com.hp.score.content.httpclient.build.KrbHttpLoginModule required\n" +
                "  doNotPrompt=true\n" +
                "  useFirstPass=true ;\n" +
                "};");
        FileWriter writer = new FileWriter(tempFile);
        IOUtils.writeLines(lines, System.lineSeparator(), writer);
        IOUtils.closeQuietly(writer);
        return tempFile;
    }
}