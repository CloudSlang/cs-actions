package io.cloudslang.content.xml.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import io.cloudslang.content.xml.entities.inputs.CommonInputs;
import io.cloudslang.content.xml.entities.inputs.CustomInputs;
import io.cloudslang.content.xml.services.ValidateService;
import io.cloudslang.content.xml.entities.Constants;
import java.util.Map;


/**
 * Created by markowis on 18/02/2016.
 */
public class Validate {
    /**
     * Service to validate an XML document. Input must be given for either "xmlDocument" or for "xmlLocation".
     * The "'xsdLocation" input is optional, but if specified then the XML document will be validated against the XSD schema.
     *
     * @param xmlDocument       XML string to test
     * @param xmlDocumentSource The source type of the xml document.
     *                          Valid values: xmlString, xmlPath, xmlUrl
     *                          Default value: xmlString
     * @param xsdDocument       optional - XSD to test given XML against
     * @param xsdDocumentSource The source type of the xsd document.
     *                          Valid values: xsdString, xsdPath
     *                          Default value: xsdString
     * @param username             The username used to connect to the remote machine.
     * @param password             The password used to connect to the remote machine.
     * @param proxyHost            The proxy server used to access the remote host.
     * @param proxyPort            The proxy server port.
     * @param proxyUsername        The username used when connecting to the proxy.
     * @param proxyPassword        The password used when connecting to the proxy.
     * @param trustAllRoots        Specifies whether to enable weak security over SSL/TSL. A certificate is trusted even if no trusted certification authority issued it.
     *                             Default value is 'false'.
     *                             Valid values are 'true' and 'false'.
     * @param x509HostnameVerifier Specifies the way the server hostname must match a domain name in the subject's Common Name (CN) or subjectAltName field of the
     *                             X.509 certificate. The hostname verification system prevents communication with other hosts other than the ones you intended.
     *                             This is done by checking that the hostname is in the subject alternative name extension of the certificate. This system is
     *                             designed to ensure that, if an attacker(Man In The Middle) redirects traffic to his machine, the client will not accept the
     *                             connection. If you set this input to "allow_all", this verification is ignored and you become vulnerable to security attacks.
     *                             For the value "browser_compatible" the hostname verifier works the same way as Curl and Firefox. The hostname must match
     *                             either the first CN, or any of the subject-alts. A wildcard can occur in the CN, and in any of the subject-alts. The only
     *                             difference between "browser_compatible" and "strict" is that a wildcard (such as "*.foo.com") with "browser_compatible" matches
     *                             all subdomains, including "a.b.foo.com". From the security perspective, to provide protection against possible Man-In-The-Middle
     *                             attacks, we strongly recommend to use "strict" option.
     *                             Valid values are 'strict', 'browser_compatible', 'allow_all'.
     *                             Default value is 'strict'.
     * @param trustKeystore        The pathname of the Java TrustStore file. This contains certificates from other parties that you expect to communicate with, or from
     *                             Certificate Authorities that you trust to identify other parties.  If the protocol selected is not 'https' or if trustAllRoots
     *                             is 'true' this input is ignored.
     *                             Format of the keystore is Java KeyStore (JKS).
     * @param trustPassword        The password associated with the TrustStore file. If trustAllRoots is false and trustKeystore is empty, trustPassword default will be supplied.
     *                             Default value is 'changeit'.
     * @param keystore             The pathname of the Java KeyStore file. You only need this if the server requires client authentication. If the protocol selected is not
     *                             'https' or if trustAllRoots is 'true' this input is ignored.
     *                             Format of the keystore is Java KeyStore (JKS).
     * @param keystorePassword     The password associated with the KeyStore file. If trustAllRoots is false and keystore is empty, keystorePassword default will be supplied.
     *                             Default value is 'changeit'.
     * @param secureProcessing  optional - whether to use secure processing
     *
     * @return map of results containing success or failure text and a result message
     */


    @Action(name = "Validate",
            outputs = {
                @Output(Constants.Outputs.RETURN_CODE),
                @Output(Constants.Outputs.RETURN_RESULT)},
            responses = {
                @Response(text = Constants.ResponseNames.SUCCESS, field = Constants.Outputs.RETURN_CODE, value = Constants.ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL),
                @Response(text = Constants.ResponseNames.FAILURE, field = Constants.Outputs.RETURN_CODE, value = Constants.ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, isDefault = true, isOnFail = true)})
    public Map<String, String> execute(
            @Param(value = Constants.Inputs.XML_DOCUMENT, required = true) String xmlDocument,
            @Param(value = Constants.Inputs.XML_DOCUMENT_SOURCE) String xmlDocumentSource,
            @Param(Constants.Inputs.XSD_DOCUMENT) String xsdDocument,
            @Param(value = Constants.Inputs.XSD_DOCUMENT_SOURCE) String xsdDocumentSource,
            @Param(value = Constants.Inputs.USERNAME) String username,
            @Param(value = Constants.Inputs.PASSWORD) String password,
            @Param(value = Constants.Inputs.TRUST_ALL_ROOTS) String trustAllRoots,
            @Param(value = Constants.Inputs.KEYSTORE) String keystore,
            @Param(value = Constants.Inputs.KEYSTORE_PASSWORD) String keystorePassword,
            @Param(value = Constants.Inputs.TRUST_KEYSTORE) String trustKeystore,
            @Param(value = Constants.Inputs.TRUST_PASSWORD) String trustPassword,
            @Param(value = Constants.Inputs.X_509_HOSTNAME_VERIFIER) String x509HostnameVerifier,
            @Param(value = Constants.Inputs.PROXY_HOST) String proxyHost,
            @Param(value = Constants.Inputs.PROXY_PORT) String proxyPort,
            @Param(value = Constants.Inputs.PROXY_USERNAME) String proxyUsername,
            @Param(value = Constants.Inputs.PROXY_PASSWORD) String proxyPassword,
            @Param(Constants.Inputs.SECURE_PROCESSING) String secureProcessing) {

        CommonInputs inputs = new CommonInputs.CommonInputsBuilder()
                .withXmlDocument(xmlDocument)
                .withXmlDocumentSource(xmlDocumentSource)
                .withUsername(username)
                .withPassword(password)
                .withTrustAllRoots(trustAllRoots)
                .withKeystore(keystore)
                .withKeystorePassword(keystorePassword)
                .withTrustKeystore(trustKeystore)
                .withTrustPassword(trustPassword)
                .withX509HostnameVerifier(x509HostnameVerifier)
                .withProxyHost(proxyHost)
                .withProxyPort(proxyPort)
                .withProxyUsername(proxyUsername)
                .withProxyPassword(proxyPassword)
                .withSecureProcessing(secureProcessing)
                .build();

        CustomInputs customInputs = new CustomInputs.CustomInputsBuilder()
                .withXsdDocument(xsdDocument)
                .withXsdDocumentSource(xsdDocumentSource)
                .build();

        return new ValidateService().execute(inputs, customInputs);
    }
}
