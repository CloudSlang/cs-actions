package io.cloudslang.content.utilities.services;

import org.jetbrains.annotations.NotNull;

import static io.cloudslang.content.utils.StringUtilities.defaultIfBlank;
import static io.cloudslang.content.utils.StringUtilities.defaultIfEmpty;

/**
 * Created by moldovai on 8/28/2017.
 */
public class DefaultIfEmptyService {

    public static String defaultIfBlankOrEmpty(final String initialValue, @NotNull final String defaultValue, boolean validTrim) {
        if (validTrim) {
            return defaultIfBlank(initialValue, defaultValue);
        }
        return defaultIfEmpty(initialValue, defaultValue);
    }
}