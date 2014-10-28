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
package com.hp.score.content.httpclient.build;

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
