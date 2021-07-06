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
package io.cloudslang.content.active_directory.services.computers;

import io.cloudslang.content.active_directory.entities.DisableComputerAccountInput;
import io.cloudslang.content.active_directory.utils.CustomSSLSocketFactory;
import io.cloudslang.content.active_directory.utils.LDAPQuery;
import io.cloudslang.content.active_directory.utils.ResultUtils;

import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.active_directory.constants.OutputNames.RESULT_COMPUTER_DN;
import static io.cloudslang.content.active_directory.utils.ResultUtils.replaceInvalidXMLCharacters;

public class DisableComputerAccountService {

    public Map<String, String> execute(DisableComputerAccountInput input) {

        Map<String, String> results = ResultUtils.createNewResultsEmptyMap();

        try {
            LDAPQuery ldap = new LDAPQuery();
            String OU = input.getDistinguishedName();
            String compCN = input.getComputerCommonName();
            DirContext ctx;

            if (input.getProtocol().toLowerCase().trim().equals(input.getProtocol().toLowerCase())) {
                if (Boolean.valueOf(input.getTrustAllRoots())) {
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

            String compDN = "CN=" + compCN + "," + OU;
            Attributes attrs = ctx.getAttributes(compDN, new String[]{"userAccountControl"});
            Attribute attr = attrs.get("userAccountControl");
            int valOld = Integer.parseInt((String) attr.get(0));
            System.out.println("old userAccountControl=" + valOld);
            if ((valOld | 0x002) == valOld) {
                results.put(RETURN_RESULT, "Computer account is already disabled");
                results.put(RETURN_CODE, "-1");
            } else {
                //disable computer account
                int valNew = valOld + 0x0002;
                System.out.println("new userAccountControl=" + valNew);
                //Specify the changes to make
                ModificationItem[] mods = new ModificationItem[1];
                mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                        new BasicAttribute("userAccountControl", Integer.toString(valNew)));
                // Perform requested modifications on named object
                ctx.modifyAttributes(compDN, mods);
                results.put(RETURN_RESULT, "Computer account has been disabled.");
                results.put(RETURN_CODE, "0");
            }
            ctx.close();
            results.put(RESULT_COMPUTER_DN, compDN);

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

