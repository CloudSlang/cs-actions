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

import io.cloudslang.content.ldap.entities.ResetCompAccountInput;
import io.cloudslang.content.ldap.utils.LDAPQuery;
import io.cloudslang.content.ldap.utils.MySSLSocketFactory;
import io.cloudslang.content.ldap.utils.ResultUtils;

import javax.naming.NamingException;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.ldap.utils.ResultUtils.replaceInvalidXMLCharacters;

public class ResetComputerAccountService {

    public Map<String, String> execute(ResetCompAccountInput input) {

        Map<String, String> results = ResultUtils.createNewEmptyMap();
        ClassLoader loaderOrig = Thread.currentThread().getContextClassLoader();

        try {
            LDAPQuery ldap = new LDAPQuery();
            DirContext ctx;

            if (input.getUseSSL()) {
                if (Boolean.valueOf(input.getTrustAllRoots())) {
                    ctx = ldap.MakeDummySSLLDAPConnection(input.getHost(), input.getUsername(), input.getPassword());
                } else {
                    ctx = ldap.MakeSSLLDAPConnection(input.getHost(), input.getUsername(), input.getPassword(), "false",
                            input.getKeyStore(), input.getKeyStorePassword(), input.getTrustKeystore(), input.getTrustPassword());
                }

            } else {
                ctx = ldap.MakeLDAPConnection(input.getHost(), input.getUsername(), input.getPassword());
            }

            String compDN = input.getComputerDN();
            String nameComp = compDN.substring(3, compDN.indexOf(","));
            String value = "\"" + nameComp + "$\"";

            byte _bytes[] = value.getBytes("UTF-16LE");//("Unicode");

            // Specify the changes to make
            ModificationItem[] mods = new ModificationItem[1];
            mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                    new BasicAttribute("unicodePwd", _bytes));
            // Perform requested modifications on named object
            ctx.modifyAttributes(compDN, mods);
            ctx.close();

            results.put(RETURN_RESULT, "The computer account has been reseted.");
            results.put(RETURN_CODE, "0");

        } catch (UnsupportedEncodingException e) {
            System.out.println("Problem encoding password: " + e);
        } catch (NamingException e) {
            Exception exception = MySSLSocketFactory.getException();
            if (exception == null)
                exception = e;
            results.put(EXCEPTION, String.valueOf(exception));
            results.put(RETURN_RESULT, replaceInvalidXMLCharacters(exception.getMessage()));
            results.put(RETURN_CODE, "-1");
        }

        Thread.currentThread().setContextClassLoader(loaderOrig);
        return results;
    }
}

