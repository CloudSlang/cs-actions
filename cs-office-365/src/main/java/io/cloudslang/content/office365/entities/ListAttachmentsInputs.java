package io.cloudslang.content.office365.entities;

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class ListAttachmentsInputs {
    private final String messageId;
    private final Office365CommonInputs commonInputs;

    @java.beans.ConstructorProperties({"messageId","commonInputs"})
    public ListAttachmentsInputs(String messageId, Office365CommonInputs commonInputs){
        this.messageId = messageId;
        this.commonInputs = commonInputs;
    }

    public static ListAttachmentsInputsBuilder builder(){
        return new ListAttachmentsInputsBuilder();
    }

    @NotNull
    public String getMessageId() {
        return messageId;
    }

    @NotNull
    public  Office365CommonInputs getCommonInputs(){
        return this.commonInputs;
    }

    public static class ListAttachmentsInputsBuilder {
        private String messageId = EMPTY;
        private Office365CommonInputs commonInputs;

    ListAttachmentsInputsBuilder(){
    }

    @NotNull
        public ListAttachmentsInputs.ListAttachmentsInputsBuilder messageId(@NotNull final String messageId){
            this.messageId = messageId;
            return this;
    }

    @NotNull
        public ListAttachmentsInputs.ListAttachmentsInputsBuilder commonInputs(@NotNull final Office365CommonInputs commonInputs){
        this.commonInputs = commonInputs;
        return this;
    }

    public ListAttachmentsInputs build() { return  new ListAttachmentsInputs(messageId,commonInputs); }
    }

}
