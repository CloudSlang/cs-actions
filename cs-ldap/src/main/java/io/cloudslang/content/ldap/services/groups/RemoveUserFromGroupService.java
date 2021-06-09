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
package io.cloudslang.content.ldap.services.groups;

import io.cloudslang.content.ldap.entities.AddRemoveUserInput;
import io.cloudslang.content.ldap.utils.LDAPQuery;
import io.cloudslang.content.ldap.utils.MySSLSocketFactory;
import io.cloudslang.content.ldap.utils.ResultUtils;

import javax.naming.NamingException;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.ldap.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.ldap.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.ldap.utils.ResultUtils.replaceInvalidXMLCharacters;

public class RemoveUserFromGroupService {

    public Map<String, String> execute(AddRemoveUserInput input) {

        Map<String, String> results = ResultUtils.createNewResultsEmptyMap();

        try {
            LDAPQuery ldap = new LDAPQuery();
            DirContext ctx;

            if (input.getProtocol().toLowerCase().trim().equals("https")) {
                if (Boolean.valueOf(input.getTrustAllRoots())) {
                    ctx = ldap.MakeDummySSLLDAPConnection(input.getHost(), input.getUsername(), input.getPassword());
                } else {
                    ctx = ldap.MakeSSLLDAPConnection(input.getHost(), input.getUsername(), input.getPassword(), "false",
                              input.getTrustKeystore(), input.getTrustPassword());
                }

            } else {
                ctx = ldap.MakeLDAPConnection(input.getHost(), input.getUsername(), input.getPassword());
            }

            //Specify the changes to make
            ModificationItem[] mods = new ModificationItem[1];
            mods[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE,
                    new BasicAttribute("member", input.getUserDistinguishedName()));
            // Perform requested modifications on named object
            ctx.modifyAttributes(input.getGroupDistinguishedName(), mods);
            ctx.close();

            results.put(RETURN_RESULT, "Removed user (" + input.getUserDistinguishedName() + ") from group (" +
                    input.getGroupDistinguishedName() + ")");
            results.put(RETURN_CODE, "0");

        } catch (NamingException e) {
            Exception exception = MySSLSocketFactory.getException();
            if (exception == null)
                exception = e;

            results.put(EXCEPTION, String.valueOf(exception));
            if (exception.getMessage().contains("NO_OBJECT")) {
                results.put(RETURN_RESULT, "LDAP object doesn't exist");
            } else {
                results.put(RETURN_RESULT, replaceInvalidXMLCharacters(exception.getMessage()));
            }
            results.put(RETURN_CODE, "-1");
        }
        return results;
    }
}
