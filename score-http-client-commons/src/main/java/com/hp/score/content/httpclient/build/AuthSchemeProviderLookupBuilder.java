package com.hp.score.content.httpclient.build;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.config.Lookup;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.impl.auth.BasicSchemeFactory;
import org.apache.http.impl.auth.DigestSchemeFactory;
import org.apache.http.impl.auth.KerberosSchemeFactory;
import org.apache.http.impl.auth.NTLMScheme;
import org.apache.http.protocol.HttpContext;
import com.hp.score.content.httpclient.HttpClientInputs;

import java.nio.charset.Charset;

public class AuthSchemeProviderLookupBuilder {
    public static final String KERBEROS = "kerberos";
    public static final String NTLM = "ntlm";
    public static final String BASIC = "basic";
    public static final String DIGEST = "digest";
    public static final String FORM = "form";
    public static final String SPRING_FORM = "spring_form";

    private String authType = "basic";

    public AuthSchemeProviderLookupBuilder setAuthType(String authType) {
        if (!StringUtils.isEmpty(authType)) {
            this.authType = authType.toLowerCase();
        }
        return this;
    }

    public Lookup<AuthSchemeProvider> buildAuthSchemeProviderLookup() {
        RegistryBuilder<AuthSchemeProvider> registryBuilder = RegistryBuilder.create();
        switch (authType) {
            //todo return empty when authType,equals("none")
            case NTLM:
                registryBuilder.register(AuthSchemes.NTLM, new AuthSchemeProvider() {
                    @Override
                    public AuthScheme create(HttpContext httpContext) {
                        return new NTLMScheme(new JCIFSEngine());
                    }
                });
                break;
            case BASIC:
                registryBuilder.register(AuthSchemes.BASIC, new BasicSchemeFactory(Charset.forName("UTF-8")));
                break;
            case DIGEST:
                registryBuilder.register(AuthSchemes.DIGEST, new DigestSchemeFactory());
                break;
            case KERBEROS:
                //AuthSchemes.SPNEGO ?
                registryBuilder.register(AuthSchemes.KERBEROS, new KerberosSchemeFactory());
                break;
//            default:
//                registryBuilder.register(AuthSchemes.NTLM, new NTLMSchemeFactory())
//                        .register(AuthSchemes.BASIC, new BasicSchemeFactory())
//                        .register(AuthSchemes.DIGEST, new DigestSchemeFactory())
//                        .register(AuthSchemes.SPNEGO, new SPNegoSchemeFactory())
//                        .register(AuthSchemes.KERBEROS, new KerberosSchemeFactory());
//                break;
            default:
                throw new IllegalStateException("Unsupported '"+ HttpClientInputs.AUTH_TYPE
                        +"'authentication scheme: " + authType);
        }
        return registryBuilder.build();
    }
}