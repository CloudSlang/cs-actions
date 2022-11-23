/*
 * (c) Copyright 2022 Micro Focus, L.P.
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

package io.cloudslang.content.redhat.utils;

public class Descriptions {
    public static class Common{
        public static final String RETURN_CODE_DESC = "0 if success, -1 if failure.";
    }
    public static class GetTokenAction {

        public static final String GET_TOKEN_NAME = "Get Token";
        public static final String RETURN_RESULT_DESC = "The authorization token for Openshift.";
        public static final String AUTH_TOKEN_DESC = "Generated authentication token.";
        public static final String EXCEPTION_DESC = "An error message in case there was an error while generating the " +
                "token.";
        public static final String TRUST_ALL_ROOTS_DESCRIPTION = "Specifies whether to enable weak security over SSL/TSL. " +
                "A certificate is trusted even if no trusted certification authority issued it.";
        public static final String X509_HOSTNAME_VERIFIER_DESCRIPTION = "Specifies the way the server hostname must match a domain name in " +
                "the subject's Common Name (CN) or subjectAltName field of the X.509 certificate. Set this to " +
                "\"allow_all\" to skip any checking. For the value \"browser_compatible\" the hostname verifier " +
                "works the same way as Curl and Firefox. The hostname must match either the first CN, or any of " +
                "the subject-alts. A wildcard can occur in the CN, and in any of the subject-alts. The only " +
                "difference between \"browser_compatible\" and \"strict\" is that a wildcard (such as \"*.foo.com\") " +
                "with \"browser_compatible\" matches all subdomains, including \"a.b.foo.com\".";
        public static final String USERNAME_DESC = "The name of the user who is logging in to Openshift.";
        public static final String PASSWORD_DESC = "The password used by the user to log in to Openshift.";
        public static final String HOST_DESC = "HOST.";

        public static final String SUCCESS_DESC = "Token generated successfully.";
        public static final String FAILURE_DESC = "There was an error while trying to retrieve token.";


    }
}
