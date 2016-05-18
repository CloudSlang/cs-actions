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

    public static final class Messages {
        public static final String HARD_REBOOT_SUCCESS = "Hard reboot started successfully.";
        public static final String SOFT_REBOOT_SUCCESS = "Soft reboot started successfully.";
        public static final String SERVER_STARTED = "Server started.";
        public static final String SERVER_STOPPED = "Server stopped.";
        public static final String SERVER_SUSPENDED = "Server suspended.";
        public static final String SERVER_RESUMED = "Server resumed.";
        public static final String SERVER_UPDATED = "Server updated successfully.";
        public static final String IMAGE_SUCCESSFULLY_DEREGISTER = "The image was successfully deregister.";
        public static final String LAUNCH_PERMISSIONS_SUCCESSFULLY_ADDED = "Launch permissions were successfully added.";
        public static final String LAUNCH_PERMISSIONS_SUCCESSFULLY_REMOVED = "Launch permissions were successfully removed.";
        public static final String LAUNCH_PERMISSIONS_SUCCESSFULLY_RESET = "Launch permissions were successfully reset.";
    }

    public static final class Miscellaneous {
        public static final String COMMA_DELIMITER = ",";
        public static final String DEFAULT_AMAZON_REGION = "us-east-1";
        public static final String EMPTY = "";
        public static final String MAXIMUM_INSTANCES_NUMBER_KEY = "maximumInstancesNumber";
        public static final String MINIMUM_INSTANCES_NUMBER_KEY = "minimumInstancesNumber";
        public static final String SELF = "self";

        public static final long DEFAULT_TIMING = 20000;
        public static final int MAXIMUM_INSTANCES_NUMBER = 50;
        public static final int MINIMUM_INSTANCES_NUMBER = 1;
    }

    public static final class ErrorMessages {
        public static final String NOT_IMPLEMENTED_ERROR_MESSAGE = "Not implemented. Use 'amazon\' or 'openstack' " +
                "providers in the provider input";
        public static final String NOT_IMPLEMENTED_OPENSTACK_ERROR_MESSAGE = "Not implemented. Use 'amazon' in provider input.";
        public static final String SERVER_NOT_FOUND = "Server not found.";
        public static final String BOTH_PERMISSIONS_INPUTS_EMPTY = "The [userIdsString] and [userGroupsString] inputs" +
                " cannot be both empty in order to add/remove permission launch on specified image.";
    }
}