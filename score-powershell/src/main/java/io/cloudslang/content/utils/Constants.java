package io.cloudslang.content.utils;

/**
 * User: bancl
 * Date: 10/6/2015
 */
public class Constants {

    public static final class InputNames {
        public static final String INPUT_HOST = "host";
        public static final String INPUT_PORT = "port";
        public static final String INPUT_USERNAME = "userName";
        public static final String INPUT_PASSWORD = "password";
        public static final String INPUT_SCRIPT = "script";
        public static final String CONNECTION_TYPE = "connectionType";
        public static final String WINRM_CONTEXT = "winrmContext";
        public static final String WINRM_ENABLE_HTTPS = "enableHTTPS";
        public static final String WINRM_ENVELOP_SIZE = "winrmEnvelopSize";
        public static final String WINRM_HTTPS_CERTIFICATE_TRUST_STRATEGY = "winrmHttpsCertificateTrustStrategy";
        public static final String WINRM_HTTPS_HOSTNAME_VERIFICATION_STRATEGY = "winrmHttpsHostnameVerificationStrategy";
        public static final String WINRM_KERBEROS_ADD_PORT_TO_SPN = "winrmKerberosAddPortToSpn";
        public static final String WINRM_KERBEROS_TICKET_CACHE = "winrmKerberosTicketCache";
        public static final String WINRM_KERBEROS_USE_HTTP_SPN = "winrmKerberosUseHttpSpn";
        public static final String WINRM_LOCALE = "winrmLocale";
        public static final String WINRM_TIMEMOUT = "winrmTimeout";
    }

    public static final class OutputNames {
        public static final String RETURN_RESULT = "returnResult";
        public static final String EXCEPTION = "exception";
        public static final String RETURN_CODE = "returnCode";
    }

    public static final class ReturnCodes {
        public static final String RETURN_CODE_FAILURE = "-1";
        public static final String RETURN_CODE_SUCCESS = "0";
    }

    public static final class ResponseNames {
        public static final String SUCCESS = "success";
        public static final String FAILURE = "failure";
    }

}