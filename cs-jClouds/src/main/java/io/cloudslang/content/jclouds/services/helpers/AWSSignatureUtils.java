package io.cloudslang.content.jclouds.services.helpers;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Mihai Tusa.
 * 8/8/2016.
 */
public class AWSSignatureUtils {
    private static final String DATE_FORMAT = "yyyyMMdd'T'HHmmss'Z'";
    private static final String TIME_ZONE = "UTC";
    private static final String SEMICOLON = ";";
    private static final String HYPHEN = "-";

    /**
     * The canonicalized (standardized) query string is formed by first sorting all the query
     * parameters, then URI encoding both the key and value and then
     * joining them, in order, separating key value pairs with an '&'.
     *
     * @param queryParameters The query parameters to be canonicalized.
     * @return A canonicalized form for the specified query parameters.
     */
    public String canonicalizedQueryString(Map<String, String> queryParameters) {
        List<Map.Entry<String, String>> sortedList = new ArrayList<>(queryParameters.entrySet());
        Collections.sort(sortedList, new UTF8StringComparator());

        StringBuilder queryString = new StringBuilder();
        for (Map.Entry<String, String> ent : sortedList) {
            queryString.append(entryToQuery(ent));
        }
        if (queryString.length() > 0) queryString.deleteCharAt(queryString.length() - 1);   //last &

        return queryString.toString();
    }

    /**
     * The canonicalized (standardized) headers string is formed by first sorting all the header
     * parameters, then converting all header names to lowercase and
     * trimming excess white space characters out of the header values
     *
     * @param headers The headers to be canonicalized.
     * @return A canonicalized form for the specified headers.
     */
    public String canonicalizedHeadersString(Map<String, String> headers) {
        List<Map.Entry<String, String>> sortedList = new ArrayList<>(headers.entrySet());
        Collections.sort(sortedList, new Comparator<Map.Entry<String, String>>() {
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return (o1.getKey().compareToIgnoreCase(o2.getKey()));
            }
        });

        String header;
        String headerValue;
        StringBuilder headerString = new StringBuilder();

        for (Map.Entry<String, String> ent : sortedList) {
            header = nullToEmpty(ent.getKey()).toLowerCase();
            headerValue = nullToEmpty(ent.getValue()).trim();
            headerString.append(header).append(Constants.Miscellaneous.COLON)
                    .append(headerValue).append(Constants.Miscellaneous.LINE_SEPARATOR);
        }

        return headerString.toString();
    }

    /**
     * Creates a comma separated list of headers.
     *
     * @param headers The headers to be signed.
     * @return The comma separated list of headers to be signed.
     */
    public String getSignedHeadersString(Map<String, String> headers) {
        List<String> sortedList = new ArrayList<>(headers.keySet());
        Collections.sort(sortedList, String.CASE_INSENSITIVE_ORDER);

        StringBuilder signedHeaderString = new StringBuilder();
        for (String header : sortedList) {
            if (signedHeaderString.length() > 0)
                signedHeaderString.append(SEMICOLON);
            signedHeaderString.append(nullToEmpty(header).toLowerCase());
        }
        return signedHeaderString.toString();
    }

    /**
     * Converts a java.util.Date to an AWS specific date format.
     * The AWS date should be in (java) format "yyyyMMdd'T'HHmmss'Z'" and in time zone UTC.
     *
     * @param date The date to be formatted.
     * @return A string representing the formatted string.
     */
    public String getAmazonDateString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
        return simpleDateFormat.format(date);
    }

    /**
     * Crates the amazon AWS credential scope (is represented by a slash-separated string of dimensions).
     *
     * @param dateStamp  The request date stamp. (Format: "yyyyMMdd")
     * @param awsRegion  The AWS region to which the request is sent.
     * @param awsService The AWS service to which the request is sent.
     * @return A string representing the AWS credential scope.
     */
    public String getAmazonCredentialScope(String dateStamp, String awsRegion, String awsService) {
        return dateStamp + Constants.Miscellaneous.SCOPE_SEPARATOR + awsRegion + Constants.Miscellaneous.SCOPE_SEPARATOR +
                awsService + Constants.Miscellaneous.SCOPE_SEPARATOR + Constants.AWSParams.AWS_REQUEST_VERSION;
    }

    /**
     * Extracts the AWS region from the endpoint.
     * If the endpoint has no region the "us-east-1" region is returned b default.
     *
     * @param endpoint The AWS request endpoint.
     * @return A (lowercase alphanumeric) string representing the AWS region.
     */
    public String getAmazonS3Region(String endpoint) {
        if (StringUtils.isNotBlank(endpoint) && endpoint.contains(HYPHEN)) {
            endpoint = endpoint.substring(3);
            return endpoint.substring(0, endpoint.indexOf('.'));
        }
        return Constants.Miscellaneous.DEFAULT_AMAZON_REGION;
    }

    private String entryToQuery(Map.Entry<String, String> entry) {
        String escapedKey = nullToEmpty(UriEncoder.escapeString(entry.getKey()));
        String escapedValue;
        if (entry.getValue() != null) {
            escapedValue = nullToEmpty(UriEncoder.escapeString(entry.getValue()));
        } else {
            escapedValue = Constants.Miscellaneous.EMPTY;
        }

        return escapedKey + Constants.Miscellaneous.EQUAL + escapedValue + Constants.Miscellaneous.AMPERSAND;
    }

    private String nullToEmpty(String inputString) {
        if (inputString == null) {
            return Constants.Miscellaneous.EMPTY;
        }
        return inputString;
    }
}