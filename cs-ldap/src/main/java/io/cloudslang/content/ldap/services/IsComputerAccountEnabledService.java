package io.cloudslang.content.ldap.services;

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

        Map<String, String> results = ResultUtils.createNewEmptyMap();

        try {
            LDAPQuery ldap = new LDAPQuery();
            String OU = input.getOU();
            String compCN = input.getComputerCommonName();
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
            }
            ctx.close();
            results.put(RESULT_COMPUTER_DN, compDN);

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

