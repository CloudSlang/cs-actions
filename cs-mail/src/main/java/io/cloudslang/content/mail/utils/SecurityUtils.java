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
package io.cloudslang.content.mail.utils;

import io.cloudslang.content.mail.constants.ExceptionMsgs;
import io.cloudslang.content.mail.entities.DecryptableMailInput;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.cms.RecipientIdentifier;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.cmp.RevocationDetailsBuilder;
import org.bouncycastle.cms.KEKRecipientId;
import org.bouncycastle.cms.KeyTransRecipientInformation;
import org.bouncycastle.cms.RecipientId;
import org.bouncycastle.cms.RecipientInformation;
import org.bouncycastle.cms.jcajce.JceKeyTransRecipient;
import org.bouncycastle.cms.jcajce.JceKeyTransRecipientId;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

public final class SecurityUtils {

    public static void addDecryptionSettings(KeyStore ks, RecipientId recId, DecryptableMailInput input) throws Exception {
        char[] smimePw = input.getDecryptionKeystorePassword().toCharArray();

        Security.addProvider(new BouncyCastleProvider());

        try (InputStream decryptionStream = new URL(input.getDecryptionKeystore()).openStream()) {
            ks.load(decryptionStream, smimePw);
        }

        if (StringUtils.EMPTY.equals(input.getDecryptionKeyAlias())) {
            Enumeration aliases = ks.aliases();
            while (aliases.hasMoreElements()) {
                String alias = (String) aliases.nextElement();

                if (ks.isKeyEntry(alias)) {
                    input.setDecryptionKeyAlias(alias);
                }
            }

            if (StringUtils.EMPTY.equals(input.getDecryptionKeyAlias())) {
                throw new Exception(ExceptionMsgs.PRIVATE_KEY_ERROR_MESSAGE);
            }
        }

        // find the certificate for the private key and generate a
        // suitable recipient identifier.
        X509Certificate cert = (X509Certificate) ks.getCertificate(input.getDecryptionKeyAlias());
        if (null == cert) {
            throw new Exception("Can't find a key pair with alias \"" + input.getDecryptionKeyAlias() +
                    "\" in the given keystore");
        }
        if (input.isVerifyCertificate()) {
            cert.checkValidity();
        }

        //        recId.setSerialNumber(cert.getSerialNumber());
//        recId.setIssuer(X500Name.getInstance(cert.getIssuerX500Principal().getEncoded()));
    }
}
