package io.cloudslang.content.couchbase.entities.couchbase;

public enum ApiUriSuffix {ADD_NODE("/addNode"),
    AUTO_FAILOVER("/autoFailover"),
    DEFAULT("/default"),
    DO_FLUSH("/doFlush"),
    DO_JOIN_CLUSTER("/doJoinCluster"),
    EJECT_NODE_ENTRY("/ejectNodeentry"),
    SELF("/self"),
    WEB("/web");

    private final String value;

    ApiUriSuffix(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
