package org.score.content.httpclient.build;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.junit.Before;
import org.junit.Test;
import org.score.content.httpclient.SessionObjectHolder;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

/**
 * User: Adina Tusa
 * Date: 8/19/14
 */
public class CookieStoreBuilderTest {

    private CookieStoreBuilder cookieStoreBuilder;

    @Before
    public void setUp() {
        cookieStoreBuilder = new CookieStoreBuilder();
    }

    @Test
    public void buildCookieStore() {
        SessionObjectHolder sessionObjectHolder = new SessionObjectHolder();
        CookieStore cookieStore = cookieStoreBuilder
                .setCookieStoreHolder(sessionObjectHolder)
                .buildCookieStore();
        assertNotNull(cookieStore);
        assertEquals(0, cookieStore.getCookies().size());
    }

    @Test
    public void buildCookieStoreWithCookies() {
        BasicCookieStore basicCookieStore = new BasicCookieStore();
        SessionObjectHolder sessionObjectHolder = new SessionObjectHolder<BasicCookieStore>();
        sessionObjectHolder.setObject(basicCookieStore);
        BasicCookieStore cookieStore = (BasicCookieStore) cookieStoreBuilder
                .setCookieStoreHolder(sessionObjectHolder)
                .buildCookieStore();

        assertNotNull(cookieStore);
        assertEquals(0, cookieStore.getCookies().size());
        assertEquals(basicCookieStore, cookieStore);
    }

    @Test
    public void buildCookieStoreWithoutCookies() {
        BasicCookieStore basicCookieStore = new BasicCookieStore();
        SessionObjectHolder sessionObjectHolder = new SessionObjectHolder<BasicCookieStore>();
        sessionObjectHolder.setObject(basicCookieStore);
        BasicCookieStore cookieStore = (BasicCookieStore) cookieStoreBuilder
                .setUseCookies("false")
                .buildCookieStore();

        assertNull(cookieStore);
    }

    @Test
    public void buildCookieStoreWithoutCookieStore() {
        BasicCookieStore cookieStore = (BasicCookieStore) cookieStoreBuilder
                .setUseCookies("true")
                .buildCookieStore();

        assertNull(cookieStore);
    }
}
