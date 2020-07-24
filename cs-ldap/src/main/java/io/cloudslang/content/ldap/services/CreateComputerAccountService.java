/*
 * (c) Copyright 2020 Micro Focus
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
package io.cloudslang.content.ldap.services;

import io.cloudslang.content.ldap.entities.CreateComputerAccountInput;
import io.cloudslang.content.ldap.utils.LDAPQuery;
import io.cloudslang.content.ldap.utils.MySSLSocketFactory;
import io.cloudslang.content.ldap.utils.ResultUtils;

import javax.naming.CompositeName;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.ldap.constants.OutputNames.RESULT_COMPUTER_DN;
import static io.cloudslang.content.ldap.utils.ResultUtils.replaceInvalidXMLCharacters;


public class CreateComputerAccountService {

    public Map<String, String> execute(CreateComputerAccountInput input) {

        Map<String, String> results = ResultUtils.createNewEmptyMap();

        try {
            LDAPQuery ldap = new LDAPQuery();
            String OU = input.getOU();
            String compCN = input.getComputerCommonName();
            Boolean useSSL = input.getUseSSL();
            Boolean trustAllRoots = input.getTrustAllRoots();


            if (input.getEscapeChars()) {
                OU = ldap.normalizeDN(OU, false);
                compCN = ldap.normalizeADExpression(compCN, false);
            }

            Name comp = new CompositeName().add("cn=" + compCN);
            Name ou = new CompositeName().add(OU);
            DirContext ctx;

            if (useSSL) {
                if (Boolean.valueOf(trustAllRoots)) {
                    ctx = ldap.MakeDummySSLLDAPConnection(input.getHost(), input.getUsername(), input.getPassword());
                } else {
                    ctx = ldap.MakeSSLLDAPConnection(input.getHost(), input.getUsername(), input.getPassword(), "false",
                            input.getKeyStore(), input.getKeyStorePassword(), input.getTrustKeystore(), input.getTrustPassword());
                }

            } else {
                ctx = ldap.MakeLDAPConnection(input.getHost(), input.getUsername(), input.getPassword());
            }
            DirContext ctxOU = (DirContext) ctx.lookup(ou);
            Attributes compAttrs = new BasicAttributes(true);

            //ou=new CompositeName().add(ouDN.replace("/", "\\/"));
            compAttrs.put("objectclass", "computer");
            compAttrs.put("ou", OU);
            compAttrs.put("sAMAccountName", input.getSAMAccountName());
            ctxOU.createSubcontext(comp, compAttrs);
            Name compDN = new CompositeName().add("CN=" + compCN + "," + OU);

            //use the same values as in Active Directory User and Compputers
            //userAccountControl =  WORKSTATION_TRUST_ACCOUNT + PASSWD_NOTREQD
            int valNew = 0x1020;
            //Specify the changes to make
            ModificationItem[] mods = new ModificationItem[1];
            mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                    new BasicAttribute("userAccountControl", Integer.toString(valNew)));
            // Perform requested modifications on named object
            ctx.modifyAttributes(compDN, mods);
            ctxOU.close();
            ctx.close();

            results.put(RETURN_RESULT, "Added computer account with CN=" + compCN);
            results.put(RESULT_COMPUTER_DN, compDN.toString());
            results.put(RETURN_CODE,"0");

        } catch (NamingException e) {
            Exception exception = MySSLSocketFactory.getException();
            if (exception == null)
                exception = e;
            results.put(EXCEPTION, String.valueOf(exception));
            results.put(RETURN_RESULT, replaceInvalidXMLCharacters(exception.getMessage()));
            results.put(RETURN_CODE,"-1");
        }
        return results;
    }
}

