package io.cloudslang.content.filesystem.constants;

public class Descriptions {

    public static class GetModifiedDate {

        public static final String RETURN_CODE_DESCRIPTION = "0, 1, 2 for success and -1 for failure.";
        public static final String RETURN_RESULT_DESCRIPTION = "A message indicating that the operation executed successfully or an error message otherwise.";
        public static final String EXCEPTION_DESCRIPTION = "An error message in case there was an error while retrieving the last modified date.";

        public static final String DATE_DESCRIPTION = "The date when the file was last modified. The date will be formatted using the values provided in " +
                "the localeLang and localeCountry inputs. If values are not provided in both inputs or the locale values are invalid , the date will be " +
                "returned in the \"MM/dd/yyyy hh:mm:ss a\" format";
        public static final String SOURCE_DESCRIPTION = "The file for which to check modification date.";
        public static final String THRESHOLD_DESCRIPTION = "The date to compare to. For example: \"July 13, 2020 10:04:08 AM\" in English date format ( other " +
                "country specific date formats are also recognized )";
        public static final String LOCALE_LANG_DESCRIPTION = "The locale language used to format the result date . For example,  en or ja.";
        public static final String LOCALE_COUNTRY_DESCRIPTION = "The locale country used to format the result date. For example, US or JP.";
    }

    public static class GetChildren {

        public static final String RETURN_CODE_DESCRIPTION = "0 for success and -1 for failure.";
        public static final String RETURN_RESULT_DESCRIPTION = "The list of paths to each child of the provided directory.";
        public static final String EXCEPTION_DESCRIPTION = "An error message in case there was an error while retrieving the children of the provided directory.";

        public static final String COUNT_DESCRIPTION = "The total number of children of the provided directory.";
        public static final String SOURCE_DESCRIPTION = "The directory for which to get the children.";
        public static final String DELIMITER_DESCRIPTION = "A delimiter to put in between each child of the provided directory.";
    }

    public static class MD5Sum {

        public static final String RETURN_CODE_DESCRIPTION = "It is -1 for failure, 1 if the checksum matched the specified checksum " +
                "and 0 if checksum did not match the specified checksum.";
        public static final String RETURN_RESULT_DESCRIPTION = "The file's calculated checksum in case of success or an error message in case of failure.";
        public static final String EXCEPTION_DESCRIPTION = "An error message in case there was an error while calculating the file's checksum.";
        public static final String CHECKSUM_DESCRIPTION = "The file's calculated checksum.";
        public static final String SOURCE_DESCRIPTION = "The file for which to create the checksum.";
        public static final String COMPARE_TO_DESCRIPTION = "A checksum to compare the file's checksum to.";
    }
}
