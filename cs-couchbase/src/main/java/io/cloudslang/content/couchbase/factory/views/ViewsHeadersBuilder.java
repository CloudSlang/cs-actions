package io.cloudslang.content.couchbase.factory.views;

import io.cloudslang.content.couchbase.entities.inputs.InputsWrapper;

import static io.cloudslang.content.couchbase.entities.constants.Constants.HttpClientInputsValues.APPLICATION_JSON;

/**
 * Created by TusaM
 * 4/28/2017.
 */
public class ViewsHeadersBuilder {
    private ViewsHeadersBuilder() {
        // prevent instantiation
    }

    public static void setViewsHeaders(InputsWrapper wrapper) {
        switch (wrapper.getCommonInputs().getAction()) {
            default:
                wrapper.getHttpClientInputs().setContentType(APPLICATION_JSON);
        }
    }
}