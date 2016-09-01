package io.cloudslang.content.utils;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * Created by victor on 01.09.2016.
 */
public class OtherUtilities {

    public static boolean isValidEmail(@NotNull final String email) {
        return Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE).matcher(email).find();
    }


}
