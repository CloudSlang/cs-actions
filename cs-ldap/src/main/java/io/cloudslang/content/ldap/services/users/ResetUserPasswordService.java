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
package io.cloudslang.content.ldap.services.users;

import io.cloudslang.content.ldap.entities.ResetUserPasswordInput;
import io.cloudslang.content.ldap.utils.LDAPQuery;
import io.cloudslang.content.ldap.utils.CustomSSLSocketFactory;
import io.cloudslang.content.ldap.utils.ResultUtils;

import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.ldap.utils.ResultUtils.replaceInvalidXMLCharacters;

public class ResetUserPasswordService {

    public Map<String, String> execute(ResetUserPasswordInput input) {

        Map<String, String> results = ResultUtils.createNewResultsEmptyMap();
        try {
            LDAPQuery ldap = new LDAPQuery();
            String userDN = input.getUserDistinguishedName();

            DirContext ctx;

            if (input.getProtocol().toLowerCase().trim().equals("https")) {
                if (input.getTrustAllRoots()) {
                    ctx = ldap.MakeDummySSLLDAPConnection(input.getHost(), input.getUsername(), input.getPassword(), input.getConnectionTimeout(), input.getExecutionTimeout());
                } else {
                    ctx = ldap.MakeSSLLDAPConnection(input.getHost(), input.getUsername(), input.getPassword(), "false",
                            input.getTrustKeystore(), input.getTrustPassword(), input.getConnectionTimeout(), input.getExecutionTimeout());
                }

            } else {
                ctx = ldap.MakeLDAPConnection(input.getHost(), input.getUsername(), input.getPassword(), input.getConnectionTimeout(), input.getExecutionTimeout());
            }
            String value = "\"" + input.getUserPassword() + "\"";
            byte[] bytesPsw = value.getBytes(StandardCharsets.UTF_16LE);


            // Specify the changes to make
            ModificationItem[] mods = new ModificationItem[1];
            mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                    new BasicAttribute("unicodePwd", bytesPsw));
            // Perform requested modifications on named object
            ctx.modifyAttributes(userDN, mods);
            ctx.close();

            results.put(RETURN_RESULT, "Password Changed.");
            results.put(RETURN_CODE, "0");

        } catch (Exception e) {
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
