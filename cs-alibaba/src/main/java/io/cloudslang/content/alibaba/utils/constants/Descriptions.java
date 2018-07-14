package io.cloudslang.content.alibaba.utils.constants;

public class Descriptions {
    public static class Common {
        // Inputs
        public static final String REGION_ID_DESC = "";
        public static final String ACCESS_KEY_ID_DESC = "";
        public static final String ACCESS_KEY_SECRET_ID_DESC = "";
        public static final String PROXY_HOST_DESC = "";
        public static final String PROXY_PORT_DESC = "";
        public static final String PROXY_USERNAME_DESC = "";
        public static final String PROXY_PASSWORD_DESC = "";
        public static final String INSTANCE_ID_DESC = "";

        //Results
        public static final String RETURN_RESULT_DESC = "The authentication token in case of success, or an error" +
                " message in case of failure.";
        public static final String AUTH_TOKEN_OUT_DESC = "The authentication token returned by the IdM service.";
        public static final String REFRESH_TOKEN_OUT_DESC = "The refresh token returned by the IdM service.";
    }

    public static class DeleteInstance {
        public static final String DELTETE_INSTANCE_DESC = "This operation is used to release a Pay-As-You-Go or expired " +
                "Subscription instance having the status Stopped.";
    }

    public static class StartInstance {
        public static final String START_INSTANCE_DESC = "This operation is used to start a specified instance.";

        //Inputs
        public static final String INIT_LOCAL_DISK_DESC = "Recover to the previous normal status of instance local disk " +
                "when exceptions occurs. Valid values: true, false";
    }
}
