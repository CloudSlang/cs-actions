package io.cloudslang.content.hashicorp.terraform.entities;


import org.jetbrains.annotations.NotNull;

import static io.cloudslang.content.hashicorp.terraform.utils.Constants.Common.ORGANIZATION_NAME;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class ListOAuthClientInputs {

    private TerraformCommonInputs commonInputs;


    @java.beans.ConstructorProperties({ORGANIZATION_NAME})
    public ListOAuthClientInputs(TerraformCommonInputs commonInputs) {
        this.commonInputs = commonInputs;

    }

    public static ListOAuthClientInputsBuilder builder() {
        return new ListOAuthClientInputsBuilder();
    }

    @NotNull
    public TerraformCommonInputs getCommonInputs() {
        return commonInputs;
    }

    public static class ListOAuthClientInputsBuilder {
        private String organizationName = EMPTY;
        private TerraformCommonInputs commonInputs;

        ListOAuthClientInputsBuilder() {
        }

        @NotNull
        public ListOAuthClientInputs.ListOAuthClientInputsBuilder commonInputs(@NotNull final TerraformCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public ListOAuthClientInputs build() {
            return new ListOAuthClientInputs(commonInputs);
        }
    }


}
