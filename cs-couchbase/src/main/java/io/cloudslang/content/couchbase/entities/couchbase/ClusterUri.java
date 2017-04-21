package io.cloudslang.content.couchbase.entities.couchbase;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Created by TusaM
 * 4/20/2017.
 */
public enum ClusterUri {
    GET_CLUSTER_DETAILS("GetClusterDetails", "/pools/default"),
    GET_CLUSTER_INFO("GetClusterInfo", "/pools");

    private final String key;
    private final String value;

    ClusterUri(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static String getClusterUri(String input) {
        for (ClusterUri uri : ClusterUri.values()) {
            if (uri.getKey().equalsIgnoreCase(input)) {
                return uri.getValue();
            }
        }

        return EMPTY;
    }

    private String getKey() {
        return key;
    }

    private String getValue() {
        return value;
    }
}