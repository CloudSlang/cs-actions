package io.cloudslang.content.oracle.oci.services.models.instances;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.node.ObjectNode;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UpdateInstanceRequestBody {
    AgentConfig agentConfig;
    ObjectNode definedTags;
    String displayName;
    ObjectNode freeformTags;
    String shape;
    ShapeConfig shapeConfig;

    public AgentConfig getAgentConfig() {
        return agentConfig;
    }

    public void setAgentConfig(AgentConfig agentConfig) {
        this.agentConfig = agentConfig;
    }

    public ObjectNode getDefinedTags() {
        return definedTags;
    }

    public void setDefinedTags(ObjectNode definedTags) {
        this.definedTags = definedTags;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public ObjectNode getFreeformTags() {
        return freeformTags;
    }

    public void setFreeformTags(ObjectNode freeformTags) {
        this.freeformTags = freeformTags;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public ShapeConfig getShapeConfig() {
        return shapeConfig;
    }

    public void setShapeConfig(ShapeConfig shapeConfig) {
        this.shapeConfig = shapeConfig;
    }


    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public class AgentConfig {

        boolean isManagementDisabled;

        boolean isMonitoringDisabled;

        public boolean getIsManagementDisabled() {
            return isManagementDisabled;
        }

        public void setIsManagementDisabled(boolean isManagementDisabled) {
            this.isManagementDisabled = isManagementDisabled;
        }

        public boolean getIsMonitoringDisabled() {
            return isMonitoringDisabled;
        }

        public void setIsMonitoringDisabled(boolean isMonitoringDisabled) {
            this.isMonitoringDisabled = isMonitoringDisabled;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public class ShapeConfig {
        int ocpus;

        public int getOcpus() {
            return ocpus;
        }

        public void setOcpus(int ocpus) {
            this.ocpus = ocpus;
        }
    }


}
