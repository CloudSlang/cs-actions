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



package io.cloudslang.content.mail.entities;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.cms.CMSAlgorithm;
import org.bouncycastle.mail.smime.SMIMEEnvelopedGenerator;

/**
 * Created by persdana on 8/3/2015.
 */
public enum EncryptionAlgorithmsEnum {
    DES_EDE3_CBC(SMIMEEnvelopedGenerator.DES_EDE3_CBC, CMSAlgorithm.DES_EDE3_CBC),
    RC2_CBC(SMIMEEnvelopedGenerator.RC2_CBC, CMSAlgorithm.RC2_CBC),
    IDEA_CBC(SMIMEEnvelopedGenerator.IDEA_CBC, CMSAlgorithm.IDEA_CBC),
    CAST5_CBC(SMIMEEnvelopedGenerator.CAST5_CBC, CMSAlgorithm.CAST5_CBC),
    AES128_CBC(SMIMEEnvelopedGenerator.AES128_CBC, CMSAlgorithm.AES128_CBC),
    AES192_CBC(SMIMEEnvelopedGenerator.AES192_CBC, CMSAlgorithm.AES192_CBC),
    AES256_CBC(SMIMEEnvelopedGenerator.AES256_CBC, CMSAlgorithm.AES256_CBC),
    CAMELLIA128_CBC(SMIMEEnvelopedGenerator.CAMELLIA128_CBC, CMSAlgorithm.CAMELLIA128_CBC),
    CAMELLIA192_CBC(SMIMEEnvelopedGenerator.CAMELLIA192_CBC, CMSAlgorithm.CAMELLIA192_CBC),
    CAMELLIA256_CBC(SMIMEEnvelopedGenerator.CAMELLIA256_CBC, CMSAlgorithm.CAMELLIA256_CBC),
    SEED_CBC(SMIMEEnvelopedGenerator.SEED_CBC, CMSAlgorithm.SEED_CBC),
    DES_EDE3_WRAP(SMIMEEnvelopedGenerator.DES_EDE3_WRAP, CMSAlgorithm.DES_EDE3_WRAP),
    AES128_WRAP(SMIMEEnvelopedGenerator.AES128_WRAP, CMSAlgorithm.AES128_WRAP),
    AES256_WRAP(SMIMEEnvelopedGenerator.AES256_WRAP, CMSAlgorithm.AES256_WRAP),
    CAMELLIA128_WRAP(SMIMEEnvelopedGenerator.CAMELLIA128_WRAP, CMSAlgorithm.CAMELLIA128_WRAP),
    CAMELLIA192_WRAP(SMIMEEnvelopedGenerator.CAMELLIA192_WRAP, CMSAlgorithm.CAMELLIA192_WRAP),
    CAMELLIA256_WRAP(SMIMEEnvelopedGenerator.CAMELLIA256_WRAP, CMSAlgorithm.CAMELLIA256_WRAP),
    SEED_WRAP(SMIMEEnvelopedGenerator.SEED_WRAP, CMSAlgorithm.SEED_WRAP);

    private final String encryptionOID;
    private final ASN1ObjectIdentifier asn10ObjId;

    EncryptionAlgorithmsEnum(String encryptionOID, ASN1ObjectIdentifier asn10ObjId) {
        this.encryptionOID = encryptionOID;
        this.asn10ObjId = asn10ObjId;
    }

    public static EncryptionAlgorithmsEnum getEncryptionAlgorithm(String encryptionAlgorithm) {
        if (StringUtils.isEmpty(encryptionAlgorithm)) {
            return AES256_CBC;
        }

        try {
            return EncryptionAlgorithmsEnum.valueOf(encryptionAlgorithm.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid encryption algorithm \"" + encryptionAlgorithm +
                    "\". Supported values:" + getSupportedEncryptionAlgorithms());
        }
    }

    public String getEncryptionOID() {
        return this.encryptionOID;
    }


    public ASN1ObjectIdentifier getAsn10ObjId() {
        return asn10ObjId;
    }


    private static String getSupportedEncryptionAlgorithms() {
        String result = "";
        EncryptionAlgorithmsEnum[] algorithms = EncryptionAlgorithmsEnum.values();
        for (EncryptionAlgorithmsEnum alg : algorithms) {
            result += alg.name();
            result += ", ";
        }
        return result.substring(0, result.length() - 2);
    }

}
