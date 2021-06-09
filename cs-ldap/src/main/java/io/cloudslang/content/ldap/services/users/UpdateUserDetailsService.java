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
package io.cloudslang.content.ldap.services.users;

import io.cloudslang.content.ldap.constants.Constants;
import io.cloudslang.content.ldap.constants.ExceptionMsgs;
import io.cloudslang.content.ldap.entities.UpdateUserDetailsInput;
import io.cloudslang.content.ldap.utils.LDAPQuery;
import io.cloudslang.content.ldap.utils.ResultUtils;
import org.apache.commons.lang3.StringUtils;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.ldap.constants.OutputNames.RETURN_CODE;

public class UpdateUserDetailsService {
    public Map<String, String> execute(UpdateUserDetailsInput input) {

        Map<String, String> results = ResultUtils.createNewResultsEmptyMap();
        DirContext context = null;

        try {

            LDAPQuery ldap = new LDAPQuery();
            List<ModificationItem> mods = new ArrayList<>();

            if (input.getProtocol().toLowerCase().trim().equals("https")) {
                if (input.getTrustAllRoots()) {
                    context = ldap.MakeDummySSLLDAPConnection(input.getHost(), input.getUsername(), input.getPassword());
                } else {
                    context = ldap.MakeSSLLDAPConnection(input.getHost(), input.getUsername(), input.getPassword(), "false",
                              input.getTrustKeystore(), input.getTrustPassword());
                }
            } else {
                context = ldap.MakeLDAPConnection(input.getHost(), input.getUsername(), input.getPassword());
            }
            //handle country attributes
            modifyCountryAndRegionAttributes(input.getCountryOrRegion(), mods);

            // handle attributes
            modifyAttributes(input.getFirstName(), input.getLastName(), input.getDisplayName(), input.getStreet(), input.getCity(),
                    input.getZipOrPostalCode(), input.getStateOrProvince(), mods);

            // handle custom attributes
            modifyCustomAttributes(input.getAttributesList(), mods);

            // make the changes
            context.modifyAttributes(Constants.AD_COMMOM_NAME + input.getUserCommonName() + ", " + input.getDistinguishedName(),
                    mods.toArray(new ModificationItem[mods.size()]));

            results.put(RETURN_CODE, "0");
            results.put(RETURN_RESULT, "User's Attributes were updated successfully.");

        } catch (Exception e) {
            results.put(RETURN_CODE, "-1");
            results.put(RETURN_RESULT, "An error occurred: User's Attributes were not updated.");
            results.put(EXCEPTION, String.valueOf(e));
        } finally {
            if (context != null) {
                try {
                    context.close();
                } catch (NamingException e) {
                    results.put(RETURN_CODE, "-1");
                    results.put(EXCEPTION, String.valueOf(e));
                    results.put(RETURN_RESULT, "User's Attributes were  updated successfully, however the session could" +
                            " not be terminated. This may cause performance issues.");
                }
            }
        }
        return results;
    }


    //generate country attribute
    public static void modifyCountryAndRegionAttributes(String countryOrRegion, List<ModificationItem> modsList) throws NamingException {
        if (!StringUtils.isBlank(countryOrRegion)) {
            if ((countOccurrences(countryOrRegion, ",") == 2)) {
                String[] countryValues = countryOrRegion.split(",", 3);
                modsList.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(Constants.AD_COUNTRY_NAME, countryValues[0])));
                modsList.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute( Constants.AD_COUNTRY_DIGITS, countryValues[1])));
                modsList.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(Constants.AD_COUNTRY_CODE, countryValues[2])));
            } else {
                throw new IllegalArgumentException(ExceptionMsgs.COUNTRY_EXPECTED_FORMAT);
            }
        }
    }

    //generate new attribute
    public static void modifyAttributes(String firstName, String lastName, String displayName, String street, String city,
                                                          String zipOrPostalCode, String stateOrProvince, List<ModificationItem> mods){
        //constructs arrays to hold the values of the inputs
        String attrsArray[] = new String[]{firstName, lastName, displayName, street, city, zipOrPostalCode, stateOrProvince};
        String adAttrsArray[] = new String[]{Constants.AD_GIVEN_NAME, Constants.AD_SURNAME, Constants.AD_DISPLAY_NAME,
                Constants.AD_STREET_ADDRESS, Constants.AD_CITY, Constants.AD_POSTAL_CODE, Constants.AD_STATE_OR_PROVINCE};

        //add not empty attributes and their values to the mods array
        for (int j = 0; j < attrsArray.length; j++) {
            if (!StringUtils.isBlank(attrsArray[j])) {
                Attribute mod = new BasicAttribute(adAttrsArray[j], attrsArray[j]);
                mods.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, mod));
            }
        }
    }

    //generate new custom attributes
    public static void modifyCustomAttributes(String attributesList, List<ModificationItem> modsList) throws IOException,
            IllegalArgumentException {
        if (!StringUtils.isBlank(attributesList)) {
            BufferedReader reader = new BufferedReader(new StringReader(attributesList));
            String line;
            while ((line = reader.readLine()) != null) {
                int index = line.indexOf(":");
                if (index != -1) {
                    String inputAttribute = line.substring(0, index);
                    String value = line.substring(index + 1, line.length());
                    Attribute mod = new BasicAttribute(inputAttribute, value);
                    modsList.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, mod));
                } else {
                    throw new IllegalArgumentException(ExceptionMsgs.EXPECTED_FORMAT);
                }
            }
        }
    }

    public static int countOccurrences(String string, String substring) {
        int c, i = string.indexOf(substring);

        for( c = 0; i != -1; i = string.indexOf(substring, i + 1)) {
            ++c;
        }
        return c;
    }
}
