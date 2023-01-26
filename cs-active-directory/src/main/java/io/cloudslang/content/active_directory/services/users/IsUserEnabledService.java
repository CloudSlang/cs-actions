/*
 * (c) Copyright 2021 Micro Focus
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
package io.cloudslang.content.active_directory.services.users;

import io.cloudslang.content.active_directory.entities.UserCommonInput;
import io.cloudslang.content.active_directory.utils.CustomSSLSocketFactory;
import io.cloudslang.content.active_directory.utils.LDAPQuery;
import io.cloudslang.content.active_directory.utils.ResultUtils;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.active_directory.constants.OutputNames.RESULT_USER_DN;
import static io.cloudslang.content.active_directory.utils.ResultUtils.replaceInvalidXMLCharacters;

public class IsUserEnabledService {

    public Map<String, String> execute(UserCommonInput input) {

        Map<String, String> results = ResultUtils.createNewResultsEmptyMap();

        try {
            String ouDN = input.getDistinguishedName();
            String userCN = input.getUserCommonName();

            LDAPQuery ldap = new LDAPQuery();
            DirContext ctx;

            if (input.getProtocol().toLowerCase().trim().equals(input.getProtocol().toLowerCase())) {
                if (input.getTrustAllRoots()) {
                    ctx = ldap.MakeDummySSLLDAPConnection(input.getHost(), input.getUsername(), input.getPassword(),
                            input.getTimeout(), input.getTlsVersion(), input.getAllowedCiphers(),
                            input.getProxyHost(), input.getProxyPort(), input.getProxyUsername(), input.getProxyPassword());
                } else {
                    ctx = ldap.MakeSSLLDAPConnection(input.getHost(), input.getUsername(), input.getPassword(),
                            input.getTrustKeystore(), input.getTrustPassword(),
                            input.getTimeout(), input.getTlsVersion(), input.getAllowedCiphers(), input.getProxyHost(),
                            input.getProxyPort(), input.getProxyUsername(), input.getProxyPassword(), input.getX509HostnameVerifier());
                }

            } else {
                ctx = ldap.MakeLDAPConnection(input.getHost(), input.getUsername(), input.getPassword(),
                        input.getTimeout(), input.getProxyHost(), input.getProxyPort(), input.getProxyUsername(), input.getProxyPassword());
            }

            String userDN = "CN=" + userCN + "," + ouDN;

            Attributes attrs = ctx.getAttributes(userDN, new String[]{"userAccountControl"});
            Attribute attr = attrs.get("userAccountControl");
            int val = Integer.parseInt((String) attr.get(0));
            if ((val | 0x002) == val) {
                results.put(RETURN_RESULT, "User account is disabled.");
                results.put(RETURN_CODE, "-1");
            } else {
                results.put(RETURN_RESULT, "User account is enabled.");
                results.put(RETURN_CODE, "0");
            }
            ctx.close();

            results.put(RESULT_USER_DN, userDN);

        } catch (NamingException e) {
            Exception exception = CustomSSLSocketFactory.getException();
            if (exception == null)
                exception = e;
            results.put(EXCEPTION, String.valueOf(exception));
            results.put(RETURN_RESULT, replaceInvalidXMLCharacters(exception.getMessage()));
            results.put(RETURN_CODE, "-1");
        }
        return results;
    }
}
