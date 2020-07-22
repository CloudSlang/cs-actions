package io.cloudslang.content.ldap.services;

import com.sun.istack.internal.NotNull;
import io.cloudslang.content.ldap.entities.DisableComputerAccountInput;
import io.cloudslang.content.ldap.utils.LDAPQuery;
import io.cloudslang.content.ldap.utils.MySSLSocketFactory;
import io.cloudslang.content.ldap.utils.ResultUtils;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.Map;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.ldap.constants.OutputNames.RESULT_COMPUTER_DN;
import static io.cloudslang.content.ldap.utils.ResultUtils.replaceInvalidXMLCharacters;

public class DisableComputerAccountService {

    public @NotNull
    Map<String, String> execute(@NotNull DisableComputerAccountInput input) {

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
            int valOld = Integer.parseInt((String) attr.get(0));
            System.out.println("old userAccountControl=" + valOld);
            if ((valOld | 0x002) == valOld) {
                results.put(RETURN_RESULT, "Computer account is already disabled");
                results.put(RETURN_CODE, "-1");
            } else {
                //disable computer account
                int valNew = valOld + 0x0002;
                System.out.println("new userAccountControl=" + valNew);
                //Specify the changes to make
                ModificationItem[] mods = new ModificationItem[1];
                mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                        new BasicAttribute("userAccountControl", Integer.toString(valNew)));
                // Perform requested modifications on named object
                ctx.modifyAttributes(compDN, mods);
                results.put(RETURN_RESULT, "Computer account has been disabled.");
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

