package io.cloudslang.content.office365.entities;

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class GetMessageInputs {
    private final String messageId;
    private final String folderId;
    private final String oDataQuery;

    private final Office365CommonInputs commonInputs;

    @java.beans.ConstructorProperties({"messageId", "folderId", "oDataQuery", "commonInputs"})
    public GetMessageInputs(String messageId, String folderId, String oDataQuery, Office365CommonInputs commonInputs) {
        this.messageId = messageId;
        this.folderId = folderId;
        this.oDataQuery = oDataQuery;
        this.commonInputs = commonInputs;
    }

    @NotNull
    public static GetMessageInputsBuilder builder() {
        return new GetMessageInputsBuilder();
    }

    @NotNull
    public String getMessageId() {
        return messageId;
    }

    @NotNull
    public String getFolderId() {
        return folderId;
    }

    @NotNull
    public String getoDataQuery() {
        return this.oDataQuery;
    }

    @NotNull
    public Office365CommonInputs getCommonInputs() {
        return this.commonInputs;
    }

    public static class GetMessageInputsBuilder {
        private String messageId = EMPTY;
        private String folderId = EMPTY;
        private String oDataQuery = EMPTY;
        private Office365CommonInputs commonInputs;

        GetMessageInputsBuilder() {
        }

        @NotNull
        public GetMessageInputs.GetMessageInputsBuilder messageId(@NotNull final String messageId) {
            this.messageId = messageId;
            return this;
        }
        @NotNull
        public GetMessageInputs.GetMessageInputsBuilder folderId(@NotNull final String folderId) {
            this.folderId = folderId;
            return this;
        }
        @NotNull
        public GetMessageInputs.GetMessageInputsBuilder oDataQuery(@NotNull final String oDataQuery) {
            this.oDataQuery = oDataQuery;
            return this;
        }

        @NotNull
        public GetMessageInputs.GetMessageInputsBuilder commonInputs(@NotNull final Office365CommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public GetMessageInputs build() {
            return new GetMessageInputs(messageId, folderId, oDataQuery, commonInputs);
        }
    }

}
