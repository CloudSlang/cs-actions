/*
 * (c) Copyright 2020 EntIT Software LLC, a Micro Focus company, L.P.
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
package io.cloudslang.content.abbyy.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.abbyy.constants.InputNames;
import io.cloudslang.content.abbyy.constants.OutputNames;
import io.cloudslang.content.abbyy.entities.ProcessTextFieldInput;
import io.cloudslang.content.abbyy.entities.WritingStyle;
import io.cloudslang.content.abbyy.services.ProcessTextFieldService;
import io.cloudslang.content.abbyy.utils.ResultUtils;
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.constants.ReturnCodes;

import java.util.Map;

public class ProcessTextFieldAction {


    /**
     * Converts a text field from a given image to text in XML output format using the ABBYY Cloud OCR REST API v1.
     *
     * @param locationId             The ID of the processing location to be used. Please note that the connection of your
     *                               application to the processing location is specified manually during application creation,
     *                               i.e. the application is bound to work with only one of the available locations.
     *                               Valid values: 'cloud-eu', 'cloud-westus'.
     * @param applicationId          The ID of the application to be used.
     * @param password               The password for the application.
     * @param region                 Specifies the region of the text field on the image. The coordinates of the region are measured
     *                               in pixels relative to the left top corner of the image and are specified in the following order:
     *                               left, top, right, bottom. By default, the region of the whole image is used.
     *                               Valid values: a list of exactly 4 numbers greater than or equal to 0 or equal to -1.
     *                               Default: '-1,-1,-1,-1'.
     * @param language               Specifies recognition language of the document. This parameter can contain several language
     *                               names separated with commas, for example "English,French,German".
     *                               For all languages supported, see the official ABBYY Cloud OCR SDK documentation.
     *                               Currently, the only official language supported by this operation is 'English'.
     *                               Default: 'English'.
     * @param destinationFile        The absolute path of a file on disk where to save the xml entity returned by the response.
     * @param sourceFile             The absolute path of the image to be loaded and converted using the API.
     * @param letterSet              Specifies the letter set, which should be used during recognition. Contains a string with
     *                               the letter set characters. For example, "ABCDabcd'-.".
     *                               By default, the letter set of the language, specified in the language parameter, is used.
     *                               Default: ''.
     * @param regExp                 Specifies the regular expression which defines which words are allowed in the field and which are not.
     *                               See the description of regular expressions. By default, the set of allowed words is defined
     *                               by the dictionary of the language, specified in the language parameter.
     *                               Default: ''.
     * @param textType               Specifies the type of the text on a page.
     *                               This parameter may also contain several text types separated with commas, for example "normal,matrix".
     *                               Valid values: 'normal', 'typewriter', 'matrix', 'index', 'ocrA', 'ocrB', 'e13b', 'cmc7', 'gothic'.
     *                               Default: 'normal'.
     * @param oneTextLine            Specifies whether the field contains only one text line. The value should be true,
     *                               if there is one text line in the field; otherwise it should be false.
     *                               Default: 'false'.
     * @param oneWordPerTextLine     Specifies whether the field contains only one word in each text line. The value should be true,
     *                               if no text line contains more than one word (so the lines of text will be recognized as a single word);
     *                               otherwise it should be false.
     *                               Default: 'false'.
     * @param markingType            This property is valid only for the handprint recognition. Specifies the type of marking around letters
     *                               (for example, underline, frame, box, etc.). By default, there is no marking around letters.
     *                               Valid values: 'simpleText', 'underlinedText', 'textInFrame', 'greyBoxes', 'charBoxSeries,
     *                               'simpleComb', combInFrame', 'partitionedFrame'.
     *                               Default: 'simpleText'.
     * @param placeholdersCount      Specifies the number of character cells for the field. This property has a sense only for the field
     *                               marking types (the markingType parameter) that imply splitting the text in cells.
     *                               Valid values: an integer greater than or equal to 0.
     *                               Default: '1'.
     * @param description            Contains the description of the processing task. Cannot contain more than 255 characters.
     *                               If the description contains more than 255 characters, then the text will be truncated.
     * @param pdfPassword            Contains a password for accessing password-protected images in PDF format.
     * @param proxyHost              The proxy server used to access the web site.
     * @param proxyPort              The proxy server port. The value '-1' indicates that the proxy port is not set
     *                               and the scheme default port will be used, e.g. if the scheme is 'http://' and
     *                               the 'proxyPort' is set to '-1' then port '80' will be used.
     *                               Valid values: -1 and integer values greater than 0.
     *                               Default value: 8080.
     * @param proxyUsername          The user name used when connecting to the proxy.
     * @param proxyPassword          The proxy server password associated with the proxyUsername input value.
     * @param trustAllRoots          Specifies whether to enable weak security over SSL/TSL. A certificate is trusted
     *                               even if no trusted certification authority issued it.
     *                               Valid values: 'true', 'false'.
     *                               Default value: 'false'.
     * @param x509HostnameVerifier   Specifies the way the server hostname must match a domain name in the subject's Common Name (CN)
     *                               or subjectAltName field of the X.509 certificate. Set this to "allow_all" to skip any checking.
     *                               For the value "browser_compatible" the hostname verifier works the same way as Curl and Firefox.
     *                               The hostname must match either the first CN, or any of the subject-alts.
     *                               A wildcard can occur in the CN, and in any of the subject-alts. The only difference
     *                               between "browser_compatible" and "strict" is that a wildcard (such as "*.foo.com")
     *                               with "browser_compatible" matches all subdomains, including "a.b.foo.com".
     *                               Valid values: 'strict','browser_compatible','allow_all'.
     *                               Default value: 'strict'.
     * @param trustKeystore          The pathname of the Java TrustStore file. This contains certificates from other parties
     *                               that you expect to communicate with, or from Certificate Authorities that you trust to
     *                               identify other parties.  If the protocol (specified by the 'url') is not 'https' or if
     *                               trustAllRoots is 'true' this input is ignored.
     *                               Default value: <OO_Home>/java/lib/security/cacerts. Format: Java KeyStore (JKS)
     * @param trustPassword          The password associated with the TrustStore file.
     *                               If trustAllRoots is 'false' and trustKeystore is empty, trustPassword default will be supplied.
     *                               Default value: 'changeit'.
     * @param connectTimeout         The time to wait for a connection to be established, in seconds.
     *                               A timeout value of '0' represents an infinite timeout.
     *                               Default value: '0'.
     * @param socketTimeout          The timeout for waiting for data (a maximum period inactivity between two consecutive data packets),
     *                               in seconds. A socketTimeout value of '0' represents an infinite timeout.
     *                               Default value: '0'.
     * @param keepAlive              Specifies whether to create a shared connection that will be used in subsequent calls.
     *                               If keepAlive is 'false', the already open connection will be used and after execution it will close it.
     *                               The operation will use a connection pool stored in a GlobalSessionObject that will be available throughout
     *                               the execution (the flow and subflows, between parallel split lanes).
     *                               Valid values: 'true', 'false'. Default value: 'true'.
     * @param connectionsMaxPerRoute The maximum limit of connections on a per route basis.
     *                               The default will create no more than 2 concurrent connections per given route.
     *                               Default value: '2'.
     * @param connectionsMaxTotal    The maximum limit of connections in total.
     *                               The default will create no more than 2 concurrent connections in total.
     *                               Default value: '20'.
     * @param responseCharacterSet   The character encoding to be used for the HTTP response.
     *                               If responseCharacterSet is empty, the charset from the 'Content-Type' HTTP response header will be used.
     *                               If responseCharacterSet is empty and the charset from the HTTP response Content-Type header is empty,
     *                               the default value will be used. You should not use this for method=HEAD or OPTIONS.
     *                               Default value: 'ISO-8859-1'.
     * @return a map containing the output of the operations. Keys present in the map are:
     * <br><b>returnResult</b> - Contains a human readable message mentioning the success or failure of the task.
     * <br><b>xmlResult</b> - The result for 'xml' export format in clear text (empty if 'xml' was not provided in 'exportFormat' input).
     * <br><b>taskId</b> - The ID of the task registered in the ABBYY server.
     * <br><b>credits</b> - The amount of ABBYY credits spent on the action.
     * <br><b>statusCode</b> - The status_code returned by the server.
     * <br><b>returnCode</b> - The returnCode of the operation: 0 for success, -1 for failure.
     * <br><b>exception</b> - The exception message if the operation goes to failure.
     * <br><b>timedOut</b> - True if the operation timed out before the document was processed, false otherwise.
     */
    @Action(name = "Process Text Field",
            outputs = {
                    @Output(OutputNames.RETURN_RESULT),
                    @Output(OutputNames.XML_RESULT),
                    @Output(OutputNames.TASK_ID),
                    @Output(OutputNames.CREDITS),
                    @Output(OutputNames.STATUS_CODE),
                    @Output(OutputNames.RETURN_CODE),
                    @Output(OutputNames.EXCEPTION),
                    @Output(OutputNames.TIMED_OUT),
            },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = OutputNames.RETURN_CODE, value = ReturnCodes.SUCCESS,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = ResponseNames.FAILURE, field = OutputNames.RETURN_CODE, value = ReturnCodes.FAILURE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR)
            })
    public Map<String, String> execute(
            @Param(value = InputNames.LOCATION_ID, required = true) String locationId,
            @Param(value = InputNames.APPLICATION_ID, required = true) String applicationId,
            @Param(value = InputNames.PASSWORD, required = true, encrypted = true) String password,
            @Param(value = InputNames.REGION) String region,
            @Param(value = InputNames.LANGUAGE) String language,
            @Param(value = InputNames.SOURCE_FILE, required = true) String sourceFile,
            @Param(value = InputNames.DESTINATION_FILE) String destinationFile,
            @Param(value = InputNames.LETTER_SET) String letterSet,
            @Param(value = InputNames.REG_EXP) String regExp,
            @Param(value = InputNames.TEXT_TYPE) String textType,
            @Param(value = InputNames.ONE_TEXT_LINE) String oneTextLine,
            @Param(value = InputNames.ONE_WORD_PER_TEXT_LINE) String oneWordPerTextLine,
            @Param(value = InputNames.MARKING_TYPE) String markingType,
            @Param(value = InputNames.PLACEHOLDERS_COUNT) String placeholdersCount,
            @Param(value = InputNames.DESCRIPTION) String description,
            @Param(value = InputNames.PDF_PASSWORD, encrypted = true) String pdfPassword,
            @Param(value = InputNames.PROXY_HOST) String proxyHost,
            @Param(value = InputNames.PROXY_PORT) String proxyPort,
            @Param(value = InputNames.PROXY_USERNAME) String proxyUsername,
            @Param(value = InputNames.PROXY_PASSWORD, encrypted = true) String proxyPassword,
            @Param(value = InputNames.TRUST_ALL_ROOTS) String trustAllRoots,
            @Param(value = InputNames.X509_HOSTNAME_VERIFIER) String x509HostnameVerifier,
            @Param(value = InputNames.TRUST_KEYSTORE) String trustKeystore,
            @Param(value = InputNames.TRUST_PASSWORD, encrypted = true) String trustPassword,
            @Param(value = InputNames.CONNECT_TIMEOUT) String connectTimeout,
            @Param(value = InputNames.SOCKET_TIMEOUT) String socketTimeout,
            @Param(value = InputNames.KEEP_ALIVE) String keepAlive,
            @Param(value = InputNames.CONNECTIONS_MAX_PER_ROUTE) String connectionsMaxPerRoute,
            @Param(value = InputNames.CONNECTIONS_MAX_TOTAL) String connectionsMaxTotal,
            @Param(value = InputNames.RESPONSE_CHARACTER_SET) String responseCharacterSet) {
        try {
            ProcessTextFieldInput inputBuilder = new ProcessTextFieldInput.Builder()
                    .locationId(locationId)
                    .applicationId(applicationId)
                    .password(password)
                    .region(region)
                    .language(language)
                    .letterSet(letterSet)
                    .regExp(regExp)
                    .textType(textType)
                    .oneTextLine(oneTextLine)
                    .oneWordPerTextLine(oneWordPerTextLine)
                    .markingType(markingType)
                    .placeholdersCount(placeholdersCount)
                    .writingStyle(WritingStyle.DEFAULT.toString())
                    .description(description)
                    .pdfPassword(pdfPassword)
                    .proxyHost(proxyHost)
                    .proxyPort(proxyPort)
                    .proxyUsername(proxyUsername)
                    .proxyPassword(proxyPassword)
                    .trustAllRoots(trustAllRoots)
                    .x509HostnameVerifier(x509HostnameVerifier)
                    .trustKeystore(trustKeystore)
                    .trustPassword(trustPassword)
                    .connectTimeout(connectTimeout)
                    .socketTimeout(socketTimeout)
                    .keepAlive(keepAlive)
                    .connectionsMaxPerRoute(connectionsMaxPerRoute)
                    .connectionsMaxTotal(connectionsMaxTotal)
                    .responseCharacterSet(responseCharacterSet)
                    .destinationFile(destinationFile)
                    .sourceFile(sourceFile)
                    .build();
            return new ProcessTextFieldService().execute(inputBuilder);
        } catch (Exception ex) {
            return ResultUtils.fromException(ex);
        }
    }

}
