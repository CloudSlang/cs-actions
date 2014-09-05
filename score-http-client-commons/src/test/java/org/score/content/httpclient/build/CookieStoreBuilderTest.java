package org.score.content.httpclient.build;

import com.hp.oo.sdk.content.plugin.SerializableSessionObject;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

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
        SerializableSessionObject sessionObjectHolder = new SerializableSessionObject();
        CookieStore cookieStore = cookieStoreBuilder
                .setCookieStoreSessionObject(sessionObjectHolder)
                .buildCookieStore();
        assertNotNull(cookieStore);
        assertEquals(0, cookieStore.getCookies().size());
    }

    @Test
    public void buildCookieStoreWithCookies() throws IOException {
        BasicCookieStore basicCookieStore = new BasicCookieStore();
        SerializableSessionObject sessionObjectHolder = new SerializableSessionObject();
        sessionObjectHolder.setValue(CookieStoreBuilder.serialize(basicCookieStore));
        BasicCookieStore cookieStore = (BasicCookieStore) cookieStoreBuilder
                .setCookieStoreSessionObject(sessionObjectHolder)
                .buildCookieStore();

        assertNotNull(cookieStore);
        assertEquals(0, cookieStore.getCookies().size());
        assertEquals(basicCookieStore.getCookies(), cookieStore.getCookies());
    }

    @Test
    public void buildCookieStoreWithoutCookies() throws IOException {
        BasicCookieStore basicCookieStore = new BasicCookieStore();
        SerializableSessionObject sessionObjectHolder = new SerializableSessionObject();
        sessionObjectHolder.setValue(CookieStoreBuilder.serialize(basicCookieStore));
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
