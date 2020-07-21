/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
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

package main.java.io.cloudslang.content.ldap.sslconfig;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public class EasyX509TrustManager implements X509TrustManager {
    private X509TrustManager standardTrustManager = null;

    public EasyX509TrustManager() throws NoSuchAlgorithmException, KeyStoreException {
        super();
        TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        try {
            factory.init((KeyStore) null);
            TrustManager[] trustManagers = factory.getTrustManagers();
            if (trustManagers.length == 0) {
                throw new NoSuchAlgorithmException("SunX509 trust manager not supported");
            }
            this.standardTrustManager = (X509TrustManager) trustManagers[0];
        } catch (Exception ex) {
            if(!ex.getMessage().contains("problem accessing trust store")) {
                throw ex;
            }
        }
    }

    public void checkClientTrusted(X509Certificate[] chain, String authType) {
        return;
    }

    public void checkServerTrusted(X509Certificate[] chain, String authType) {
        return;
    }

    public X509Certificate[] getAcceptedIssuers() {
        if (standardTrustManager != null) {
            return standardTrustManager.getAcceptedIssuers();
        }
        return new X509Certificate[]{};
    }
}
