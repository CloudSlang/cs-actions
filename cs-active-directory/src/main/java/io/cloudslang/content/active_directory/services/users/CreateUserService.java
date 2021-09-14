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

import io.cloudslang.content.active_directory.entities.CreateUserInput;
import io.cloudslang.content.active_directory.utils.CustomSSLSocketFactory;
import io.cloudslang.content.active_directory.utils.LDAPQuery;
import io.cloudslang.content.active_directory.utils.ResultUtils;

import javax.naming.CompositeName;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.active_directory.constants.OutputNames.RESULT_COMPUTER_DN;
import static io.cloudslang.content.active_directory.utils.ResultUtils.replaceInvalidXMLCharacters;

public class CreateUserService {

    public Map<String, String> execute(CreateUserInput input) {

        Map<String, String> results = ResultUtils.createNewResultsEmptyMap();

        try {
            LDAPQuery ldap = new LDAPQuery();
            String OU = input.getDistinguishedName();
            String sAMAccountName = input.getSAMAccountName();
            String userCN = input.getUserCommonName();

            // if sAMAccountName not provided assign it from compCN
            if (input.getSAMAccountName().equalsIgnoreCase("")) {
                sAMAccountName = userCN;
            }

            sAMAccountName = ldap.replaceIllegalCharactersForSAM(sAMAccountName);

            if (input.getEscapeChars()) {
                OU = ldap.normalizeDN(OU, false);
                userCN = ldap.normalizeADExpression(userCN, false);
            }

            Name ou = new CompositeName().add(OU);
            Name user = new CompositeName().add("CN=" + userCN);

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

            DirContext ctxOU = (DirContext) ctx.lookup(ou);

            String value = "\"" + input.getUserPassword() + "\"";
            byte[] bytesPsw = value.getBytes(StandardCharsets.UTF_16LE);

            Attributes userAttrs = new BasicAttributes(true);
            userAttrs.put("objectclass", "user");
            userAttrs.put("ou", OU);
            userAttrs.put("sAMAccountName", sAMAccountName);
            userAttrs.put("unicodePwd", bytesPsw);

            ctxOU.createSubcontext(user, userAttrs);

            Name userDN = new CompositeName().add("CN=" + userCN + "," + OU);
            // enable user account
            Attributes attrs = ctx.getAttributes(userDN, new String[]{"userAccountControl"});
            Attribute attr = attrs.get("userAccountControl");
            int valOld = Integer.parseInt((String) attr.get(0));
            int valNew = valOld & ~0x0002;
            //Specify the changes to make
            ModificationItem[] mods = new ModificationItem[1];
            mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                    new BasicAttribute("userAccountControl", Integer.toString(valNew)));
            // Perform requested modifications on named object
            ctx.modifyAttributes(userDN, mods);
            ctxOU.close();
            ctx.close();

            results.put(RETURN_RESULT, "Added user account with CN=" + userCN);
            results.put(RESULT_COMPUTER_DN, userDN.toString());
            results.put(RETURN_CODE, "0");

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
