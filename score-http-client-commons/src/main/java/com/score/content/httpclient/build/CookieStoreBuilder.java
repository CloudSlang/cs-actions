package com.score.content.httpclient.build;

import com.hp.oo.sdk.content.plugin.SerializableSessionObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;

import java.io.*;

public class CookieStoreBuilder {
    private String useCookies = "true";
    private SerializableSessionObject cookieStoreSessionObject;

    public CookieStoreBuilder setUseCookies(String useCookies) {
        if (!StringUtils.isEmpty(useCookies)) {
            this.useCookies = useCookies;
        }
        return this;
    }

    public CookieStoreBuilder setCookieStoreSessionObject(SerializableSessionObject cookieStoreSessionObject) {
        this.cookieStoreSessionObject = cookieStoreSessionObject;
        return this;
    }

    public CookieStore buildCookieStore() {
        if (Boolean.parseBoolean(useCookies) && cookieStoreSessionObject != null) {
            BasicCookieStore cookieStore;
            if (cookieStoreSessionObject.getValue() == null) {
                cookieStore = new BasicCookieStore();
            } else {
                try {
                    cookieStore = (BasicCookieStore) deserialize((byte[]) cookieStoreSessionObject.getValue());
                } catch (IOException | ClassNotFoundException  e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
            return cookieStore;
        }
        return null;
    }

    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(b);
        o.writeObject(obj);
        return b.toByteArray();
    }

    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInputStream o = new ObjectInputStream(b);
        return o.readObject();
    }

}