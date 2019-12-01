package io.cloudslang.content.hashicorp.terraform.services.createmodels.runs;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApplyRunRequestBody {
    @JsonProperty("comment")
    private String runComment;

    public String getRunComment() {
        return runComment;
    }

    public void setRunComment(String runComment) {
        this.runComment = runComment;
    }
}
