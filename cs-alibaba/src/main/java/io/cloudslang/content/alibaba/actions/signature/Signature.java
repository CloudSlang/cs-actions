/*
 * (c) Copyright 2018 Micro Focus, L.P.
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

package io.cloudslang.content.alibaba.actions.signature;

import io.cloudslang.content.alibaba.entities.SignatureInputs;
import io.cloudslang.content.alibaba.entities.inputs.CommonInputs;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

import static io.cloudslang.content.alibaba.entities.constants.Constants.AlibabaParams.FORMAT_TYPE;
import static io.cloudslang.content.alibaba.entities.constants.Constants.AlibabaSignatureParams.ISO8601_DATE_FORMAT;
import static io.cloudslang.content.alibaba.entities.constants.Constants.AlibabaSignatureParams.ALIBABA_SIGNATURE_METHOD;
import static io.cloudslang.content.alibaba.entities.constants.Constants.AlibabaSignatureParams.ALIBABA_SIGNATURE_VERSION;
import static io.cloudslang.content.alibaba.entities.constants.Constants.AlibabaSignatureParams.ALGORITHM;
import static io.cloudslang.content.alibaba.entities.constants.Constants.AlibabaParams.FORMAT;
import static io.cloudslang.content.alibaba.entities.constants.Constants.Miscellaneous.*;

public class Signature {

    private static String percentEncode(String value) throws UnsupportedEncodingException {
        return value != null ? URLEncoder.encode(value, ENCODING).replace("+", "%20").replace("*", "%2A").replace("%7E", "~") : null;
    }


    private static String formatIso8601Date(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(ISO8601_DATE_FORMAT);
        df.setTimeZone(new SimpleTimeZone(0, "GMT"));
        return df.format(date);
    }

    public static SignatureInputs getSignature(Map<String, String> parameters, CommonInputs commonInputs) throws Exception {


        final String HTTP_METHOD = commonInputs.getHttpClientMethod();

        String timestamp = formatIso8601Date(new Date());

        parameters.put("Action", commonInputs.getAction());
        parameters.put("Version", commonInputs.getVersion());
        parameters.put("AccessKeyId", commonInputs.getAccessKeyId());
        parameters.put("Timestamp", timestamp);
        parameters.put("SignatureMethod", ALIBABA_SIGNATURE_METHOD);
        parameters.put("SignatureVersion", ALIBABA_SIGNATURE_VERSION);
        String SignatureNonce = UUID.randomUUID().toString();
        parameters.put("SignatureNonce", SignatureNonce);
        System.out.println(SignatureNonce);
        parameters.put("Format", FORMAT_TYPE);
        String[] sortedKeys = parameters.keySet().toArray(new String[]{});
        Arrays.sort(sortedKeys);

        StringBuilder stringToSign = new StringBuilder();
        stringToSign.append(HTTP_METHOD).append(AMPERSAND);
        stringToSign.append(percentEncode(SCOPE_SEPARATOR)).append(AMPERSAND);
        StringBuilder canonicalizedQueryString = new StringBuilder();
        for (String key : sortedKeys) {
            canonicalizedQueryString.append(AMPERSAND)
                    .append(percentEncode(key)).append(EQUAL)
                    .append(percentEncode(parameters.get(key)));
        }
        stringToSign.append(percentEncode(
                canonicalizedQueryString.toString().substring(1)));


        String key = commonInputs.getAccessKeySecret() + AMPERSAND;
        Mac mac = Mac.getInstance(ALGORITHM);
        mac.init(new SecretKeySpec(key.getBytes(ENCODING), ALGORITHM));
        byte[] signData = mac.doFinal((EMPTY + stringToSign).getBytes(ENCODING));

        String signature = new String(Base64.encodeBase64(signData));

        return SignatureInputs.builder().signature(signature).signatureMethod(ALIBABA_SIGNATURE_METHOD).signatureVersion(ALIBABA_SIGNATURE_VERSION).signatureNonce(SignatureNonce).timestamp(timestamp).build();
    }
}

