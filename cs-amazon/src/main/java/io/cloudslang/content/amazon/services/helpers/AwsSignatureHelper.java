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

package io.cloudslang.content.amazon.services.helpers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import static org.apache.commons.lang3.StringUtils.isBlank;

import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.AWS_REQUEST_VERSION;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.DEFAULT_AMAZON_REGION;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.AMPERSAND;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.COLON;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EQUAL;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.LINE_SEPARATOR;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.SCOPE_SEPARATOR;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.DOT;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.AMAZON_HOSTNAME;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.CN_AMAZON_HOSTNAME;

import static io.cloudslang.content.amazon.entities.constants.Constants.Values.ONE;
import static io.cloudslang.content.amazon.entities.constants.Constants.Values.START_INDEX;

/**
 * Created by Mihai Tusa.
 * 8/8/2016.
 */
public class AwsSignatureHelper {
    private static final String DATE_FORMAT = "yyyyMMdd'T'HHmmss'Z'";
    private static final String HYPHEN = "-";
    private static final String SEMICOLON = ";";
    private static final String TIME_ZONE = "UTC";

    private static final char DOT_CHAR = '.';

    /**
     * Canonicalized (standardized) query string is formed by first sorting all the query
     * parameters, then URI encoding both the key and value and then
     * joining them, in order, separating key value pairs with an '&'.
     *
     * @param queryParameters Query parameters to be canonicalized.
     * @return A canonicalized form for the specified query parameters.
     */
    public String canonicalizedQueryString(Map<String, String> queryParameters) {
        List<Map.Entry<String, String>> sortedList = getSortedMapEntries(queryParameters);

        StringBuilder queryString = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedList) {
            queryString.append(entryToQuery(entry));
        }

        if (queryString.length() > START_INDEX) {
            queryString.deleteCharAt(queryString.length() - ONE);   //removing last '&'
        }

        return queryString.toString();
    }

    /**
     * Canonicalized (standardized) headers string is formed by first sorting all the header
     * parameters, then converting all header names to lowercase and
     * trimming excess white space characters out of the header values
     *
     * @param headers Headers to be canonicalized.
     * @return A canonicalized form for the specified headers.
     */
    public String canonicalizedHeadersString(Map<String, String> headers) {
        List<Map.Entry<String, String>> sortedList = getSortedMapEntries(headers);

        String header;
        String headerValue;
        StringBuilder headerString = new StringBuilder();

        for (Map.Entry<String, String> ent : sortedList) {
            header = nullToEmpty(ent.getKey()).toLowerCase();
            headerValue = nullToEmpty(ent.getValue()).trim();
            headerString.append(header).append(COLON).append(headerValue).append(LINE_SEPARATOR);
        }

        return headerString.toString();
    }

    /**
     * Creates a comma separated list of headers.
     *
     * @param headers Headers to be signed.
     * @return Comma separated list of headers to be signed.
     */
    public String getSignedHeadersString(Map<String, String> headers) {
        List<String> sortedList = new ArrayList<>(headers.keySet());
        Collections.sort(sortedList, String.CASE_INSENSITIVE_ORDER);

        StringBuilder signedHeaderString = new StringBuilder();
        for (String header : sortedList) {
            if (signedHeaderString.length() > START_INDEX) {
                signedHeaderString.append(SEMICOLON);
            }
            signedHeaderString.append(nullToEmpty(header).toLowerCase());
        }
        return signedHeaderString.toString();
    }

    /**
     * Converts a java.util.Date to an AWS specific date format.
     * The AWS date should be in (java) format "yyyyMMdd'T'HHmmss'Z'" and in time zone UTC.
     *
     * @param date Date to be formatted.
     * @return A string representing the formatted date.
     */
    public String getAmazonDateString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
        return simpleDateFormat.format(date);
    }

    /**
     * Crates the amazon AWS credential scope (is represented by a slash-separated string of dimensions).
     *
     * @param dateStamp  Request date stamp. (Format: "yyyyMMdd")
     * @param awsRegion  AWS region to which the request is sent.
     * @param awsService AWS service to which the request is sent.
     * @return A string representing the AWS credential scope.
     */
    public String getAmazonCredentialScope(String dateStamp, String awsRegion, String awsService) {
        return dateStamp + SCOPE_SEPARATOR + awsRegion + SCOPE_SEPARATOR + awsService + SCOPE_SEPARATOR + AWS_REQUEST_VERSION;
    }

    /**
     * Extracts the AWS region from the endpoint.
     * If the endpoint has no region the "us-east-1" region is returned b default.
     *
     * @param endpoint AWS request endpoint.
     * @return A (lowercase alphanumeric) string representing the AWS region.
     */
    //todo does not recognize properly us-east-2.quicksight.amazonaws.com, us-west-1.queue.amazonaws.com types of hostname
    public String getAmazonRegion(String endpoint) {
        if (isBlank(endpoint)) {
            return DEFAULT_AMAZON_REGION;
        }
        if (endpoint.endsWith(DOT+AMAZON_HOSTNAME)) {
            endpoint = endpoint.substring(0, endpoint.length()-(DOT+AMAZON_HOSTNAME).length());
        } else if (endpoint.endsWith(DOT+CN_AMAZON_HOSTNAME)) {
            endpoint = endpoint.substring(0, endpoint.length() - (DOT + CN_AMAZON_HOSTNAME).length());
        } else return DEFAULT_AMAZON_REGION; //the hostname is not correct
        if (endpoint.startsWith("https://")) {
            endpoint = endpoint.substring("https://".length());
        } else if (endpoint.startsWith("http://")) {
            endpoint = endpoint.substring("http://".length());
        }
        int i = endpoint.lastIndexOf(DOT_CHAR); //search from the end
        if (i < 0) {
            return DEFAULT_AMAZON_REGION;
        }
        return endpoint.substring(i+1);
    }

    private String entryToQuery(Map.Entry<String, String> entry) {
        String escapedKey = nullToEmpty(UriEncoder.escapeString(entry.getKey()));
        String escapedValue = nullToEmpty(UriEncoder.escapeString(entry.getValue()));

        return escapedKey + EQUAL + escapedValue + AMPERSAND;
    }

    private String nullToEmpty(String inputString) {
        if (inputString == null) {
            return EMPTY;
        }
        return inputString;
    }

    private List<Map.Entry<String, String>> getSortedMapEntries(Map<String, String> map) {
        List<Map.Entry<String, String>> sortedList = new ArrayList<>(map.entrySet());
        Collections.sort(sortedList, new Comparator<Map.Entry<String, String>>() {
            public int compare(Map.Entry<String, String> firstEntry, Map.Entry<String, String> secondEntry) {
                return (firstEntry.getKey().compareToIgnoreCase(secondEntry.getKey()));
            }
        });
        return sortedList;
    }
}
