package io.cloudslang.content.ldap.services.groups;

import io.cloudslang.content.ldap.entities.AddUserToGroupInput;
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

public class AddUserToGroupService {

    public Map<String, String> execute(AddUserToGroupInput input) {

        Map<String, String> results = ResultUtils.createNewEmptyMap();

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

            //Specify the changes to make
            ModificationItem[] mods = new ModificationItem[1];
            mods[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE,
                    new BasicAttribute("member", input.getUserDN()));
            // Perform requested modifications on named object
            ctx.modifyAttributes(input.getGroupDN(), mods);
            ctx.close();

            results.put(RETURN_RESULT, "Added user (" + input.getUserDN() + ") to group (" + input.getGroupDN() + ")");
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
