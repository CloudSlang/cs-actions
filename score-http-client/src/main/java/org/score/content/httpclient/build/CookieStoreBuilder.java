package org.score.content.httpclient.build;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.score.content.httpclient.SessionObjectHolder;

public class CookieStoreBuilder {
    private String useCookies = "true";
    private SessionObjectHolder cookieStoreHolder;

    public CookieStoreBuilder setUseCookies(String useCookies) {
        if (!StringUtils.isEmpty(useCookies)) {
            this.useCookies = useCookies;
        }
        return this;
    }

    public CookieStoreBuilder setCookieStoreHolder(SessionObjectHolder cookieStoreHolder) {
        this.cookieStoreHolder = cookieStoreHolder;
        return this;
    }

    public CookieStore buildCookieStore() {
        if (Boolean.parseBoolean(useCookies) && cookieStoreHolder != null) {
            CookieStore cookieStore;
            synchronized (this.getClass()) {
                if (cookieStoreHolder.getObject() == null) {
                    cookieStore = new BasicCookieStore();
                    //noinspection unchecked
                    cookieStoreHolder.setObject(cookieStore);
                }
            }
            cookieStore = (CookieStore) cookieStoreHolder.getObject();
            return cookieStore;
        }
        return null;
    }
}