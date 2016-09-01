package io.cloudslang.content.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by victor on 31.08.2016.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtilities {

    public boolean isEmpty(final CharSequence string) {
        return StringUtils.isEmpty(string);
    }

    public boolean isBlank(final CharSequence string) {
        return StringUtils.isBlank(string);
    }
}
