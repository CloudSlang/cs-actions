/*
 * Copyright 2022-2024 Open Text
 * This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.cloudslang.content.httpclient.services;

import com.hp.oo.sdk.content.plugin.SerializableSessionObject;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.CookieStore;

import java.io.*;

public class CookieStoreBuilder {

    public static CookieStore buildCookieStore(SerializableSessionObject cookieStoreSessionObject, String useCookies) {
        if (Boolean.parseBoolean(useCookies) && cookieStoreSessionObject != null) {
            BasicCookieStore cookieStore;
            if (cookieStoreSessionObject.getValue() == null) {
                cookieStore = new BasicCookieStore();
            } else {
                try {
                    cookieStore = (BasicCookieStore) deserialize((byte[]) cookieStoreSessionObject.getValue());
                } catch (IOException | ClassNotFoundException e) {
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
