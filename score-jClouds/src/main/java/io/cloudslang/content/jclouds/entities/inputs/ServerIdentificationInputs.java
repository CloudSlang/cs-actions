package io.cloudslang.content.jclouds.entities.inputs;

/**
 * Created by persdana on 5/25/2015.
 */
public class ServerIdentificationInputs extends CommonInputs {
    public static final String REGION = "region";
    public static final String SERVER_ID = "serverId";

    private String region;
    private String serverId;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

}
