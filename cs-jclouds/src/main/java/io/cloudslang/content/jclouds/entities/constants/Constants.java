package io.cloudslang.content.jclouds.entities.constants;

/**
 * Created by Mihai Tusa.
 * 5/4/2016.
 */
public class Constants {
    public static class Providers {
        public static final String OPENSTACK = "openstack";
    }

    public static class Apis {
        public static final String AMAZON_EC2_API = "ec2";
    }

    public static class Miscellaneous {
        public static final String AMPERSAND = "&";
        public static final String COLON = ":";
        public static final String COMMA_DELIMITER = ",";
        public static final String DEFAULT_AMAZON_REGION = "us-east-1";
        public static final String DOT = ".";
        public static final String EMPTY = "";
        public static final String ENCODING = "UTF-8";
        public static final String EQUAL = "=";
        public static final String LINE_SEPARATOR = "\n";
        public static final String NOT_RELEVANT = "Not relevant";
        public static final String SCOPE_SEPARATOR = "/";
        public static final String SET_FLAG = "1";
        public static final String STANDARD = "standard";
    }

    public static class ErrorMessages {
        public static final String NOT_IMPLEMENTED_OPENSTACK_ERROR_MESSAGE = "Not implemented. Use 'amazon' in provider input.";
        public static final String BOTH_PERMISSION_INPUTS_EMPTY = "The [userIdsString] and [userGroupsString] inputs" +
                " cannot be both empty in order to add/remove permission launch on specified image.";
        public static final String UNSUPPORTED_QUERY_API = "Unsupported Query API.";
    }

    public static class ValidationValues {
        public static final int ONE = 1;
        public static final int THOUSAND_AND_TWENTY_FOUR = 1024;
    }

    public static class AwsParams {
        public static final String ACTION = "Action";
        public static final String AMAZON_HOSTNAME = "amazonaws.com";
        public static final String AUTHORIZATION_HEADER_RESULT = "authorizationHeader";
        public static final String AUTHORIZATION_TYPE_ANONYMOUS = "anonymous";
        public static final String AWS_REQUEST_VERSION = "aws4_request";
        public static final String HEADER_DELIMITER = "\\r?\\n";
        public static final String INSTANCE_ID = "InstanceId";
        public static final String SIGNATURE_RESULT = "signature";
        public static final String VERSION = "Version";
    }

    public static class QueryApiActions {
        public static final String ALLOCATE_ADDRESS = "AllocateAddress";
        public static final String ATTACH_NETWORK_INTERFACE = "AttachNetworkInterface";
        public static final String CREATE_VOLUME = "CreateVolume";
        public static final String DETACH_NETWORK_INTERFACE = "DetachNetworkInterface";
        public static final String HTTP_CLIENT_METHOD_GET = "GET";
    }
}