package io.cloudslang.content.ldap.services.users;

import io.cloudslang.content.ldap.entities.ResetUserPasswordInput;
import io.cloudslang.content.ldap.utils.LDAPQuery;
import io.cloudslang.content.ldap.utils.MySSLSocketFactory;
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
            String userDN = input.getUserDN();
            String host = input.getHost();
            String username = input.getUsername();
            String password = input.getPassword();
            String keyStore = input.getKeyStore();
            String keyStorePassword = input.getKeyStorePassword();
            String trustStore = input.getTrustKeystore();
            String trustStorePassword = input.getKeyStorePassword();

            DirContext ctx;

            if (input.getUseSSL()) {
                if (input.getTrustAllRoots()) {
                    ctx = ldap.MakeDummySSLLDAPConnection(host, username, password);
                } else {
                    ctx = ldap.MakeSSLLDAPConnection(host, username, password, "false", keyStore, keyStorePassword, trustStore, trustStorePassword);
                }

            } else {
                ctx = ldap.MakeLDAPConnection(host, username, password);
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


            results.put(RETURN_RESULT, "Password Changed");
            results.put(RETURN_CODE, "0");

        }catch (Exception e) {
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
