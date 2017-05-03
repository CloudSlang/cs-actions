package io.cloudslang.content.couchbase.factory.views;

import io.cloudslang.content.couchbase.entities.inputs.InputsWrapper;

import static io.cloudslang.content.couchbase.entities.constants.Constants.ViewsActions.GET_DESIGN_DOCS_INFO;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Created by TusaM
 * 4/28/2017.
 */
public class ViewsUriFactory {
    private ViewsUriFactory() {
        // prevent instantiation
    }

    public static String getViewsUri(InputsWrapper wrapper) {
        switch (wrapper.getCommonInputs().getAction()) {
            case GET_DESIGN_DOCS_INFO:
                return wrapper.getBucketInputs().getBucketName();
            default:
                return EMPTY;
        }
    }
}