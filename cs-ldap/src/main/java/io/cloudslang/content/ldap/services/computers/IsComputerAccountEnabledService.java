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
package io.cloudslang.content.ldap.services.computers;

import io.cloudslang.content.ldap.entities.IsComputerAccountEnabledInput;
import io.cloudslang.content.ldap.utils.LDAPQuery;
import io.cloudslang.content.ldap.utils.MySSLSocketFactory;
import io.cloudslang.content.ldap.utils.ResultUtils;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.ldap.constants.OutputNames.RESULT_COMPUTER_DN;
import static io.cloudslang.content.ldap.utils.ResultUtils.replaceInvalidXMLCharacters;

public class IsComputerAccountEnabledService {

    public Map<String, String> execute(IsComputerAccountEnabledInput input) {

        Map<String, String> results = ResultUtils.createNewResultsEmptyMap();

        try {
            LDAPQuery ldap = new LDAPQuery();
            String OU = input.getDistinguishedName();
            String compCN = input.getComputerCommonName();
            DirContext ctx;

            if (input.getProtocol().toLowerCase().trim().equals("https")) {
                if (Boolean.valueOf(input.getTrustAllRoots())) {
                    ctx = ldap.MakeDummySSLLDAPConnection(input.getHost(), input.getUsername(), input.getPassword(), input.getConnectionTimeout(), input.getExecutionTimeout());
                } else {
                    ctx = ldap.MakeSSLLDAPConnection(input.getHost(), input.getUsername(), input.getPassword(), "false",
                            input.getTrustKeystore(), input.getTrustPassword(), input.getConnectionTimeout(), input.getExecutionTimeout());
                }

            } else {
                ctx = ldap.MakeLDAPConnection(input.getHost(), input.getUsername(), input.getPassword(), input.getConnectionTimeout(), input.getExecutionTimeout());
            }

            String compDN = "CN=" + compCN + "," + OU;
            Attributes attrs = ctx.getAttributes(compDN, new String[]{"userAccountControl"});
            Attribute attr = attrs.get("userAccountControl");
            int val = Integer.parseInt((String) attr.get(0));
            if ((val | 0x002) == val) {
                results.put(RETURN_RESULT, "Computer account is disabled.");
                results.put(RETURN_CODE, "-1");
            } else {
                results.put(RETURN_RESULT, "Computer account is enabled.");
                results.put(RETURN_CODE, "0");
                results.put(RESULT_COMPUTER_DN, compDN);
            }
            ctx.close();

        } catch (NamingException e) {
            Exception exception = MySSLSocketFactory.getException();
            if (exception == null)
                exception = e;
            results.put(EXCEPTION, String.valueOf(exception));
            results.put(RETURN_RESULT, replaceInvalidXMLCharacters(exception.getMessage()));
            results.put(RETURN_CODE, "-1");
        }
        return results;
    }
}

