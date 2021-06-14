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

package io.cloudslang.content.ldap.actions.users;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.ldap.constants.InputNames;
import io.cloudslang.content.ldap.constants.OutputNames;
import io.cloudslang.content.ldap.entities.UpdateUserDetailsInput;
import io.cloudslang.content.ldap.services.users.UpdateUserDetailsService;
import io.cloudslang.content.ldap.utils.ResultUtils;

import java.util.Map;

public class UpdateUserDetailsAction {

    /**
     * This operation adds attributes to a new user in Active Directory. It can also be used to edit the provided inputs of
     * a new user or to add custom attributes to an existing one, by providing a list of attributes and values, separated by
     * new line in the format: attribute:value.
     *
     * @param host              The IP or host name of the domain controller. The port number can be mentioned as well, along
     *                          with the host (hostNameOrIP:PortNumber).
     *                          Examples: test.example.com,  test.example.com:636, <IPv4Address>, <IPv6Address>,
     *                          [<IPv6Address>]:<PortNumber> etc.
     *                          Value format: The format of an IPv4 address is: [0-225].[0-225].[0-225].[0-225]. The format of an
     *                          IPv6 address is ####:####:####:####:####:####:####:####/### (with a prefix), where each #### is
     *                          a hexadecimal value between 0 to FFFF and the prefix /### is a decimal value between 0 to 128.
     *                          The prefix length is optional.
     * @param distinguishedName The Organizational Unit DN or Common Name DN to add the user to.
     *                          Example: OU=OUTest1,DC=battleground,DC=ad
     * @param userCommonName    The CN, generally the full name of the user that will be updated.
     *                          Example: Bob Smith
     * @param username          User to connect to Active Directory as.
     * @param password          Password to connect to Active Directory as.
     * @param firstName         User's first name to change.
     * @param lastName          User's last name to change.
     * @param displayName       User's display name to change.
     * @param street            User's street.
     * @param city              User's city.
     * @param stateOrProvince   User's state or province option.
     * @param zipOrPostalCode   User's first zip or postal code
     * @param countryOrRegion   User's country or region. The format for this input should be countryName,countryAbbreviation,countryCode.
     *                          CountryName sets the value for the "co" property, countryAbbreviation sets the "c" property using the
     *                          two-letter country code, countryCode sets the "countryCode" property using the numeric value of the
     *                          country.
     * @param attributesList    The list of the attributes to set to the user. This should be provided in the following format:
     *                          attribute:value, separated by new line. Make sure that the attributes are valid Active Directory
     *                          attributes.
     *                          Example: streetAddress:My Address
     *                                   postalCode:123456
     * @param protocol          The protocol to use when connecting to the AD server.
     *                          Valid values: 'HTTP' and 'HTTPS'.
     * @param trustAllRoots     Specifies whether to enable weak security over SSL. A SSL certificate is trusted even if
     *                          no trusted certification authority issued it.
     *                          Default value: true.
     *                          Valid values: true, false.
     * @param trustKeystore     The location of the TrustStore file.
     *                          Example: %JAVA_HOME%/jre/lib/security/cacerts
     * @param trustPassword     The password associated with the TrustStore file.
     * @param connectionTimeout Time in milliseconds to wait for the connection to be made.
     *                          Default value: 10000.
     * @param executionTimeout  Time in milliseconds to wait for the command to complete.
     *                          Default value: 90000.
     * @return - a map containing the output of the operation. Keys present in the map are:
     * returnResult - This will contain the response entity in case of success or the error message in case of failure.
     * returnCode - The return code of the operation. 0 if the operation goes to success, -1 if the operation goes to failure.
     * exception - The exception message if the operation fails.
     */

    @Action(name = "Update User Details",
            outputs = {
                    @Output(OutputNames.RETURN_RESULT),
                    @Output(OutputNames.RETURN_CODE),
                    @Output(OutputNames.EXCEPTION)
            },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = OutputNames.RETURN_CODE, value = ReturnCodes.SUCCESS,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = ResponseNames.FAILURE, field = OutputNames.RETURN_CODE,
                            value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR)
            })
    public Map<String, String> execute(
            @Param(value = InputNames.HOST, required = true) String host,
            @Param(value = InputNames.DISTINGUISHED_NAME, required = true) String distinguishedName,
            @Param(value = InputNames.USER_COMMON_NAME, required = true) String userCommonName,
            @Param(value = InputNames.USERNAME, required = true) String username,
            @Param(value = InputNames.PASSWORD, required = true, encrypted = true) String password,
            @Param(value = InputNames.FIRST_NAME) String firstName,
            @Param(value = InputNames.LAST_NAME) String lastName,
            @Param(value = InputNames.DISPLAY_NAME) String displayName,
            @Param(value = InputNames.STREET) String street,
            @Param(value = InputNames.CITY) String city,
            @Param(value = InputNames.STATE_OR_PROVINCE) String stateOrProvince,
            @Param(value = InputNames.ZIP_OR_POSTAL_CODE) String zipOrPostalCode,
            @Param(value = InputNames.COUNTRY_OR_REGION) String countryOrRegion,
            @Param(value = InputNames.ATTRIBUTES_LIST) String attributesList,
            @Param(value = InputNames.PROTOCOL) String protocol,
            @Param(value = InputNames.TRUST_ALL_ROOTS) String trustAllRoots,
            @Param(value = InputNames.TRUST_KEYSTORE) String trustKeystore,
            @Param(value = InputNames.TRUST_PASSWORD, encrypted = true) String trustPassword,
            @Param(value = InputNames.CONNECTION_TIMEOUT) String connectionTimeout,
            @Param(value = InputNames.EXECUTION_TIMEOUT) String executionTimeout) {
        UpdateUserDetailsInput.Builder inputBuilder = new UpdateUserDetailsInput.Builder()
                .host(host)
                .distinguishedName(distinguishedName)
                .userCommonName(userCommonName)
                .username(username)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .displayName(displayName)
                .street(street)
                .city(city)
                .stateOrProvince(stateOrProvince)
                .zipOrPostalCode(zipOrPostalCode)
                .countryOrRegion(countryOrRegion)
                .attributesList(attributesList)
                .protocol(protocol)
                .trustAllRoots(trustAllRoots)
                .trustKeystore(trustKeystore)
                .trustPassword(trustPassword)
                .connectionTimeout(connectionTimeout)
                .executionTimeout(executionTimeout);
        try {
            return new UpdateUserDetailsService().execute(inputBuilder.build());
        } catch (Exception e) {
            return ResultUtils.fromException(e);
        }
    }
}
