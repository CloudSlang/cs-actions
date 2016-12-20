/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
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
