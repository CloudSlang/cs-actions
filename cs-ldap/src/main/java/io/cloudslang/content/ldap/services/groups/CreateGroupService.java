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

import io.cloudslang.content.ldap.entities.CreateGroupInput;
import io.cloudslang.content.ldap.utils.LDAPQuery;
import io.cloudslang.content.ldap.utils.MySSLSocketFactory;
import io.cloudslang.content.ldap.utils.ResultUtils;

import javax.naming.CompositeName;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import java.util.Arrays;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.ldap.constants.OutputNames.*;
import static io.cloudslang.content.ldap.utils.ResultUtils.replaceInvalidXMLCharacters;

public class CreateGroupService {

    public Map<String, String> execute(CreateGroupInput input) {

        Map<String, String> results = ResultUtils.createNewResultsEmptyMap();

        try {
            LDAPQuery ldap = new LDAPQuery();
            //  groupCN = ldap.replaceIllegalCharactersForSAM(groupCN);
            String sAMAccountName = input.getSAMAccountName();
            String ouDN = input.getDistinguishedName();
            String groupCN = input.getGroupCommonName();

            sAMAccountName = ldap.replaceIllegalCharactersForSAM(sAMAccountName);

            if (input.getEscapeChars()) {
                ouDN = ldap.normalizeDN(ouDN, false);
                groupCN = ldap.normalizeADExpression(groupCN, false);
            }
            Name ou = new CompositeName().add(ouDN);
            Name group = new CompositeName().add("cn=" + groupCN);

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

            DirContext ctxOU = (DirContext) ctx.lookup(ou);

            String groupTypeValues[] = new String[]{"-2147483646", "-2147483644", "-2147483640", "2", "4", "8"};

            String groupType = input.getGroupType();

            if (groupType.isEmpty()) {
                groupType = "-2147483646"; //default value = Global Security Group
            } else {
                if (!Arrays.asList(groupTypeValues).contains(groupType))
                    throw new RuntimeException("Invalid group type.");
            }

            Attributes groupAttrs = new BasicAttributes(true);
            groupAttrs.put("objectclass", "group");
            groupAttrs.put("groupType", groupType);
            groupAttrs.put("sAMAccountName", sAMAccountName);

            ctxOU.createSubcontext(group, groupAttrs);

            String groupDN = "CN=" + groupCN + "," + ouDN;

            ctxOU.close();
            ctx.close();

            results.put(RETURN_RESULT, "Added group with CN=" + groupCN);
            results.put(RESULT_GROUP_DN, groupDN);
            results.put(RETURN_CODE, "0");

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