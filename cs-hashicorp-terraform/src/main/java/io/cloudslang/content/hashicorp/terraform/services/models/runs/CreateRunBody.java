package io.cloudslang.content.hashicorp.terraform.services.models.runs;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateRunBody {

CreateRunData data;

    public CreateRunData getData() {
        return data;
    }

    public void setData(CreateRunData data) {
        this.data = data;
    }

    public class CreateRunData{
        Attributes attributes;
        String type;
        Relationships relationships;

        public Attributes getAttributes() {
            return attributes;
        }

        public void setAttributes(Attributes attributes) {
            this.attributes = attributes;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Relationships getRelationships() {
            return relationships;
        }

        public void setRelationships(Relationships relationships) {
            this.relationships = relationships;
        }
    }
   public class Attributes{

        boolean isDestroy;
        String runMessage;

       @JsonProperty("message")
        public String getRunMessage() {
            return runMessage;
        }
       @JsonProperty("message")
        public void setRunMessage(String runMessage) {
            this.runMessage = runMessage;
        }
       @JsonProperty("is-Destroy")
       public boolean isDestroy() {
           return isDestroy;
       }
       @JsonProperty("is-Destroy")
       public void setDestroy(boolean isDestroy) {
          this.isDestroy = isDestroy;
       }
   }
    public  class Relationships{
        Workspace workspace;

        public Workspace getWorkspace() {
            return workspace;
        }

        public void setWorkspace(Workspace workspace) {
            this.workspace = workspace;
        }
    }
    public  class Workspace{
        WorkspaceData data;

        public WorkspaceData getData() {
            return data;
        }

        public void setData(WorkspaceData data) {
            this.data = data;
        }
    }
    public class WorkspaceData{
        String type;
        String id;

        public String getType() {
            return type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setType(String type) {
            this.type = type;
        }

    }


}
