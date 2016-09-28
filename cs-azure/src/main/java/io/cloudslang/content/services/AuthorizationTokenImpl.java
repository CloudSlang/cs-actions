package io.cloudslang.content.services;

import io.cloudslang.content.utils.DateUtilities;
import io.cloudslang.content.utils.NumberUtilities;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

/**
 * Created by victor on 28.09.2016.
 */
public class AuthorizationTokenImpl {

    @NotNull
    public static String getToken(@NotNull final String identifier, @NotNull final String primaryOrSecondaryKey, @NotNull final String expiryOffset) {
        final int offset = NumberUtilities.toInteger(expiryOffset);
        final String expiry = DateUtilities.getDateWithOffset(offset);

        return "";
    }
}
