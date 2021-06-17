/*
 * (c) Copyright 2020 Micro Focus
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
package io.cloudslang.content.ldap.constants;

public final class Constants {

    public static final String DEFAULT_JAVA_KEYSTORE = System.getProperty("java.home") + "/lib/security/cacerts";
    public static final String SSL = "SSL";
    public static final String HTTP = "http";
    public static final String HTTPS = "https";
    public static final String FALSE = "false";
    public static final String DEFAULT_PASSWORD_FOR_STORE = "changeit";
    public static final String HOST = ".host";
    public static final String JAVA_HOME = "java.home";
    public static final String AD_COMMOM_NAME = "cn=";
    public static final String AD_GIVEN_NAME = "givenName";
    public static final String AD_SURNAME = "sn";
    public static final String AD_DISPLAY_NAME = "displayName";
    public static final String AD_STREET_ADDRESS = "streetAddress";
    public static final String AD_CITY = "l";
    public static final String AD_POSTAL_CODE = "postalCode";
    public static final String AD_STATE_OR_PROVINCE = "st";
    public static final String AD_COUNTRY_NAME = "co";
    public static final String AD_COUNTRY_DIGITS = "c";
    public static final String AD_COUNTRY_CODE = "countryCode";
    public static final String[] ENABLED_PROTOCOLS =  {"TLSv1.3", "TLSv1.2", "TLSv1.1", "TLSv1"};

}
