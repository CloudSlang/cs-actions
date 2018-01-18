/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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

/**
 *
 *     The 'Http Client' does an http request and a parsing of the response. It provides features like: http autentication, http secure, connection pool, cookies, proxy. To acomplish this it uses the third parties from Apache: HttpClient 4.3, HttpCore 4.3. It also uses the JCIFS library from the Samba for the 'NTLM' authentication.
 *
 *     <br>The operation will build for the request:
 *     <br>- the URL: composed of the 'url' input (that needs to be standard) and the 'queryParams' input (that doesn't need to be url encoded). If 'encodeQueryParams'='false' queryParams will not get double encoded.
 *     <br>- the content-type: This will be the 'contentType' input completed by 'requestCharacterSet' if that info is missing (so if you say 'contentType=text/html' and 'charset=utf-8' you will get the header 'Content-Type: text/html; charset=utf-8'. But you can also specify the whole header as input 'contentType'='text/html; charset=utf-8' or as input 'headers'='Content-Type: text/html; charset=utf-8'). Content-type will default to 'text/html; charset=ISO_8859_1' unless 'formParams' are specified and then it will default to 'application/x-www-form-urlencoded; charset=ISO_8859_1'
 *     <br>- the request entity: 'formParams' input has priority in building this. Then comes 'body' and then 'filePath'. The previously built 'contentType' will be taken into account.
 *     <br>- the headers: This will be composed from the 'headers' input and the previously built 'contentType'. Extra, protocol specific headers will also be added the Apache Http Client api:
 *
 *     <br><br>For <b>GET</b> you will find in the request
 *     <br>Content-Type: text/plain; charset=ISO-8859-1
 *     <br>Host: 16.77.58.58:8080
 *     <br>Connection: Keep-Alive
 *     <br>User-Agent: Apache-HttpClient/4.3.4 (java 1.5)
 *     <br>Accept-Encoding: gzip,deflate
 *     <br>
 *     <br>For <b>POST</b> you will find in addition the Content-Length header, e.g.:
 *     <br>Content-Length: 34
 *     <br>
 *     <br>- the request configuration: This includes connectionTimeout, socketTimeout, followRedirects, proxyHost and proxyPort
 *     <br>- the credentials provider: This will build simple user-password credentials for basic and digest and domain-user-password credentials for ntlm. These will be asociated to the host and port in the url. The same thing will be done for proxyUsername, proxyPassword, proxyHost and  proxyPort.
 *     <br>- the autehntication scheme: The action will register the Apache HTTP Client autentication scheme coresponding to the given authType. This will know how to resopond tu http ww-autenticate challenges. For NTLM it will register not the default Apache scheme but the JCIFS one.
 *     <br>- the cookieStore: This is a memory object that holds all the cookies. It is taken from the session and deserialized (and created if not present). At the end of the execution it will be serialized back into the session. Because of the serializable behaviour it cannot be used in a multithreaded execution. If 'useCookies' is false the cookie store will be lost for each execution.
 *     <br>- the https connection: Apache Http Client uses Java Secure Socket Extension (JSEE) and this supports SSL versions 2.0 and 3.0 and Transport Layer Security (TLS) 1.0. The operation will take into account 'trustAllRoots' , 'keystore', 'keystorePassword', 'trustKeystore' and 'trustPassword'. With trustAllRoots=true you do not need to spcify anything else. Otherwise you may need to import the selfsigned certificates into your default keystore (<OO_Home>/java/lib/security/cacerts)  or the one specified by the 'keystore' input. 'trustKeystore' is for server side https autentication and is less used.
 *     <br>- the connection pool: This will take the connection pool from the 'Global Session' and build it if it does not exist. This will allow reuse of the existing connection. If 'keepAlive' is true the currrent connection will not be closed.
 *     <br>
 *     <br>The request will be executed.
 *     <br>
 *     <br>And the response will be parsed:
 *     <br>- the entity will be read using the character set specified by the 'responseCharacterSet'. If no 'responseCharacterSet' present, the charset from the content-type from the response headers will be used. Otherwise charset will default to 'ISO_8859_1'. If 'destinationFile' is specified the entity content will go there, if not it will go to the 'returnResult' output.
 *     <br>
 *     <br>
 *     <br>Other notes
 *     <br>1. You can use KeyTool from <OO_Home>/java/bin to import a SSL certificate in a KeyStore. For example, to import a certificate in the Java cacerts KeyStore, run the following command from the KeyTool folder: keytool -import -file c:/../<your_cert>.cer -alias <your_alias> -keystore <OO_Home>/java/lib/security/cacerts. If the KeyStore does not exist, the command tries to create a new one containing only your certificate.
 *     <br>2. You can use KeyTool to create a certificate chain through the following command: <pre>
 *         keytool -genkey -dname "cn=Mark Jones, ou=JavaSoft, o=Sun, c=US"
 *            -alias business -keypass kpi135 -keystore C:\working\mykeystore
 *            -storepass ab987c -validity 180 </pre>
 *     <br>This creates a certificate chain in a new KeyStore containing only one certificate. It is useful in SSL client authentication because the KeyManager ignores any certificates not included in a certificate chain.
 *     <br>
 *     <br>3. If the value assigned to the <trustAllRoots> input is 'false' and <trustKeystore> is empty, the operation tries to use the default trust store, using the password "changeit".
 *     <br>4. If the authentication type is Kerberos, you might need to add an SPN (Service Principal Name) for the web server's computer account.  In order to do this, you can use ADUC (Active Directory Users and Computers) with enabled 'Advanced Features'. Go to the web server's computer account (example: CN=<web_server_name>,CN=Computers,DC=domain,DC=com), open the 'Properties' window and press the 'Attribute Editor' tab. Observe the values of the 'servicePrincipalName' attribute and add your SPN if not present. For example, HTTP/<web_server_FQDN>:port.
 *     <br>5. If you need to specify query parameters when making the request, use the <queryParams> input. The operation also accepts including query parameters in the URL. For example, http://hostname:80/page.asp?parameterName1=parameterValue1&#38;parameterName2=parameterValue2. If you specify the same query parameter both through the <queryParams> and the <url> inputs, the url build will contain the query parameter twice with the two values.
 *     <br>6. Notes about the Kerberos authentication
 *     <br>
 *     <br>The simplest way to use Kerberos authentication is to write the URL using a FQDN and specify the username and password that has access to that resource. The OO service running this operation should be run as that user.  Following the example is the explanation about how the operation works in this use case.
 *     <br>Example 1 inputs: <pre>
 *          url->http://mywebserver.contoso.com
 *          authType->kerberos
 *          username->Administrator
 *          password->******
 *          method->GET
 *     </pre>
 *     <br>Kerberos authentication is built into JRE and needs two configuration files, as specified by the JAAS framework, and the Kerberos mechanism required by the Java GSS-API methods. These will be created by the operation as temporary files if the 'kerberosConfFile' and 'kerberosLoginConfFile' inputs are not specified. The corresponding java system properties 'java.security.krb5.conf' and 'java.security.auth.login.config' will also be set to the above file paths. The domain name will be obtained from the 'url' input, from the host part.
 *
 *     <br>Created temporary files on a windows machine will be like these: <pre>
 *     c:\Users\Administrator.CONTOSO\AppData\Local\Temp\2\krb2289023294434647612kdc
 *          [libdefaults]
 *              default_realm = CONTOSO.COM
 *          [realms]
 *              CONTOSO.COM = {
 *                  kdc = contoso.com
 *                  admin_server = contoso.com
 *              }
 *     c:\Users\Administrator.HPOO\AppData\Local\Temp\2\krb6566019897713925126loginConf
 *          com.sun.security.jgss.initiate {
 *              com.hp.score.content.httpclient.build.KrbHttpLoginModule required
 *              doNotPrompt=true;
 *          };
 *     </pre>
 *     Note that the operation's 'com.hp.score.content.httpclient.build.KrbHttpLoginModule' extends the java standard 'com.sun.security.auth.module.Krb5LoginModule' in order to provide the given username and password. The operation will use the specified 'username' and 'password' to get a TGT similar to what the command line tool '<OO instalation>/java/bin/kinit' does when no keytab exists.
 *     <br>
 *     <br>Another use case is to specify 'kerberosConfFile' and 'kerberosLoginConfFile' paths and use a keytab instead of 'username' and 'password' to get a TGT. To configure you machine
 *     <br>6.1 Generate a keytab use the ktab command line like:
 *     <br>'<OO instalation>/java/bin/ktab -a Administrator password'. On windows a keytab file will be created: C:\Users\Administrator.CONTOSO\krb5.keytab
 *
 *     <br>6.2 Write a krb5.conf file with content similar to (where you replace CONTOSO.COM with your domain and 'ad.contoso.com' with your kdc FQDN. More info here: http://web.mit.edu/kerberos/krb5-1.4/krb5-1.4.4/doc/krb5-admin/krb5.conf.html):
 *     <pre>
 *          [libdefaults]
 *              default_realm = CONTOSO.COM
 *          [realms]
 *              CONTOSO.COM = {
 *                  kdc = ad.contoso.com
 *                  admin_server = ad.contoso.com
 *              }
 *     </pre>
 *     <br>6.3. Write a login.conf file with the content (more info here: http://docs.oracle.com/javase/7/docs/jre/api/security/jaas/spec/com/sun/security/auth/module/Krb5LoginModule.html )
 *     <pre>
 *          com.sun.security.jgss.initiate {
 *          com.sun.security.auth.module.Krb5LoginModule required principal=Administrator
 *          doNotPrompt=true
 *          useKeyTab=true
 *          keyTab="file:/C:/Users/Administrator.CONTOSO/krb5.keytab";
 *     };
 *     </pre>
 *     <br>6.4. Configure operation inputs like this
 *     <pre>
 *          Example 2 inputs:
 *          url->http://mywebserver.contoso.com
 *          authType->kerberos
 *          kerberosConfFile->c:/OO/krb5.conf
 *          kerberosLoginConfFile->c:/OO/login.conf
 *          method->GET
 *     </pre>
 *
 */
package io.cloudslang.content.httpclient;