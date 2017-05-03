package io.cloudslang.content.couchbase.entities.couchbase;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Created by TusaM
 * 4/28/2017.
 */
public enum ViewsUri {
    GET_DESIGN_DOC_INFO("GetDesignDocsInfo", "/pools/default/buckets");

    private final String key;
    private final String value;

    ViewsUri(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static String getViewsUri(String input) {
        for (ViewsUri uri : ViewsUri.values()) {
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