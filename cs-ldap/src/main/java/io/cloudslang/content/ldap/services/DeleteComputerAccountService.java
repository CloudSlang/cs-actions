package io.cloudslang.content.ldap.services;

import io.cloudslang.content.ldap.entities.DeleteComputerAccountInput;
import io.cloudslang.content.ldap.utils.LDAPQuery;
import io.cloudslang.content.ldap.utils.MySSLSocketFactory;
import io.cloudslang.content.ldap.utils.ResultUtils;

import javax.naming.CompositeName;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.ldap.constants.OutputNames.RESULT_COMPUTER_DN;
import static io.cloudslang.content.ldap.utils.ResultUtils.replaceInvalidXMLCharacters;

public class DeleteComputerAccountService {

    public Map<String, String> execute(DeleteComputerAccountInput input) {

        Map<String, String> results = ResultUtils.createNewEmptyMap();

        try {
            LDAPQuery ldap = new LDAPQuery();
            String OU = input.getOU();
            String compCN = input.getComputerCommonName();
            if (input.getEscapeChars()) {
                OU = ldap.normalizeDN(OU, false);
                compCN = ldap.normalizeADExpression(compCN, false);
            }

            DirContext ctx;

            if (input.getUseSSL()) {
                if (Boolean.valueOf(input.getTrustAllRoots())) {
                    ctx = ldap.MakeDummySSLLDAPConnection(input.getHost(), input.getUsername(), input.getPassword());
                } else {
                    ctx = ldap.MakeSSLLDAPConnection(input.getHost(), input.getUsername(), input.getPassword(),"false",
                            input.getKeyStore(), input.getKeyStorePassword(), input.getTrustKeystore(), input.getTrustPassword());
                }

            } else {
                ctx = ldap.MakeLDAPConnection(input.getHost(), input.getUsername(), input.getPassword());
            }
            String compDN = "CN=" + compCN + "," + OU;
            Name comp = new CompositeName().add(compDN);
            ctx.lookup(comp);
            ctx.destroySubcontext(comp);
            ctx.close();
            results.put(RETURN_RESULT, "Deleted computer account with CN=" + compCN);
            results.put(RESULT_COMPUTER_DN, compDN);
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

