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

import io.cloudslang.content.active_directory.entities.UpdateComputerAccountInput;
import io.cloudslang.content.active_directory.utils.CustomSSLSocketFactory;
import io.cloudslang.content.active_directory.utils.LDAPQuery;
import io.cloudslang.content.active_directory.utils.ResultUtils;

import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.cloudslang.content.active_directory.constants.OutputNames.*;
import static io.cloudslang.content.active_directory.utils.ResultUtils.replaceInvalidXMLCharacters;

public class UpdateComputerAccountService {

    public Map<String, String> execute(UpdateComputerAccountInput input) {


        Map<String, String> results = ResultUtils.createNewResultsEmptyMap();

        try {
            LDAPQuery ldap = new LDAPQuery();
            String OU = input.getDistinguishedName();
            String compCN = input.getComputerCommonName();
            if (input.getEscapeChars()) {
                OU = ldap.normalizeDN(OU, false);
                compCN = ldap.normalizeADExpression(compCN, false);
            }

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
            List<ModificationItem> modsList = new ArrayList<>();


            // Add non-empty attributes to the modification list
            addAttributeIfNotEmpty(modsList, "description", "Updated description");

            ModificationItem[] mods = modsList.toArray(new ModificationItem[0]);
            // Perform modification of attributes
            ctx.modifyAttributes(compDN, mods);

            // Close LDAP context
            ctx.close();
            results.put(RETURN_RESULT, "Computer account attributes updated successfully.");
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

    private void addAttributeIfNotEmpty(List<ModificationItem> modsList, String attributeName, String attributeValue) {
        if (attributeValue != null && !attributeValue.isEmpty()) {
            modsList.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(attributeName, attributeValue)));
        }
    }
}
