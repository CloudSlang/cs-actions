/*
 * Copyright 2021-2024 Open Text
 * This program and the accompanying materials
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

import io.cloudslang.content.active_directory.entities.DeleteComputerAccountInput;
import io.cloudslang.content.active_directory.utils.CustomSSLSocketFactory;
import io.cloudslang.content.active_directory.utils.LDAPQuery;
import io.cloudslang.content.active_directory.utils.ResultUtils;

import javax.naming.CompositeName;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.active_directory.constants.OutputNames.RESULT_COMPUTER_DN;
import static io.cloudslang.content.active_directory.utils.ResultUtils.replaceInvalidXMLCharacters;

public class DeleteComputerAccountService {

    public Map<String, String> execute(DeleteComputerAccountInput input) {

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
            Name comp = new CompositeName().add(compDN);
            ctx.lookup(comp);
            ctx.destroySubcontext(comp);
            ctx.close();
            results.put(RETURN_RESULT, "Deleted computer account with CN=" + compCN);
            results.put(RESULT_COMPUTER_DN, compDN);
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

