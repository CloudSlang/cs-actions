package io.cloudslang.content.jclouds.entities.constants;

/**
 * Created by Mihai Tusa.
 * 5/4/2016.
 */
public class Constants {
    public static final class Providers {
        public static final String AMAZON = "amazon";
        public static final String OPENSTACK = "openstack";
    }

    public static final class Apis {
        public static final String AMAZON_PROVIDER = "ec2";
        public static final String OPENSTACK_PROVIDER = "openstack-nova";
    }

    public static final class Miscellaneous {
        public static final String EMPTY = "";
        public static final String NOT_RELEVANT = "Not relevant";
        public static final String NOT_RELEVANT_INT_CODE = "-1";
        public static final String TAG = "tag";
        public static final String EQUAL = "=";
    }

    public static final class ErrorMessages {
        public static final String NOT_IMPLEMENTED_OPENSTACK_ERROR_MESSAGE = "Not implemented. Use 'amazon' in provider input.";
        public static final String BOTH_PERMISSIONS_INPUTS_EMPTY = "The [userIdsString] and [userGroupsString] inputs" +
                " cannot be both empty in order to add/remove permission launch on specified image.";
        public static final String MIN_COUNT_MAX_COUNT_VALIDATION = "The value provided for [maxCount] input should be " +
                "greater or equal than the value provided for [minxCount] input.";
        public static final String TAG_KEYS_TAG_VALUES_MISMATCH = "Incorrect supplied values for: [keyTagsString] and/or " +
                "[valueTagsString] inputs. Number of tag keys should be the same with number of tag values.";
    }
}