
package io.cloudslang.content.json.utils;

import static io.cloudslang.content.constants.OtherValues.EMPTY_STRING;

/**
 * Created by Folea Ilie Cristian on 2/5/2016.
 */
public class StringUtils {
    public static boolean isEmpty(Object val) {
        return (val == null) || (EMPTY_STRING.equals(val));
    }


}
