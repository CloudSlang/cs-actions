package io.cloudslang.content.utils;

//import lombok.AccessLevel;
//import lombok.NoArgsConstructor;
//import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Created by victor on 31.08.2016.
 */
//@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtilities {

    public static boolean isEmpty(final String string) {
        return StringUtils.isEmpty(string);
    }

    public static boolean isBlank(final String string) {
        return StringUtils.isBlank(string);
    }

    @NotNull
    public static String defaultIfEmpty(final String string, @NotNull final String defaultValue ) {
        return isEmpty(string) ? defaultValue : string;
    }

    @NotNull
    public static String defaultIfBlank(final String string, @NotNull final String defaultValue ) {
        return isBlank(string) ? defaultValue : string;
    }

}
