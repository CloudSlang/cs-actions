package io.cloudslang.content.xml.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.xml.entities.inputs.CommonInputs;
import io.cloudslang.content.xml.entities.inputs.CustomInputs;
import io.cloudslang.content.xml.services.ValidateService;

import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.xml.utils.Constants.Inputs.KEYSTORE;
import static io.cloudslang.content.xml.utils.Constants.Inputs.KEYSTORE_PASSWORD;
import static io.cloudslang.content.xml.utils.Constants.Inputs.PASSWORD;
import static io.cloudslang.content.xml.utils.Constants.Inputs.PROXY_HOST;
import static io.cloudslang.content.xml.utils.Constants.Inputs.PROXY_PASSWORD;
import static io.cloudslang.content.xml.utils.Constants.Inputs.PROXY_PORT;
import static io.cloudslang.content.xml.utils.Constants.Inputs.PROXY_USERNAME;
import static io.cloudslang.content.xml.utils.Constants.Inputs.SECURE_PROCESSING;
import static io.cloudslang.content.xml.utils.Constants.Inputs.TRUST_ALL_ROOTS;
import static io.cloudslang.content.xml.utils.Constants.Inputs.TRUST_KEYSTORE;
import static io.cloudslang.content.xml.utils.Constants.Inputs.TRUST_PASSWORD;
import static io.cloudslang.content.xml.utils.Constants.Inputs.USERNAME;
import static io.cloudslang.content.xml.utils.Constants.Inputs.XML_DOCUMENT;
import static io.cloudslang.content.xml.utils.Constants.Inputs.XML_DOCUMENT_SOURCE;
import static io.cloudslang.content.xml.utils.Constants.Inputs.XSD_DOCUMENT;
import static io.cloudslang.content.xml.utils.Constants.Inputs.XSD_DOCUMENT_SOURCE;
import static io.cloudslang.content.xml.utils.Constants.Inputs.X_509_HOSTNAME_VERIFIER;
import static io.cloudslang.content.xml.utils.Constants.Outputs.ERROR_MESSAGE;
import static io.cloudslang.content.xml.utils.Constants.Outputs.RESULT_TEXT;


/**
 * Created by markowis on 18/02/2016.
 */
public class Validate {
    /**
     * Service to validate an XML document. Input must be given for either "xmlDocument" or for "xmlLocation".
     * The "'xsdLocation" input is optional, but if specified then the XML document will be validated against the XSD schema.
     *
     * @param xmlDocument          XML string to test
     * @param xmlDocumentSource    The source type of the xml document.
     *                             Valid values: xmlString, xmlPath, xmlUrl
     *                             Default value: xmlString
     * @param xsdDocument          optional - XSD to test given XML against
     * @param xsdDocumentSource    The source type of the xsd document.
     *                             Valid values: xsdString, xsdPath
     *                             Default value: xsdString
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
     * @param secureProcessing     optional - whether to use secure processing
     * @return map of results containing success or failure text and a result message
     */

    @Action(name = "Validate",
            outputs = {
                    @Output(RETURN_CODE),
                    @Output(RESULT_TEXT),
                    @Output(RETURN_RESULT),
                    @Output(ERROR_MESSAGE)},
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = RETURN_CODE, value = SUCCESS, matchType = COMPARE_EQUAL),
                    @Response(text = ResponseNames.FAILURE, field = RETURN_CODE, value = FAILURE, matchType = COMPARE_EQUAL, isDefault = true, isOnFail = true)})
    public Map<String, String> execute(
            @Param(value = XML_DOCUMENT, required = true) String xmlDocument,
            @Param(value = XML_DOCUMENT_SOURCE) String xmlDocumentSource,
            @Param(value = XSD_DOCUMENT) String xsdDocument,
            @Param(value = XSD_DOCUMENT_SOURCE) String xsdDocumentSource,
            @Param(value = USERNAME) String username,
            @Param(value = PASSWORD, encrypted = true) String password,
            @Param(value = TRUST_ALL_ROOTS) String trustAllRoots,
            @Param(value = KEYSTORE) String keystore,
            @Param(value = KEYSTORE_PASSWORD, encrypted = true) String keystorePassword,
            @Param(value = TRUST_KEYSTORE) String trustKeystore,
            @Param(value = TRUST_PASSWORD, encrypted = true) String trustPassword,
            @Param(value = X_509_HOSTNAME_VERIFIER) String x509HostnameVerifier,
            @Param(value = PROXY_HOST) String proxyHost,
            @Param(value = PROXY_PORT) String proxyPort,
            @Param(value = PROXY_USERNAME) String proxyUsername,
            @Param(value = PROXY_PASSWORD, encrypted = true) String proxyPassword,
            @Param(value = SECURE_PROCESSING) String secureProcessing) {

        final CommonInputs inputs = new CommonInputs.CommonInputsBuilder()
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

        final CustomInputs customInputs = new CustomInputs.CustomInputsBuilder()
                .withXsdDocument(xsdDocument)
                .withXsdDocumentSource(xsdDocumentSource)
                .build();

        return new ValidateService().execute(inputs, customInputs);
    }
}
