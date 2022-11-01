package io.cloudslang.content.office365.services;

import com.azure.core.credential.AccessToken;
import com.azure.core.credential.TokenCredential;
import com.azure.core.credential.TokenRequestContext;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;

public class AuthTokenCredential implements TokenCredential {
    private String token;

    public AuthTokenCredential(String token) {
        this.token = token;
    }

    @Override
    public Mono<AccessToken> getToken(TokenRequestContext tokenRequestContext) {
        return Mono.just(new AccessToken(this.token, OffsetDateTime.MAX));
    }
}
